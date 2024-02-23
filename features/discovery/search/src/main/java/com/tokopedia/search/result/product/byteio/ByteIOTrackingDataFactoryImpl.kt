package com.tokopedia.search.result.product.byteio

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

    fun renew(requestId: String, searchParameter: Map<String, Any>) {
        update(requestId)

        searchId = requestId
    }

    fun update(requestId: String) {
        this.requestId = requestId
    }

    override fun create(isFirstPage: Boolean) =
        ByteIOTrackingData(
            imprId = requestId,
            searchId = searchId,
            searchEntrance = "", // TODO
            enterFrom = "", // TODO
            keyword = queryKey,
            isFirstPage = isFirstPage,
        )
}
