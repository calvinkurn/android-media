package com.tokopedia.search.result.product.safesearch

import androidx.lifecycle.LifecycleOwner
import com.tokopedia.search.result.presentation.model.TickerDataView

class SafeSearchPresenterDelegate(
    private val safeSearchPreference: SafeSearchPreference,
    private val lifecycleOwner: LifecycleOwner?,
) : SafeSearchPresenter {
    override val isShowAdult: Boolean
        get() = safeSearchPreference.isShowAdult

    override fun initSafeSearch() {
        SafeSearchLifecycleHelper.resetIfNoObserver(safeSearchPreference)
    }

    override fun enableShowAdult() {
        safeSearchPreference.isShowAdult = true
        SafeSearchLifecycleHelper.registerProcessLifecycleOwner(safeSearchPreference, lifecycleOwner)
    }

    override fun isShowAdultTicker(ticker: TickerDataView): Boolean {
        return SAFE_SEARCH_TICKER_COMPONENT_ID == ticker.componentId
    }
}
