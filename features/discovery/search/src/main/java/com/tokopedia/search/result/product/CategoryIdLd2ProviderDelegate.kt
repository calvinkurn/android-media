package com.tokopedia.search.result.product

import com.tokopedia.search.di.scope.SearchScope
import javax.inject.Inject

@SearchScope
class CategoryIdL2ProviderDelegate @Inject constructor(): CategoryIdL2Provider {
    override var categoryIdL2: String = ""
}