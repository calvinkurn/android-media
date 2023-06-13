package com.tokopedia.search.utils

import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey.SEARCH_DEBUG_LOG
import com.tokopedia.search.utils.SearchLogger.Companion.DISCOVERY_SEARCH_DEBUG_TAG
import com.tokopedia.search.utils.SearchLogger.Companion.FILTER
import com.tokopedia.search.utils.SearchLogger.Companion.KEYWORD
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
        verify (exactly = 0) {
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
    fun `logSearchDebug will send log when toggle is true`() {
        `given debug toggle is enabled`()

        val keyword = "samsung"
        searchLogger.logSearchDebug(keyword)

        assertLogSearchDebug(keyword)
    }

    private fun `given debug toggle is enabled`() {
        every { remoteConfig.getString(SEARCH_DEBUG_LOG) } returns DUMMY_VERSION_CODE.toString()
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
        `given debug toggle is enabled`()

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
        verify (exactly = 0) {
            ServerLogger.log(any(), keyword3, any())
        }
    }

    private fun `given config with multiple version code`(versionCodes: List<String>) {
        val configVersionCode = versionCodes.joinToString(separator = VERSION_CODE_SEPARATOR)

        every { remoteConfig.getString(SEARCH_DEBUG_LOG) } returns configVersionCode
    }

    @Test
    fun `logSearchDebug will not log without versionCode`() {
        `given debug toggle is enabled`()

        val searchLogger = SearchLogger(remoteConfig, null)
        searchLogger.logSearchDebug("samsung")

        assertNoLogging()
    }

    companion object {
        private const val DUMMY_VERSION_CODE = 300400100
    }
}
