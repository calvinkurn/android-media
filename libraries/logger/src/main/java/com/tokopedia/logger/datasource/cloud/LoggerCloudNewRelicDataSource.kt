package com.tokopedia.logger.datasource.cloud

import com.tokopedia.keys.Keys
import com.tokopedia.logger.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.DataOutputStream
import java.net.HttpURLConnection
import java.net.URL

class LoggerCloudNewRelicDataSource : LoggerCloudNewRelicImpl {

    override suspend fun sendToLogServer(message: List<String>): Boolean {
        var errCode = Constants.LOG_DEFAULT_ERROR_CODE
        withContext(Dispatchers.IO) {
            try {
                errCode = openURL(message.firstOrNull())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return errCode == Constants.RESPONSE_SUCCESS_CODE
    }

    private fun openURL(message: String?): Int {
        var urlConnection: HttpURLConnection? = null
        val url: URL

        var responseCode = Constants.LOG_DEFAULT_ERROR_CODE

        try {
            url = URL(Constants.getNewRelicEventURL(Keys.AUTH_NEW_RELIC_USER_ID))
            urlConnection = url.openConnection() as HttpURLConnection
            urlConnection  = urlConnection.setRequestProperty()
            val wr = DataOutputStream(urlConnection.outputStream)
            wr.writeBytes(message ?: "")
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

    private fun HttpURLConnection.setRequestProperty(): HttpURLConnection {
        requestMethod = Constants.METHOD_POST
        doOutput = true
        useCaches = false
        setRequestProperty(Constants.HEADER_CONTENT_TYPE, Constants.HEADER_CONTENT_TYPE_JSON)
        setRequestProperty(Constants.HEADER_NEW_RELIC_KEY, Keys.AUTH_NEW_RELIC_API_KEY)
        return this
    }
}