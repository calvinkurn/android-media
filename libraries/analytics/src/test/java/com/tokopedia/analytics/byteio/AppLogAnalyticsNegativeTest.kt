package com.tokopedia.analytics.byteio

import com.tokopedia.analytics.byteio.AppLogParam.PAGE_NAME
import com.tokopedia.analytics.byteio.AppLogParam.TRACK_ID
import org.junit.Before
import org.junit.Test
import kotlin.test.assertNull

class AppLogAnalyticsNegativeTest {

    @Before
    fun setup() {
        SUT.clearAllPageData()
    }

    @Test
    fun `when stack is empty then it returns null safely`() {
        assertNull(SUT.getLastData(PAGE_NAME))
    }

    @Test
    fun `when attribute was never set then getLastData return null`() {
        SUT.pushPageData(ActivityBasicOne)
        SUT.pushPageData(ActivityBasicTwo)

        assertNull(SUT.getLastData(TRACK_ID))
    }
}
