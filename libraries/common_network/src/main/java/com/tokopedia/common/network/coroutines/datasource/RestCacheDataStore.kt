package com.tokopedia.common.network.coroutines.datasource

import com.tokopedia.common.network.data.model.RestRequest
import com.tokopedia.common.network.data.model.RestResponseIntermediate
import com.tokopedia.common.network.util.*
import com.tokopedia.common.network.util.RestConstant.RES_CODE_CACHE
import javax.inject.Inject

/**
 * Retrieve the response from cache only
 */
class RestCacheDataStore @Inject constructor() : RestDataStore {

    private val mCacheManager: RestCacheManager
    private val mFingerprintManager: FingerprintManager

    init {
        this.mCacheManager = RestCacheManager()
        this.mFingerprintManager = NetworkClient.getFingerPrintManager()
    }

    override suspend fun getResponse(request: RestRequest): RestResponseIntermediate?{
        var returnResponse: RestResponseIntermediate
        try {
            val rawJson: String? = mCacheManager.get(mFingerprintManager.generateFingerPrint(request.toString(), request.cacheStrategy.isSessionIncluded))

            if (rawJson == null || rawJson.isEmpty()) {
                return null
            }

            returnResponse = RestResponseIntermediate(CommonUtil.fromJson(rawJson, request.typeOfT), request.typeOfT, true)
            returnResponse.code = RES_CODE_CACHE
            returnResponse.isError = false
            return returnResponse
        } catch (e: Exception) {
            //For any kind of error body always be null,
            //E.g. JSONException while serializing json to POJO.
            returnResponse = RestResponseIntermediate(null, request.typeOfT, true)
            returnResponse.code = RestConstant.INTERNAL_EXCEPTION
            returnResponse.errorBody = "Caught Exception please fix it--> Responsible class : " + e.javaClass.toString() + " Detailed Message: " + e.message + ", Cause by: " + e.cause
            returnResponse.isError = true
        }

        return returnResponse
    }


}

