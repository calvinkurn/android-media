package com.tokopedia.analytics.byteio

import com.tokopedia.analytics.byteio.AppLogParam.ENTER_FROM
import com.tokopedia.analytics.byteio.AppLogParam.PAGE_NAME
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamKey.SEARCH_ID
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNull


class AppLogAnalyticsTest {

    @Before
    fun setup() {
        SUT.clearAllPageData()
    }

    @Test
    fun `given data is pushed multiple then data on first stack can be obtained`() {
        val expected = "search123"
        SUT.pushPageData(ActivityBasicOne)
        SUT.putPageData(SEARCH_ID, expected)
        SUT.pushPageData(ActivityBasicTwo)

        val actual = SUT.getLastData(SEARCH_ID)
        assertEquals(expected, actual)
        // making sure data is not from ActivityBasicOne
        assertEquals(SUT.getLastData(PAGE_NAME), "Page Two")
    }

    @Test
    fun `given enterFrom activity is pushed then data can be obtained`() {
        val expected = "Page EnterFrom"
        SUT.pushPageData(ActivityEnterFrom)
        SUT.pushPageData(ActivityBasicOne)
        SUT.pushPageData(ActivityBasicTwo)

        val actual = SUT.getLastData(ENTER_FROM)
        assertEquals(expected, actual)
    }

    @Test
    fun `given stack is popped with previous shadow then shadow activity is popped`() {
        SUT.pushPageData(ActivityBasicOne)
        SUT.pushPageData(ActivityShadow)
        SUT.pushPageData(ActivityBasicTwo)

        SUT.removePageData(ActivityBasicTwo)
        val actual = SUT.getLastData(PAGE_NAME)
        assertEquals(ActivityBasicOne.getPageName(), actual)
    }

    @Test
    fun `when getLastDataBeforeCurrent then n-1 data should be retrieved`() {
        SUT.pushPageData(ActivityBasicOne)
        SUT.pushPageData(ActivityBasicTwo)

        val actual = SUT.getLastDataBeforeCurrent(PAGE_NAME)
        assertEquals(ActivityBasicOne.getPageName(), actual)
    }

    @Test
    fun `when getLastDataBeforeHash then hash-1 data should be retrieved`() {
        SUT.pushPageData(ActivityBasicOne)
        SUT.pushPageData(ActivityBasicTwo)
        SUT.pushPageData(ActivityBasicThree)

        val actual = SUT.getLastDataBeforeHash(PAGE_NAME, ActivityBasicTwo.hashCode())
        assertEquals(ActivityBasicOne.getPageName(), actual)
    }

    @Test
    fun `when getDataBeforeStep should return the data at exact step`() {
        SUT.pushPageData(ActivityBasicOne)
        val expected = "search123"
        SUT.putPageData(SEARCH_ID, expected)
        SUT.pushPageData(ActivityBasicTwo)

        val actual = SUT.getDataBeforeStep(SEARCH_ID, 1)
        assertEquals(expected, actual)
    }

    @Test
    fun `when getDataBeforeStep should not return the data if the data is not in the exact step`() {
        SUT.pushPageData(ActivityBasicOne)
        val expected = "search123"
        SUT.putPageData(SEARCH_ID, expected)
        SUT.pushPageData(ActivityBasicTwo)
        SUT.pushPageData(ActivityBasicThree)

        val actual = SUT.getDataBeforeStep(SEARCH_ID, 1)
        assertNull(actual)
    }

}


