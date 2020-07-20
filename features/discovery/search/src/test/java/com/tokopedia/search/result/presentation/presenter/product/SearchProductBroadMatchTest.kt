package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.search.*
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.presentation.model.BroadMatchItemViewModel
import com.tokopedia.search.result.presentation.model.BroadMatchViewModel
import com.tokopedia.search.result.presentation.model.ProductItemViewModel
import com.tokopedia.search.result.presentation.model.SuggestionViewModel
import com.tokopedia.search.shouldBe
import com.tokopedia.search.shouldBeInstanceOf
import io.mockk.*
import org.junit.Test
import rx.Subscriber

private const val broadMatchResponseCode1EmptySearch = "searchproduct/broadmatch/response-code-1-empty-search.json"
private const val broadMatchResponseCode1NotEmptySearch = "searchproduct/broadmatch/response-code-1-not-empty-search.json"
private const val broadMatchResponseCode4 = "searchproduct/broadmatch/response-code-4.json"
private const val broadMatchResponseCode4ButNoBroadmatch = "searchproduct/broadmatch/response-code-4-but-no-broadmatch.json"
private const val broadMatchResponseCode4NoSuggestion = "searchproduct/broadmatch/response-code-4-no-suggestion.json"
private const val broadMatchResponseCode5With5Products = "searchproduct/broadmatch/response-code-5-with-5-products.json"
private const val broadMatchResponseCode5With8Products = "searchproduct/broadmatch/response-code-5-with-8-products.json"
private const val broadMatchResponseCode5Page1With12Products = "searchproduct/broadmatch/response-code-5-page-1-with-12-products.json"
private const val broadMatchResponseCode5Page2With12Products = "searchproduct/broadmatch/response-code-5-page-2-with-12-products.json"
private const val broadMatchResponseCode5Page1With16Products = "searchproduct/broadmatch/response-code-5-page-1-with-16-products.json"
private const val broadMatchResponseCode5Page2With16Products = "searchproduct/broadmatch/response-code-5-page-2-with-16-products.json"
private const val broadMatchResponseCode5Page1WithResponseLowerThanCount = "searchproduct/broadmatch/response-code-5-page-1-with-response-lower-than-count.json"
private const val broadMatchResponseCode5Page2WithResponseLowerThanCount = "searchproduct/broadmatch/response-code-5-page-2-with-response-lower-than-count.json"

internal class SearchProductBroadMatchTest: ProductListPresenterTestFixtures() {

    private val visitableListSlot = slot<List<Visitable<*>>>()

    @Test
    fun `Show empty result when response code is NOT 4 or 5`() {
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
        `Then assert visitable list contains BroadMatchViewModel`(
                1, visitableList, searchProductModel
        )
        `Then assert tracking event impression broad match`(visitableList)
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
        val otherRelated = searchProductModel.searchProduct.data.related.otherRelatedList
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
        broadMatchItemViewModelList.size shouldBe otherRelated.productList.size

        otherRelated.productList.forEachIndexed { index, otherRelatedProduct ->
            broadMatchItemViewModelList[index].assertBroadMatchItemViewModel(
                    otherRelatedProduct, index + 1, otherRelated.keyword
            )
        }
    }

    private fun BroadMatchItemViewModel.assertBroadMatchItemViewModel(
            otherRelatedProduct: SearchProductModel.OtherRelatedProduct,
            expectedPosition: Int,
            expectedAlternativeKeyword: String
    ) {
        id shouldBe otherRelatedProduct.id
        name shouldBe otherRelatedProduct.name
        price shouldBe otherRelatedProduct.price
        imageUrl shouldBe otherRelatedProduct.imageUrl
        rating shouldBe otherRelatedProduct.rating
        countReview shouldBe otherRelatedProduct.countReview
        url shouldBe otherRelatedProduct.url
        applink shouldBe otherRelatedProduct.applink
        priceString shouldBe otherRelatedProduct.priceString
        position shouldBe expectedPosition
        alternativeKeyword shouldBe expectedAlternativeKeyword
        shopLocation shouldBe otherRelatedProduct.shop.city

        badgeItemViewModelList.listShouldBe(otherRelatedProduct.badgeList) { actual, expected ->
            actual.imageUrl shouldBe expected.imageUrl
            actual.isShown shouldBe expected.isShown
        }

        freeOngkirViewModel.isActive shouldBe otherRelatedProduct.freeOngkir.isActive
        freeOngkirViewModel.imageUrl shouldBe otherRelatedProduct.freeOngkir.imageUrl
    }

    private fun `Then assert tracking event impression broad match`(visitableList: List<Visitable<*>>) {
        val broadMatchViewModelList = visitableList.filterIsInstance<BroadMatchViewModel>()

        broadMatchViewModelList.forEach { broadMatchViewModel ->
            verify {
                productListView.trackBroadMatchImpression(
                        broadMatchViewModel.keyword,
                        broadMatchViewModel.broadMatchItemViewModelList.map { it.asImpressionObjectDataLayer() }
                )
            }
        }
    }

    @Test
    fun `Show broad match without suggestion title`() {
        val searchProductModel = broadMatchResponseCode4NoSuggestion.jsonToObject<SearchProductModel>()

        `Given Search Product API will return SearchProductModel`(searchProductModel)

        `When Load Data`()

        `Then assert view will show product list`()

        val visitableList = visitableListSlot.captured
        `Then assert visitable list contains BroadMatchViewModel`(
                0, visitableList, searchProductModel
        )
        `Then assert tracking event impression broad match`(visitableList)
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
    fun `Show broad match under product list in page 1 with response product size lower than requested size`() {
        val searchProductModel = broadMatchResponseCode5With5Products.jsonToObject<SearchProductModel>()

        `Test Broad Match shown from page 1`(searchProductModel)
    }

    private fun `Test Broad Match shown from page 1`(searchProductModel: SearchProductModel) {
        `Given Search Product API will return SearchProductModel`(searchProductModel)

        `When Load Data`()

        `Then assert view will show product list`()

        val visitableList = visitableListSlot.captured
        `Then assert suggestion view model is positioned after product list`(visitableList)

        val expectedBroadMatchStartingPosition = visitableList.indexOfLast { it is SuggestionViewModel } + 1
        `Then assert visitable list contains BroadMatchViewModel`(
                expectedBroadMatchStartingPosition, visitableList, searchProductModel
        )
        `Then assert tracking event impression broad match`(visitableList)
    }

    private fun `Then assert suggestion view model is positioned after product list`(visitableList: List<Visitable<*>>) {
        val lastProductItemIndex = visitableList.indexOfLast { it is ProductItemViewModel }
        val expectedSuggestionViewModelIndex = lastProductItemIndex + 1
        val actualSuggestionViewModelIndex = visitableList.indexOfFirst { it is SuggestionViewModel }

        actualSuggestionViewModelIndex shouldBe expectedSuggestionViewModelIndex
    }

    @Test
    fun `Show broad match under product list in page 1 with response product size equal to requested size`() {
        val searchProductModel = broadMatchResponseCode5With8Products.jsonToObject<SearchProductModel>()

        `Test Broad Match shown from page 1`(searchProductModel)
    }

    @Test
    fun `Show broad match under product list in page 2 or above with response product size lower than requested size`() {
        val searchProductModelPage1 = broadMatchResponseCode5Page1With12Products.jsonToObject<SearchProductModel>()
        val searchProductModelPage2 = broadMatchResponseCode5Page2With12Products.jsonToObject<SearchProductModel>()

        `Test Broad Match shown from page 2 or above`(searchProductModelPage1, searchProductModelPage2)
    }

    private fun `Test Broad Match shown from page 2 or above`(searchProductModelPage1: SearchProductModel, searchProductModelPage2: SearchProductModel) {
        val visitableList = mutableListOf<Visitable<*>>()

        `Given Search Product API will return SearchProductModel`(searchProductModelPage1)
        `Given Search Product Load More API will return SearchProductModel`(searchProductModelPage2)
        `Given Product List Presenter already load data`(visitableList)

        `When Load More Data`()

        `Then assert view will add product list`(visitableList)
        `Then assert suggestion view model is positioned after product list`(visitableList)
        val expectedBroadMatchStartingPosition = visitableList.indexOfLast { it is SuggestionViewModel } + 1
        `Then assert visitable list contains BroadMatchViewModel`(
                expectedBroadMatchStartingPosition, visitableList, searchProductModelPage1
        )
        `Then assert tracking event impression broad match`(visitableList)
    }

    private fun `Given Search Product Load More API will return SearchProductModel`(searchProductModel: SearchProductModel) {
        every { searchProductLoadMoreUseCase.execute(any(), any()) }.answers {
            secondArg<Subscriber<SearchProductModel>>().complete(searchProductModel)
        }
    }

    private fun `Given Product List Presenter already load data`(visitableList: MutableList<Visitable<*>>) {
        every {
            productListView.setProductList(capture(visitableListSlot))
        } just runs

        productListPresenter.loadData(mapOf())

        visitableList.addAll(visitableListSlot.captured)
        visitableListSlot.clear()
    }

    private fun `When Load More Data`() {
        productListPresenter.loadMoreData(mapOf())
    }

    private fun `Then assert view will add product list`(visitableList: MutableList<Visitable<*>>) {
        verify {
            productListView.addProductList(capture(visitableListSlot))
        }

        visitableList.addAll(visitableListSlot.captured)
    }

    @Test
    fun `Show broad match under product list in page 2 or above with response product size equal to requested size`() {
        val searchProductModelPage1 = broadMatchResponseCode5Page1With16Products.jsonToObject<SearchProductModel>()
        val searchProductModelPage2 = broadMatchResponseCode5Page2With16Products.jsonToObject<SearchProductModel>()

        `Test Broad Match shown from page 2 or above`(searchProductModelPage1, searchProductModelPage2)
    }

    @Test
    fun `Show broad match under product list in page 2 or above with response product size lower than count (anomaly from backend)`() {
        val searchProductModelPage1 = broadMatchResponseCode5Page1WithResponseLowerThanCount.jsonToObject<SearchProductModel>()
        val searchProductModelPage2 = broadMatchResponseCode5Page2WithResponseLowerThanCount.jsonToObject<SearchProductModel>()

        `Test Broad Match shown from page 2 or above`(searchProductModelPage1, searchProductModelPage2)
    }
}