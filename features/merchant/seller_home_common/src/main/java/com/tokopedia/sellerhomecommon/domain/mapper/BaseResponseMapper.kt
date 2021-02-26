package com.tokopedia.sellerhomecommon.domain.mapper

interface BaseResponseMapper<R: Any, U : Any> {
    fun mapRemoteDataToUiData(response: R, isFromCache: Boolean): U
}