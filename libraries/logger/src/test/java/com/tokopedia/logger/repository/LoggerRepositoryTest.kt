package com.tokopedia.logger.repository

import com.tokopedia.logger.datasource.db.Logger
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Test

class LoggerRepositoryTest : LoggerRepositoryTestFixture() {

    @Test
    fun `insert log should return success`() {
        runBlocking(Dispatchers.IO) {
            val loggerList = mutableListOf<Logger>().apply {
                add(Logger(1500L, "P1", 1, """
                    {"log_tag":"DEV_CRASH","log_timestamp":"1617557576322","log_time":"05/04/2021 00:32:56:322","log_did":"972e71fc8257e7e4","log_uid":"12299749","log_vernm":"2.00.123","log_vercd":"200000123","log_os":"10","log_device":"SM-A315G","log_packageName":"com.tokopedia.sellerapp","log_installer":"","log_debug":"true","log_priority":"1","ABC":"123","EDF":"456"}
                """.trimIndent()))
                add(Logger(1500L, "P1", 1, """
                    {"log_tag":"DEV_CRASH","log_timestamp":"1617557576322","log_time":"05/04/2021 00:32:56:322","log_did":"972e71fc8257e7e4","log_uid":"12299749","log_vernm":"2.00.123","log_vercd":"200000123","log_os":"10","log_device":"SM-A315G","log_packageName":"com.tokopedia.sellerapp","log_installer":"","log_debug":"true","log_priority":"1","ABC":"123","EDF":"456"}
                """.trimIndent()))
            }

            loggerList.forEach {
                coEvery {
                    loggerDao.insert(it)
                } just Runs
                loggerRepository.insert(it)
                encrypt?.let { encrypt ->
                    coVerify {
                        loggerDao.insert(it.copy(message = encrypt.invoke(it.message)))
                    }
                }
            }
        }
    }

    @Test
    fun `delete log should return success`() {
        runBlocking(Dispatchers.IO) {
            val loggerList = mutableListOf<Logger>().apply {
                add(Logger(1500L, "P1", 1, """
                    {"log_tag":"DEV_CRASH","log_timestamp":"1617557576322","log_time":"05/04/2021 00:32:56:322","log_did":"972e71fc8257e7e4","log_uid":"12299749","log_vernm":"2.00.123","log_vercd":"200000123","log_os":"10","log_device":"SM-A315G","log_packageName":"com.tokopedia.sellerapp","log_installer":"","log_debug":"true","log_priority":"1","ABC":"123","EDF":"456"}
                """.trimIndent()))
                add(Logger(1500L, "P1", 1, """
                    {"log_tag":"DEV_CRASH","log_timestamp":"1617557576322","log_time":"05/04/2021 00:32:56:322","log_did":"972e71fc8257e7e4","log_uid":"12299749","log_vernm":"2.00.123","log_vercd":"200000123","log_os":"10","log_device":"SM-A315G","log_packageName":"com.tokopedia.sellerapp","log_installer":"","log_debug":"true","log_priority":"1","ABC":"123","EDF":"456"}
                """.trimIndent()))
            }

            coEvery { loggerDao.deleteEntries(loggerList) } just Runs

            loggerRepository.deleteEntries(loggerList)

            coVerify { loggerDao.deleteEntries(loggerList) }
        }
    }
}