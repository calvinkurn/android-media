package com.tokopedia.analytics.byteio

import com.tokopedia.analytics.byteio.AppLogParam.ENTER_FROM
import com.tokopedia.analytics.byteio.AppLogParam.PAGE_NAME
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamKey.SEARCH_ID
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals


class AppLogAnalyticsTest {

    @Before
    fun setup() {
        SUT.clearAllPageData()
    }

    @Test
    fun `when data is pushed multiple then data on first stack can be obtained`() {
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
    fun `when enterFrom activity is pushed then data can be obtained`() {
        val expected = "Page EnterFrom"
        SUT.pushPageData(ActivityEnterFrom)
        SUT.pushPageData(ActivityBasicOne)
        SUT.pushPageData(ActivityBasicTwo)

        val actual = SUT.getLastData(ENTER_FROM)
        assertEquals(expected, actual)
    }

    @Test
    fun `when stack is popped with previous shadow then shadow activity is popped`() {
        SUT.pushPageData(ActivityBasicOne)
        SUT.pushPageData(ActivityShadow)
        SUT.pushPageData(ActivityBasicTwo)

        SUT.removePageData(ActivityBasicTwo)
        val actual = SUT.getLastData(PAGE_NAME)
        assertEquals("Page One", actual)
    }

}


