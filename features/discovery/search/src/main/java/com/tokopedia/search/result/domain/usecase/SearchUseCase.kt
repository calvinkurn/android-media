package com.tokopedia.search.result.domain.usecase

import com.tokopedia.usecase.coroutines.UseCase

abstract class SearchUseCase<T: Any>: UseCase<T>() {

    protected val requestParams = mutableMapOf<String, Any?>()

    fun setRequestParams(requestParams: Map<String, Any?>) {
        this.requestParams.clear()
        this.requestParams.putAll(requestParams)
    }
}
