package com.tokopedia.akamai_bot_lib.interceptor

import com.akamai.botman.CYFMonitor
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okio.Buffer
import java.io.EOFException
import java.io.IOException
import java.nio.charset.Charset

class GqlAkamaiBotInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response  {
        CYFMonitor.setLogLevel(CYFMonitor.INFO)

        val request = chain.request()
        val newRequest: Request.Builder = request.newBuilder()


        val requestBody = request.body()
        val hasRequestBody = requestBody != null

        if (hasRequestBody && !bodyEncoded(request.headers())) {
            val UTF8 = Charset.forName("UTF-8")
            val buffer = Buffer()
            requestBody?.writeTo(buffer)
            var charset: Charset? = UTF8
            val contentType = requestBody?.contentType()
            charset = contentType?.charset(UTF8)
            if (isPlaintext(buffer)) {
                charset?.let {
                    readFromBuffer(buffer, it).let {
                        if (it.contains("login")) {
                            newRequest.addHeader("X-acf-sensor-data", CYFMonitor.getSensorData()
                                    ?: "")
                            newRequest.addHeader("X-TKPD-AKAMAI","login")
                        }else if (it.contains("register") ){
                            newRequest.addHeader("X-acf-sensor-data", CYFMonitor.getSensorData()
                                    ?: "")
                            newRequest.addHeader("X-TKPD-AKAMAI","register")
                        }else{
                            // none
                        }

                    }
                }
            }
        }

        return chain.proceed(newRequest.build())
    }

    private fun isPlaintext(buffer: Buffer): Boolean {
        try {
            val prefix = Buffer()
            val byteCount = if (buffer.size < 64) buffer.size else 64
            buffer.copyTo(prefix, 0, byteCount)
            for (i in 0..15) {
                if (prefix.exhausted()) {
                    break
                }
                val codePoint = prefix.readUtf8CodePoint()
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false
                }
            }
            return true
        } catch (e: EOFException) {
            return false // Truncated UTF-8 sequence.
        }

    }

    private fun bodyEncoded(headers: Headers): Boolean {
        val contentEncoding = headers.get("Content-Encoding")
        return contentEncoding != null && !contentEncoding.equals("identity", ignoreCase = true)
    }

    private fun readFromBuffer(buffer: Buffer, charset: Charset): String {
        val bufferSize = buffer.size
        val maxBytes = Math.min(bufferSize, 250000L)
        var body = ""
        try {
            body = buffer.readString(maxBytes, charset)
        } catch (e: EOFException) {
            body += "\n\n--- Unexpected end of content ---"
        }

        if (bufferSize > 250000L) {
            body += "\n\ntruncated"
        }
        return body
    }
}