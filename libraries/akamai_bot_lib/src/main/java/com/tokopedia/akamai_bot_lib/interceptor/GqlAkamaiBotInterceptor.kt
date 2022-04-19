package com.tokopedia.akamai_bot_lib.interceptor

import com.tokopedia.akamai_bot_lib.ERROR_CODE
import com.tokopedia.akamai_bot_lib.ERROR_MESSAGE_AKAMAI
import com.tokopedia.akamai_bot_lib.HEADER_AKAMAI_KEY
import com.tokopedia.akamai_bot_lib.HEADER_AKAMAI_VALUE
import com.tokopedia.akamai_bot_lib.exception.AkamaiErrorException
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class GqlAkamaiBotInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response  {
        val response = chain.proceed(chain.request())
        if (response.code == ERROR_CODE && response.header(HEADER_AKAMAI_KEY)?.contains(HEADER_AKAMAI_VALUE, true) == true) {
            logError(response)
            throw AkamaiErrorException(ERROR_MESSAGE_AKAMAI)
        }
        return response
    }

    private fun logError(response: Response){
        var messageMap: Map<String, String>? = mapOf(
            "request_body" to response.request.toString(),
            "user-agent" to response.request.header("User-Agent").toString(),
            "response" to response.peekBody(1024).string()
        )
        if (messageMap != null) {
            ServerLogger.log(Priority.P1, "AKAMAI_403_ERROR", messageMap)
        }
    }
}
