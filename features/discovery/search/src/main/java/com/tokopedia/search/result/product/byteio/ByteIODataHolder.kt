package com.tokopedia.search.result.product.byteio

import com.tokopedia.search.di.scope.SearchScope
import javax.inject.Inject

@SearchScope
class ByteIODataHolder @Inject constructor() {

    var enterMethod: String = ""
        private set

    fun updateEnterMethod(enterMethod: String?) {
        this.enterMethod = enterMethod ?: ""
    }
}
