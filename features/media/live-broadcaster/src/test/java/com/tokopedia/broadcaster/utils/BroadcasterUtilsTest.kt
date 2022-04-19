package com.tokopedia.broadcaster.utils

import android.media.MediaCodecList
import com.tokopedia.broadcaster.data.BroadcasterConfig
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

class BroadcasterUtilsTest {

    @Test
    fun `should be able change audio config`() {
        // given
        val audioRate = 123
        // when
        val result = BroadcasterUtils.getAudioConfig(BroadcasterConfig(
            audioRate = audioRate
        ))

        // then
        assertEquals(audioRate, result.sampleRate)
    }

    @Test
    fun `should be able change video config`() {
        // given
        val videoBitrate = 123
        val mediaCodecList = mockk<MediaCodecList>()

        every { mediaCodecList.codecInfos } returns arrayOf()

        // when
        val result = BroadcasterUtils.getVideoConfig(
            config = BroadcasterConfig(videoBitrate = videoBitrate),
            mediaCodecList = mediaCodecList
        )

        // then
        assertEquals(videoBitrate, result.bitRate)
    }

}