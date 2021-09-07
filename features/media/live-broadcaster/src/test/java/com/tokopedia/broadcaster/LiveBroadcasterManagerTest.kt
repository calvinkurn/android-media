package com.tokopedia.broadcaster

import android.content.Context
import android.os.Handler
import com.tokopedia.broadcaster.camera.CameraManager
import com.tokopedia.broadcaster.utils.BroadcasterUtil
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Before
import org.junit.Test

class LiveBroadcasterManagerTest : BaseLiveBroadcasterManagerTest() {

    /**
     * Test cases without unhappy flow:
     * ✅ init
     * ✅ prepare
     * ⏳ set listener
     * ⏳ start preview
     * ✅ stop preview
     * ⏳ switch camera
     * ⏳ start live streaming with init
     * ⏳ start live streaming without init
     * ⏳ resume live
     * ⏳ pause
     * ⏳ reconnect
     * ⏳ stop
     * ⏳ get handler
     * ⏳ connection state changed
     * ⏳ video capture state changed
     * ⏳ audio capture state changed
     * ⏳ create streamer
     * ⏳ safe start preview
     */

    @Before
    fun setUp() {
        mockkObject(BroadcasterUtil)
        mockkObject(CameraManager)
    }

    @Test
    fun `Should be able to init LiveBroadcasterManager and return non-null Handler`() {
        // Given
        `Given isDeviceSupported as`(true)
        `Given getAvailableCameras from CameraManager`()
        `Given isDeviceHaveCameraAvailable as`(true)

        // When
        broadcaster.init(context, Handler())

        // Then
        `Then should be not null for receiver of`<Context>("mContext")
    }

    @Test
    fun `Should not be able to init because device is not supported`() {
        // Given
        `Given isDeviceSupported as`(false)
        `Given getAvailableCameras from CameraManager`()
        `Given isDeviceHaveCameraAvailable as`(true)

        // Then
        `Then should be throw fails as`<IllegalAccessException> {
            broadcaster.init(context, Handler())
        }
    }

    @Test
    fun `Should not be able to init because device has not available camera`() {
        // Given
        `Given isDeviceSupported as`(true)
        `Given getAvailableCameras from CameraManager`(isNotEmpty = false)
        `Given isDeviceHaveCameraAvailable as`(false)

        // Then
        `Then should be throw fails as`<IllegalAccessException> {
            broadcaster.init(context, Handler())
        }
    }

    @Test
    fun `Should be able to prepare with null config from param`() {
        // When
        broadcaster.prepare(null)

        // Then
        `Then a property from BroadcasterConfig equals of`(
            `Given BroadcasterConfig`()
        )
    }

    @Test
    fun `Should be able to prepare with non-null config from param`() {
        // Given
        val expectedValueOfConfig = `Given BroadcasterConfig` {
            ingestUrl = "rtmp://test.url.net"
        }

        // When
        broadcaster.prepare(expectedValueOfConfig)

        // Then
        `Then a property from BroadcasterConfig equals of`(expectedValueOfConfig)
    }

    @Test
    fun `Should be able to start preview`() {
        // Given

        // When

        // Then
    }

    @Test
    fun `Should be able to stop preview`() {
        // Given
        `Given Streamer`()
        `Given stop audio capture from streamerGL`()

        // When
        broadcaster.stopPreview()

        // Then
        `Then stop audio capture is called`()
    }

//    @Test
//    fun `Should be able to switch camera front to back and vice versa`() {
//        // Given
//        `Given Streamer`()
//        `Given available camera as`(isEmpty = true)
//
//        // When
//        broadcaster.switchCamera()
//
//        // Then
//        `Then the flip camera is called`()
//    }

    @After
    fun tearDown() {
        unmockkAll()
    }

}