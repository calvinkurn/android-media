package com.tokopedia.common.network.coroutines.datasource

import android.content.Context
import com.google.gson.Gson
import com.tokopedia.common.network.data.model.*
import com.tokopedia.common.network.data.source.cloud.api.RestApi
import com.tokopedia.common.network.util.*
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.coroutineScope
import kotlinx.coroutines.experimental.withContext
import okhttp3.*
import retrofit2.Response
import java.io.File

class RestCloudDataStore(
        private var mApi: RestApi,
        private val mCacheManager: RestCacheManager,
        private val mFingerprintManager: FingerprintManager
) : RestDataStore{

    fun updateInterceptor(interceptors: List<Interceptor>, context: Context){
        mApi = RestUtil.getApiInterface(interceptors, context)
    }

    private fun getResponseJob(request: RestRequest): Deferred<Response<String>> {
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

    override suspend fun getResponse(request: RestRequest): RestResponse =
            withContext(Dispatchers.IO) { processData(request, getResponseJob(request).await()) }

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
                    request.body as Map<String, String>,
                    request.queryParams,
                    request.headers)
        } else {
            val body: String = (if (request.body is String) {
                request.body
            } else {
                try {
                    CommonUtil.toJson(request.body)
                } catch (e: Exception) {
                    e.printStackTrace()
                    null
                }

            }) ?: throw RuntimeException("Invalid json object provided")

            return mApi.postDeferred(request.url,
                    body,
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
                    request.body as Map<String, String>,
                    request.queryParams,
                    request.headers)
        } else {
            val body: String = (if (request.body is String) {
                request.body
            } else {
                try {
                    CommonUtil.toJson(request.body)
                } catch (e: Exception) {
                    e.printStackTrace()
                    null
                }
            }) ?: throw RuntimeException("Invalid json object provided")

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

        return mApi.postMultipartDeferred(request.url, multipartBody,
                request.queryParams,
                request.headers)
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
    private suspend fun cachedData(request: RestRequest, responseString: String) = coroutineScope {
        //trying to store the data into cache based on cache strategy;
        try {
            when (request.cacheStrategy.type) {
                CacheType.NONE, CacheType.CACHE_ONLY -> { }
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

    private suspend fun processData(request: RestRequest, response: Response<String>): RestResponse {
        return try {
            if (response.code() == RestConstant.HTTP_SUCCESS) {
                //For success case saving the data into cache
                cachedData(request, response.body())

                RestResponse(Gson().fromJson(response.body(), request.typeOfT), response.code(), false)
                        .apply {
                            type = request.typeOfT
                            isError = false
                        }
            } else {
                //For any kind of error body always be null
                //E.g. error response like HTTP error code = 400,401,410 or 500 etc.
                RestResponse(Unit, response.code(), false).apply {
                    type = request.typeOfT
                    errorBody = if (response.body() == null) response.errorBody().string() else response.body()
                    isError = true
                }
            }
        } catch (e: Exception) {
            //For any kind of error body always be null
            //E.g. JSONException while serializing json to POJO.
            RestResponse(Unit, RestConstant.INTERNAL_EXCEPTION, false).apply {
                type = request.typeOfT
                errorBody = "Caught Exception please fix it--> Responsible class : " + e.javaClass.toString() + " Detailed Message: " + e.message + ", Cause by: " + e.cause
                isError = true
            }
        }
    }



}
