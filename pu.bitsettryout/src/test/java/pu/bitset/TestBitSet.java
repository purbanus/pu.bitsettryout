package pu.bitset;

import static org.assertj.core.api.Assertions.*;

import java.util.BitSet;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Dit komt uit https://www.baeldung.com/java-bitset
 *
 */
public class TestBitSet
{
private static final Logger LOG = LoggerFactory.getLogger( TestBitSet.class );

// @Test
// public void givenBoolArray_whenMemoryLayout_thenConsumeMoreThanOneBit() {
// boolean[] bits = new boolean[1024 * 1024];
//
// LOG.debug(ClassLayout.parseInstance(bits).toPrintable());
// }
//
// @Test
// public void givenBitSet_whenMemoryLayout_thenConsumeOneBitPerFlag() {
// BitSet bitSet = new BitSet(1024 * 1024);
//
// LOG.debug(GraphLayout.parseInstance(bitSet).toPrintable());
// }

/**
 * set() makes one or more bit one. get() gets a bit
 */
@Test
public void givenBitSet_whenSetting_thenShouldBeTrue()
{
	BitSet bitSet = new BitSet();

	bitSet.set( 10 );
	assertThat( bitSet.get( 10 ) ).isTrue();

	bitSet.set( 20, 30 );
	for ( int i = 20; i <= 29; i++ )
	{
		assertThat( bitSet.get( i ) ).isTrue();
	}
	assertThat( bitSet.get( 30 ) ).isFalse();

	bitSet.set( 10, false );
	assertThat( bitSet.get( 10 ) ).isFalse();

	bitSet.set( 20, 30, false );
	for ( int i = 20; i <= 30; i++ )
	{
		assertThat( bitSet.get( i ) ).isFalse();
	}
}
/**
 * Initially all bits are off Als je buiten het beriek van de bitset komt, breidt hij de bitset niet uit maar
 * retourneert false
 */
@Test
public void givenBitSet_defaultShouldBeFalse()
{
	BitSet bitSet = new BitSet();

	assertThat( bitSet.get( 10 ) ).isFalse();
	assertThat( bitSet.get( 100 ) ).isFalse();
	assertThat( bitSet.get( 2_000_000_000 ) ).isFalse();
}

/**
 * clear turns one or more bit off
 */
@Test
public void givenBitSet_whenClearing_thenShouldBeFalse()
{
	BitSet bitSet = new BitSet();
	bitSet.set( 42 );
	assertThat( bitSet.get( 42 ) ).isTrue();

	bitSet.clear( 42 );
	assertThat( bitSet.get( 42 ) ).isFalse();

	bitSet.set( 10, 20 );
	for ( int i = 10; i < 20; i++ )
	{
		assertThat( bitSet.get( i ) ).isTrue();
	}

	bitSet.clear( 10, 20 );
	for ( int i = 10; i < 20; i++ )
	{
		assertThat( bitSet.get( i ) ).isFalse();
	}

	bitSet.set( 10, 20 );
	bitSet.clear();
	for ( int i = 0; i < 100; i++ )
	{
		assertThat( bitSet.get( i ) ).isFalse();
	}
}

/**
 * Je kunt een nieuwe BitSet maken met bits van een bestaande bitset. Dat doe je mety get( fromIndex, toIndex );
 */
@Test
public void givenBitSet_whenGettingElements_thenShouldReturnRequestedBits()
{
	BitSet bitSet = new BitSet();
	bitSet.set( 10, 20 );
	BitSet newBitSet = bitSet.get( 10, 20 );
	for ( int i = 0; i < 10; i++ )
	{
		assertThat( newBitSet.get( i ) ).isTrue();
	}
}

@Test
public void checkToString()
{
	BitSet bitSet = new BitSet();
	bitSet.set( 42 );
	bitSet.set( 10, 15 );
	assertThat( bitSet.toString() ).isEqualTo( "{10, 11, 12, 13, 14, 42}" );
}

/**
 * flip toggles the state of one or more bits
 */
@Test
public void givenBitSet_whenFlip_thenTogglesTrueToFalseAndViceVersa()
{
	BitSet bitSet = new BitSet();
	bitSet.set( 42 );
	bitSet.flip( 42 );
	assertThat( bitSet.get( 42 ) ).isFalse();

	bitSet.flip( 12 );
	assertThat( bitSet.get( 12 ) ).isTrue();

	bitSet.flip( 30, 40 );
	for ( int i = 30; i < 40; i++ )
	{
		assertThat( bitSet.get( i ) ).isTrue();
	}
}

/**
 * size - th
 */
@Test
public void givenBitSet_whenGettingTheSize_thenReturnsTheSize()
{
	BitSet defaultBitSet = new BitSet();
	assertThat( defaultBitSet.size() ).isEqualTo( 64 );

	BitSet bitSet = new BitSet( 1024 );
	assertThat( bitSet.size() ).isEqualTo( 1024 );

	assertThat( bitSet.cardinality() ).isEqualTo( 0 );
	bitSet.set( 10, 30 );
	assertThat( bitSet.cardinality() ).isEqualTo( 30 - 10 );

	assertThat( bitSet.length() ).isEqualTo( 30 );
	bitSet.set( 100 );
	assertThat( bitSet.length() ).isEqualTo( 101 );

	assertThat( bitSet.isEmpty() ).isFalse();
	bitSet.clear();
	assertThat( bitSet.isEmpty() ).isTrue();
}

@Test
public void givenBitSet_whenSetOperations_thenShouldReturnAnotherBitSet()
{
	BitSet first = new BitSet();
	first.set( 5, 10 );

	BitSet second = new BitSet();
	second.set( 7, 15 );

	assertThat( first.intersects( second ) ).isTrue();

	first.and( second );
	assertThat( first.get( 7 ) ).isTrue();
	assertThat( first.get( 8 ) ).isTrue();
	assertThat( first.get( 9 ) ).isTrue();
	assertThat( first.get( 10 ) ).isFalse();

	first.clear();
	first.set( 5, 10 );

	first.xor( second );
	for ( int i = 5; i < 7; i++ )
	{
		assertThat( first.get( i ) ).isTrue();
	}
	for ( int i = 10; i < 15; i++ )
	{
		assertThat( first.get( i ) ).isTrue();
	}
}

@Test
public void givenBitSet_whenStream_thenStreamsAllSetBits()
{
	BitSet bitSet = new BitSet();
	bitSet.set( 15, 25 );

	bitSet.stream()
	    .forEach( bit -> LOG.debug( String.valueOf( bit ) ) );
	assertThat( bitSet.stream()
	    .count() ).isEqualTo( 10 );
}

@Test
public void givenBitSet_whenNextOrPrev_thenReturnsTheNextOrPrevClearOrSetBit()
{
	BitSet bitSet = new BitSet();
	bitSet.set( 15, 25 );

	assertThat( bitSet.nextSetBit( 13 ) ).isEqualTo( 15 );
	assertThat( bitSet.nextSetBit( 25 ) ).isEqualTo( -1 );

	assertThat( bitSet.nextClearBit( 23 ) ).isEqualTo( 25 );

	assertThat( bitSet.previousClearBit( 24 ) ).isEqualTo( 14 );
	assertThat( bitSet.previousSetBit( 29 ) ).isEqualTo( 24 );
	assertThat( bitSet.previousSetBit( 14 ) ).isEqualTo( -1 );
}
}
