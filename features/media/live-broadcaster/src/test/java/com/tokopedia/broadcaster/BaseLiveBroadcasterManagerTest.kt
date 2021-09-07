package com.tokopedia.broadcaster

import android.content.Context
import com.tokopedia.broadcaster.camera.CameraInfo
import com.tokopedia.broadcaster.camera.CameraManager
import com.tokopedia.broadcaster.data.BroadcasterConfig
import com.tokopedia.broadcaster.utils.DeviceInfoTest.Companion.ARM_64
import com.tokopedia.broadcaster.utils.DeviceInfoTest.Companion.MINIMUM_SUPPORTED_SDK
import io.mockk.*
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull

open class BaseLiveBroadcasterManagerTest {

    val broadcaster = LiveBroadcasterManager()

    private val broadcasterMock = spyk(broadcaster, recordPrivateCalls = true)
    private val streamer = mockk<ExternalStreamerGL>(relaxUnitFun = true)

    protected val context = mockk<Context>(relaxed = true)

    fun `Given Streamer`() {
        broadcaster.setPrivateProperty(
            MockProperty(name = "streamer", value = streamer)
        )
    }

    fun `Given BroadcasterConfig`(config: BroadcasterConfig.() -> Unit = {}): BroadcasterConfig {
        return BroadcasterConfig().apply(config)
    }

    fun `Given isDeviceSupported as`(value: Boolean) {
        val sdkInt: Int
        val isAboveLollipop: Boolean
        val archBuild: Any

        if (value) {
            sdkInt = 22
            isAboveLollipop = true
            archBuild = arrayOf(ARM_64)
        } else {
            sdkInt = 0
            isAboveLollipop = false
            archBuild = ""
        }

        mockSdkInt(sdkInt)
        mockVersionCodeOf(MINIMUM_SUPPORTED_SDK, 21)
        mockArchBuild(isAboveLollipop, archBuild)
    }

    fun `Given isDeviceHaveCameraAvailable as`(value: Boolean) {
        val methodName = "isDeviceHaveCameraAvailable"
        every { broadcasterMock[methodName](any() as Context) } returns value
    }

    fun `Given getAvailableCameras from CameraManager`(isNotEmpty: Boolean = true) {
        every {
            CameraManager.getAvailableCameras(context)
        } returns if (isNotEmpty) {
            mutableListOf(CameraInfo(""))
        } else {
            mutableListOf()
        }
    }

    fun `Given stop audio capture from streamerGL`() {
        justRun { streamer.stopAudioCapture() }
    }

    fun `Given available camera as`(isEmpty: Boolean) {
        broadcaster.setPrivateProperty(
            MockProperty("mAvailableCameras", if (isEmpty) {
                listOf()
            } else {
                listOf(
                    CameraInfo("")
                )
            })
        )
    }

    fun `Then stop audio capture is called`() {
        verify(exactly = 1) { streamer.stopAudioCapture() }
    }

    fun `Then the flip camera is called`() {
        verify(exactly = 1) { streamer.flip() }
    }

    fun `Then a property from BroadcasterConfig equals of`(broadcasterConfig: BroadcasterConfig) {
        val internalBroadcasterConfig = broadcaster
            .getPrivateProperty<BroadcasterConfig>("mConfig")

        assertEquals(broadcasterConfig, internalBroadcasterConfig)
    }

    inline fun <reified T> `Then should be not null for receiver of`(propertyName: String) {
        assertNotNull(broadcaster.getPrivateProperty<T>(propertyName))
    }

    inline fun <reified T : Throwable> `Then should be throw fails as`(`when`: () -> Unit) {
        assertFailsWith<T> {
            `when`()
        }
    }

}
