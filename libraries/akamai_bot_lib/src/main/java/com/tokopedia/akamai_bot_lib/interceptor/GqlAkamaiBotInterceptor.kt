package com.tokopedia.akamai_bot_lib.interceptor

import com.tokopedia.akamai_bot_lib.ERROR_CODE
import com.tokopedia.akamai_bot_lib.ERROR_MESSAGE_AKAMAI
import com.tokopedia.akamai_bot_lib.HEADER_AKAMAI_KEY
import com.tokopedia.akamai_bot_lib.HEADER_AKAMAI_VALUE
import com.tokopedia.akamai_bot_lib.exception.AkamaiErrorException
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class GqlAkamaiBotInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response  {
        val response = chain.proceed(chain.request())
        if (response.code() == ERROR_CODE && response.header(HEADER_AKAMAI_KEY)?.contains(HEADER_AKAMAI_VALUE, true) == true) {
            throw AkamaiErrorException(ERROR_MESSAGE_AKAMAI)
        }
        return response
    }
}
