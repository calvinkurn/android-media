package com.tokopedia.logger.datasource.cloud

import com.google.gson.Gson
import com.tokopedia.keys.Keys
import com.tokopedia.logger.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.zip.GZIPOutputStream

class LoggerCloudNewRelicDataSource : LoggerCloudNewRelicImpl {

    private val gson = Gson()

    override suspend fun sendToLogServer(message: List<String>): Boolean {
        var errCode = Constants.LOG_DEFAULT_ERROR_CODE
        withContext(Dispatchers.IO) {
            try {
                errCode = openURL(message)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return errCode == Constants.RESPONSE_SUCCESS_CODE
    }

    private fun openURL(message: List<String>): Int {
        var urlConnection: HttpURLConnection? = null
        val url: URL

        var responseCode = Constants.LOG_DEFAULT_ERROR_CODE

        try {
            val requestBody = compressRequestBody(message)
            url = URL(Constants.getNewRelicEventURL(Keys.AUTH_NEW_RELIC_USER_ID))
            urlConnection = url.openConnection() as HttpURLConnection
            urlConnection.setRequestProperty(requestBody.size)
            urlConnection.outputStream.use {
                it.write(requestBody)
                it.flush()
                it.close()
            }

            responseCode = urlConnection.responseCode
        } catch (e: Throwable) {
            e.printStackTrace()
        } finally {
            urlConnection?.disconnect()
            return responseCode
        }
    }

    private fun HttpURLConnection.setRequestProperty(size: Int): HttpURLConnection {
        requestMethod = Constants.METHOD_POST
        doOutput = true
        useCaches = false
        setRequestProperty(Constants.HEADER_CONTENT_TYPE, Constants.HEADER_CONTENT_TYPE_JSON)
        setRequestProperty(Constants.HEADER_NEW_RELIC_KEY, Keys.AUTH_NEW_RELIC_API_KEY)
        setRequestProperty(Constants.HEADER_CONTENT_ENCODING, Constants.HEADER_CONTENT_ENCODING_GZIP)
        setRequestProperty(Constants.HEADER_CONTENT_LENGTH, size.toString())
        return this
    }

    private fun compressRequestBody(message: List<String>): ByteArray {
        val requestBodyInBytes = gson.toJson(message).toByteArray()
        return ByteArrayOutputStream(requestBodyInBytes.size).use { byteArrayOutputStream ->
            GZIPOutputStream(byteArrayOutputStream).use { gzipOutputStream ->
                gzipOutputStream.write(requestBodyInBytes, 0, requestBodyInBytes.size)
            }
            byteArrayOutputStream.toByteArray()
        }
    }


}