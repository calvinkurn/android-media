package com.tokopedia.search.result.product.lastfilter

import com.tokopedia.iris.Iris
import com.tokopedia.search.result.product.QueryKeyProvider
import com.tokopedia.track.TrackApp

class LastFilterListenerDelegate(
    private val iris: Iris,
    private val queryKeyProvider: QueryKeyProvider,
) : LastFilterListener {
    private val queryKey: String
        get() = queryKeyProvider.queryKey

    override fun onImpressedLastFilter(lastFilterDataView: LastFilterDataView) {
        lastFilterDataView.impress(iris)
    }

    override fun applyLastFilter(lastFilterDataView: LastFilterDataView) {
        lastFilterDataView.click(TrackApp.getInstance().gtm)
    }

    override fun closeLastFilter(lastFilterDataView: LastFilterDataView) {
        LastFilterTracking.trackEventLastFilterClickClose(
            queryKey,
            lastFilterDataView.sortFilterParamsString(),
            lastFilterDataView.dimension90,
        )
    }
}