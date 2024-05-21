package com.tokopedia.analytics.byteio

import com.tokopedia.analytics.byteio.search.AppLogSearch
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class AppLogAnalyticsGetLastDataTest {

    @Before
    fun setup() {
        SUT.clearAllPageData()
    }

    @Test
    fun `given data is pushed multiple then data on first stack can be obtained`() {
        val expected = "search123"
        SUT.pushPageData(ActivityBasicOne)
        SUT.putPageData(AppLogSearch.ParamKey.SEARCH_ID, expected)
        SUT.pushPageData(ActivityBasicTwo)

        val actual = SUT.getLastData(AppLogSearch.ParamKey.SEARCH_ID)
        assertEquals(expected, actual)
        // making sure data is not from ActivityBasicOne
        assertEquals(SUT.getLastData(AppLogParam.PAGE_NAME), "Page Two")
    }

    @Test
    fun `given enterFrom activity is pushed then data can be obtained`() {
        val expected = "Page EnterFrom"
        SUT.pushPageData(ActivityEnterFrom)
        SUT.pushPageData(ActivityBasicOne)
        SUT.pushPageData(ActivityBasicTwo)

        val actual = SUT.getLastData(AppLogParam.ENTER_FROM)
        assertEquals(expected, actual)
    }

    @Test
    fun `given stack is popped with previous shadow then shadow activity is popped`() {
        SUT.pushPageData(ActivityBasicOne)
        SUT.pushPageData(ActivityShadow)
        SUT.pushPageData(ActivityBasicTwo)

        SUT.removePageData(ActivityBasicTwo)
        val actual = SUT.getLastData(AppLogParam.PAGE_NAME)
        assertEquals(ActivityBasicOne.getPageName(), actual)
    }

    @Test
    fun `given stack is empty then it returns null safely`() {
        assertNull(SUT.getLastData(AppLogParam.PAGE_NAME))
    }

    @Test
    fun `given attribute was never set then getLastData return null`() {
        SUT.pushPageData(ActivityBasicOne)
        SUT.pushPageData(ActivityBasicTwo)

        assertNull(SUT.getLastData(AppLogParam.TRACK_ID))
    }
}
