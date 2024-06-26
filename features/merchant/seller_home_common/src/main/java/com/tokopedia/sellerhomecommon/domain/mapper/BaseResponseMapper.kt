package com.tokopedia.sellerhomecommon.domain.mapper

interface BaseResponseMapper<R : Any, U : Any> {

    fun mapRemoteDataToUiData(response: R, isFromCache: Boolean): U
    fun mapRemoteDataToUiData(response: R, isFromCache: Boolean, extra: Map<String, Any?>): U {
        return mapRemoteDataToUiData(response, isFromCache)
    }
}
