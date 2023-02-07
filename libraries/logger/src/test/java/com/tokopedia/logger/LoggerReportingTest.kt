package com.tokopedia.logger

import com.tokopedia.logger.model.newrelic.NewRelicConfig
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
        val expectedTagMapsNewRelic = mutableMapOf<String, Tag>().apply {
            put("P1#DEV_CRASH", Tag(TestUtilsHelper.getPriority("offline")))
            put("P1#DEV_ANR", Tag(TestUtilsHelper.getPriority("offline")))
            put("P1#DEV_TOO_LARGE", Tag(TestUtilsHelper.getPriority("offline")))
            put("P1#DFM", Tag(TestUtilsHelper.getPriority("offline")))
            put("P1#DFM_DEFERRED", Tag(TestUtilsHelper.getPriority("offline")))
            put("P1#DFM_FALLBACK", Tag(TestUtilsHelper.getPriority("offline")))
            put("P1#DFM_OPENED", Tag(TestUtilsHelper.getPriority("offline")))
            put("P1#DFM_DOWNLOAD_PAGE", Tag(TestUtilsHelper.getPriority("offline")))
        }

        LoggerReporting.getInstance().setPopulateTagMapsNewRelic(mockTagNewRelic)

        LoggerReporting.getInstance().tagMapsNewRelic.keys.forEach { key ->
            val keyActualResult = LoggerReporting.getInstance().tagMapsNewRelic.entries.first {
                it.value == LoggerReporting.getInstance().tagMapsNewRelic.getValue(key)
            }.key
            val valueActualResult = LoggerReporting.getInstance().tagMapsNewRelic.getValue(key).postPriority
            assertEquals(keyActualResult, expectedTagMapsNewRelic.entries.firstOrNull { it.key == key }?.key)
            assertEquals(valueActualResult, expectedTagMapsNewRelic.getValue(key).postPriority)
        }
    }

    @Test
    fun `set populate tag maps of embrace should return equal value`() {
        val mockEmbrace = TestUtilsHelper.createSuccessResponse(EMBRACE_SUCCESS_RESPONSE) as DataLogConfig
        val mockTagEmbrace = mockEmbrace.tags
        val expectedTagEmbrace = mutableMapOf<String, Tag>().apply {
            put("P1#DEV_CRASH", Tag(TestUtilsHelper.getPriority("offline")))
            put("P1#DEV_ANR", Tag(TestUtilsHelper.getPriority("offline")))
            put("P1#DEV_TOO_LARGE", Tag(TestUtilsHelper.getPriority("offline")))
            put("P1#DFM", Tag(TestUtilsHelper.getPriority("offline")))
            put("P1#DFM_DEFERRED", Tag(TestUtilsHelper.getPriority("offline")))
            put("P1#DFM_FALLBACK", Tag(TestUtilsHelper.getPriority("offline")))
            put("P1#DFM_OPENED", Tag(TestUtilsHelper.getPriority("offline")))
            put("P1#DFM_DOWNLOAD_PAGE", Tag(TestUtilsHelper.getPriority("offline")))
        }

        LoggerReporting.getInstance().setPopulateTagMapsEmbrace(mockTagEmbrace)

        LoggerReporting.getInstance().tagMapsEmbrace.keys.forEach { key ->
            val keyActualResult = LoggerReporting.getInstance().tagMapsEmbrace.entries.first {
                it.value == LoggerReporting.getInstance().tagMapsEmbrace.getValue(key)
            }.key
            val valueActualResult = LoggerReporting.getInstance().tagMapsEmbrace.getValue(key).postPriority
            assertEquals(keyActualResult, expectedTagEmbrace.entries.firstOrNull { it.key == key }?.key)
            assertEquals(valueActualResult, expectedTagEmbrace.getValue(key).postPriority)
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
    fun `set populate tag maps of embrace with empty value should return false`() {
        val mockEmbrace = TestUtilsHelper.createSuccessResponse(EMBRACE_FAIL_RESPONSE) as DataLogConfig
        val mockTagEmbrace = mockEmbrace.tags
        val expectedTagMapsEmbrace = mutableMapOf<String, Tag>().apply {
            put("P1#DEV_CRASH", Tag(TestUtilsHelper.getPriority("offline")))
            put("P1#DEV_ANR", Tag(TestUtilsHelper.getPriority("offline")))
            put("P1#DEV_TOO_LARGE", Tag(TestUtilsHelper.getPriority("offline")))
            put("P1#DFM", Tag(TestUtilsHelper.getPriority("offline")))
            put("P1#DFM_DEFERRED", Tag(TestUtilsHelper.getPriority("offline")))
        }

        LoggerReporting.getInstance().setPopulateTagMapsEmbrace(mockTagEmbrace)

        LoggerReporting.getInstance().tagMapsEmbrace.keys.forEach { key ->
            val keyActualResult = LoggerReporting.getInstance().tagMapsEmbrace.entries.first {
                it.value == LoggerReporting.getInstance().tagMapsEmbrace.getValue(key)
            }.key
            val valueActualResult = LoggerReporting.getInstance().tagMapsEmbrace.getValue(key).postPriority
            assertTrue(keyActualResult != expectedTagMapsEmbrace.entries.firstOrNull { it.key == key }?.key)
            assertTrue(valueActualResult != expectedTagMapsEmbrace.getValue(key).postPriority)
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
    fun `set query limits of embrace with value should return equal`() {
        val mockEmbrace = TestUtilsHelper.createSuccessResponse(EMBRACE_SUCCESS_RESPONSE) as DataLogConfig
        val mockQueryLimitsEmbrace = mockEmbrace.queryLimits
        val expectedQueryLimitsEmbrace = mutableListOf<Int>().apply {
            add(50)
            add(50)
            add(50)
        }

        LogManager.queryLimits = mockQueryLimitsEmbrace?.toList() ?: listOf()

        LogManager.queryLimits.forEachIndexed { index, query ->
            assertEquals(query, expectedQueryLimitsEmbrace[index])
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

    @Test
    fun `set query limits of embrace with empty value should return empty`() {
        val mockEmbrace = TestUtilsHelper.createSuccessResponse(EMBRACE_FAIL_RESPONSE) as DataLogConfig
        val mockQueryLimitsEmbrace = mockEmbrace.queryLimits

        LogManager.queryLimits = mockQueryLimitsEmbrace?.toList() ?: listOf()

        assertTrue(LogManager.queryLimits.isEmpty())
    }

    @Test
    fun `set populate key maps of new relic, then should return success`() {
        val mockNewRelicV3 = TestUtilsHelper.createSuccessResponse(NEW_RELIC_SUCCESS_V3_RESPONSE) as DataLogConfig
        val mockKeysNewRelicV3 = mockNewRelicV3.keys
        val expectedKeysNrV3 = hashMapOf<String, NewRelicConfig>().apply {
            put("key_new_relic_android", NewRelicConfig("31345657577", "OnReGNYndyS6M1MnEhHsBtrXywe3EDSb+dLT3p4kGuDjO4ART5YhSgUJLPp9qKRh5DAd1ZjSIUQOBoGeeSE1L4sWGyZg9aaFOIG2R9VdbyTdSVyZVXwyQlbGFFBQlCRgchrwDOftEarhCj2fkCcvzEaT31R0QDZKUWHoPaFVJ12Oqt99KjX+jInVCE0iurniMInp4sY3dYK2hiAtNBWpZxH6uzeArWWQqfgMfH5peLEReDoFPv8WNowzk//cGP0ajgCzvGwuo/er34+v6wJkHrHEFDgoTSMSgc5u000HCD4ulAbttWXkHMYwsdZMhiraw0DHYVBSoFiefzZt9V0F9A=="))
        }

        LoggerReporting.getInstance().setPopulateKeyMapsNewRelic(mockKeysNewRelicV3)

        LoggerReporting.getInstance().tagMapsNrKey.forEach { (key, newRelicConfig) ->
            assertEquals(newRelicConfig, expectedKeysNrV3[key])
        }
    }

    @Test
    fun `set populate key tables of new relic, then should return success`() {
        val mockNewRelicV3 = TestUtilsHelper.createSuccessResponse(NEW_RELIC_SUCCESS_V3_RESPONSE) as DataLogConfig
        val mockTablesNewRelicV3 = mockNewRelicV3.tables
        val expectedQueryLimitsNrV3 = hashMapOf<String, String>().apply {
            put("table_sf", "androidsf")
        }

        LoggerReporting.getInstance().setPopulateTableMapsNewRelic(mockTablesNewRelicV3)

        LoggerReporting.getInstance().tagMapsNrTable.forEach { (key, value) ->
            assertEquals(value, expectedQueryLimitsNrV3[key])
        }
    }

    companion object {
        const val SCALYR_SUCCESS_RESPONSE = "json/mock_scalyr_success_response.json"
        const val NEW_RELIC_SUCCESS_RESPONSE = "json/mock_new_relic_success_response.json"
        const val NEW_RELIC_SUCCESS_V3_RESPONSE = "json/mock_new_relic_v3_success_response.json"
        const val SCALYR_FAIL_RESPONSE = "json/mock_scalyr_fail_response.json"
        const val NEW_RELIC_FAIL_RESPONSE = "json/mock_new_relic_fail_response.json"
        const val EMBRACE_SUCCESS_RESPONSE = "json/mock_embrace_success_response.json"
        const val EMBRACE_FAIL_RESPONSE = "json/mock_embrace_fail_response.json"
    }
}
