package com.tokopedia.search.utils

import com.tokopedia.search.shouldBe
import okhttp3.internal.http2.ConnectionShutdownException
import org.apache.commons.lang3.exception.ExceptionUtils
import org.junit.Test
import java.io.InterruptedIOException
import java.net.ConnectException
import java.net.SocketException
import java.net.UnknownHostException

internal class SearchLoggerTest {

    class SearchLoggerMock : SearchLogger() {
        private var isTimberCalled = false
        private var message = ""
        private var stackTrace = ""

        override fun timberLogWarning(message: String, stackTrace: String) {
            this.isTimberCalled = true
            this.message = message
            this.stackTrace = stackTrace
        }

        fun assertTimberCalled(expectedIsTimberCalled: Boolean, expectedMessage: String = "", expectedStackTrace: String = "") {
            this.isTimberCalled shouldBe expectedIsTimberCalled
            this.message shouldBe expectedMessage
            this.stackTrace shouldBe expectedStackTrace
        }
    }

    private val searchLogger = SearchLoggerMock()

    @Test
    fun `logWarning with null message or null exception should not call timber`() {
        searchLogger.logWarning(null, Exception())
        searchLogger.assertTimberCalled(false)

        searchLogger.logWarning("message", null)
        searchLogger.assertTimberCalled(false)
    }

    @Test
    fun `logWarning with message and exception should call timber`() {
        val errorMessage = "message"
        val exception = Exception()
        searchLogger.logWarning(errorMessage, exception)
        searchLogger.assertTimberCalled(true, errorMessage, ExceptionUtils.getStackTrace(exception))
    }

    @Test
    fun `logWarning with excluded exception should not call timber`() {
        val errorMessage = "message"

        searchLogger.logWarning(errorMessage, UnknownHostException())
        searchLogger.assertTimberCalled(false)

        searchLogger.logWarning(errorMessage, SocketException())
        searchLogger.assertTimberCalled(false)

        searchLogger.logWarning(errorMessage, InterruptedIOException())
        searchLogger.assertTimberCalled(false)

        searchLogger.logWarning(errorMessage, ConnectionShutdownException())
        searchLogger.assertTimberCalled(false)

        searchLogger.logWarning(errorMessage, ConnectException())
        searchLogger.assertTimberCalled(false)
    }
}