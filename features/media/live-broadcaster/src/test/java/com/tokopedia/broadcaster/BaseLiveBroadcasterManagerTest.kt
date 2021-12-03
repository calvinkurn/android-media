package com.tokopedia.broadcaster

import android.content.Context
import com.tokopedia.broadcaster.camera.CameraInfo
import com.tokopedia.broadcaster.camera.CameraManager
import com.tokopedia.broadcaster.data.BroadcasterConfig
import com.tokopedia.broadcaster.lib.BroadcasterConnection
import com.tokopedia.broadcaster.state.BroadcasterState
import com.tokopedia.broadcaster.utils.BroadcasterUtils
import com.tokopedia.broadcaster.utils.DeviceInfoTest.Companion.ARM_64
import com.tokopedia.broadcaster.utils.DeviceInfoTest.Companion.MINIMUM_SUPPORTED_SDK
import com.wmspanel.libstream.AudioConfig
import com.wmspanel.libstream.Streamer
import com.wmspanel.libstream.VideoConfig
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import org.json.JSONObject
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

open class BaseLiveBroadcasterManagerTest {

    val broadcaster = LiveBroadcasterManager(
        streamer = mockk(relaxUnitFun = true),
        statistic = mockk(relaxUnitFun = true),
    )

    protected val context = mockk<Context>(relaxed = true)

    fun `Given BroadcasterConfig`(config: BroadcasterConfig.() -> Unit = {}): BroadcasterConfig {
        return BroadcasterConfig().apply(config)
    }

    fun `Given local BroadcasterConfig from constructor`(config: BroadcasterConfig.() -> Unit = {}) {
        val newConfig = `Given BroadcasterConfig`(config)
        broadcaster.mConfig = newConfig
    }

    fun `Given create streamer connection with id`(id: Int = 123, block: (Int) -> Unit = {}) {
        every {
            broadcaster.streamer?.createConnection(any() as BroadcasterConnection)
        } returns id

        broadcaster.mConnection.connectionId = id

        // seamless with the same of connection id
        block(id)
    }

    fun `Given connection uri`(ingestUrl: String) {
        broadcaster.mConnection.uri = ingestUrl
    }

    fun `Given data logger`() {
        justRun {
            broadcaster.statistic.init(
                any(),
                any()
            )
        }
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

    fun `Given Switch Camera Supported`() {
        val mockSampleActiveCameraId = 1

        broadcaster.mAvailableCameras.addAll(listOf(
            CameraInfo(mockSampleActiveCameraId.toString()), // 1
            CameraInfo((mockSampleActiveCameraId + 1).toString()), // 2
            CameraInfo((mockSampleActiveCameraId + 2).toString()) // 3
        ))

        every {
            broadcaster.streamer?.activeCameraId
        } returns mockSampleActiveCameraId.toString()
    }

    fun `Given audio config`() {
        every {
            BroadcasterUtils.getAudioConfig(any() as BroadcasterConfig)
        } returns AudioConfig()
    }

    fun `Given video config`() {
        // first, mock the verify of video resolution of the camera
        every {
            CameraManager.verifyResolution(
                any(),
                any(),
                any(),
                any()
            )
        } returns Streamer.Size(0, 0)

        // then, mock the video config from BroadcasterUtil
        every {
            BroadcasterUtils.getVideoConfig(any() as BroadcasterConfig)
        } returns VideoConfig()
    }

    fun `Given device has available cameras as`(value: Boolean) {
        every {
            CameraManager.getAvailableCameras(any() as Context)
        } returns if (value) {
            mutableListOf(CameraInfo("123"))
        } else {
            mutableListOf()
        }
    }

    fun `Given stop audio capture from streamerGL`() {
        justRun { broadcaster.streamer?.stopAudioCapture() }
    }

    fun `Given start video capture from streamerGL`() {
        justRun { broadcaster.streamer?.startVideoCapture() }
    }

    fun `Given start audio capture from streamerGL`() {
        justRun { broadcaster.streamer?.startAudioCapture() }
    }

    fun `Given last state as`(state: BroadcasterState) {
        broadcaster.broadcastState(state)
    }

    fun `Given the state of isPushStarted`(value: Boolean) {
        broadcaster.isPushStarted = value
    }

    fun `When connection changed with state and status`(
        connectionId: Int,
        state: Streamer.CONNECTION_STATE,
        status: Streamer.STATUS? = null,
        infoJson: JSONObject? = null
    ) {
        broadcaster.onConnectionStateChanged(
            connectionId,
            state,
            status,
            infoJson
        )
    }

    fun `Then stop audio capture is called`() {
        verify(atLeast = 1) { broadcaster.streamer?.stopAudioCapture() }
    }

    fun `Then changeAudioConfig of streamer is called`() {
        verify(exactly = 1) {
            broadcaster.streamer?.changeAudioConfig(any() as AudioConfig)
        }
    }

    fun `Then changeVideoConfig of streamer is called`() {
        verify(exactly = 1) {
            broadcaster.streamer?.changeVideoConfig(any() as VideoConfig)
        }
    }

    fun `Then startVideoCapture of streamer is called`() {
        verify(exactly = 1) {
            broadcaster.streamer?.startVideoCapture()
        }
    }

    fun `Then startAudioCapture of streamer is called`() {
        verify(exactly = 1) {
            broadcaster.streamer?.startAudioCapture()
        }
    }

    fun `Then ingestUrl is empty`(givenUrl: String) {
        assertTrue { givenUrl.isEmpty() }
    }

    fun `Then the flip camera should be`(called: Boolean) {
        verify(exactly = if (called) 1 else 0) {
            broadcaster.streamer?.flip()
        }
    }

    fun `Then the streamer is released`() {
        verify(atLeast = 1) { broadcaster.streamer?.release() }
    }

    fun `Then the connection config url should be equals of`(ingestUrl: String) {
        assertTrue { broadcaster.mConnection.uri == ingestUrl }
    }

    fun `Then the state should be expected with actual`(
        expectedState: BroadcasterState,
        actualState: BroadcasterState
    ) {
        assertEquals(expectedState.javaClass, actualState.javaClass)
    }

    fun `Then the state should be expected with actual`(state: BroadcasterState) {
        `Then the state should be expected with actual`(broadcaster.mState, state)
    }

    fun `Then a property from BroadcasterConfig equals of`(broadcasterConfig: BroadcasterConfig) {
        assertEquals(broadcasterConfig, broadcaster.mConfig)
    }

    fun `Then data log is succeed to init`() {
        verify(exactly = 1) {
            broadcaster.statistic.init(
                any(),
                any()
            )
        }
    }

    inline fun <reified T> `Then should be null for receiver of`(propertyName: String) {
        assertTrue { `Get private property value`<T>(propertyName) == null }
    }

    inline fun <reified T> `Then should be not null for receiver of`(propertyName: String) {
        assertNotNull(`Get private property value`<T>(propertyName))
    }

    inline fun <reified T : Throwable> `Then should be throw fails as`(`when`: () -> Unit) {
        assertFailsWith<T> {
            `when`()
        }
    }

    inline fun <reified T> `Get private property value`(propertyName: String): T? {
        return broadcaster.getPrivateProperty(propertyName)
    }

}
