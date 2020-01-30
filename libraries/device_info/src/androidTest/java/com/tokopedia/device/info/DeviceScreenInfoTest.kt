package com.tokopedia.device.info;

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import junit.framework.Assert.assertEquals
import org.junit.Assert

import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by mzennis on 2020-01-17.
 */

@RunWith(AndroidJUnit4::class)
class DeviceScreenInfoTest {

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.tokopedia.device.info.test", appContext.packageName)
    }


    @Test
    fun testScreenResolution() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        val screenResolution = DeviceScreenInfo.getScreenResolution(appContext)
        Assert.assertNotNull(screenResolution)

        println("$TAG_DEVICE_INFO, Screen Resolution:  $screenResolution")
    }

    @Test
    fun testIsTablet() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        val isTablet = DeviceScreenInfo.isTablet(appContext)
        Assert.assertNotNull(isTablet)
        Assert.assertSame(isTablet, false)

        println("$TAG_DEVICE_INFO, IsTablet: $isTablet")
    }
}
