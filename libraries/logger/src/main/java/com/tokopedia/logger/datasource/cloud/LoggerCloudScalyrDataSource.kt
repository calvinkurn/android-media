package com.tokopedia.logger.datasource.cloud

import com.google.gson.Gson
import com.tokopedia.logger.model.ScalyrBody
import com.tokopedia.logger.model.ScalyrEvent
import com.tokopedia.logger.model.ScalyrSessionInfo
import com.tokopedia.logger.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.DataOutputStream
import java.net.HttpURLConnection
import java.net.URL

class LoggerCloudScalyrDataSource(val session: String): LoggerCloudDataSource<ScalyrEvent>() {
    companion object {
        private val gson = Gson()
    }

    override suspend fun sendLogToServer(token: String, eventList: List<ScalyrEvent>): Int {
        var errCode = Constants.LOG_DEFAULT_ERROR_CODE
        withContext(Dispatchers.IO) {
            try {
                errCode = openURL(token, eventList)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return errCode
    }

    private fun openURL(token: String, scalyrEventList: List<ScalyrEvent>): Int {
        var urlConnection: HttpURLConnection? = null
        val url: URL

        var responseCode = Constants.LOG_DEFAULT_ERROR_CODE

        try {
            val scalyrBody = ScalyrBody(token, session,
                ScalyrSessionInfo(Constants.ANDROID_APP_VALUE, Constants.SCALYR_PARSER),
                scalyrEventList)
            url = URL(Constants.SCALYR_SERVER_URL)
            urlConnection = url.openConnection() as HttpURLConnection
            urlConnection.requestMethod = Constants.METHOD_POST
            urlConnection.doOutput = true
            val wr = DataOutputStream(urlConnection.outputStream)
            wr.writeBytes(gson.toJson(scalyrBody))
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