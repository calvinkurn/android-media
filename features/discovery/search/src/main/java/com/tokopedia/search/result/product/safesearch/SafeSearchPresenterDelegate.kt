package com.tokopedia.search.result.product.safesearch

import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.model.SearchParameter
import com.tokopedia.search.result.presentation.model.TickerDataView

class SafeSearchPresenterDelegate(
    private val safeSearchPreference: MutableSafeSearchPreference,
    private val safeSearchView: SafeSearchView,
) : SafeSearchPresenter {
    override val isShowAdult: Boolean
        get() = safeSearchPreference.isShowAdult

    override fun modifySearchParameterIfShowAdultEnabled(searchParameter: SearchParameter) {
        if (isShowAdult) {
            searchParameter.set(SearchApiConst.SHOW_ADULT, SearchApiConst.SHOW_ADULT_ENABLED)
        }
    }

    private fun enableShowAdult() {
        safeSearchPreference.isShowAdult = true
        safeSearchView.registerSameSessionListener(safeSearchPreference)
    }

    private fun isShowAdultTicker(ticker: TickerDataView): Boolean {
        return SAFE_SEARCH_TICKER_COMPONENT_ID == ticker.componentId
    }

    override fun showAdultForAdultTicker(ticker: TickerDataView) {
        if (isShowAdultTicker(ticker)) {
            enableShowAdult()
        }
    }

    override fun onSafeSearchViewDestroyed() {
        safeSearchView.release()
    }
}
