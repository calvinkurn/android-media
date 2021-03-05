package com.tokopedia.logger.datasource.cloud

import com.google.gson.Gson
import com.tokopedia.logger.model.ScalyrBody
import com.tokopedia.logger.model.ScalyrConfig
import com.tokopedia.logger.model.ScalyrEvent
import com.tokopedia.logger.model.ScalyrSessionInfo
import com.tokopedia.logger.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.DataOutputStream
import java.net.HttpURLConnection
import java.net.URL

class LoggerCloudScalyrDataSource: LoggerCloudDataSource<ScalyrConfig, ScalyrEvent>() {
    companion object {
        private val gson = Gson()
    }

    override suspend fun sendLogToServer(config: ScalyrConfig, eventList: List<ScalyrEvent>): Boolean {
        var errCode = Constants.LOG_DEFAULT_ERROR_CODE
        withContext(Dispatchers.IO) {
            try {
                errCode = openURL(config, eventList)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return errCode == Constants.SCALYR_SUCCESS_CODE
    }

    private fun openURL(config: ScalyrConfig, scalyrEventList: List<ScalyrEvent>): Int {
        var urlConnection: HttpURLConnection? = null
        val url: URL

        var responseCode = Constants.LOG_DEFAULT_ERROR_CODE

        try {
            val scalyrBody = ScalyrBody(config.token, config.session,
                ScalyrSessionInfo(config.serverHost, config.parser, config.packageName,
                        config.installer,
                        config.debug, config.priority),
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