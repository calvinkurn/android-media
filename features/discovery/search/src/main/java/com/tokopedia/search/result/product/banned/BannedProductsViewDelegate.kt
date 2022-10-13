package com.tokopedia.search.result.product.banned

import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.search.result.product.QueryKeyProvider
import javax.inject.Inject

@SearchScope
class BannedProductsViewDelegate @Inject constructor(
    queryKeyProvider: QueryKeyProvider
): BannedProductsView,
    QueryKeyProvider by queryKeyProvider {

    override fun trackEventImpressionBannedProducts() {
        BannedProductsTracking.trackEventImpressionBannedProductsEmptySearch(queryKey)
    }
}
