package com.tokopedia.search.result.product.byteio

import com.tokopedia.analytics.byteio.AppLogAnalytics
import com.tokopedia.analytics.byteio.search.AppLogSearch
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamKey.SEARCH_ENTRANCE
import com.tokopedia.discovery.common.analytics.SearchEntrance
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

    val searchEntrance = AppLogAnalytics.getCurrentData(SEARCH_ENTRANCE)?.toString().orEmpty()

    fun renew(requestId: String, searchId: String) {
        update(requestId)

        this.searchId = searchId

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
            searchEntrance = searchEntrance,
        )
}
