package com.tokopedia.search.result.product.lastfilter

import com.tokopedia.iris.Iris
import com.tokopedia.track.TrackApp

class LastFilterListenerDelegate(
    private val iris: Iris,
) : LastFilterListener {

    override fun onImpressedLastFilter(lastFilterDataView: LastFilterDataView) {
        lastFilterDataView.impress(iris)
    }

    override fun applyLastFilter(lastFilterDataView: LastFilterDataView) {
        lastFilterDataView.click(TrackApp.getInstance().gtm)
    }

    override fun closeLastFilter(lastFilterDataView: LastFilterDataView) {
        lastFilterDataView.clickOtherAction(TrackApp.getInstance().gtm)
    }
}