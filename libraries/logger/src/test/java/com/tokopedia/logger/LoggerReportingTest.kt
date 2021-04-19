package com.tokopedia.logger

import com.tokopedia.logger.utils.DataLogConfig
import com.tokopedia.logger.utils.LoggerReporting
import com.tokopedia.logger.utils.Tag
import com.tokopedia.logger.utils.TestUtilsHelper
import org.junit.Assert.*
import org.junit.Test

class LoggerReportingTest {

    @Test
    fun `set populate tag maps of scalyr should return equal value`() {
        val mockScalyr = TestUtilsHelper.createSuccessResponse(SCALYR_SUCCESS_RESPONSE) as DataLogConfig
        val mockTagScalyr = mockScalyr.tags?.take(5)
        val expectedTagMapsScalyr = mutableMapOf<String, Tag>().apply {
            put("P1#DEV_CRASH", Tag(TestUtilsHelper.getPriority("offline")))
            put("P1#DEV_ANR", Tag(TestUtilsHelper.getPriority("offline")))
            put("P1#DEV_TOO_LARGE", Tag(TestUtilsHelper.getPriority("offline")))
            put("P1#DFM", Tag(TestUtilsHelper.getPriority("offline")))
            put("P1#DFM_DEFERRED", Tag(TestUtilsHelper.getPriority("offline")))
        }

        LoggerReporting.getInstance().setPopulateTagMapsScalyr(mockTagScalyr)

        LoggerReporting.getInstance().tagMapsScalyr.keys.forEach { key ->
            val keyActualResult = LoggerReporting.getInstance().tagMapsScalyr.entries.first {
                it.value == LoggerReporting.getInstance().tagMapsScalyr.getValue(key)
            }.key
            val valueActualResult = LoggerReporting.getInstance().tagMapsScalyr.getValue(key).postPriority
            assertEquals(keyActualResult, expectedTagMapsScalyr.entries.firstOrNull { it.key == key }?.key)
            assertEquals(valueActualResult, expectedTagMapsScalyr.getValue(key).postPriority)
        }
    }

    @Test
    fun `set populate tag maps of new relic should return equal value`() {
        val mockNewRelic = TestUtilsHelper.createSuccessResponse(NEW_RELIC_SUCCESS_RESPONSE) as DataLogConfig
        val mockTagNewRelic = mockNewRelic.tags
        val expectedTagMapsScalyr = mutableMapOf<String, Tag>().apply {
            put("P1#DEV_CRASH", Tag(TestUtilsHelper.getPriority("offline")))
            put("P1#DEV_ANR", Tag(TestUtilsHelper.getPriority("offline")))
            put("P1#DEV_TOO_LARGE", Tag(TestUtilsHelper.getPriority("offline")))
            put("P1#DFM", Tag(TestUtilsHelper.getPriority("offline")))
            put("P1#DFM_DEFERRED", Tag(TestUtilsHelper.getPriority("offline")))
            put("P1#DFM_FALLBACK", Tag(TestUtilsHelper.getPriority("offline")))
        }

        LoggerReporting.getInstance().setPopulateTagMapsNewRelic(mockTagNewRelic)

        LoggerReporting.getInstance().tagMapsScalyr.keys.forEach { key ->
            val keyActualResult = LoggerReporting.getInstance().tagMapsScalyr.entries.first {
                it.value == LoggerReporting.getInstance().tagMapsScalyr.getValue(key)
            }.key
            val valueActualResult = LoggerReporting.getInstance().tagMapsScalyr.getValue(key).postPriority
            assertEquals(keyActualResult, expectedTagMapsScalyr.entries.firstOrNull { it.key == key }?.key)
            assertEquals(valueActualResult, expectedTagMapsScalyr.getValue(key).postPriority)
        }
    }

    @Test
    fun `set populate tag maps of scalyr with empty value should return false`() {
        val mockScalyr = TestUtilsHelper.createSuccessResponse(SCALYR_FAIL_RESPONSE) as DataLogConfig
        val mockTagScalyr = mockScalyr.tags?.take(5)
        val expectedTagMapsScalyr = mutableMapOf<String, Tag>().apply {
            put("P1#DEV_CRASH", Tag(TestUtilsHelper.getPriority("offline")))
            put("P1#DEV_ANR", Tag(TestUtilsHelper.getPriority("offline")))
            put("P1#DEV_TOO_LARGE", Tag(TestUtilsHelper.getPriority("offline")))
            put("P1#DFM", Tag(TestUtilsHelper.getPriority("offline")))
            put("P1#DFM_DEFERRED", Tag(TestUtilsHelper.getPriority("offline")))
        }

        LoggerReporting.getInstance().setPopulateTagMapsScalyr(mockTagScalyr)

        LoggerReporting.getInstance().tagMapsScalyr.keys.forEach { key ->
            val keyActualResult = LoggerReporting.getInstance().tagMapsScalyr.entries.first {
                it.value == LoggerReporting.getInstance().tagMapsScalyr.getValue(key)
            }.key
            val valueActualResult = LoggerReporting.getInstance().tagMapsScalyr.getValue(key).postPriority
            assertTrue(keyActualResult != expectedTagMapsScalyr.entries.firstOrNull { it.key == key }?.key)
            assertTrue(valueActualResult != expectedTagMapsScalyr.getValue(key).postPriority)
        }
    }

    @Test
    fun `set populate tag maps of new relic with empty value should return false`() {
        val mockNewRelic = TestUtilsHelper.createSuccessResponse(NEW_RELIC_FAIL_RESPONSE) as DataLogConfig
        val mockTagNewRelic = mockNewRelic.tags
        val expectedTagMapsNewRelic = mutableMapOf<String, Tag>().apply {
            put("P1#DEV_CRASH", Tag(TestUtilsHelper.getPriority("offline")))
            put("P1#DEV_ANR", Tag(TestUtilsHelper.getPriority("offline")))
            put("P1#DEV_TOO_LARGE", Tag(TestUtilsHelper.getPriority("offline")))
            put("P1#DFM", Tag(TestUtilsHelper.getPriority("offline")))
            put("P1#DFM_DEFERRED", Tag(TestUtilsHelper.getPriority("offline")))
        }

        LoggerReporting.getInstance().setPopulateTagMapsNewRelic(mockTagNewRelic)

        LoggerReporting.getInstance().tagMapsNewRelic.keys.forEach { key ->
            val keyActualResult = LoggerReporting.getInstance().tagMapsScalyr.entries.first {
                it.value == LoggerReporting.getInstance().tagMapsScalyr.getValue(key)
            }.key
            val valueActualResult = LoggerReporting.getInstance().tagMapsNewRelic.getValue(key).postPriority
            assertTrue(keyActualResult != expectedTagMapsNewRelic.entries.firstOrNull { it.key == key }?.key)
            assertTrue(valueActualResult != expectedTagMapsNewRelic.getValue(key).postPriority)
        }
    }

    @Test
    fun `set query limits of scalyr with value should return equal`() {
        val mockScalyr = TestUtilsHelper.createSuccessResponse(SCALYR_SUCCESS_RESPONSE) as DataLogConfig
        val mockQueryLimitsScalyr = mockScalyr.queryLimits
        val expectedQueryLimitsScalyr = mutableListOf<Int>().apply {
            add(50)
            add(50)
        }

        LogManager.queryLimits = mockQueryLimitsScalyr?.toList() ?: listOf()

        LogManager.queryLimits.forEachIndexed { index, query ->
            assertEquals(query, expectedQueryLimitsScalyr[index])
        }
    }


    @Test
    fun `set query limits of new relic with value should return equal`() {
        val mockNewRelic = TestUtilsHelper.createSuccessResponse(NEW_RELIC_SUCCESS_RESPONSE) as DataLogConfig
        val mockQueryLimitsNewRelic = mockNewRelic.queryLimits
        val expectedQueryLimitsNewRelic = mutableListOf<Int>().apply {
            add(50)
            add(50)
            add(50)
        }

        LogManager.queryLimits = mockQueryLimitsNewRelic?.toList() ?: listOf()

        LogManager.queryLimits.forEachIndexed { index, query ->
            assertEquals(query, expectedQueryLimitsNewRelic[index])
        }
    }

    @Test
    fun `set query limits of scalyr with empty value should return empty`() {
        val mockScalyr = TestUtilsHelper.createSuccessResponse(SCALYR_FAIL_RESPONSE) as DataLogConfig
        val mockQueryLimitsScalyr = mockScalyr.queryLimits

        LogManager.queryLimits = mockQueryLimitsScalyr?.toList() ?: listOf()

        assertTrue(LogManager.queryLimits.isEmpty())
    }

    @Test
    fun `set query limits of new relic with empty value should return empty`() {
        val mockNewRelic = TestUtilsHelper.createSuccessResponse(NEW_RELIC_FAIL_RESPONSE) as DataLogConfig
        val mockQueryLimitsNewRelic = mockNewRelic.queryLimits

        LogManager.queryLimits = mockQueryLimitsNewRelic?.toList() ?: listOf()

        assertTrue(LogManager.queryLimits.isEmpty())
    }

    companion object {
        const val SCALYR_SUCCESS_RESPONSE = "json/mock_scalyr_success_response.json"
        const val NEW_RELIC_SUCCESS_RESPONSE = "json/mock_new_relic_success_response.json"
        const val SCALYR_FAIL_RESPONSE = "json/mock_scalyr_fail_response.json"
        const val NEW_RELIC_FAIL_RESPONSE = "json/mock_new_relic_fail_response.json"
    }
}