package com.tokopedia.common.network.coroutines.datasource

import com.tokopedia.common.network.data.model.RestRequest
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.common.network.util.*
import com.tokopedia.common.network.util.RestConstant.RES_CODE_CACHE
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.withContext

/**
 * Retrieve the response from cache only
 */
class RestCacheDataStore constructor(
        private val mCacheManager: RestCacheManager,
        private val mFingerprintManager: FingerprintManager
) : RestDataStore {

    override suspend fun getResponse(request: RestRequest): RestResponse =
            withContext(Dispatchers.IO){

                val rawJson =  mCacheManager.get(mFingerprintManager.generateFingerPrint(request.toString(),
                        request.cacheStrategy.isSessionIncluded))
                try {
                    if (rawJson == null){
                        RestResponse(Unit, RestConstant.INTERNAL_EXCEPTION, true).apply {
                            type = request.typeOfT
                            errorBody = "No Cache found"
                            isError = true
                        }
                    } else {
                        RestResponse(CommonUtil.fromJson(rawJson, request.typeOfT), RES_CODE_CACHE, true).apply {
                            type = request.typeOfT
                            isError = false
                        }
                    }
                } catch (e: Throwable){
                    RestResponse(Unit, RestConstant.INTERNAL_EXCEPTION, true).apply {
                        type = request.typeOfT
                        errorBody = "Caught Exception please fix it--> Responsible class : " + e.javaClass.toString() +
                                " Detailed Message: " + e.message + ", Cause by: " + e.cause
                        isError = true
                    }
                }
    }


}

