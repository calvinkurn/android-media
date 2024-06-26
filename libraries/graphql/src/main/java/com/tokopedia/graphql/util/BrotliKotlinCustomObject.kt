package com.tokopedia.graphql.util
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody
import okio.GzipSource
import okio.buffer
import okio.source
import org.brotli.dec.BrotliInputStream

/**
 * Transparent Brotli response support.
 *
 * Adds Accept-Encoding: br to request and checks (and strips) for Content-Encoding: br in
 * responses.  n.b. this replaces the transparent gzip compression in BridgeInterceptor.
 */
object BrotliKotlinCustomObject : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response =
            if (chain.request().header("Accept-Encoding") == null) {
                val request = chain.request().newBuilder()
                        .header("Accept-Encoding", "br,gzip")
                        .build()

                val response = chain.proceed(request)

                uncompress(response)
            } else {
                chain.proceed(chain.request())
            }

    internal fun uncompress(response: Response): Response {
        val body = response.body ?: return response
        val encoding = response.header("Content-Encoding") ?: return response

        val decompressedSource = when {
            encoding.equals("br", ignoreCase = true) ->
                BrotliInputStream(body.source().inputStream()).source().buffer()
            encoding.equals("gzip", ignoreCase = true) ->
                GzipSource(body.source()).buffer()
            else -> return response
        }


        return response.newBuilder()
            .removeHeader("Content-Encoding")
            .removeHeader("Content-Length")
            .body(ResponseBody.create(body.contentType(), -1, decompressedSource))
            .build()
    }
}
