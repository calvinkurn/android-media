package com.tokopedia.play.datetime

import com.tokopedia.play.util.assertEqualTo
import com.tokopedia.play.util.video.millisToFormattedVideoDuration
import org.junit.Test
import java.util.concurrent.TimeUnit

/**
 * Created by kenny.hadisaputra on 31/03/22
 */
class VideoDurationFormatterTest {

    @Test
    fun `test duration 1`() {
        val durationInMillis = TimeUnit.SECONDS.toMillis(1)
        durationInMillis.millisToFormattedVideoDuration()
            .assertEqualTo("0:01")
    }

    @Test
    fun `test duration 2`() {
        val durationInMillis = TimeUnit.SECONDS.toMillis(53)
        durationInMillis.millisToFormattedVideoDuration()
            .assertEqualTo("0:53")
    }

    @Test
    fun `test duration 3`() {
        val durationInMillis = TimeUnit.SECONDS.toMillis(100)
        durationInMillis.millisToFormattedVideoDuration()
            .assertEqualTo("1:40")
    }

    @Test
    fun `test duration 4`() {
        val durationInMillis = TimeUnit.MINUTES.toMillis(20)
        durationInMillis.millisToFormattedVideoDuration()
            .assertEqualTo("20:00")
    }

    @Test
    fun `test duration 5`() {
        val durationInMillis = TimeUnit.MINUTES.toMillis(20) + TimeUnit.SECONDS.toMillis(17)
        durationInMillis.millisToFormattedVideoDuration()
            .assertEqualTo("20:17")
    }

    @Test
    fun `test duration 6`() {
        val durationInMillis =
            TimeUnit.HOURS.toMillis(3) +
                    TimeUnit.MINUTES.toMillis(20) +
                    TimeUnit.SECONDS.toMillis(17)
        durationInMillis.millisToFormattedVideoDuration()
            .assertEqualTo("3:20:17")
    }

    @Test
    fun `test duration 7`() {
        val durationInMillis =
            TimeUnit.HOURS.toMillis(3) +
                    TimeUnit.MINUTES.toMillis(1) +
                    TimeUnit.SECONDS.toMillis(5)
        durationInMillis.millisToFormattedVideoDuration()
            .assertEqualTo("3:01:05")
    }

    @Test
    fun `test duration 8`() {
        val durationInMillis = 1100L
        durationInMillis.millisToFormattedVideoDuration()
            .assertEqualTo("0:01")
    }
}