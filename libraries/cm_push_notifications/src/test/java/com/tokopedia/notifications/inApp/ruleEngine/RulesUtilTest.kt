package com.tokopedia.notifications.inApp.ruleEngine

import android.os.SystemClock
import com.tokopedia.notifications.inApp.ruleEngine.repository.RepositoryManager
import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.ElapsedTime
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*

class RulesUtilTest {

    @Before
    @Throws(Exception::class)
    fun setUp() {
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
    }

    /*startTime = 1582525504408
    endTime = 1583130304408
    currentTimeStamp = 1582525568345
    lastElapsedTime = 293396879
    deltaTime = 132895
    updatedLastElapsedTime = 293557321
    correctedCurrentTime = 1582525701240*/
    @Test
    fun isValidTimeFrameReturnsTrue() {
        val startTime = 1582525504408L
        val endTime = 1583130304408L
        val currentTimeStamp = 1582525568345L
        val lastElapsedTime = ElapsedTime()
        lastElapsedTime.elapsedTime = 293396879

        mockkStatic(SystemClock::class)
        mockkStatic(RulesUtil::class)
        mockkStatic(RepositoryManager::class)
        every { SystemClock.elapsedRealtime() } returns 293529774L

        every { RepositoryManager.getInstance().storageProvider.putElapsedTimeToStore(lastElapsedTime).subscribe() } returns mockk()

        assertTrue(RulesUtil.isValidTimeFrame(startTime, endTime, currentTimeStamp, lastElapsedTime))
    }

    @Test
    fun isValidTimeFrameReturnsFalse() {
        val startTime = 1582525568345L + 132896
        val endTime = 1583130304408L
        val currentTimeStamp = 1582525568345L
        val lastElapsedTime = ElapsedTime()
        lastElapsedTime.elapsedTime = 293396879

        mockkStatic(SystemClock::class)
        mockkStatic(RulesUtil::class)
        mockkStatic(RepositoryManager::class)
        every { SystemClock.elapsedRealtime() } returns 293529774L

        every { RepositoryManager.getInstance().storageProvider.putElapsedTimeToStore(lastElapsedTime).subscribe() } returns mockk()

        assertFalse(RulesUtil.isValidTimeFrame(startTime, endTime, currentTimeStamp, lastElapsedTime))
    }
}