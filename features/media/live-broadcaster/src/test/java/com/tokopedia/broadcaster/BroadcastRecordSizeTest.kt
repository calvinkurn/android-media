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
    fun `given null supported preview sizes, then findFlipSize() should return default resolution`() {
        val actual = BroadcasterUtil.findFlipSize(
            null,
            defaultResolution
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
    fun `given supported preview sizes with same ratio, then findFlipSize() should return best resolution`() {
        val supportedPreviewSizes = listOf(
            Streamer.Size(640, 480),
            Streamer.Size(352, 288),
            Streamer.Size(176, 144),
            Streamer.Size(1280, 720),
            Streamer.Size(1280, 960),
            Streamer.Size(1920, 1080),
            Streamer.Size(2400, 1350),
        )

        val actual = BroadcasterUtil.findFlipSize(
            supportedPreviewSizes,
            defaultResolution
        )

        val expected = Streamer.Size(1920, 1080)

        assertEquals(actual.width, expected.width)
        assertEquals(actual.height, expected.height)
    }

    @Test
    fun `given supported preview sizes with different ratio, then getVideoSize() should return first resolution`() {
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

        val expected = Streamer.Size(640, 480)

        assertEquals(actual.width, expected.width)
        assertEquals(actual.height, expected.height)
    }

    @Test
    fun `given supported preview sizes with different ratio, then findFlipSize() should return less frame sides`() {
        val supportedPreviewSizes = listOf(
            Streamer.Size(640, 480),
            Streamer.Size(352, 288),
            Streamer.Size(176, 144),
            Streamer.Size(1280, 960),
            Streamer.Size(2400, 1350),
        )

        val actual = BroadcasterUtil.findFlipSize(
            supportedPreviewSizes,
            defaultResolution
        )

        val expected = Streamer.Size(640, 480)

        assertEquals(actual.width, expected.width)
        assertEquals(actual.height, expected.height)
    }

    @Test
    fun `given supported preview sizes with different ratio and no less frame size, then findFlipSize() should return first resolution`() {
        val supportedPreviewSizes = listOf(
            Streamer.Size(1280, 960),
            Streamer.Size(2400, 1350),
        )

        val actual = BroadcasterUtil.findFlipSize(
            supportedPreviewSizes,
            defaultResolution
        )

        val expected = Streamer.Size(1280, 960)

        assertEquals(actual.width, expected.width)
        assertEquals(actual.height, expected.height)
    }
}
