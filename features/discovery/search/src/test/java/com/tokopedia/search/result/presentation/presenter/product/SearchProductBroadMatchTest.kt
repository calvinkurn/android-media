package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.presentation.model.BroadMatchItemViewModel
import com.tokopedia.search.result.presentation.model.BroadMatchViewModel
import com.tokopedia.search.result.presentation.model.ProductItemViewModel
import com.tokopedia.search.result.presentation.model.SuggestionViewModel
import com.tokopedia.search.shouldBe
import com.tokopedia.search.shouldBeInstanceOf
import io.mockk.every
import io.mockk.slot
import io.mockk.verify
import org.junit.Test
import rx.Subscriber

private const val broadMatchResponseCode1EmptySearch = "searchproduct/broadmatch/broad-match-response-code-1-empty-search.json"
private const val broadMatchResponseCode1NotEmptySearch = "searchproduct/broadmatch/broad-match-response-code-1-not-empty-search.json"
private const val broadMatchResponseCode4 = "searchproduct/broadmatch/broad-match-response-code-4.json"
private const val broadMatchResponseCode4ButNoBroadmatch = "searchproduct/broadmatch/broad-match-response-code-4-but-no-broadmatch.json"
private const val broadMatchResponseCode4NoSuggestion = "searchproduct/broadmatch/broad-match-response-code-4-no-suggestion.json"
private const val broadMatchResponseCode5 = "searchproduct/broadmatch/broad-match-response-code-5.json"

internal class SearchProductBroadMatchTest: ProductListPresenterTestFixtures() {

    private val visitableListSlot = slot<List<Visitable<*>>>()

    @Test
    fun `DO NOT show broad match and show empty search when response code is NOT 4 or 5`() {
        val searchProductModel = broadMatchResponseCode1EmptySearch.jsonToObject<SearchProductModel>()

        `Given Search Product API will return SearchProductModel`(searchProductModel)

        `When Load Data`()

        `Then assert view will only show empty search`()
    }

    private fun `Given Search Product API will return SearchProductModel`(searchProductModel: SearchProductModel) {
        every { searchProductFirstPageUseCase.execute(any(), any()) }.answers {
            secondArg<Subscriber<SearchProductModel>>().complete(searchProductModel)
        }
    }

    private fun `When Load Data`() {
        productListPresenter.loadData(mapOf())
    }

    private fun `Then assert view will only show empty search`() {
        verify {
            productListView.setEmptyProduct(null)
        }
    }

    @Test
    fun `Show empty result when response code is 4 or 5 but does not have broad match`() {
        val searchProductModel = broadMatchResponseCode4ButNoBroadmatch.jsonToObject<SearchProductModel>()

        `Given Search Product API will return SearchProductModel`(searchProductModel)

        `When Load Data`()

        `Then assert view will only show empty search`()
    }

    @Test
    fun `Show broad match and DO NOT show empty search for response code 4 and no product list`() {
        val searchProductModel = broadMatchResponseCode4.jsonToObject<SearchProductModel>()
        `Given Search Product API will return SearchProductModel`(searchProductModel)

        `When Load Data`()

        `Then assert view will show product list`()

        val visitableList = visitableListSlot.captured
        `Then assert visitable list contains SuggestionViewModel as first item`(visitableList)
        `Then assert visitable list contains BroadMatchViewModel`(1, visitableList, searchProductModel)
    }

    private fun `Then assert view will show product list`() {
        verify {
            productListView.setProductList(capture(visitableListSlot))
            productListView.updateScrollListener()
            productListView.hideRefreshLayout()
        }
    }

    private fun `Then assert visitable list contains SuggestionViewModel as first item`(visitableList: List<Visitable<*>>) {
        visitableList[0].shouldBeInstanceOf<SuggestionViewModel>()
    }

    private fun `Then assert visitable list contains BroadMatchViewModel`(
            expectedBroadMatchStartingPosition: Int,
            visitableList: List<Visitable<*>>,
            searchProductModel: SearchProductModel
    ) {
        val otherRelated = searchProductModel.searchProduct.related.otherRelated
        visitableList.filterIsInstance<BroadMatchViewModel>().size shouldBe otherRelated.size

        var index = visitableList.indexOfFirst { it is BroadMatchViewModel }
        index shouldBe expectedBroadMatchStartingPosition

        otherRelated.forEach {
            val visitable = visitableList[index]
            visitable.shouldBeInstanceOf<BroadMatchViewModel>()

            val broadMatchViewModel = visitable as BroadMatchViewModel
            broadMatchViewModel.assertBroadMatchViewModel(it)

            index++
        }
    }

    private fun BroadMatchViewModel.assertBroadMatchViewModel(otherRelated: SearchProductModel.OtherRelated) {
        keyword shouldBe otherRelated.keyword
        applink shouldBe otherRelated.applink
        broadMatchItemViewModelList.size shouldBe otherRelated.otherRelatedProductList.size

        otherRelated.otherRelatedProductList.forEachIndexed { index, otherRelatedProduct ->
            broadMatchItemViewModelList[index].assertBroadMatchItemViewModel(otherRelatedProduct)
        }
    }

    private fun BroadMatchItemViewModel.assertBroadMatchItemViewModel(otherRelatedProduct: SearchProductModel.OtherRelatedProduct) {
        id shouldBe otherRelatedProduct.id
        name shouldBe otherRelatedProduct.name
        price shouldBe otherRelatedProduct.price
        imageUrl shouldBe otherRelatedProduct.imageUrl
        rating shouldBe otherRelatedProduct.rating
        countReview shouldBe otherRelatedProduct.countReview
        url shouldBe otherRelatedProduct.url
        applink shouldBe otherRelatedProduct.applink
        priceString shouldBe otherRelatedProduct.priceString
    }

    @Test
    fun `Show broad match without suggestion`() {
        val searchProductModel = broadMatchResponseCode4NoSuggestion.jsonToObject<SearchProductModel>()

        `Given Search Product API will return SearchProductModel`(searchProductModel)

        `When Load Data`()

        `Then assert view will show product list`()

        val visitableList = visitableListSlot.captured
        `Then assert visitable list contains BroadMatchViewModel`(0, visitableList, searchProductModel)
    }

    @Test
    fun `DO NOT show broad match and show product list when response code is NOT 4 or 5`() {
        val searchProductModel = broadMatchResponseCode1NotEmptySearch.jsonToObject<SearchProductModel>()
        `Given Search Product API will return SearchProductModel`(searchProductModel)

        `When Load Data`()

        `Then assert view will not show broad match`()
    }

    private fun `Then assert view will not show broad match`() {
        verify {
            productListView.setProductList(capture(visitableListSlot))
        }

        visitableListSlot.captured.find { it is BroadMatchViewModel } shouldBe null
    }

    @Test
    fun `Show broad match under product list in page 1`() {
        val searchProductModel = broadMatchResponseCode5.jsonToObject<SearchProductModel>()
        `Given Search Product API will return SearchProductModel`(searchProductModel)

        `When Load Data`()

        `Then assert view will show product list`()

        val visitableList = visitableListSlot.captured
        `Then assert suggestion view model is positioned after product list`(visitableList)

        val expectedBroadMatchStartingPosition = visitableList.indexOfLast { it is SuggestionViewModel } + 1
        `Then assert visitable list contains BroadMatchViewModel`(expectedBroadMatchStartingPosition, visitableList, searchProductModel)
    }

    private fun `Then assert suggestion view model is positioned after product list`(visitableList: List<Visitable<*>>) {
        val lastProductItemIndex = visitableList.indexOfLast { it is ProductItemViewModel }
        val expectedSuggestionViewModelIndex = lastProductItemIndex + 1

        visitableList.indexOfFirst { it is SuggestionViewModel } shouldBe expectedSuggestionViewModelIndex
    }

    @Test
    fun `Test broad match with product list on page 2 and above`() {

    }
}