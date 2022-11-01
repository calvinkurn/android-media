package com.tokopedia.search.result.product.ticker

import com.tokopedia.filter.newdynamicfilter.controller.FilterController
import com.tokopedia.iris.Iris
import com.tokopedia.search.result.presentation.model.TickerDataView
import com.tokopedia.search.result.presentation.view.listener.TickerListener
import com.tokopedia.search.result.product.ProductListParameterListener
import com.tokopedia.search.result.product.ViewUpdater
import com.tokopedia.search.result.product.safesearch.SafeSearchPresenter
import com.tokopedia.search.utils.UrlParamUtils
import com.tokopedia.search.utils.addFilterOrigin
import com.tokopedia.track.TrackApp

class TickerListenerDelegate(
    private val iris: Iris,
    private val filterController: FilterController,
    private val viewUpdater: ViewUpdater,
    private val tickerPresenter: TickerPresenter?,
    private val parameterListener: ProductListParameterListener,
    private val safeSearchPresenter: SafeSearchPresenter?,
) : TickerListener {
    override fun onTickerImpressed(tickerDataView: TickerDataView) {
        tickerDataView.impress(iris)
    }

    override fun onTickerClicked(tickerDataView: TickerDataView) {
        tickerDataView.click(TrackApp.getInstance().gtm)

        safeSearchPresenter?.showAdultForAdultTicker(tickerDataView)

        applyParamsFromTicker(UrlParamUtils.getParamMap(tickerDataView.query))
    }

    private fun applyParamsFromTicker(tickerParams: HashMap<String?, String?>) {
        val params = HashMap(filterController.getParameter().addFilterOrigin())
        params.putAll(tickerParams)
        parameterListener.refreshSearchParameter(params)
        parameterListener.reloadData()
    }

    override fun onTickerDismissed() {
        tickerPresenter?.onPriceFilterTickerDismissed()
        viewUpdater.removeFirstItemWithCondition {
            it is TickerDataView
        }
    }

    override val isTickerHasDismissed
        get() = tickerPresenter?.isTickerHasDismissed ?: false
}
