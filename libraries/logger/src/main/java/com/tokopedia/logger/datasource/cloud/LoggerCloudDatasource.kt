package com.tokopedia.logger.datasource.cloud

import com.tokopedia.logger.datasource.db.Logger
import com.tokopedia.logger.utils.Constants
import com.tokopedia.logger.utils.decrypt
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.DataOutputStream
import java.net.HttpURLConnection
import java.net.URL
import javax.crypto.SecretKey

class LoggerCloudDatasource {
    suspend fun sendLogToServer(serverSeverity: Int, TOKEN: Array<String>, logger: Logger, key:SecretKey): Int{
        val message = decrypt(logger.message, key)
        val truncatedMessage: String
        var errCode = 404
        truncatedMessage = if (message.length > Constants.MAX_BUFFER) {
            message.substring(0, Constants.MAX_BUFFER)
        } else {
            message
        }
        val token = TOKEN[serverSeverity - 1]
        withContext(Dispatchers.IO) {
            try {
                errCode = openURL(token, truncatedMessage)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return errCode
    }

    private fun openURL(token: String, message: String): Int{
        var urlConnection: HttpURLConnection? = null
        val url: URL

        try {
            Timber.d("SENDING")
            url = URL(Constants.SERVER_URL + token)
            urlConnection = url.openConnection() as HttpURLConnection
            urlConnection.requestMethod = "POST"
            urlConnection.doOutput = true
            val wr = DataOutputStream(urlConnection.outputStream)
            wr.writeBytes(message)
            wr.flush()
            wr.close()

            urlConnection.responseCode
            Timber.d(urlConnection.responseCode.toString())
            Timber.d("SUCCESS")

        } catch (e: Throwable) {
            e.printStackTrace()
        } finally {
            urlConnection?.disconnect()
            return urlConnection!!.responseCode
        }
    }
}