package com.tokopedia.logger.datasource.cloud

import com.google.gson.Gson
import com.tokopedia.logger.model.newrelic.NewRelicConfig
import com.tokopedia.logger.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.zip.GZIPOutputStream

class LoggerCloudNewRelicDataSource : LoggerCloudNewRelicImpl {

    companion object {
        val gson = Gson()
    }

    override suspend fun sendToLogServer(newRelicConfig: NewRelicConfig, message: List<String>): Boolean {
        var errCode = Constants.LOG_DEFAULT_ERROR_CODE
        withContext(Dispatchers.IO) {
            try {
                errCode = openURL(newRelicConfig, message)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return errCode == Constants.RESPONSE_SUCCESS_CODE
    }

    private fun openURL(newRelicConfig: NewRelicConfig, message: List<String>): Int {
        var urlConnection: HttpURLConnection? = null
        val url: URL

        var responseCode = Constants.LOG_DEFAULT_ERROR_CODE

        try {
            val requestBody = compressRequestBody(message)
            url = URL(Constants.getNewRelicEventURL(newRelicConfig.userId))
            urlConnection = url.openConnection() as HttpURLConnection
            urlConnection = urlConnection.setRequestProperty(requestBody.size, newRelicConfig)
            urlConnection.outputStream.use { outputStream ->
                outputStream.write(requestBody)
            }

            responseCode = urlConnection.responseCode
        } catch (e: Throwable) {
            e.printStackTrace()
        } finally {
            urlConnection?.disconnect()
            return responseCode
        }
    }

    private fun HttpURLConnection.setRequestProperty(size: Int, newRelicConfig: NewRelicConfig): HttpURLConnection {
        requestMethod = Constants.METHOD_POST
        doOutput = true
        useCaches = false
        setRequestProperty(Constants.HEADER_CONTENT_TYPE, Constants.HEADER_CONTENT_TYPE_JSON)
        setRequestProperty(Constants.HEADER_NEW_RELIC_KEY, newRelicConfig.token)
        setRequestProperty(Constants.HEADER_CONTENT_ENCODING, Constants.HEADER_CONTENT_ENCODING_GZIP)
        setRequestProperty(Constants.HEADER_CONTENT_LENGTH, size.toString())
        return this
    }

    private fun compressRequestBody(message: List<String>): ByteArray {
        val requestBodyInBytes = message.toString().toByteArray()
        return ByteArrayOutputStream(requestBodyInBytes.size).use { byteArrayOutputStream ->
            GZIPOutputStream(byteArrayOutputStream).use { gzipOutputStream ->
                gzipOutputStream.write(requestBodyInBytes, 0, requestBodyInBytes.size)
            }
            byteArrayOutputStream.toByteArray()
        }
    }
}