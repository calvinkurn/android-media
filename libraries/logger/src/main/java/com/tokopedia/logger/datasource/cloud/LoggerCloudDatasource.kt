package com.tokopedia.logger.datasource.cloud

import com.tokopedia.logger.datasource.db.Logger
import com.tokopedia.logger.utils.Constants
import com.tokopedia.logger.utils.decrypt
import com.tokopedia.logger.utils.launchCatchError
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import java.io.DataOutputStream
import java.net.HttpURLConnection
import java.net.URL
import javax.crypto.SecretKey

class LoggerCloudDatasource {
    fun sendLogToServer(serverSeverity: Int, TOKEN: Array<String>, logger: Logger, key:SecretKey): Int{
         val message = decrypt(logger.message , key)
         val truncatedMessage: String
         var errCode = 404
         truncatedMessage = if (message.length > Constants.MAX_BUFFER) {
             message.substring(0, Constants.MAX_BUFFER)
         } else {
             message
         }
         val token = TOKEN[serverSeverity - 1]
         runBlocking {
            launchCatchError(block = {
                errCode = openURL(token,truncatedMessage)
            }){
                Timber.d("Error here")
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