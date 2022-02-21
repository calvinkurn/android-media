package com.tokopedia.broadcaster

import android.content.Context
import android.os.Handler
import com.tokopedia.broadcaster.camera.CameraManager
import com.tokopedia.broadcaster.data.BroadcasterConfig
import com.tokopedia.broadcaster.state.BroadcasterState
import com.tokopedia.broadcaster.utils.BroadcasterUtils
import com.wmspanel.libstream.Streamer
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.json.JSONObject
import org.junit.After
import org.junit.Before
import org.junit.Test
import com.wmspanel.libstream.Streamer.CONNECTION_STATE as streamerState

class LiveBroadcasterManagerTest : BaseLiveBroadcasterManagerTest() {

    @Before
    fun setUp() {
        mockkObject(BroadcasterUtils)
        mockkObject(CameraManager)
    }

    @Test
    fun `Should be return default broadcaster config`() {
        // When
        val mConfig = broadcaster.config

        // Then
        `Then a property from BroadcasterConfig equals of`(mConfig)
    }

    @Test
    fun `Should be return initial broadcaster state as Idle`() {
        // Given
        val idle = BroadcasterState.Idle

        // When
        val mState = broadcaster.state

        // Then
        `Then the state should be expected with actual`(mState, idle)
    }

    @Test
    fun `Should be return empty connection uri`() {
        // Given
        `Given connection uri`("")

        // When
        val mIngestUrl = broadcaster.ingestUrl

        // Then
        `Then ingestUrl is empty`(mIngestUrl)
    }

    @Test
    fun `Should be able to init LiveBroadcasterManager and return non-null Context`() {
        // Given
        `Given isDeviceSupported as`(true)
        `Given device has available cameras as`(true)

        // When
        broadcaster.init(context, Handler())

        // Then
        `Then should be not null for receiver of`<Context>("mContext")
    }

    @Test
    fun `Should not be able to init because device is not supported`() {
        // Given
        `Given isDeviceSupported as`(false)
        `Given device has available cameras as`(true)

        // Then
        `Then should be throw fails as`<IllegalAccessException> {
            broadcaster.init(context, Handler())
        }
    }

    @Test
    fun `Should not be able to init because device has not available camera`() {
        // Given
        `Given isDeviceSupported as`(true)
        `Given device has available cameras as`(false)

        // Then
        `Then should be throw fails as`<IllegalAccessException> {
            broadcaster.init(context, Handler())
        }
    }

    @Test
    fun `Should be able to prepare with null config from param`() {
        // Given
        `Given audio config`()
        `Given video config`()

        // When
        broadcaster.prepare(null)

        // Then
        `Then changeAudioConfig of streamer is called`()
        `Then changeVideoConfig of streamer is called`()
        `Then a property from BroadcasterConfig equals of`(
            `Given BroadcasterConfig`()
        )
    }

    @Test
    fun `Should be able to prepare with non-null config from param`() {
        // Given
        `Given audio config`()
        `Given video config`()

        val expectedValueOfConfig = `Given BroadcasterConfig`()

        // When
        broadcaster.prepare(expectedValueOfConfig)

        // Then
        `Then changeAudioConfig of streamer is called`()
        `Then changeVideoConfig of streamer is called`()
        `Then a property from BroadcasterConfig equals of`(expectedValueOfConfig)
    }

    @Test
    fun `Should be able to start preview if streamer non-null`() {}

    @Test
    fun `Should be able to stop preview`() {
        // Given
        `Given stop audio capture from streamerGL`()

        // When
        broadcaster.stopPreview()

        // Then
        `Then stop audio capture is called`()
    }

    @Test
    fun `Should be able to switch camera front to back and vice versa`() {
        // Given
        `Given Switch Camera Supported`()

        // When
        broadcaster.switchCamera()

        // Then
        `Then the flip camera should be`(called = true)
    }

    @Test
    fun `Should not be able to switch camera`() {
        // When
        broadcaster.switchCamera()

        // Then
        `Then the flip camera should be`(called = false)
    }

    @Test
    fun `Should be able to start live streaming following init first`() {
        // Given
        `Given isDeviceSupported as`(true)
        `Given device has available cameras as`(true)
        `Given create streamer connection with id`(123)

        // When
        broadcaster.init(context, Handler())
        broadcaster.start(INGEST_URL)

        // Then
        `Then the state should be expected with actual`(BroadcasterState.Connecting)
        `Then the connection config url should be equals of`(INGEST_URL)
        `Then a property from BroadcasterConfig equals of`(
            BroadcasterConfig()
        )
    }

    @Test
    fun `Should not be able to start live streaming without init`() {
        // Given
        `Given create streamer connection with id`(0)

        // Then
        `Then should be throw fails as`<IllegalAccessException> {
            broadcaster.start(INGEST_URL)
        }
    }

    @Test
    fun `Should be able to resume the live stream`() {
        // Given
        `Given create streamer connection with id`(123)

        // When
        broadcaster.resume()

        // Then
        `Then the state should be expected with actual`(BroadcasterState.Connecting)
    }

    @Test
    fun `Should be able to pause the live stream`() {
        // Given
        `Given create streamer connection with id`(123)

        // When
        broadcaster.pause()

        // Then
        `Then the state should be expected with actual`(BroadcasterState.Paused)
    }

    @Test
    fun `Should be able to stop the live stream`() {
        // When
        broadcaster.stop()

        // Then
        `Then the state should be expected with actual`(BroadcasterState.Stopped)
    }

    @Test
    fun `Should be connection state changed return as Idle when streamer is Idle`() {
        // Given
        val connectionId = 123
        val streamerState = streamerState.IDLE
        `Given create streamer connection with id`(connectionId)

        // When
        broadcaster.onConnectionStateChanged(
            connectionId,
            streamerState,
            null,
            null
        )

        // Then
        `Then the state should be expected with actual`(BroadcasterState.Idle)
    }

    // TODO
    @Test
    fun `Should be connection state changed return as Idle when streamer is initialized`() {
        // Given
        `Given create streamer connection with id`(456) {

            // When
            `When connection changed with state and status`(
                connectionId = it,
                state = Streamer.CONNECTION_STATE.INITIALIZED
            )
        }

        // Then
        `Then the state should be expected with actual`(BroadcasterState.Idle)
    }

    // TODO
    @Test
    fun `Should be connection state changed return as Idle when streamer is setup`() {
        // Given
        `Given create streamer connection with id`(456) {

            // When
            `When connection changed with state and status`(
                connectionId = it,
                state = Streamer.CONNECTION_STATE.SETUP
            )
        }

        // Then
        `Then the state should be expected with actual`(BroadcasterState.Idle)
    }

    // TODO
    @Test
    fun `Should be onConnectionStateChanged return as Error when streamer as record and last state is error`() {
        // Given
        `Given last state as`(BroadcasterState.Error())
        `Given create streamer connection with id`(456) {

            // When
            `When connection changed with state and status`(
                connectionId = it,
                state = Streamer.CONNECTION_STATE.RECORD
            )
        }

        // Then
        `Then the state should be expected with actual`(BroadcasterState.Error())
    }

    // TODO
    @Test
    fun `Should be onConnectionStateChanged return as Idle when when streamer as record`() {
        // Given
        `Given the state of isPushStarted`(true)
        `Given create streamer connection with id`(456) {
            // When
            `When connection changed with state and status`(
                connectionId = it,
                state = Streamer.CONNECTION_STATE.RECORD
            )
        }

        // Then
        `Then the state should be expected with actual`(BroadcasterState.Idle)
    }

    // TODO
    @Test
    fun `Should be onConnectionStateChanged return as Idle when when streamer as record and push has not started`() {
        // Given
        `Given the state of isPushStarted`(false)
        `Given create streamer connection with id`(456) {

            // When
            `When connection changed with state and status`(
                connectionId = it,
                state = Streamer.CONNECTION_STATE.RECORD
            )
        }

        // Then
        `Then the state should be expected with actual`(BroadcasterState.Idle)
    }

    @Test
    fun `Should be onConnectionStateChanged return as Error when pusher has started, state as disconnected and status as connection fail`() {
        // Given
        `Given the state of isPushStarted`(true)
        `Given create streamer connection with id`(456) {

            // When
            `When connection changed with state and status`(
                connectionId = it,
                state = Streamer.CONNECTION_STATE.DISCONNECTED,
                status = Streamer.STATUS.CONN_FAIL
            )
        }

        // Then
        `Then the state should be expected with actual`(BroadcasterState.Error())
    }

    @Test
    fun `Should be onConnectionStateChanged return as Error when pusher has not started, state as disconnected and status as connection fail`() {
        // Given
        `Given the state of isPushStarted`(false)
        `Given create streamer connection with id`(456) {

            // When
            `When connection changed with state and status`(
                connectionId = it,
                state = Streamer.CONNECTION_STATE.DISCONNECTED,
                status = Streamer.STATUS.CONN_FAIL
            )
        }

        // Then
        `Then the state should be expected with actual`(BroadcasterState.Error())
    }

    @Test
    fun `Should be onConnectionStateChanged return as Error when state as disconnected and status as auth fail`() {
        // Given
        `Given create streamer connection with id`(456) {

            // When
            `When connection changed with state and status`(
                connectionId = it,
                state = Streamer.CONNECTION_STATE.DISCONNECTED,
                status = Streamer.STATUS.AUTH_FAIL
            )
        }

        // Then
        `Then the state should be expected with actual`(BroadcasterState.Error())
    }

    @Test
    fun `Should be onConnectionStateChanged return as Error when there is info, state as disconnected and status as unknown fail`() {
        // Given
        `Given create streamer connection with id`(456) {

            // When
            `When connection changed with state and status`(
                connectionId = it,
                state = Streamer.CONNECTION_STATE.DISCONNECTED,
                status = Streamer.STATUS.UNKNOWN_FAIL
            )
        }

        // Then
        `Then the state should be expected with actual`(BroadcasterState.Error())
    }

    @Test
    fun `Should be onConnectionStateChanged return as Error when there is no info, state as disconnected and status as unknown fail`() {
        // Given
        `Given create streamer connection with id`(456) {

            // When
            `When connection changed with state and status`(
                connectionId = it,
                state = Streamer.CONNECTION_STATE.DISCONNECTED,
                status = Streamer.STATUS.UNKNOWN_FAIL,
                infoJson = JSONObject("""
                {
                    "reason": "loren ipsum"
                }
            """.trimIndent())
            )
        }

        // Then
        `Then the state should be expected with actual`(BroadcasterState.Error())
    }

    // TODO
    @Test
    fun `Should be onConnectionStateChanged return as Idle when streamer successful disconnected`() {
        // Given
        `Given create streamer connection with id`(456) {

            // When
            `When connection changed with state and status`(
                connectionId = it,
                state = Streamer.CONNECTION_STATE.DISCONNECTED,
                status = Streamer.STATUS.SUCCESS
            )
        }

        // Then
        `Then the state should be expected with actual`(BroadcasterState.Idle)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    companion object {
        private const val INGEST_URL = "rtmp://test.url.net"
    }

}