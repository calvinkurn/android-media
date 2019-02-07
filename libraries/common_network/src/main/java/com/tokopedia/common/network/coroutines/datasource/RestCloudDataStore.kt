package com.tokopedia.common.network.coroutines.datasource

import android.content.Context
import com.tokopedia.common.network.data.model.CacheType
import com.tokopedia.common.network.data.model.RequestType

import com.tokopedia.common.network.data.model.RestRequest
import com.tokopedia.common.network.data.model.RestResponseIntermediate
import com.tokopedia.common.network.data.source.cloud.api.RestApi
import com.tokopedia.common.network.util.CommonUtil
import com.tokopedia.common.network.util.FingerprintManager
import com.tokopedia.common.network.util.NetworkClient
import com.tokopedia.common.network.util.RestCacheManager
import com.tokopedia.common.network.util.RestConstant
import com.tokopedia.network.CoroutineCallAdapterFactory
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.converter.StringResponseConverter
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.utils.TkpdOkHttpBuilder
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.withContext

import java.io.File

import javax.inject.Inject

import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory

class RestCloudDataStore {
    private var mApi: RestApi
    private var mCacheManager: RestCacheManager
    private var mFingerprintManager: FingerprintManager

    @Inject
    constructor() {
        this.mApi = NetworkClient.getApiInterface()
        this.mCacheManager = RestCacheManager()
        this.mFingerprintManager = NetworkClient.getFingerPrintManager()
    }

    constructor(interceptors: List<Interceptor>, context: Context) {
        this.mApi = getApiInterface(interceptors, context)
        this.mCacheManager = RestCacheManager()
        this.mFingerprintManager = NetworkClient.getFingerPrintManager()
    }

    fun getResponseJob(request: RestRequest): Deferred<Response<String>> {
        return when (request.requestType) {
            RequestType.GET -> doGet(request)
            RequestType.POST -> doPost(request)
            RequestType.PUT -> doPut(request)
            RequestType.DELETE -> delete(request)
            RequestType.POST_MULTIPART -> if (request.body is MultipartBody.Part) {
                postMultipart(request)
            } else {
                postPartMap(request)

            }
            RequestType.PUT_MULTIPART -> if (request.body is MultipartBody.Part) {
                putMultipart(request)
            } else {
                putPartMap(request)

            }
            RequestType.PUT_REQUEST_BODY -> if (request.body is RequestBody) {
                putRequestBody(request)
            } else {
                throw IllegalArgumentException("RequestBody must have params")
            }
            else -> doGet(request)
        }
    }

    suspend fun getResponse(request: RestRequest): RestResponseIntermediate? {
        return withContext(Dispatchers.IO) {
            getResponseJob(request).await().process(request)
        }
    }

    /**
     * Helper method to Invoke HTTP get request
     *
     * @param request - Request object
     * @return Observable which represent server response
     */
    private fun doGet(request: RestRequest): Deferred<Response<String>> {
        return mApi.getDeferred(request.url, request.queryParams,
                request.headers)
    }

    /**
     * Helper method to Invoke HTTP post request
     *
     * @param request - Request object
     * @return Observable which represent server response
     */
    private fun doPost(request: RestRequest): Deferred<Response<String>> {
        if (request.body != null && request.body is Map<*, *>) {
            return mApi.postDeferred(request.url,
                    request.body as Map<String, Any>,
                    request.queryParams,
                    request.headers)
        } else {
            var body: String? = null
            if (request.body is String) {
                body = request.body as String
            } else {
                try {
                    body = CommonUtil.toJson(request.body)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }

            if (body == null) {
                throw RuntimeException("Invalid json object provided")
            }

            return mApi.postDeferred(request.url,
                    request.body as Map<String, Any>,
                    request.queryParams,
                    request.headers)
        }
    }

    /**
     * Helper method to Invoke HTTP put request
     *
     * @param request - Request object
     * @return Observable which represent server response
     */
    private fun doPut(request: RestRequest): Deferred<Response<String>> {
        if (request.body != null && request.body is Map<*, *>) {
            return mApi.putDeferred(request.url,
                    request.body as Map<String, Any>,
                    request.queryParams,
                    request.headers)
        } else {
            var body: String? = null
            if (request.body is String) {
                body = request.body as String
            } else {
                try {
                    body = CommonUtil.toJson(request.body)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }

            if (body == null) {
                throw RuntimeException("Invalid json object provided")
            }

            return mApi.postDeferred(request.url,
                    body,
                    request.queryParams,
                    request.headers)
        }
    }

    /**
     * Helper method to Invoke HTTP delete request
     *
     * @param request - Request object
     * @return Observable which represent server response
     */
    private fun delete(request: RestRequest): Deferred<Response<String>> {
        return mApi.deleteDeferred(request.url,
                request.queryParams,
                request.headers)
    }

    private fun postMultipart(request: RestRequest): Deferred<Response<String>> {
        val file = File(request.body.toString())
        val reqFile = RequestBody.create(MediaType.parse("image/*"), file)
        val multipartBody = MultipartBody.Part.createFormData("upload", file.name, reqFile)

        return mApi.postMultipartDeferred(request.url, multipartBody, request.queryParams, request.headers)
    }

    private fun postPartMap(request: RestRequest): Deferred<Response<String>> {
        return mApi
                .postMultipartDeferred(request.url, request.body as Map<String, RequestBody>, request.queryParams, request.headers)
    }

    private fun putMultipart(request: RestRequest): Deferred<Response<String>> {
        val file = File(request.body.toString())
        val reqFile = RequestBody.create(MediaType.parse("image/*"), file)
        val multipartBody = MultipartBody.Part.createFormData("upload", file.name, reqFile)

        return mApi
                .putMultipartDeferred(request.url, multipartBody, request.queryParams, request.headers)
    }

    private fun putRequestBody(request: RestRequest): Deferred<Response<String>> {
        return mApi.putRequestBodyDeferred(request.url, request.body as RequestBody, request.headers)
    }

    private fun putPartMap(request: RestRequest): Deferred<Response<String>> {
        return mApi.putMultipartDeferred(request.url, request.body as Map<String, RequestBody>, request.queryParams, request.headers)
    }

    /**
     * Helper method to Dump the data into cache
     *
     * @param responseString - Current server response body
     * @param request        - Current server request
     */
    private fun cachedData(request: RestRequest, responseString: String) {
        //trying to store the data into cache based on cache strategy;
        try {
            when (request.cacheStrategy.type) {
                CacheType.NONE, CacheType.CACHE_ONLY -> {
                }
                null, CacheType.CACHE_FIRST, CacheType.ALWAYS_CLOUD ->
                    //store the data into disk
                    mCacheManager.save(mFingerprintManager.generateFingerPrint(request.toString(),
                            request.cacheStrategy.isSessionIncluded),
                            responseString,
                            request.cacheStrategy.expiryTime)
            }//do nothing for now
        } catch (e: Exception) {
            //Just a defencive check in order to avoid any collision between success response.
            e.printStackTrace()
        }

    }

    fun Response<String>.process(request: RestRequest): RestResponseIntermediate? {
        return processData(request, this)
    }

    private fun processData(request: RestRequest, response: Response<String>): RestResponseIntermediate {
        var returnResponse: RestResponseIntermediate
        try {
            if (response.code() == RestConstant.HTTP_SUCCESS) {
                returnResponse = RestResponseIntermediate(CommonUtil.fromJson(response.body(), request.typeOfT), request.typeOfT, false)
                returnResponse.code = response.code()
                returnResponse.isError = false

                //For success case saving the data into cache
                cachedData(request, response.body())
            } else {
                //For any kind of error body always be null
                //E.g. error response like HTTP error code = 400,401,410 or 500 etc.
                returnResponse = RestResponseIntermediate(null, request.typeOfT, false)
                returnResponse.code = response.code()
                returnResponse.errorBody = if (response.body() == null) response.errorBody().string() else response.body()
                returnResponse.isError = true
            }
        } catch (e: Exception) {
            //For any kind of error body always be null
            //E.g. JSONException while serializing json to POJO.
            returnResponse = RestResponseIntermediate(null, request.typeOfT, false)
            returnResponse.code = RestConstant.INTERNAL_EXCEPTION
            returnResponse.errorBody = "Caught Exception please fix it--> Responsible class : " + e.javaClass.toString() + " Detailed Message: " + e.message + ", Cause by: " + e.cause
            returnResponse.isError = true
        }

        return returnResponse
    }

    private fun getApiInterface(interceptors: List<Interceptor?>?, context: Context): RestApi {
        val userSession: UserSessionInterface = UserSession(context.applicationContext)
        val okkHttpBuilder = TkpdOkHttpBuilder(context, OkHttpClient.Builder())
        if (interceptors != null) {
            okkHttpBuilder.addInterceptor(FingerprintInterceptor(context.applicationContext as NetworkRouter, userSession))
            for (interceptor in interceptors) {
                if (interceptor == null) {
                    continue
                }

                okkHttpBuilder.addInterceptor(interceptor)
            }
        }

        return Retrofit.Builder()
                .baseUrl("http://tokopedia.com/")
                .addConverterFactory(StringResponseConverter())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory.invoke())
                .client(okkHttpBuilder.build()).build().create(RestApi::class.java)
    }

}
