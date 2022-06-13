package com.tokopedia.broadcaster.bitrate

import com.tokopedia.broadcaster.lib.LarixStreamer
import com.tokopedia.broadcaster.bitrate.LogarithmicDescendMode.Companion.NORMALIZATION_DELAY
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.test.assertTrue

class LogarithmicDescendModeTest {

    private val bitrate = LogarithmicDescendMode()
    private val streamer = mockk<LarixStreamer>(relaxed = true)

    @Test
    fun `start logarithmic ascend mode properly`() {
        // given
        val connectionId = 123

        justRun { streamer.changeBitRate(0) }

        // when
        bitrate.start(streamer, 0, connectionId)

        // then
        assertEquals(connectionId, bitrate.mConnectionId)
    }

    @Test
    fun `should not be able to change the bitrate`() {
        // given
        mockLossAndBitrateHistory(
            normalizationDelay = 1000
        )

        justRun { streamer.changeBitRate(0) }

        bitrate.mMinBitrate = 456

        // when
        bitrate.check(0, 0)

        // then
        verify(inverse = true) {
            streamer.changeBitRate(any())
        }
    }

    @Test
    fun `should be able to change the bitrate`() {
        // given
        mockLossAndBitrateHistory(
            normalizationDelay = NORMALIZATION_DELAY * 2,
            audioLossHistory = -123,
            videoLossHistory = -789,
        )

        // when
        bitrate.check(0, 0)

        // then
        assertTrue { bitrate.mBitrateHistory.size > 1 }
    }

    private fun mockLossAndBitrateHistory(
        bitrateHistory: Long = 123L,
        audioLossHistory: Long = 123L,
        videoLossHistory: Long = 123L,
        normalizationDelay: Long = NORMALIZATION_DELAY
    ) {

        bitrate.mLossHistory.clear()
        bitrate.mBitrateHistory.clear()

        // add the loss history
        bitrate.mLossHistory.addElement(BitrateAdapter.LossHistory(
            ts = 0L,
            audio = audioLossHistory,
            video = videoLossHistory
        ))

        // add the bitrate history
        bitrate.mBitrateHistory.addElement(BitrateAdapter.BitrateHistory(
            // make ts lest than current time to prevent bitrate change
            ts = System.currentTimeMillis() - normalizationDelay,
            bitrate = bitrateHistory
        ))

    }


}