package com.tokopedia.tokopedianow.search.presentation.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.common.domain.mapper.TickerMapper
import com.tokopedia.tokopedianow.common.model.TokoNowTickerUiModel
import com.tokopedia.tokopedianow.search.domain.model.SearchModel
import com.tokopedia.tokopedianow.searchcategory.jsonToObject
import com.tokopedia.tokopedianow.util.TestUtils.getParentPrivateField
import org.junit.Assert
import org.junit.Test

class SearchTickerTest: BaseSearchPageLoadTest() {

    @Test
    fun `get targeted ticker should return success with blockAddToCart value as true`() {
        val searchModel = "search/first-page-8-products-block-add-to-cart.json".jsonToObject<SearchModel>()

        `Given get search first page use case will be successful`(searchModel, requestParamsSlot)
        `Given choose address data`()
        `Given search view model`()

        `When view created`()

        val visitableList = tokoNowSearchViewModel.visitableListLiveData.value!!
        val tickerWidget = visitableList.getTicketWidgetUiModel()

        `Then assert ticker data`(searchModel, tickerWidget)
        `Then assert hasBlockedAddToCart`(true)
    }

    @Test
    fun `get targeted ticker should return success with blockAddToCart value as false`() {
        val searchModel = "search/first-page-8-products-not-block-add-to-cart.json".jsonToObject<SearchModel>()

        `Given get search first page use case will be successful`(searchModel, requestParamsSlot)
        `Given choose address data`()
        `Given search view model`()

        `When view created`()

        val visitableList = tokoNowSearchViewModel.visitableListLiveData.value!!
        val tickerWidget = visitableList.getTicketWidgetUiModel()

        `Then assert ticker data`(searchModel, tickerWidget)
        `Then assert hasBlockedAddToCart`(false)
    }

    private fun List<Visitable<*>>.getTicketWidgetUiModel() = find { it is TokoNowTickerUiModel } as TokoNowTickerUiModel

    private fun `Then assert ticker data`(searchModel: SearchModel, tickerUiModel: TokoNowTickerUiModel) {
        Assert.assertEquals(TickerMapper.mapTickerData(searchModel.targetedTicker).second, tickerUiModel.tickers)
    }

    private fun `Then assert hasBlockedAddToCart`(expected: Boolean) {
        val hasBlockedAddToCart = tokoNowSearchViewModel.getParentPrivateField<Boolean>("hasBlockedAddToCart")
        Assert.assertEquals(expected, hasBlockedAddToCart)
    }
}
