package com.tokopedia.broadcaster.utils

import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import kotlin.test.assertEquals

class DeviceInfoTest {

    @Test
    fun `the device should be supported arm v7a and v8a on lollipop above`() {
        // given
        val buildVersionProvider = mockk<BuildVersionProvider>()
        every { buildVersionProvider.isLollipopOrAbove() } returns true
        every { buildVersionProvider.supportedAbis() } returns "armeabi-v7a"

        // when
        val result = DeviceInfo.isDeviceSupported(buildVersionProvider)

        // then
        assertEquals(true, result)
    }

    @Test
    fun `the device should be supported cpu abi on lollipop below`() {
        // given
        val buildVersionProvider = mockk<BuildVersionProvider>()
        every { buildVersionProvider.isLollipopOrAbove() } returns false
        every { buildVersionProvider.cpuAbi() } returns "arm64-v8a"

        // when
        val result = DeviceInfo.isDeviceSupported(buildVersionProvider)

        // then
        assertEquals(true, result)
    }

}