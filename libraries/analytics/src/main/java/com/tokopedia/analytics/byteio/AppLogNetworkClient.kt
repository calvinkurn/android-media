package com.tokopedia.analytics.byteio

import android.net.Uri
import android.util.Log
import android.util.Pair
import com.bytedance.bdinstall.INetworkClient
import com.bytedance.bdinstall.RangersHttpException
import com.bytedance.common.utility.CommonHttpException
import com.bytedance.retrofit2.Call
import com.bytedance.retrofit2.client.Header
import com.bytedance.retrofit2.http.AddCommonParam
import com.bytedance.retrofit2.http.Body
import com.bytedance.retrofit2.http.FieldMap
import com.bytedance.retrofit2.http.FormUrlEncoded
import com.bytedance.retrofit2.http.GET
import com.bytedance.retrofit2.http.HeaderList
import com.bytedance.retrofit2.http.MaxLength
import com.bytedance.retrofit2.http.POST
import com.bytedance.retrofit2.http.Url
import com.bytedance.retrofit2.mime.TypedByteArray
import com.bytedance.retrofit2.mime.TypedInput
import com.bytedance.retrofit2.mime.TypedOutput
import com.bytedance.ttnet.utils.RetrofitUtils
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.concurrent.ExecutionException

/**
 * Created by daijun.mark on 2023/10/11
 *
 * @author daijun.mark@bytedance.com
 */
/**
 * @author YuHuiJun  <yuhuijun></yuhuijun>@bytedance.com>
 * @since 十一月 22, 2017
 */
internal class AppLogNetworkClient : INetworkClient {
    @Volatile
    private var getApi: AppLogGetApi? = null

    @Volatile
    private var postApi: AppLogPostApi? = null
    private val thousand = 1024

    private fun checkAndGetPostApi(url: String): AppLogPostApi? {
        val pair = parseUrl(url)
        val baseUrl = pair.first ?: return null
        if (null == postApi) {

            postApi = RetrofitUtils.createSsService(baseUrl, AppLogPostApi::class.java)
//            postApi = ServiceManager.get().getService(IRetrofitFactory::class.java)
//                .createBuilder(baseUrl).build().create(AppLogPostApi::class.java)
        }
        return postApi
    }

    @Throws(Exception::class)
    override fun get(url: String, requestHeaders: Map<String, String>): String {
        return try {
            if (null == getApi) {
                val pair = parseUrl(url)
                val baseUrl = pair.first ?: return ""
                getApi = RetrofitUtils.createSsService(baseUrl, AppLogGetApi::class.java)
//                getApi = ServiceManager.get().getService(
//                    IRetrofitFactory::class.java,
//                )
//                    .createBuilder(baseUrl).build().create(AppLogGetApi::class.java)
            }
            getApi?.getResponse(url)?.execute()?.body() ?: ""
        } catch (e: ExecutionException) {
            throw CommonHttpException(0, e.message)
        } catch (e: Exception) {
            throw CommonHttpException(0, e.message)
        }
    }

    @Throws(Exception::class)
    override fun post(url: String, data: ByteArray, requestHeaders: Map<String, String>): String {
        //url = NetworkUtils.filterUrl(url);
        return try {
            run {
                val contentEncoding = requestHeaders["Content-Encoding"]
                val contentType = requestHeaders["Content-Type"]
                val croNetHeaders: MutableList<Header> = ArrayList()
                if (contentEncoding != null) {
                    croNetHeaders.add(Header("Content-Encoding", contentEncoding))
                }
                if (contentType != null && contentType.isNotEmpty()) {
                    croNetHeaders.add(Header("Content-Type", contentType))
                }
                for ((key, value) in requestHeaders) {
                    croNetHeaders.add(Header(key, value))
                }
                if (url.contains("device_register")) {
                    Log.d("DeviceID Change", "request headers are ${croNetHeaders} and url is $url")
                }
                val typedOutput: TypedOutput = TypedByteArray(contentType, data)
                checkAndGetPostApi(url)?.doPost(url, typedOutput, 200 * thousand, croNetHeaders)
                    ?.execute()?.body() ?: ""
            }
        } catch (e: ExecutionException) {
            throw CommonHttpException(0, e.message)
        } catch (e: Exception) {
            throw CommonHttpException(0, e.message)
        }
    }

    @Throws(Exception::class)
    override fun post(url: String, bytes: ByteArray, contentType: String): String {
        val typedOutput: TypedOutput = TypedByteArray(contentType, bytes)
        return try {
            checkAndGetPostApi(url)?.doPost(url, typedOutput, 200 * thousand, null)?.execute()?.body() ?: ""
        } catch (e: ExecutionException) {
            throw CommonHttpException(0, e.message)
        } catch (e: Exception) {
            throw CommonHttpException(0, e.message)
        }
    }

    @Throws(Exception::class)
    override fun post(url: String, params: List<Pair<String, String>>): String {
        val map: MutableMap<String, String> = HashMap()
        for (pair in params) {
            map[pair.first] = pair.second
        }
        return try {
            checkAndGetPostApi(url)?.getResponse(url, map, 200 * thousand)?.execute()?.body() ?: ""
        } catch (e: ExecutionException) {
            throw CommonHttpException(0, e.message)
        } catch (e: Exception) {
            throw CommonHttpException(0, e.message)
        }
    }

    @Throws(RangersHttpException::class)
    override fun postStream(
        url: String,
        data: ByteArray,
        requestHeaders: MutableMap<String, String>
    ): ByteArray {
        var `is`: InputStream? = null
        var baos: ByteArrayOutputStream? = null
        return try {
            val pair = parseUrl(url)
            val baseUrl = pair.first ?: return ByteArray(0)
            val path = pair.second
            var contentType: String? = null
            if (requestHeaders != null) {
                val k = "Content-Type"
                contentType = requestHeaders[k]
                requestHeaders.remove(k)
            }
            val headers = convertHeaders(requestHeaders)
            val body = TypedByteArray(contentType, data)
            val api = RetrofitUtils.createSsService(baseUrl, AppLogPostApi::class.java)
//            val api = ServiceManager.get().getService(IRetrofitFactory::class.java)
//                .createBuilder(baseUrl).build().create(AppLogPostApi::class.java)
            val typedInput = api.postDataStream(-1, path, body, headers, true).execute().body()
                ?: return ByteArray(0)
            `is` = typedInput.`in`()
            baos = ByteArrayOutputStream()
            val range = 1024
            val buffer = ByteArray(range)
            var len = 0
            while (`is`.read(buffer).also { len = it } != -1) {
                baos.write(buffer, 0, len)
            }
            baos.toByteArray()
        } catch (e: Exception) {
            throw RangersHttpException(0, e.message)
        } finally {
            if (`is` != null) {
                try {
                    `is`.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            if (baos != null) {
                try {
                    baos.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    internal interface AppLogGetApi {
        @GET
        fun getResponse(@Url url: String?): Call<String>
    }

    internal interface AppLogPostApi {
        @POST
        fun doPost(
            @Url url: String?, @Body body: TypedOutput?, @MaxLength maxLength: Int,
            @HeaderList headerList: List<Header>?
        ): Call<String>

        @POST
        @FormUrlEncoded
        fun getResponse(
            @Url url: String?,
            @FieldMap map: Map<String, String>?,
            @MaxLength maxLength: Int
        ): Call<String>

        @POST
        fun postDataStream(
            @MaxLength maxLength: Int,
            @Url relativePath: String?,
            @Body data: TypedOutput?,
            @HeaderList headers: List<Header>?,
            @AddCommonParam addCommonParam: Boolean
        ): Call<TypedInput?>
    }

    companion object {
        private fun parseUrl(url: String): Pair<String?, String?> {
            val uri = Uri.parse(url)
            val buffer = StringBuilder()
            val scheme = uri.scheme
            val host = uri.host
            val port = uri.port
            if (host != null) {
                if (scheme != null) {
                    buffer.append(scheme)
                    buffer.append("://")
                }
                buffer.append(host)
                if (port > 0) {
                    buffer.append(':')
                    buffer.append(port)
                }
            }
            val baseUrl = buffer.toString()
            var path = uri.path
            val query = uri.query
            if (query != null) {
                path = "$path?$query"
            }
            return Pair<String?, String?>(baseUrl, path)
        }

        private fun convertHeaders(headers: Map<String, String>?): List<Header>? {
            if (headers == null) {
                return null
            }
            val list: MutableList<Header> = ArrayList()
            for ((key, value) in headers) {
                list.add(Header(key, value))
            }
            return list
        }
    }
}
