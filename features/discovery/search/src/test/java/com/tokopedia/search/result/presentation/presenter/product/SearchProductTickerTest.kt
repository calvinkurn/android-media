package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.utils.Dimension90Utils
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.presentation.model.TickerDataView
import io.mockk.every
import io.mockk.slot
import io.mockk.verify
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Test
import rx.Subscriber

private const val ticker = "searchproduct/ticker/ticker.json"

internal class SearchProductTickerTest: ProductListPresenterTestFixtures() {

    private val searchProductModel = ticker.jsonToObject<SearchProductModel>()
    private val keyword = "laptop murah"
    private val searchParameter = mapOf<String, Any>(
        SearchApiConst.Q to keyword,
        SearchApiConst.NAVSOURCE to "campaign",
        SearchApiConst.SRP_PAGE_ID to "1234",
        SearchApiConst.SRP_PAGE_TITLE to "test page title",
    )
    private val visitableListSlot = slot<List<Visitable<*>>>()
    private val visitableList by lazy { visitableListSlot.captured }

    @Test
    fun `show ticker in visitable list`() {
        `Given Search Product API will return SearchProductModel`(searchProductModel)
        `Given view will return keyword`(keyword)

        `When Load Data`(searchParameter)

        `Then verify view set visitable list`()
        `Then assert ticker data view`(
            searchProductModel.searchProduct.data.ticker,
            keyword,
            Dimension90Utils.getDimension90(searchParameter),
        )
    }

    private fun `Given Search Product API will return SearchProductModel`(
        searchProductModel: SearchProductModel
    ) {
        every { searchProductFirstPageUseCase.execute(any(), any()) } answers {
            secondArg<Subscriber<SearchProductModel>>().complete(searchProductModel)
        }
    }

    private fun `Given view will return keyword`(keyword: String) {
        every { productListView.queryKey } returns keyword
    }

    private fun `When Load Data`(searchParameter: Map<String, Any>) {
        productListPresenter.loadData(searchParameter)
    }

    private fun `Then verify view set visitable list`() {
        verify {
            productListView.setProductList(capture(visitableListSlot))
        }
    }

    private fun `Then assert ticker data view`(
        expectedTickerModel: SearchProductModel.Ticker,
        expectedKeyword: String,
        expectedDimension90: String,
    ) {
        val tickerDataView = getTickerDataView()

        assertThat(tickerDataView.query, `is`(expectedTickerModel.query))
        assertThat(tickerDataView.text, `is`(expectedTickerModel.text))
        assertThat(tickerDataView.typeId, `is`(expectedTickerModel.typeId))
        assertThat(tickerDataView.componentId, `is`(expectedTickerModel.componentId))
        assertThat(tickerDataView.trackingOption, `is`(expectedTickerModel.trackingOption))
        assertThat(tickerDataView.keyword, `is`(expectedKeyword))
        assertThat(tickerDataView.dimension90, `is`(expectedDimension90))
    }

    private fun getTickerDataView() =
        visitableList.filterIsInstance<TickerDataView>().first()
}