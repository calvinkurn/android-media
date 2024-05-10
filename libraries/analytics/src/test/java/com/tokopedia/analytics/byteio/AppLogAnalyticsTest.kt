package com.tokopedia.analytics.byteio

import com.tokopedia.analytics.byteio.AppLogAnalytics.addEnterMethodPdp
import com.tokopedia.analytics.byteio.AppLogParam.PAGE_NAME
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamKey.SEARCH_ID
import org.json.JSONObject
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull


class AppLogAnalyticsTest {

    @Before
    fun setup() {
        SUT.clearAllPageData()
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

    @Test
    fun `given enter method set in first but not second then third struct enterMethodPdp should get null`() {
        SUT.pushPageData(ActivityBasicOne)
        SUT.putPageData(AppLogParam.ENTER_METHOD, "entryOne")
        SUT.pushPageData(ActivityBasicTwo)
        SUT.pushPageData(ActivityBasicThree)
        val actual = JSONObject().apply { addEnterMethodPdp() }

        assertNull(actual.getOrNull(AppLogParam.ENTER_METHOD))
    }

    private fun JSONObject.getOrNull(key: String): Any? = runCatching { get(key) }.getOrNull()

}


