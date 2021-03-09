package com.tokopedia.pocnewrelic.datasource

import com.google.gson.Gson
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.keys.Keys
import com.tokopedia.pocnewrelic.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.ByteArrayOutputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.zip.GZIPOutputStream
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class NewRelicCloudDataSource @Inject constructor(
        private val dispatchers: CoroutineDispatchers
): CoroutineScope {

    private val gson: Gson = Gson()
    override val coroutineContext: CoroutineContext = dispatchers.io

    fun sendData(data: Map<String, Any>) {
        launch(context = dispatchers.io, block = {
            var urlConnection: HttpURLConnection? = null
            try {
                val requestBody = compressRequestBody(data)
                urlConnection = openConnection(requestBody.size)
                urlConnection.writeData(requestBody)
                urlConnection.responseCode
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                urlConnection?.disconnect()
            }
        })
    }

    private fun compressRequestBody(requestBody: Map<String, Any>): ByteArray {
        val requestBodyInBytes = gson.toJson(requestBody).toByteArray()
        return ByteArrayOutputStream(requestBodyInBytes.size).use { byteArrayOutputStream ->
            GZIPOutputStream(byteArrayOutputStream).use { gzipOutputStream ->
                gzipOutputStream.write(requestBodyInBytes, 0, requestBodyInBytes.size)
                gzipOutputStream.finish()
                byteArrayOutputStream.toByteArray()
            }
        }
    }

    private fun openConnection(size: Int): HttpURLConnection {
        val urlConnection: HttpURLConnection?
        val url = URL(URL_NEW_RELIC_EVENT)
        urlConnection = (url.openConnection() as HttpURLConnection).setUpConnection(size)
        return urlConnection
    }

    private fun HttpURLConnection.setUpConnection(size: Int): HttpURLConnection {
        requestMethod = REQUEST_METHOD_POST
        doOutput = true
        useCaches = false
        connectTimeout = 60000
        readTimeout = 60000
        setRequestProperty(HEADER_CONTENT_TYPE, HEADER_CONTENT_TYPE_JSON)
        setRequestProperty(HEADER_NEW_RELIC_KEY, Keys.AUTH_NEW_RELIC_API_KEY)
        setRequestProperty(HEADER_CONTENT_ENCODING, HEADER_CONTENT_ENCODING_GZIP)
        setRequestProperty(HEADER_CONTENT_LENGTH, size.toString())
        return this
    }

    private fun HttpURLConnection.writeData(requestBody: ByteArray): HttpURLConnection {
        outputStream.use { outputStream ->
            outputStream.write(requestBody)
        }
        return this
    }
}