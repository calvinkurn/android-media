package com.tokopedia.broadcaster.utils

import com.tokopedia.broadcaster.mockArchBuild
import com.tokopedia.broadcaster.mockSdkInt
import com.tokopedia.broadcaster.mockVersionCodeOf
import org.junit.Test
import kotlin.test.assertEquals

class DeviceInfoTest {

    @Test
    fun `the device should be supported arm v7a and v8a on lollipop above`() {
        // given
        mockSdkInt(22)
        mockVersionCodeOf(MINIMUM_SUPPORTED_SDK, 21)
        mockArchBuild(true, arrayOf(ARM_64))

        // when
        val result = DeviceInfo.isDeviceSupported()

        // then
        assertEquals(true, result)
    }

    @Test
    fun `the device should be supported cpu abi on lollipop below`() {
        // given
        mockSdkInt(0)
        mockVersionCodeOf(MINIMUM_SUPPORTED_SDK, 0)
        mockArchBuild(false, ARMEABI_V7A)

        // when
        val result = DeviceInfo.isDeviceSupported()

        // then
        assertEquals(true, result)
    }

    @Test
    fun `the device did not supported`() {
        // given
        mockSdkInt(0)
        mockVersionCodeOf(MINIMUM_SUPPORTED_SDK, 21)
        mockArchBuild(false, "")

        // when
        val result = DeviceInfo.isDeviceSupported()

        // then
        assertEquals(false, result)
    }

    companion object {
        const val MINIMUM_SUPPORTED_SDK = "LOLLIPOP"
        const val ARM_64 = "arm64-v8a"
        const val ARMEABI_V7A = "armeabi-v7a"
    }

}