package com.tokopedia.common.network.coroutines.datasource

import android.content.Context
import com.google.gson.Gson
import com.tokopedia.common.network.data.model.CacheType
import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.common.network.data.model.RestRequest
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.common.network.data.source.cloud.api.RestApi
import com.tokopedia.common.network.util.CommonUtil
import com.tokopedia.common.network.util.FingerprintManager
import com.tokopedia.common.network.util.RestCacheManager
import com.tokopedia.common.network.util.RestConstant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
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

    private suspend fun getResponseJob(request: RestRequest): Response<String>{
        return when (request.requestType) {
            RequestType.PATCH -> doPatch(request)
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
            withContext(Dispatchers.IO) { processData(request, getResponseJob(request)) }

    /**
     * Helper method to Invoke HTTP get request
     *
     * @param request - Request object
     * @return Observable which represent server response
     */
    private suspend fun doGet(request: RestRequest): Response<String> {
        return mApi.getDeferred(request.url, request.queryParams,
                request.headers)
    }

    /**
     * Helper method to Invoke HTTP patch request
     *
     * @param request - Request object
     * @return Observable which represent server response
     */

    private suspend fun doPatch(request: RestRequest): Response<String> {
        if (request.body != null && request.body is Map<*, *>) {
            return mApi.patchDeferred(request.url,
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

            return mApi.patchDeferred(request.url,
                    body,
                    request.queryParams,
                    request.headers)
        }
    }

    /**
     * Helper method to Invoke HTTP post request
     *
     * @param request - Request object
     * @return Observable which represent server response
     */
    private suspend fun doPost(request: RestRequest): Response<String> {
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
    private suspend fun doPut(request: RestRequest): Response<String> {
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
    private suspend fun delete(request: RestRequest): Response<String> {
        return mApi.deleteDeferred(request.url,
                request.queryParams,
                request.headers)
    }

    private suspend fun postMultipart(request: RestRequest): Response<String> {

        return mApi.postMultipartDeferred(request.url, request.body as MultipartBody.Part,
                request.queryParams,
                request.headers)
    }

    private suspend fun postPartMap(request: RestRequest): Response<String> {
        return mApi
                .postMultipartDeferred(request.url, request.body as Map<String, RequestBody>, request.queryParams, request.headers)
    }

    private suspend fun putMultipart(request: RestRequest): Response<String> {
        val file = File(request.body.toString())
        val reqFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        val multipartBody = MultipartBody.Part.createFormData("upload", file.name, reqFile)

        return mApi
            .putMultipartDeferred(request.url, multipartBody, request.queryParams, request.headers)
    }

    private suspend fun putRequestBody(request: RestRequest): Response<String> {
        return mApi.putRequestBodyDeferred(request.url, request.body as RequestBody, request.headers)
    }

    private suspend fun putPartMap(request: RestRequest): Response<String> {
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
                cachedData(request, response.body()?:"")

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
                    errorBody = if (response.body() == null) response.errorBody()?.string() else response.body()
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
