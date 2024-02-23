package com.tokopedia.search.result.product.byteio

import com.tokopedia.discovery.common.analytics.SearchId
import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.search.result.product.QueryKeyProvider
import javax.inject.Inject

@SearchScope
class ByteIOTrackingDataFactoryImpl @Inject constructor(
    queryKeyProvider: QueryKeyProvider,
): ByteIOTrackingDataFactory,
    QueryKeyProvider by queryKeyProvider {

    var requestId = ""; private set
    var searchId = ""; private set

    fun renew(requestId: String) {
        update(requestId)

        searchId = requestId

        SearchId.update(searchId)
    }

    fun update(requestId: String) {
        this.requestId = requestId
    }

    override fun create(isFirstPage: Boolean) =
        ByteIOTrackingData(
            imprId = requestId,
            searchId = searchId,
            keyword = queryKey,
            isFirstPage = isFirstPage,
        )
}
