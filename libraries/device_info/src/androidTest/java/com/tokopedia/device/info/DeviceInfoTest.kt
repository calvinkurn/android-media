package com.tokopedia.device.info

import androidx.test.runner.AndroidJUnit4
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Created by mzennis on 2020-01-17.
 */

@RunWith(AndroidJUnit4::class)
class DeviceInfoTest {

    @Test
    fun testIsRooted() {
        val isRooted = DeviceInfo.isRooted()

        Assert.assertNotNull(isRooted)
        Assert.assertSame(isRooted, false)

        println("$TAG_DEVICE_INFO, IsRooted: $isRooted")
    }

    @Test
    fun testGetModelName() {
        val modelName = DeviceInfo.getModelName()

        Assert.assertNotNull(modelName)

        println("$TAG_DEVICE_INFO, ModelName: $modelName")
    }

    @Test
    fun testGetManufacturerName() {
        val manufacturer = DeviceInfo.getManufacturerName()

        Assert.assertNotNull(manufacturer)

        println("$TAG_DEVICE_INFO, Manufacturer: $manufacturer")
    }

    @Test
    fun testGetOsName() {
        val osName = DeviceInfo.getOSName()

        Assert.assertNotNull(osName)

        println("$TAG_DEVICE_INFO, OS Name: $osName")
    }

    @Test
    fun testGetLanguage() {
        val language = DeviceInfo.getLanguage()

        Assert.assertNotNull(language)

        println("$TAG_DEVICE_INFO, Lang: $language")
    }

    @Test
    fun testIsEmulated() {
        val isEmulated = DeviceInfo.isEmulated()

        Assert.assertNotNull(isEmulated)

        println("$TAG_DEVICE_INFO, IsEmulated: $isEmulated")
    }

    @Test
    fun testGetTimezoneOffset() {
        val timeZoneOffset = DeviceInfo.getTimeZoneOffset()

        Assert.assertNotNull(timeZoneOffset)

        println("$TAG_DEVICE_INFO, TimeZone: $timeZoneOffset")
    }
}