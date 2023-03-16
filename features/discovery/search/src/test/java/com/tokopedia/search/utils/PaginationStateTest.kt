package com.tokopedia.search.utils

import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.DEFAULT_VALUE_OF_PARAMETER_ROWS
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PaginationStateTest {

    @Test
    fun `hasNextPage if startFrom is smaller than totalData`() {
        assertTrue(PaginationState(startFrom = 0, totalData = 10).hasNextPage)

        assertFalse(PaginationState(startFrom = 16, totalData = 10).hasNextPage)
    }

    @Test
    fun `isFirstPage if startFrom is 0`() {
        assertTrue(PaginationState(startFrom = 0).isFirstPage)

        assertFalse(PaginationState(startFrom = 8).isFirstPage)
    }

    @Test
    fun `isLastPage when does not hasNextPage`() {
        assertFalse(PaginationState(startFrom = 0, totalData = 10).isLastPage)

        assertTrue(PaginationState(startFrom = 16, totalData = 10).isLastPage)
    }

    @Test
    fun `incrementStart will increment startFrom by certain value`() {
        assertEquals(
            DEFAULT_VALUE_OF_PARAMETER_ROWS.toLong(),
            PaginationState(startFrom = 0).incrementStart().startFrom,
        )
    }

    @Test
    fun `decrementStart will decrement startFrom by certain value`() {
        assertEquals(
            0,
            PaginationState(
                startFrom = DEFAULT_VALUE_OF_PARAMETER_ROWS.toLong()
            ).decrementStart().startFrom,
        )
    }
}
