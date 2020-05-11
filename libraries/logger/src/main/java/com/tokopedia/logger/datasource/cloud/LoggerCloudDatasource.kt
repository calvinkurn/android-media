package com.tokopedia.logger.datasource.cloud

import com.tokopedia.logger.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.DataOutputStream
import java.net.HttpURLConnection
import java.net.URL

class LoggerCloudDatasource {
    suspend fun sendLogToServer(serverSeverity: Int, TOKEN: Array<String>,
                                message:String): Int{
        var errCode = Constants.LOG_DEFAULT_ERROR_CODE
        val token = TOKEN[serverSeverity - 1]
        withContext(Dispatchers.IO) {
            try {
                errCode = openURL(token, message)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return errCode
    }

    private fun openURL(token: String, message: String): Int{
        var urlConnection: HttpURLConnection? = null
        val url: URL

        var responseCode = Constants.LOG_DEFAULT_ERROR_CODE

        try {
            url = URL(Constants.SERVER_URL + token)
            urlConnection = url.openConnection() as HttpURLConnection
            urlConnection.requestMethod = Constants.METHOD_POST
            urlConnection.doOutput = true
            val wr = DataOutputStream(urlConnection.outputStream)
            wr.writeBytes(message)
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