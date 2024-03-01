package com.tokopedia.search.utils

import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey.SEARCH_DEBUG_LOG
import com.tokopedia.search.utils.SearchLogger.Companion.DISCOVERY_SEARCH_DEBUG_TAG
import com.tokopedia.search.utils.SearchLogger.Companion.FILTER
import com.tokopedia.search.utils.SearchLogger.Companion.KEYWORD
import com.tokopedia.search.utils.SearchLogger.Companion.USER_VERSION_SEPARATOR
import com.tokopedia.search.utils.SearchLogger.Companion.VERSION_CODE_SEPARATOR
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.mockk.verify
import okhttp3.internal.http2.ConnectionShutdownException
import org.apache.commons.lang3.exception.ExceptionUtils
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.InterruptedIOException
import java.net.ConnectException
import java.net.SocketException
import java.net.UnknownHostException

internal class SearchLoggerTest {

    private val remoteConfig = mockk<RemoteConfig>(relaxed = true)
    private val searchLogger = SearchLogger(
        remoteConfig,
        DUMMY_VERSION_CODE,
        DUMMY_USER_ID
    )

    @Before
    fun setUp() {
        mockkStatic(ServerLogger::class)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `logWarning with null message or null exception will not log`() {
        searchLogger.logWarning(null, Exception())
        assertNoLogging()

        searchLogger.logWarning("message", null)
        assertNoLogging()
    }

    private fun assertNoLogging() {
        verify(exactly = 0) {
            ServerLogger.log(any(), any(), any())
        }
    }

    @Test
    fun `logWarning with message and exception will log search error`() {
        val errorMessage = "message"
        val exception = Exception()

        searchLogger.logWarning(errorMessage, exception)

        assertDiscoverySearchErrorLog(errorMessage, ExceptionUtils.getStackTrace(exception))
    }

    private fun assertDiscoverySearchErrorLog(
        expectedMessage: String = "",
        expectedStackTrace: String = ""
    ) {
        verify {
            ServerLogger.log(
                Priority.P2,
                SearchLogger.DISCOVERY_SEARCH_ERROR_TAG,
                mapOf(
                    "type" to expectedMessage,
                    "error" to expectedStackTrace,
                ),
            )
        }
    }

    @Test
    fun `logWarning with excluded exception will not log`() {
        val errorMessage = "message"

        searchLogger.logWarning(errorMessage, UnknownHostException())
        assertNoLogging()

        searchLogger.logWarning(errorMessage, SocketException())
        assertNoLogging()

        searchLogger.logWarning(errorMessage, InterruptedIOException())
        assertNoLogging()

        searchLogger.logWarning(errorMessage, ConnectionShutdownException())
        assertNoLogging()

        searchLogger.logWarning(errorMessage, ConnectException())
        assertNoLogging()
    }

    @Test
    fun `logSearchDebug with null or empty keyword will not send log`() {
        searchLogger.logSearchDebug(null)

        assertNoLogging()
    }

    @Test
    fun `logSearchDebug will send log when version code is true`() {
        `given version code toggle is enabled`()

        val keyword = "samsung"
        searchLogger.logSearchDebug(keyword)

        assertLogSearchDebug(keyword)
    }

    @Test
    fun `logSearchDebug will send log when user id is true`() {
        `given user id toggle is enabled`()

        val keyword = "samsung"
        searchLogger.logSearchDebug(keyword)

        assertLogSearchDebug(keyword)
    }

    private fun `given version code toggle is enabled`() {
        every { remoteConfig.getString(SEARCH_DEBUG_LOG) } returns DUMMY_VERSION_CODE.toString()
    }

    private fun `given user id toggle is enabled`() {
        every { remoteConfig.getString(SEARCH_DEBUG_LOG) } returns DUMMY_USER_ID
    }

    private fun assertLogSearchDebug(
        expectedKeyword: String = "",
        expectedFilterParams: String = "",
    ) {
        verify {
            ServerLogger.log(
                Priority.P2,
                DISCOVERY_SEARCH_DEBUG_TAG,
                mapOf(
                    KEYWORD to expectedKeyword,
                    FILTER to expectedFilterParams,
                ),
            )
        }
    }

    @Test
    fun `logSearchDebug can send filter params`() {
        `given version code toggle is enabled`()

        val keyword = "samsung"
        val filterParams = "is_official=true"
        searchLogger.logSearchDebug(keyword, filterParams)

        assertLogSearchDebug(keyword, filterParams)
    }

    @Test
    fun `logSearchDebug will not send log if toggle is false`() {
        `given debug toggle is disabled`()

        searchLogger.logSearchDebug("samsung")

        assertNoLogging()
    }

    private fun `given debug toggle is disabled`() {
        every { remoteConfig.getString(SEARCH_DEBUG_LOG) } returns ""
    }

    @Test
    fun `logSearchDebug remote config can support multiple versions`() {
        val configVersionCode1 = "300400100"
        val configVersionCode2 = "300400101"

        `given config with multiple version code`(listOf(configVersionCode1, configVersionCode2))

        var searchLogger: SearchLogger

        val keyword1 = "samsung"
        searchLogger = SearchLogger(remoteConfig, configVersionCode1.toInt())
        searchLogger.logSearchDebug(keyword1)
        assertLogSearchDebug(keyword1)

        val keyword2 = "samsung galaxy"
        searchLogger = SearchLogger(remoteConfig, configVersionCode2.toInt())
        searchLogger.logSearchDebug(keyword2)
        assertLogSearchDebug(keyword2)

        val keyword3 = "iphone"
        searchLogger = SearchLogger(remoteConfig, "300500999".toInt())
        searchLogger.logSearchDebug(keyword3)
        verify(exactly = 0) {
            ServerLogger.log(any(), keyword3, any())
        }
    }

    @Test
    fun `logSearchDebug remote config can support filtering with user id AND version code`() {
        val configVersionCode1 = "300400100"
        val userId = "123123"

        `given config with user id AND version code`(userId, configVersionCode1)

        val keyword1 = "samsung"
        val searchLogger: SearchLogger = SearchLogger(remoteConfig, configVersionCode1.toInt())
        searchLogger.logSearchDebug(keyword1)
        assertLogSearchDebug(keyword1)
    }

    // todo
    @Test
    fun `logSearchDebug remote config can support filtering with multiple user ids and multiple version codes`() {
        val userId1 = "1937403"
        val userId2 = "9563845"

        val configVersionCode1 = "300400100"
        val configVersionCode2 = "300400101"

        `given config with multiple user ids AND version codes`(
            userId = listOf(userId1, userId2),
            versionCode = listOf(configVersionCode1, configVersionCode2)
        )

        var searchLogger: SearchLogger

        val keyword1 = "samsung"
        searchLogger =
            SearchLogger(remoteConfig, userId = userId1, versionCode = configVersionCode1.toInt())
        searchLogger.logSearchDebug(keyword1)
        assertLogSearchDebug(keyword1)

        val keyword12 = "oppo"
        searchLogger =
            SearchLogger(remoteConfig, userId = userId1, versionCode = configVersionCode2.toInt())
        searchLogger.logSearchDebug(keyword12)
        assertLogSearchDebug(keyword12)

        val keyword2 = "samsung galaxy"
        searchLogger =
            SearchLogger(remoteConfig, userId = userId2, versionCode = configVersionCode2.toInt())
        searchLogger.logSearchDebug(keyword2)
        assertLogSearchDebug(keyword2)

        val keyword21 = "huawei"
        searchLogger =
            SearchLogger(remoteConfig, userId = userId2, versionCode = configVersionCode1.toInt())
        searchLogger.logSearchDebug(keyword21)
        assertLogSearchDebug(keyword21)

        val keyword3 = "iphone"
        searchLogger = SearchLogger(remoteConfig, userId = "998998", versionCode = 77658394)
        searchLogger.logSearchDebug(keyword3)
        verify(exactly = 0) {
            ServerLogger.log(any(), keyword3, any())
        }

        val keyword4 = "redmi"
        searchLogger = SearchLogger(remoteConfig, userId = userId2, versionCode = 22222)
        searchLogger.logSearchDebug(keyword4)
        assertLogSearchDebug(keyword4)

        val keyword5 = "nokia"
        searchLogger = SearchLogger(
            remoteConfig,
            userId = "48247859",
            versionCode = configVersionCode2.toInt()
        )
        searchLogger.logSearchDebug(keyword5)
        assertLogSearchDebug(keyword5)
    }

    @Test
    fun `logSearchDebug remote config can support filtering with multiple user id`() {
        val userId1 = "1937403"
        val userId2 = "9563845"

        `given config with multiple user ids`(listOf(userId1, userId2))

        var searchLogger: SearchLogger

        val keyword1 = "samsung"
        searchLogger = SearchLogger(remoteConfig, userId = userId1)
        searchLogger.logSearchDebug(keyword1)
        assertLogSearchDebug(keyword1)

        val keyword2 = "samsung galaxy"
        searchLogger = SearchLogger(remoteConfig, userId = userId2)
        searchLogger.logSearchDebug(keyword2)
        assertLogSearchDebug(keyword2)

        val keyword3 = "iphone"
        searchLogger = SearchLogger(remoteConfig, userId = "998998")
        searchLogger.logSearchDebug(keyword3)
        verify(exactly = 0) {
            ServerLogger.log(any(), keyword3, any())
        }
    }

    private fun `given config with multiple version code`(versionCodes: List<String>) {
        val configVersionCode = versionCodes.joinToString(separator = VERSION_CODE_SEPARATOR)

        every { remoteConfig.getString(SEARCH_DEBUG_LOG) } returns configVersionCode
    }

    private fun `given config with multiple user ids`(userIds: List<String>) {
        val remoteConfigValue = userIds.joinToString(separator = VERSION_CODE_SEPARATOR)

        every { remoteConfig.getString(SEARCH_DEBUG_LOG) } returns remoteConfigValue
    }

    private fun `given config with user id AND version code`(userId: String, versionCode: String) {
        val remoteConfigValue = "$versionCode$USER_VERSION_SEPARATOR$userId"

        every { remoteConfig.getString(SEARCH_DEBUG_LOG) } returns remoteConfigValue
    }

    private fun `given config with multiple user ids AND version codes`(
        userId: List<String>,
        versionCode: List<String>
    ) {
        val remoteConfigValue =
            "${versionCode.joinToString(separator = VERSION_CODE_SEPARATOR)}$USER_VERSION_SEPARATOR${
                userId.joinToString(
                    separator = VERSION_CODE_SEPARATOR
                )
            }"

        every { remoteConfig.getString(SEARCH_DEBUG_LOG) } returns remoteConfigValue
    }

    @Test
    fun `logSearchDebug will not log without versionCode`() {
        `given version code toggle is enabled`()

        val searchLogger = SearchLogger(remoteConfig, null)
        searchLogger.logSearchDebug("samsung")

        assertNoLogging()
    }

    companion object {
        private const val DUMMY_VERSION_CODE = 300400100
        private const val DUMMY_USER_ID = "123412"
    }
}
