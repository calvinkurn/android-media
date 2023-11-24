package com.tokopedia.search.result.product.requestparamgenerator

import com.tokopedia.search.di.scope.SearchScope
import javax.inject.Inject


@SearchScope
class LastClickedProductIdProviderImpl @Inject constructor(): LastClickedProductIdProvider {

    override var lastClickedProductId: String = ""
}
