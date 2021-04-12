package com.tokopedia.logger.repository

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.logger.datasource.db.Logger
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P])
class LoggerRepositoryTest: LoggerRepositoryTestFixture() {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun `insert logger should return success`() {
        runBlocking {
            val log = Logger(15000L, "P1", 1, """
            {"log_tag":"DEV_CRASH","log_timestamp":"1617557576322","log_time":"05/04/2021 00:32:56:322","log_did":"972e71fc8257e7e4","log_uid":"12299749","log_vernm":"2.00.123","log_vercd":"200000123","log_os":"10","log_device":"SM-A315G","log_packageName":"com.tokopedia.sellerapp","log_installer":"","log_debug":"true","log_priority":"1","ABC":"123","EDF":"456"}
        """.trimIndent())

            coEvery { loggerDao.insert(log) } returns Unit

            loggerRepository.insert(log)

            coVerify { loggerDao.insert(log) }

        }
    }
}