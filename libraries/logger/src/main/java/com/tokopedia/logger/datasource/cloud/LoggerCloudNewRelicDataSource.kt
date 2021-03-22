package com.tokopedia.logger.datasource.cloud

import com.google.gson.Gson
import com.tokopedia.logger.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.DataOutputStream
import java.net.HttpURLConnection
import java.net.URL

class LoggerCloudNewRelicDataSource: LoggerCloudNewRelicImpl {

    private val gson = Gson()

    override suspend fun sendToLogServer(message: String): Boolean {
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

    private fun openURL(message: String): Int {
        var urlConnection: HttpURLConnection? = null
        val url: URL

        var responseCode = Constants.LOG_DEFAULT_ERROR_CODE

        try {
            url = URL(Constants.NEW_RELIC_SERVER_URL)
            urlConnection = url.openConnection() as HttpURLConnection
            urlConnection.requestMethod = Constants.METHOD_POST
            urlConnection.setRequestProperty("Api-Key", Constants.NEW_RELIC_API_KEY)
            urlConnection.doOutput = true
            val wr = DataOutputStream(urlConnection.outputStream)
            wr.writeBytes(gson.toJson(message))
            wr.flush()
            wr.close()

            responseCode = urlConnection.responseCode
        } catch (e: Throwable) {
            e.printStackTrace()
        } finally {
            urlConnection?.disconnect()
            return responseCode
        }
    }
}