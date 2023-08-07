package com.tokopedia.search.result.product.responsecode

import com.tokopedia.search.di.scope.SearchScope
import javax.inject.Inject


@SearchScope
class ResponseCodeImpl @Inject constructor(): ResponseCodeProvider {
    private var _responseCode: String = ""
    override val responseCode: String
        get() = _responseCode

    fun setResponseCode(responseCode: String) {
        _responseCode = responseCode
    }

    fun clearData() {
        _responseCode = ""
    }
}
