package com.tokopedia.broadcaster.bitrate

import com.tokopedia.broadcaster.lib.LarixStreamer
import com.tokopedia.broadcaster.bitrate.BitrateLadderAscendMode.Companion.LOST_BANDWIDTH_TOLERANCE_FRACTION
import com.tokopedia.broadcaster.bitrate.BitrateLadderAscendMode.Companion.NORMALIZATION_DELAY
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import kotlin.test.assertTrue

class BitrateLadderAscendModeTest {

    private val bitrate = BitrateLadderAscendMode()
    private val streamer = mockk<LarixStreamer>(relaxed = true)

    @Test
    fun `start ladder ascend mode properly`() {
        // given
        justRun { streamer.changeBitRate(0) }

        // when
        bitrate.start(streamer, 0, 0)

        // then
        verify(atLeast = 1) {
            streamer.changeBitRate(0)
        }
    }

    @Test
    fun `should not be able to change the bitrate`() {
        // given
        mockLossAndBitrateHistory()

        justRun { streamer.changeBitRate(0) }

        // set the step to zero
        bitrate.mStep = 0

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
            bitrateHistory = LOST_BANDWIDTH_TOLERANCE_FRACTION * 2
        )

        // set the step to zero
        bitrate.mStep = 1

        // when
        bitrate.check(0, 0)

        // then
        assertTrue {
            /*
            * the 2 items comes from a mocking data from [mockLossAndBitrateHistory],
            * and the second one if the streamer change the bitrate.
            * */
            val expectedBitrateHistory = 2

            bitrate.mBitrateHistory.size == expectedBitrateHistory
        }
    }

    @Test
    fun `should be able to change the bitrate in recovery phase`() {
        // given
        mockLossAndBitrateHistory()

        // set the step to zero
        bitrate.mStep = 0

        // set high full bitrate
        bitrate.mFullBitrate = 456L

        // when
        bitrate.check(123L, 123L)

        // then
        assertTrue {
            /*
            * the 2 items comes from a mocking data from [mockLossAndBitrateHistory],
            * and the second one if the streamer change the bitrate.
            * */
            val expectedBitrateHistory = 2

            bitrate.mBitrateHistory.size == expectedBitrateHistory
        }
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