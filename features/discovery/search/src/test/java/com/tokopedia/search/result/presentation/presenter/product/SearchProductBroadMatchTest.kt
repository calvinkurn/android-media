package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.search.*
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.presentation.model.*
import com.tokopedia.search.shouldBe
import com.tokopedia.search.shouldBeInstanceOf
import io.mockk.*
import org.junit.Test
import rx.Subscriber

private const val broadMatchResponseCode1EmptySearch = "searchproduct/broadmatch/response-code-1-empty-search.json"
private const val broadMatchResponseCode1NotEmptySearch = "searchproduct/broadmatch/response-code-1-not-empty-search.json"
private const val broadMatchResponseCode1Page2NotEmptySearch = "searchproduct/broadmatch/response-code-1-page-2-not-empty-search.json"
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
private const val broadMatchResponseCode0Page1Position0 = "searchproduct/broadmatch/response-code-0-page-1-position-0.json"
private const val broadMatchResponseCode0Page1Position1 = "searchproduct/broadmatch/response-code-0-page-1-position-1.json"
private const val broadMatchResponseCode0Page1Position4 = "searchproduct/broadmatch/response-code-0-page-1-position-4.json"
private const val broadMatchResponseCode0Page1Position12 = "searchproduct/broadmatch/response-code-0-page-1-position-12.json"
private const val broadMatchResponseCode0Page2 = "searchproduct/broadmatch/response-code-0-page-2.json"
private const val broadMatchResponseCode0Page2Empty = "searchproduct/broadmatch/response-code-0-page-2-empty.json"
private const val broadMatchResponseCode0Page2With1Product = "searchproduct/broadmatch/response-code-0-page-2-with-1-product.json"

internal class SearchProductBroadMatchTest: ProductListPresenterTestFixtures() {

    private val visitableListSlot = slot<List<Visitable<*>>>()

    @Test
    fun `Show empty result when response code is NOT 0, 4, or 5`() {
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
            productListView.setEmptyProduct(null, any())
        }
    }

    @Test
    fun `Show empty result when response code is 0, 4, or 5 but does not have broad match`() {
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
        `Then assert visitable list does not contain SeparatorViewModel`(visitableList)
    }

    private fun `Then assert visitable list does not contain SeparatorViewModel`(visitableList: List<Visitable<*>>) {
        val separatorIndex = visitableList.indexOfFirst { it is SeparatorDataView }

        separatorIndex.shouldBe(
                -1,
                "Separator is found on visitable list index $separatorIndex"
        )
    }

    private fun `Then assert view will show product list`() {
        verify {
            productListView.setProductList(capture(visitableListSlot))
            productListView.updateScrollListener()
            productListView.hideRefreshLayout()
        }
    }

    private fun `Then assert visitable list contains SuggestionViewModel as first item`(visitableList: List<Visitable<*>>) {
        visitableList[0].shouldBeInstanceOf<SuggestionDataView>()
    }

    private fun `Then assert visitable list contains BroadMatchViewModel`(
            expectedBroadMatchStartingPosition: Int,
            visitableList: List<Visitable<*>>,
            searchProductModel: SearchProductModel
    ) {
        val otherRelated = searchProductModel.searchProduct.data.related.otherRelatedList
        visitableList.filterIsInstance<BroadMatchDataView>().size shouldBe otherRelated.size

        var index = visitableList.indexOfFirst { it is BroadMatchDataView }
        index shouldBe expectedBroadMatchStartingPosition

        otherRelated.forEach {
            val visitable = visitableList[index]
            visitable.shouldBeInstanceOf<BroadMatchDataView>()

            val broadMatchViewModel = visitable as BroadMatchDataView
            broadMatchViewModel.assertBroadMatchViewModel(it)

            index++
        }
    }

    private fun BroadMatchDataView.assertBroadMatchViewModel(otherRelated: SearchProductModel.OtherRelated) {
        keyword shouldBe otherRelated.keyword
        applink shouldBe otherRelated.applink
        broadMatchItemDataViewList.size shouldBe otherRelated.productList.size

        otherRelated.productList.forEachIndexed { index, otherRelatedProduct ->
            broadMatchItemDataViewList[index].assertBroadMatchItemViewModel(
                    otherRelatedProduct, index + 1, otherRelated.keyword
            )
        }
    }

    private fun BroadMatchItemDataView.assertBroadMatchItemViewModel(
            otherRelatedProduct: SearchProductModel.OtherRelatedProduct,
            expectedPosition: Int,
            expectedAlternativeKeyword: String
    ) {
        id shouldBe otherRelatedProduct.id
        name shouldBe otherRelatedProduct.name
        price shouldBe otherRelatedProduct.price
        imageUrl shouldBe otherRelatedProduct.imageUrl
        url shouldBe otherRelatedProduct.url
        applink shouldBe otherRelatedProduct.applink
        priceString shouldBe otherRelatedProduct.priceString
        position shouldBe expectedPosition
        alternativeKeyword shouldBe expectedAlternativeKeyword
        isWishlisted shouldBe otherRelatedProduct.isWishlisted
        shopLocation shouldBe otherRelatedProduct.shop.city
        ratingAverage shouldBe otherRelatedProduct.ratingAverage

        badgeItemDataViewList.listShouldBe(otherRelatedProduct.badgeList) { actual, expected ->
            actual.imageUrl shouldBe expected.imageUrl
            actual.isShown shouldBe expected.isShown
        }

        labelGroupDataList.listShouldBe(otherRelatedProduct.labelGroupList) { actual, expected ->
            actual.title shouldBe expected.title
            actual.position shouldBe expected.position
            actual.type shouldBe expected.type
            actual.imageUrl shouldBe expected.url
        }

        freeOngkirDataView.isActive shouldBe otherRelatedProduct.freeOngkir.isActive
        freeOngkirDataView.imageUrl shouldBe otherRelatedProduct.freeOngkir.imageUrl

        isOrganicAds shouldBe otherRelatedProduct.ads.id.isNotEmpty()
        topAdsViewUrl shouldBe otherRelatedProduct.ads.productViewUrl
        this.topAdsClickUrl shouldBe otherRelatedProduct.ads.productClickUrl
        topAdsWishlistUrl shouldBe otherRelatedProduct.ads.productWishlistUrl
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
        `Then assert visitable list does not contain SeparatorViewModel`(visitableList)
    }

    @Test
    fun `DO NOT show broad match and show product list when response code is NOT 0, 4, or 5`() {
        val searchProductModel = broadMatchResponseCode1NotEmptySearch.jsonToObject<SearchProductModel>()
        `Given Search Product API will return SearchProductModel`(searchProductModel)

        `When Load Data`()

        `Then assert view will not show broad match`()
    }

    private fun `Then assert view will not show broad match`() {
        verify {
            productListView.setProductList(capture(visitableListSlot))
        }

        visitableListSlot.captured.assertNotContainBroadMatch()
    }

    private fun List<Visitable<*>>.assertNotContainBroadMatch() {
        val broadMatchIndex = indexOfFirst { it is BroadMatchDataView }

        broadMatchIndex.shouldBe(
                -1,
                "Broad match is found on visitable list index $broadMatchIndex"
        )
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
        `Then assert top separator view model and suggestion view model is positioned under product list`(visitableList)

        val expectedBroadMatchStartingPosition = visitableList.indexOfLast { it is SuggestionDataView } + 1
        `Then assert visitable list contains BroadMatchViewModel`(
                expectedBroadMatchStartingPosition, visitableList, searchProductModel
        )
    }

    private fun `Then assert top separator view model and suggestion view model is positioned under product list`(visitableList: List<Visitable<*>>) {
        val lastProductItemIndex = visitableList.indexOfLast { it is ProductItemDataView }

        val expectedTopSeparatorViewModelIndex = lastProductItemIndex + 1
        val actualTopSeparatorViewModelIndex = visitableList.indexOfFirst { it is SeparatorDataView }

        val expectedSuggestionViewModelIndex = lastProductItemIndex + 2
        val actualSuggestionViewModelIndex = visitableList.indexOfFirst { it is SuggestionDataView }

        expectedTopSeparatorViewModelIndex.shouldBe(actualTopSeparatorViewModelIndex,
                "Separator View Model is at position $expectedTopSeparatorViewModelIndex, should be at position $actualTopSeparatorViewModelIndex"
        )

        expectedSuggestionViewModelIndex.shouldBe(actualSuggestionViewModelIndex,
                "Suggestion View Model is at position $expectedSuggestionViewModelIndex, should be at position $actualSuggestionViewModelIndex"
        )
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
        `Then assert top separator view model and suggestion view model is positioned under product list`(visitableList)
        val expectedBroadMatchStartingPosition = visitableList.indexOfLast { it is SuggestionDataView } + 1
        `Then assert visitable list contains BroadMatchViewModel`(
                expectedBroadMatchStartingPosition, visitableList, searchProductModelPage1
        )
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

    @Test
    fun `DO NOT show broad match in load more when response code is NOT 4 or 5`() {
        val visitableList = mutableListOf<Visitable<*>>()
        val searchProductModelPage1 = broadMatchResponseCode1NotEmptySearch.jsonToObject<SearchProductModel>()
        val searchProductModelPage2 = broadMatchResponseCode1Page2NotEmptySearch.jsonToObject<SearchProductModel>()

        `Given Search Product API will return SearchProductModel`(searchProductModelPage1)
        `Given Search Product Load More API will return SearchProductModel`(searchProductModelPage2)
        `Given Product List Presenter already load data`(visitableList)

        `When Load More Data`()

        `Then assert view will add product list`(visitableList)
        visitableList.assertNotContainBroadMatch()
    }

    @Test
    fun `Show broad match at bottom of all product cards for position 0`() {
        val searchProductModelPage1 = broadMatchResponseCode0Page1Position0.jsonToObject<SearchProductModel>()
        val searchProductModelPage2 = broadMatchResponseCode0Page2.jsonToObject<SearchProductModel>()

        val expectedTopSeparatorPosition = searchProductModelPage1.getTotalProductItem() + searchProductModelPage2.getTotalProductItem()
        val expectedBottomSeparatorPosition = -1

        `Test broad match with position`(searchProductModelPage1, searchProductModelPage2, expectedTopSeparatorPosition, expectedBottomSeparatorPosition) { visitableList ->
            val firstProductItemPosition = visitableList.indexOfFirst { it is ProductItemDataView }

            firstProductItemPosition + expectedTopSeparatorPosition + 1
        }
    }

    private fun SearchProductModel.getTotalProductItem(): Int {
        return this.searchProduct.data.productList.size + this.topAdsModel.data.size
    }

    @Test
    fun `Show broad match at top of all product cards for position 1`() {
        val searchProductModelPage1 = broadMatchResponseCode0Page1Position1.jsonToObject<SearchProductModel>()
        val searchProductModelPage2 = broadMatchResponseCode0Page2.jsonToObject<SearchProductModel>()

        val expectedTopSeparatorPosition = -1
        val expectedBottomSeparatorPosition = 5
        val expectedSuggestionViewModelPosition = expectedTopSeparatorPosition + 1

        `Test broad match with position`(
                searchProductModelPage1, searchProductModelPage2,
                expectedTopSeparatorPosition, expectedBottomSeparatorPosition
        ) { expectedSuggestionViewModelPosition }
    }

    @Test
    fun `Show broad match at product card position 4`() {
        val searchProductModelPage1 = broadMatchResponseCode0Page1Position4.jsonToObject<SearchProductModel>()
        val searchProductModelPage2 = broadMatchResponseCode0Page2.jsonToObject<SearchProductModel>()

        val expectedTopSeparatorPosition = 4
        val expectedBottomSeparatorPosition = 10
        val expectedSuggestionViewModelPosition = expectedTopSeparatorPosition + 1

        `Test broad match with position`(
                searchProductModelPage1, searchProductModelPage2,
                expectedTopSeparatorPosition, expectedBottomSeparatorPosition)
        { expectedSuggestionViewModelPosition }
    }

    @Test
    fun `Show broad match at product card position 12`() {
        val searchProductModelPage1 = broadMatchResponseCode0Page1Position12.jsonToObject<SearchProductModel>()
        val searchProductModelPage2 = broadMatchResponseCode0Page2.jsonToObject<SearchProductModel>()

        val expectedTopSeparatorPosition = 12
        val expectedBottomSeparatorPosition = 18
        val expectedSuggestionViewModelPosition = expectedTopSeparatorPosition + 1

        `Test broad match with position`(
                searchProductModelPage1, searchProductModelPage2,
                expectedTopSeparatorPosition, expectedBottomSeparatorPosition)
        { expectedSuggestionViewModelPosition }
    }

    private fun `Test broad match with position`(
            searchProductModelPage1: SearchProductModel,
            searchProductModelPage2: SearchProductModel,
            expectedTopSeparatorPosition: Int,
            expectedBottomSeparatorPosition: Int = -1,
            getExpectedSuggestionViewModelPosition: (List<Visitable<*>>) -> Int
    ) {
        val visitableList = mutableListOf<Visitable<*>>()

        `Given Search Product API will return SearchProductModel`(searchProductModelPage1)
        `Given Search Product Load More API will return SearchProductModel`(searchProductModelPage2)

        `When load first page and load more data`(visitableList)

        val expectedSuggestionViewModelPosition = getExpectedSuggestionViewModelPosition(visitableList)

        `Then assert top separator view model is positioned before suggestion`(expectedTopSeparatorPosition, expectedSuggestionViewModelPosition, visitableList)

        val expectedBroadMatchViewModelPosition = expectedSuggestionViewModelPosition + 1

        `Then assert visitable list contains SuggestionViewModel`(expectedSuggestionViewModelPosition, visitableList)
        `Then assert visitable list contains BroadMatchViewModel`(
                expectedBroadMatchViewModelPosition, visitableList, searchProductModelPage1
        )

        `Then assert bottom separator view model is positioned after broad match list`(expectedBottomSeparatorPosition,expectedSuggestionViewModelPosition, visitableList)

    }

    private fun `When load first page and load more data`(visitableList: MutableList<Visitable<*>>) {
        val firstPageVisitableListSlot = slot<List<Visitable<*>>>()
        every {
            productListView.setProductList(capture(firstPageVisitableListSlot))
        } answers {
            visitableList.addAll(firstPageVisitableListSlot.captured)
        }

        val nextPageVisitableListSlot = slot<List<Visitable<*>>>()
        every {
            productListView.addProductList(capture(nextPageVisitableListSlot))
        } answers {
            visitableList.addAll(nextPageVisitableListSlot.captured)
        }

        productListPresenter.loadData(mapOf())
        productListPresenter.loadMoreData(mapOf())
    }

    private fun `Then assert visitable list contains SuggestionViewModel`(expectedPosition: Int, visitableList: List<Visitable<*>>) {
        val actualSuggestionViewModelIndex = visitableList.indexOfFirst { it is SuggestionDataView }

        actualSuggestionViewModelIndex.shouldBe(expectedPosition,
                "Suggestion View Model is at position $actualSuggestionViewModelIndex, should be at position $expectedPosition"
        )
    }

    private fun `Then assert top separator view model is positioned before suggestion`(expectedTopSeparatorPosition: Int, suggestionViewModelIndex: Int, visitableList: List<Visitable<*>>) {
        val actualTopSeparatorViewModelIndex = visitableList.withIndex().find {
            it.value is SeparatorDataView && it.index < suggestionViewModelIndex
        }?.index ?: -1

        actualTopSeparatorViewModelIndex.shouldBe(expectedTopSeparatorPosition,
                "Separator View Model is at position $actualTopSeparatorViewModelIndex, should be at position $expectedTopSeparatorPosition"
        )
    }

    private fun `Then assert bottom separator view model is positioned after broad match list`(expectedBottomSeparatorPosition: Int, suggestionViewModelIndex: Int, visitableList: List<Visitable<*>>) {
        val actualBottomSeparatorViewModelIndex = visitableList.withIndex().find {
            it.value is SeparatorDataView && it.index > suggestionViewModelIndex
        }?.index ?: -1

        actualBottomSeparatorViewModelIndex.shouldBe(expectedBottomSeparatorPosition,
                "Separator View Model is at position $actualBottomSeparatorViewModelIndex, should be at position $expectedBottomSeparatorPosition"
        )
    }

    @Test
    fun `Do not show broad match when load more is empty and position become invalid`() {
        val searchProductModelPage1 = broadMatchResponseCode0Page1Position12.jsonToObject<SearchProductModel>()
        val searchProductModelPage2 = broadMatchResponseCode0Page2Empty.jsonToObject<SearchProductModel>()

        `Test do not show broad match with invalid position`(searchProductModelPage1, searchProductModelPage2)
    }

    @Test
    fun `Do not show broad match when position is invalid with product card`() {
        val searchProductModelPage1 = broadMatchResponseCode0Page1Position12.jsonToObject<SearchProductModel>()
        val searchProductModelPage2 = broadMatchResponseCode0Page2With1Product.jsonToObject<SearchProductModel>()

        `Test do not show broad match with invalid position`(searchProductModelPage1, searchProductModelPage2)
    }

    private fun `Test do not show broad match with invalid position`(
            searchProductModelPage1: SearchProductModel,
            searchProductModelPage2: SearchProductModel
    ) {
        val visitableList = mutableListOf<Visitable<*>>()

        `Given Search Product API will return SearchProductModel`(searchProductModelPage1)
        `Given Search Product Load More API will return SearchProductModel`(searchProductModelPage2)

        `When load first page and load more data`(visitableList)

        visitableList.assertNotContainBroadMatch()
        `Then verify tracking event impression not hit`()
    }

    private fun `Then verify tracking event impression not hit`() {
        verify(exactly = 0) {
            productListView.trackBroadMatchImpression(any())
        }
    }
}