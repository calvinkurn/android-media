package com.tokopedia.search.result.shop.presentation.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.shop.domain.model.SearchShopModel
import com.tokopedia.search.result.shop.presentation.model.ShopSuggestionDataView
import com.tokopedia.search.result.stubExecute
import com.tokopedia.search.shouldBe
import com.tokopedia.search.shouldBeInstanceOf
import org.junit.Test

private const val suggestionJSON = "searchshop/suggestion/suggestion.json"
internal class SearchShopSuggestionTest: SearchShopDataViewTestFixtures() {

    @Test
    fun `Search shop with suggestion`() {
        val searchShopModel = suggestionJSON.jsonToObject<SearchShopModel>()
        `Given search shop API will be succesful`(searchShopModel)

        `When search shop tab is visible`()

        `Then verify search shop suggestion`(searchShopModel)
    }

    private fun `Given search shop API will be succesful`(searchShopModel: SearchShopModel) {
        searchShopFirstPageUseCase.stubExecute().returns(searchShopModel)
    }

    private fun `When search shop tab is visible`() {
        searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
    }

    private fun `Then verify search shop suggestion`(searchShopModel: SearchShopModel) {
        val searchShopState = searchShopViewModel.getSearchShopLiveData().value
        val visitableList = searchShopState?.data as List<Visitable<*>>
        visitableList[0].shouldBeInstanceOf<ShopSuggestionDataView>()

        val suggestionModel = searchShopModel.aceSearchShop.suggestion
        val suggestionViewModel = visitableList[0] as ShopSuggestionDataView
        suggestionViewModel.currentKeyword shouldBe suggestionModel.currentKeyword
        suggestionViewModel.query shouldBe suggestionModel.query
        suggestionViewModel.text shouldBe suggestionModel.text
    }
}