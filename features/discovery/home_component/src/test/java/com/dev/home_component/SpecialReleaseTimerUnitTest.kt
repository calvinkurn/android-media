package com.dev.home_component

import com.tokopedia.home_component.customview.SpecialReleaseTimerCopyGenerator
import org.junit.Assert
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*

class SpecialReleaseTimerUnitTest {
    companion object {
        private const val TWO_DAYS_MILLISECOND = 172800000L
        private const val TWENTY_FOUR_HOURS = 86400000L
        private const val ONE_HOUR = 3600000L
        private const val ONE_MINUTE = 60000L
    }

    @Test
    fun `given current time have diff above 2 days with expired time on getCopy then should return empty`() {
        val expiredTime = "2021-01-20T16:13:30+07:00"
        val expiredTimeDate = getExpiredTime(expiredTime)
        val currentTimeDate = expiredTimeDate.getDateBefore(2* TWO_DAYS_MILLISECOND)
        val copy = SpecialReleaseTimerCopyGenerator.getCopy(currentTimeDate = currentTimeDate, expiredTimeDate = expiredTimeDate)
        Assert.assertEquals("", copy)
    }

    @Test
    fun `given current time have diff below 2 days and above 24 hour with expired time on getCopy then should return Sisa 2 hari`() {
        val expiredTime = "2021-01-20T16:13:30+07:00"
        val expiredTimeDate = getExpiredTime(expiredTime)
        val currentTimeDate = expiredTimeDate.getDateBefore(TWO_DAYS_MILLISECOND)
        val copy = SpecialReleaseTimerCopyGenerator.getCopy(currentTimeDate = currentTimeDate, expiredTimeDate = expiredTimeDate)
        Assert.assertEquals("Sisa 2 Hari", copy)
    }

    @Test
    fun `given current time have diff 1 days with expired time on getCopy then should return Sisa 1 hari`() {
        val expiredTime = "2021-01-20T16:13:30+07:00"
        val expiredTimeDate = getExpiredTime(expiredTime)
        val currentTimeDate = expiredTimeDate.getDateBefore(TWENTY_FOUR_HOURS)
        val copy = SpecialReleaseTimerCopyGenerator.getCopy(currentTimeDate = currentTimeDate, expiredTimeDate = expiredTimeDate)
        Assert.assertEquals("Sisa 1 Hari", copy)
    }

    @Test
    fun `given current time have diff below 24 hours with expired time on getCopy then should return Sisa x jam`() {
        val expiredTime = "2022-02-10T23:59:59+07:00"
        val expiredTimeDate = getExpiredTime(expiredTime)
        val currentTimeDate = expiredTimeDate.getDateBefore((2* ONE_HOUR))
        val copy = SpecialReleaseTimerCopyGenerator.getCopy(currentTimeDate = currentTimeDate, expiredTimeDate = expiredTimeDate)
        Assert.assertEquals("Sisa 2 Jam", copy)
    }

    @Test
    fun `given current time have diff below 1 hours with expired time on getCopy then should return Sisa x menit`() {
        val expiredTime = "2022-02-10T23:59:59+07:00"
        val expiredTimeDate = getExpiredTime(expiredTime)
        val currentTimeDate = expiredTimeDate.getDateBefore((15* ONE_MINUTE))
        val copy = SpecialReleaseTimerCopyGenerator.getCopy(currentTimeDate = currentTimeDate, expiredTimeDate = expiredTimeDate)
        Assert.assertEquals("Sisa 15 Menit", copy)
    }

    @Test
    fun `given current time have diff below 15 minutes with expired time on getCopy then should return Segera berakhir`() {
        val expiredTime = "2022-02-10T23:59:59+07:00"
        val expiredTimeDate = getExpiredTime(expiredTime)
        val currentTimeDate = expiredTimeDate.getDateBefore((2* ONE_MINUTE))
        val copy = SpecialReleaseTimerCopyGenerator.getCopy(currentTimeDate = currentTimeDate, expiredTimeDate = expiredTimeDate)
        Assert.assertEquals("Segera Berakhir", copy)
    }

    private fun Date.getDateBefore(offsetMs: Long): Date {
        val expiredTimeMs = time
        val twoDaysBeforeMs = expiredTimeMs - offsetMs
        return Date().apply { time = twoDaysBeforeMs }
    }

    /**
     * SimpleDateFormat class from JUnit and Android is different,
     * in JUnit we need to use X as timezone
     * in Android we need to use Z as timezone
     *
     * So I created getExpiredTime function in unit test instead of directly use it from our Android
     * repository in DateHelper
     *
     * https://stackoverflow.com/questions/34686477/junit4-test-causes-java-text-parseexception-unparseable-date
     */
    fun getExpiredTime(expiredTimeString: String?): Date {
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX")
        return try {
            format.parse(expiredTimeString)
        } catch (e: Exception) {
            e.printStackTrace()
            Date()
        }
    }
}