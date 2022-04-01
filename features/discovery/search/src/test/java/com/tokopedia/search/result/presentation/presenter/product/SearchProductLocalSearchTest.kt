package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchConstant.SearchProduct.SEARCH_PRODUCT_SKIP_GLOBAL_NAV
import com.tokopedia.discovery.common.constants.SearchConstant.SearchProduct.SEARCH_PRODUCT_SKIP_HEADLINE_ADS
import com.tokopedia.discovery.common.constants.SearchConstant.SearchProduct.SEARCH_PRODUCT_SKIP_INSPIRATION_CAROUSEL
import com.tokopedia.discovery.common.constants.SearchConstant.SearchProduct.SEARCH_PRODUCT_SKIP_INSPIRATION_WIDGET
import com.tokopedia.discovery.common.constants.SearchConstant.SearchProduct.SEARCH_PRODUCT_SKIP_PRODUCT_ADS
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.presentation.model.BroadMatchDataView
import com.tokopedia.search.result.presentation.model.CpmDataView
import com.tokopedia.search.result.presentation.model.ProductItemDataView
import com.tokopedia.search.result.presentation.model.SearchProductTitleDataView
import com.tokopedia.search.result.presentation.model.SuggestionDataView
import com.tokopedia.search.result.product.emptystate.EmptyStateDataView
import com.tokopedia.search.result.product.emptystate.EmptyStateFilterDataView
import com.tokopedia.search.result.product.emptystate.EmptyStateKeywordDataView
import com.tokopedia.search.result.product.searchintokopedia.SearchInTokopediaDataView
import com.tokopedia.search.shouldBe
import com.tokopedia.search.shouldBeInstanceOf
import com.tokopedia.usecase.RequestParams
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import io.mockk.slot
import io.mockk.verify
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.hamcrest.core.IsInstanceOf.instanceOf
import org.junit.Test
import rx.Subscriber

private const val searchProductLocalSearchFirstPageJSON = "searchproduct/localsearch/first-page.json"
private const val searchProductLocalSearchSecondPageJSON = "searchproduct/localsearch/second-page.json"
private const val searchProductLocalSearchEmptyResponseCode1JSON = "searchproduct/localsearch/empty-search-response-code-1.json"
private const val searchProductLocalSearchEmptyResponseCode11JSON = "searchproduct/localsearch/empty-search-response-code-11.json"
private const val searchProductLocalSearchFirstPageEndOfPageJSON = "searchproduct/localsearch/first-page-end-of-page.json"
private const val searchProductLocalSearchFirstPage12ProductsJSON = "searchproduct/localsearch/first-page-12-products.json"
private const val searchProductLocalSearchSecondPageEndOfPageJSON = "searchproduct/localsearch/second-page-end-of-page.json"
private const val searchProductLocalSearchSecondPageEmptyProductJSON = "searchproduct/localsearch/second-page-empty-product.json"

internal class SearchProductLocalSearchTest: ProductListPresenterTestFixtures() {

    private val requestParamsFirstPageSlot = slot<RequestParams>()
    private val requestParamsFirstPage by lazy { requestParamsFirstPageSlot.captured }
    private val requestParamsLoadMoreSlot = slot<RequestParams>()
    private val requestParamsLoadMore by lazy { requestParamsLoadMoreSlot.captured }
    private val visitableListSlot = slot<List<Visitable<*>>>()
    private val visitableList by lazy { visitableListSlot.captured }
    private val searchProductPageTitle = "Waktu Indonesia Belanja"
    private val keyword = "asus"
    private val searchParameter = mapOf(
        SearchApiConst.Q to keyword,
        SearchApiConst.NAVSOURCE to "clp",
        SearchApiConst.SRP_PAGE_TITLE to searchProductPageTitle,
        SearchApiConst.SRP_PAGE_ID to "1234"
    )
    private val expectedDimension90 =
            "${searchParameter[SearchApiConst.SRP_PAGE_TITLE]}.${searchParameter[SearchApiConst.NAVSOURCE]}." +
                    "local_search.${searchParameter[SearchApiConst.SRP_PAGE_ID]}"


    @Test
    fun `Show page title and remove top ads in page 1`() {
        val searchProductModel = searchProductLocalSearchFirstPageJSON.jsonToObject<SearchProductModel>()

        `Given Search Product API will return SearchProductModel`(searchProductModel)
        `Given visitable list will be captured`()

        `When Load Data`(searchParameter)

        `Then verify request params during local search`(requestParamsFirstPage)
        `Then verify visitable list contains title`()
        `Then verify visitable list does not contain CPM`()
        `Then verify visitable list does not contain top ads`(searchProductModel)
        `Then verify product item`(searchProductModel)
        `Then verify product has dimension90`()
    }

    private fun `Given Search Product API will return SearchProductModel`(searchProductModel: SearchProductModel) {
        every { searchProductFirstPageUseCase.execute(capture(requestParamsFirstPageSlot), any()) }.answers {
            secondArg<Subscriber<SearchProductModel>>().complete(searchProductModel)
        }
    }

    private fun `Given visitable list will be captured`() {
        every { productListView.setProductList(capture(visitableListSlot)) } just runs
    }

    private fun `When Load Data`(searchParameter: Map<String, Any>) {
        productListPresenter.loadData(searchParameter)
    }

    private fun `Then verify request params during local search`(requestParams: RequestParams) {
        requestParams.getBoolean(SEARCH_PRODUCT_SKIP_PRODUCT_ADS, false) shouldBe true
        requestParams.getBoolean(SEARCH_PRODUCT_SKIP_HEADLINE_ADS, false) shouldBe true
        requestParams.getBoolean(SEARCH_PRODUCT_SKIP_INSPIRATION_CAROUSEL, false) shouldBe true
        requestParams.getBoolean(SEARCH_PRODUCT_SKIP_INSPIRATION_WIDGET, false) shouldBe true
        requestParams.getBoolean(SEARCH_PRODUCT_SKIP_GLOBAL_NAV, false) shouldBe true
    }

    private fun `Then verify visitable list contains title`() {
        val searchProductTitle = visitableList[1]
        searchProductTitle.shouldBeInstanceOf<SearchProductTitleDataView>()

        val searchProductTitleViewModel = searchProductTitle as SearchProductTitleDataView
        searchProductTitleViewModel.title shouldBe searchProductPageTitle
        searchProductTitleViewModel.isRecommendationTitle shouldBe false
    }

    private fun `Then verify visitable list does not contain CPM`() {
        visitableList.filterIsInstance<CpmDataView>().size.shouldBe(
                0,
                "Visitable list should not contain CPM."
        )
    }

    private fun `Then verify visitable list does not contain top ads`(
            searchProductModel: SearchProductModel
    ) {
        visitableList.filterIsInstance<ProductItemDataView>().size shouldBe searchProductModel.searchProduct.data.productList.size

        visitableList.forEach {
            if (it is ProductItemDataView) {
                it.isTopAds shouldBe false
            }
        }
    }

    private fun `Then verify product item`(
        searchProductModel: SearchProductModel,
    ) {
        visitableList
            .filterIsInstance<ProductItemDataView>()
            .forEachIndexed { index, productItemDataView ->
            val productItem = searchProductModel.searchProduct.data.productList[index]

            productItemDataView.assertOrganicProduct(productItem, index + 1)
        }
    }

    private fun `Then verify product has dimension90`() {
        val visitableList = visitableListSlot.captured
        val index = visitableList.indexOfFirst { it is ProductItemDataView }

        val visitable = visitableList[index]
        visitable.shouldBeInstanceOf<ProductItemDataView>()

        val productItemDataView = visitable as ProductItemDataView
        productItemDataView.dimension90 shouldBe expectedDimension90
    }

    @Test
    fun `Remove top ads on page 2`() {
        val searchProductModel = searchProductLocalSearchFirstPageJSON.jsonToObject<SearchProductModel>()
        val searchProductModelPage2 = searchProductLocalSearchSecondPageJSON.jsonToObject<SearchProductModel>()

        `Given Search Product API will return SearchProductModel`(searchProductModel)
        `Given Search Product Load More API will return Search Product Model`(searchProductModelPage2)
        `Given view already load data`(searchParameter)
        `Given visitable list page 2 will be captured`()

        `When load more data`(searchParameter)

        `Then verify request params during local search`(requestParamsLoadMore)
        `Then verify visitable list does not contain top ads`(searchProductModelPage2)
        `Then verify product item`(searchProductModelPage2)
        `Then verify product has dimension90`()
    }

    private fun `Given Search Product Load More API will return Search Product Model`(searchProductModelPage2: SearchProductModel) {
        every { searchProductLoadMoreUseCase.execute(capture(requestParamsLoadMoreSlot), any()) } answers {
            secondArg<Subscriber<SearchProductModel>>().complete(searchProductModelPage2)
        }
    }

    private fun `Given view already load data`(searchParameter: Map<String, Any>) {
        productListPresenter.loadData(searchParameter)
    }

    private fun `Given visitable list page 2 will be captured`() {
        every { productListView.addProductList(capture(visitableListSlot)) } just runs
    }

    private fun `When load more data`(searchParameter: Map<String, Any>) {
        productListPresenter.loadMoreData(searchParameter)
    }

    @Test
    fun `Empty search by keyword with response code 11 during local search`() {
        val searchProductModel = searchProductLocalSearchEmptyResponseCode11JSON.jsonToObject<SearchProductModel>()

        `Given Search Product API will return SearchProductModel`(searchProductModel)
        `Given getQueryKey will return keyword`()

        `When Load Data`(searchParameter)

        `Then verify recommendation use case not called`()
        `Then verify empty search view model during local search`()
        `Then verify view added broad match`(searchProductModel)
        `Then verify local search recommendation use case is called`()
    }

    private fun `Given getQueryKey will return keyword`() {
        every { productListView.queryKey } returns (searchParameter[SearchApiConst.Q] ?: "")
    }

    private fun `Then verify recommendation use case not called`() {
        verify(exactly = 0) {
            recommendationUseCase.execute(any(), any())
        }
    }

    private fun `Then verify empty search view model during local search`() {
        verify {
            productListView.setProductList(capture(visitableListSlot))
        }

        val emptyStateDataView = visitableList.filterIsInstance<EmptyStateKeywordDataView>().first()

        emptyStateDataView.isLocalSearch shouldBe true
        emptyStateDataView.globalSearchApplink shouldBe "${ApplinkConstInternalDiscovery.SEARCH_RESULT}?q=asus"
        emptyStateDataView.keyword shouldBe keyword
        emptyStateDataView.pageTitle shouldBe searchProductPageTitle
    }

    private fun `Then verify view added broad match`(searchProductModel: SearchProductModel) {
        verify {
            productListView.addProductList(capture(visitableListSlot))
        }

        val visitableList = visitableListSlot.captured
        visitableList[0].shouldBeInstanceOf<SuggestionDataView>()

        val otherRelated = searchProductModel.searchProduct.data.related.otherRelatedList
        visitableList.filterIsInstance<BroadMatchDataView>().size shouldBe otherRelated.size

        var index = visitableList.indexOfFirst { it is BroadMatchDataView }
        index shouldBe 1

        repeat(otherRelated.size) {
            val visitable = visitableList[index]
            visitable.shouldBeInstanceOf<BroadMatchDataView>()

            val broadMatchViewModel = visitable as BroadMatchDataView
            broadMatchViewModel.isAppendTitleInTokopedia shouldBe true
            broadMatchViewModel.dimension90 shouldBe expectedDimension90

            index++
        }
    }

    private fun `Then verify local search recommendation use case is called`() {
        verify {
            getLocalSearchRecommendationUseCase.execute(any(), any())
        }
    }

    @Test
    fun `Empty search by filter with response code 11 during local search`() {
        val searchProductModel = searchProductLocalSearchEmptyResponseCode11JSON.jsonToObject<SearchProductModel>()

        `Given Search Product API will return SearchProductModel`(searchProductModel)
        `Given getQueryKey will return keyword`()
        `Given view has active filter`()

        `When Load Data`(searchParameter)

        `Then verify recommendation use case not called`()
        `Then verify empty search view model by filter`()
        `Then verify local search recommendation use case is called`()
    }

    private fun `Given view has active filter`() {
        every { productListView.isAnyFilterActive } returns true
    }

    private fun `Then verify empty search view model by filter`() {
        verify {
            productListView.setProductList(capture(visitableListSlot))
        }

        assertThat(visitableList.first(), instanceOf(EmptyStateFilterDataView::class.java))
    }

    @Test
    fun `Empty search with response code 1 during local search`() {
        val searchProductModel = searchProductLocalSearchEmptyResponseCode1JSON.jsonToObject<SearchProductModel>()

        `Given Search Product API will return SearchProductModel`(searchProductModel)
        `Given getQueryKey will return keyword`()

        `When Load Data`(searchParameter)

        `Then verify recommendation use case is called`()
        `Then verify empty search view model for non local search`()
        `Then verify get local search recommendation is not called`()
    }

    private fun `Then verify recommendation use case is called`() {
        verify {
            recommendationUseCase.execute(any(), any())
        }
    }

    private fun `Then verify empty search view model for non local search`() {
        verify {
            productListView.setProductList(capture(visitableListSlot))
        }

        val emptyStateDataView = visitableList.filterIsInstance<EmptyStateKeywordDataView>().first()

        emptyStateDataView.isLocalSearch shouldBe false
        emptyStateDataView.globalSearchApplink shouldBe ""
        emptyStateDataView.keyword shouldBe keyword
        emptyStateDataView.pageTitle shouldBe ""
    }

    private fun `Then verify get local search recommendation is not called`() {
        verify(exactly = 0) {
            getLocalSearchRecommendationUseCase.execute(any(), any())
        }
    }

    @Test
    fun `Show search in tokopedia button at bottom of last page - page 1`() {
        val searchProductModel = searchProductLocalSearchFirstPageEndOfPageJSON.jsonToObject<SearchProductModel>()

        `Given Search Product API will return SearchProductModel`(searchProductModel)
        `Given visitable list will be captured`()

        `When Load Data`(searchParameter)

        `Then verify visitable list have search in tokopedia at bottom`()
    }

    private fun `Then verify visitable list have search in tokopedia at bottom`() {
        visitableList.last().shouldBeInstanceOf<SearchInTokopediaDataView>()
        (visitableList.last() as SearchInTokopediaDataView).applink shouldBe "${ApplinkConstInternalDiscovery.SEARCH_RESULT}?q=asus"
    }

    @Test
    fun `Show search in tokopedia button at bottom of last page - page 2`() {
        val searchProductModel = searchProductLocalSearchFirstPage12ProductsJSON.jsonToObject<SearchProductModel>()
        val searchProductModelPage2 = searchProductLocalSearchSecondPageEndOfPageJSON.jsonToObject<SearchProductModel>()

        `Given Search Product API will return SearchProductModel`(searchProductModel)
        `Given Search Product Load More API will return Search Product Model`(searchProductModelPage2)
        `Given view already load data`(searchParameter)
        `Given visitable list page 2 will be captured`()

        `When load more data`(searchParameter)

        `Then verify visitable list have search in tokopedia at bottom`()
    }

    @Test
    fun `Show search in tokopedia button at bottom of last page - empty page 2`() {
        val searchProductModel = searchProductLocalSearchFirstPage12ProductsJSON.jsonToObject<SearchProductModel>()
        val searchProductModelPage2 = searchProductLocalSearchSecondPageEmptyProductJSON.jsonToObject<SearchProductModel>()

        `Given Search Product API will return SearchProductModel`(searchProductModel)
        `Given Search Product Load More API will return Search Product Model`(searchProductModelPage2)
        `Given view already load data`(searchParameter)
        `Given visitable list page 2 will be captured`()

        `When load more data`(searchParameter)

        `Then verify visitable list have search in tokopedia at bottom`()
    }
}