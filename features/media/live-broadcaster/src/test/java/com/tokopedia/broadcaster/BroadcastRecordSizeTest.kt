package com.tokopedia.broadcaster

import com.tokopedia.broadcaster.revamp.util.BroadcasterUtil
import com.wmspanel.libstream.Streamer
import org.junit.Test
import kotlin.test.assertEquals

/**
 * Created by meyta.taliti on 12/08/22.
 */
class BroadcastRecordSizeTest {

    private val defaultResolution = Streamer.Size(
        1280,
        720
    )
    private val defaultAspectRatio = 16.0/9.0

    @Test
    fun `given null supported preview sizes, then getVideoSize() should return default resolution`() {
        val actual = BroadcasterUtil.getVideoSize(
            null,
            defaultAspectRatio
        )

        val expected = defaultResolution

        assertEquals(actual.width, expected.width)
        assertEquals(actual.height, expected.height)
    }

    @Test
    fun `given supported preview sizes with same ratio, then getVideoSize() should return best resolution`() {
        val supportedPreviewSizes = listOf(
            Streamer.Size(640, 480),
            Streamer.Size(352, 288),
            Streamer.Size(176, 144),
            Streamer.Size(1280, 720),
            Streamer.Size(1280, 960),
            Streamer.Size(1920, 1080),
            Streamer.Size(2400, 1350),
        )

        val actual = BroadcasterUtil.getVideoSize(
            supportedPreviewSizes,
            defaultAspectRatio
        )

        val expected = Streamer.Size(1920, 1080)

        assertEquals(actual.width, expected.width)
        assertEquals(actual.height, expected.height)
    }


    @Test
    fun `given supported preview sizes with different ratio, when there is no supported resolution, then it should return default resolution`() {
        val supportedPreviewSizes = listOf(
            Streamer.Size(640, 480),
            Streamer.Size(352, 288),
            Streamer.Size(176, 144),
            Streamer.Size(1280, 960),
            Streamer.Size(2400, 1350),
        )

        val actual = BroadcasterUtil.getVideoSize(
            supportedPreviewSizes,
            defaultAspectRatio
        )

        assertEquals(actual.width, defaultResolution.width)
        assertEquals(actual.height, defaultResolution.height)
    }
}
