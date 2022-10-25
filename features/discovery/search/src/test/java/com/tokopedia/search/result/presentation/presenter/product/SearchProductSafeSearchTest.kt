package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.presentation.model.TickerDataView
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import io.mockk.slot
import io.mockk.verify
import org.junit.Test
import rx.Subscriber

private const val ticker = "searchproduct/ticker/ticker.json"
private const val showAdult = "searchproduct/safesearch/show_adult.json"

internal class SearchProductSafeSearchTest : ProductListPresenterTestFixtures() {
    private val showAdultSearchProductModel = showAdult.jsonToObject<SearchProductModel>()
    private val tickerSearchProductModel = ticker.jsonToObject<SearchProductModel>()
    private val keyword = "bawahan wanita remaja"
    private val searchParameter = mapOf<String, Any>(
        SearchApiConst.Q to keyword,
    )
    private val visitableListSlot = slot<List<Visitable<*>>>()
    private val visitableList by lazy { visitableListSlot.captured }

    @Test
    fun `click show_adult ticker should enable showAdult flag`() {
        `Given Search Product API return searchProductModel`(showAdultSearchProductModel)

        val showAdultTicker = getTickerDataView()
        `When show_adult ticker clicked`(showAdultTicker)

        `Then verify showAdult safeSearchPreference is enabled`()
        `Then verify SafeSearchLifecycleHelper is called`()
    }

    @Test
    fun `click other ticker should not enable showAdult flag`() {
        `Given Search Product API return searchProductModel`(tickerSearchProductModel)

        val showAdultTicker = getTickerDataView()
        `When show_adult ticker clicked`(showAdultTicker)

        `Then verify showAdult safeSearchPreference is not enabled`()
        `Then verify SafeSearchLifecycleHelper is not called`()
    }

    private fun `Given Search Product API return searchProductModel`(searchProductModel: SearchProductModel) {
        every { searchProductFirstPageUseCase.execute(any(), any()) } answers {
            secondArg<Subscriber<SearchProductModel>>().complete(searchProductModel)
        }

        every {
            productListView.setProductList(capture(visitableListSlot))
        } just runs

        productListPresenter.loadData(searchParameter)
    }

    private fun `When show_adult ticker clicked`(tickerDataView: TickerDataView) {
        productListPresenter.showAdultForAdultTicker(tickerDataView)
    }

    private fun getTickerDataView() = visitableList.filterIsInstance<TickerDataView>().first()

    private fun `Then verify showAdult safeSearchPreference is enabled`() {
        verify {
            safeSearchPreference.isShowAdult = true
        }
    }

    private fun `Then verify SafeSearchLifecycleHelper is called`() {
        verify {
            safeSearchView.registerSameSessionListener(safeSearchPreference)
        }
    }

    private fun `Then verify showAdult safeSearchPreference is not enabled`() {
        verify(exactly = 0) {
            safeSearchPreference.isShowAdult = true
        }
    }

    private fun `Then verify SafeSearchLifecycleHelper is not called`() {
        verify(exactly = 0) {
            safeSearchView.registerSameSessionListener(safeSearchPreference)
        }
    }
}
