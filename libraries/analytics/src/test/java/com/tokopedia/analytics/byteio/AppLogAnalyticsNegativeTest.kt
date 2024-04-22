package com.tokopedia.analytics.byteio

import com.tokopedia.analytics.byteio.AppLogAnalytics.addEnterMethodPdp
import com.tokopedia.analytics.byteio.AppLogParam.ENTER_METHOD
import com.tokopedia.analytics.byteio.AppLogParam.PAGE_NAME
import com.tokopedia.analytics.byteio.AppLogParam.TRACK_ID
import org.json.JSONObject
import org.junit.Before
import org.junit.Test
import kotlin.test.assertNull

class AppLogAnalyticsNegativeTest {

    @Before
    fun setup() {
        SUT.clearAllPageData()
    }

    @Test
    fun `given stack is empty then it returns null safely`() {
        assertNull(SUT.getLastData(PAGE_NAME))
    }

    @Test
    fun `given attribute was never set then getLastData return null`() {
        SUT.pushPageData(ActivityBasicOne)
        SUT.pushPageData(ActivityBasicTwo)

        assertNull(SUT.getLastData(TRACK_ID))
    }

    @Test
    fun `given enter method set in first but not second then third struct enterMethodPdp should get null`() {
        SUT.pushPageData(ActivityBasicOne)
        SUT.putPageData(ENTER_METHOD, "entryOne")
        SUT.pushPageData(ActivityBasicTwo)
        SUT.pushPageData(ActivityBasicThree)
        val actual = JSONObject().apply { addEnterMethodPdp() }

        assertNull(actual.getOrNull(ENTER_METHOD))
    }

    private fun JSONObject.getOrNull(key: String): Any? = runCatching { get(key) }.getOrNull()
}
