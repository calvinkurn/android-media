package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.presentation.model.*
import com.tokopedia.search.shouldBe
import com.tokopedia.search.shouldBeInstanceOf
import io.mockk.*
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

    private val visitableListSlot = slot<List<Visitable<*>>>()
    private val visitableList by lazy { visitableListSlot.captured }
    private val searchProductPageTitle = "Waktu Indonesia Belanja"
    private val keyword = "asus"
    private val searchParameter = mapOf(
        SearchApiConst.Q to keyword,
        SearchApiConst.NAVSOURCE to "campaign",
        SearchApiConst.SRP_PAGE_TITLE to searchProductPageTitle,
        SearchApiConst.SRP_PAGE_ID to "1234"
    )

    @Test
    fun `Show page title and remove top ads in page 1`() {
        val searchProductModel = searchProductLocalSearchFirstPageJSON.jsonToObject<SearchProductModel>()

        `Given Search Product API will return SearchProductModel`(searchProductModel)
        `Given visitable list will be captured`()

        `When Load Data`(searchParameter)

        `Then verify visitable list contains title`()
        `Then verify visitable list does not contain CPM`()
        `Then verify visitable list does not contain top ads`(searchProductModel)
        `Then verify product item contains page title`()
    }

    private fun `Given Search Product API will return SearchProductModel`(searchProductModel: SearchProductModel) {
        every { searchProductFirstPageUseCase.execute(any(), any()) }.answers {
            secondArg<Subscriber<SearchProductModel>>().complete(searchProductModel)
        }
    }

    private fun `Given visitable list will be captured`() {
        every { productListView.setProductList(capture(visitableListSlot)) } just runs
    }

    private fun `When Load Data`(searchParameter: Map<String, Any>) {
        productListPresenter.loadData(searchParameter)
    }

    private fun `Then verify visitable list contains title`() {
        val searchProductTitle = visitableList[1]
        searchProductTitle.shouldBeInstanceOf<SearchProductTitleViewModel>()

        (searchProductTitle as SearchProductTitleViewModel).title shouldBe searchProductPageTitle
    }

    private fun `Then verify visitable list does not contain CPM`() {
        visitableList.filterIsInstance<CpmViewModel>().size.shouldBe(
                0,
                "Visitable list should not contain CPM."
        )
    }

    private fun `Then verify visitable list does not contain top ads`(
            searchProductModel: SearchProductModel
    ) {
        visitableList.filterIsInstance<ProductItemViewModel>().size shouldBe searchProductModel.searchProduct.data.productList.size

        visitableList.forEach {
            if (it is ProductItemViewModel) {
                it.isTopAds shouldBe false
            }
        }
    }

    private fun `Then verify product item contains page title`() {
        visitableList.filterIsInstance<ProductItemViewModel>().all {
            it.pageTitle == searchProductPageTitle
        } shouldBe true
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

        `Then verify visitable list does not contain top ads`(searchProductModelPage2)
        `Then verify product item contains page title`()
    }

    private fun `Given Search Product Load More API will return Search Product Model`(searchProductModelPage2: SearchProductModel) {
        every { searchProductLoadMoreUseCase.execute(any(), any()) } answers {
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
        `Then verify local search recommendation use case is called`()
    }

    private fun `Given getQueryKey will return keyword`() {
        every { productListView.queryKey } returns searchParameter[SearchApiConst.Q]
    }

    private fun `Then verify recommendation use case not called`() {
        verify(exactly = 0) {
            recommendationUseCase.execute(any(), any())
        }
    }

    private fun `Then verify empty search view model during local search`() {
        val emptySearchViewModelSlot = slot<EmptySearchProductViewModel>()
        verify {
            productListView.setEmptyProduct(null, capture(emptySearchViewModelSlot))
        }

        val emptySearchViewModel = emptySearchViewModelSlot.captured
        emptySearchViewModel.isFilterActive shouldBe false
        emptySearchViewModel.isLocalSearch shouldBe true
        emptySearchViewModel.globalSearchApplink shouldBe "${ApplinkConstInternalDiscovery.SEARCH_RESULT}?q=asus"
        emptySearchViewModel.keyword shouldBe keyword
        emptySearchViewModel.pageTitle shouldBe searchProductPageTitle
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
        every { productListView.isAnyFilterActive } returns true

        `When Load Data`(searchParameter)

        `Then verify recommendation use case not called`()
        `Then verify empty search view model by filter`()
        `Then verify local search recommendation use case is called`()
    }

    private fun `Then verify empty search view model by filter`() {
        val emptySearchViewModelSlot = slot<EmptySearchProductViewModel>()
        verify {
            productListView.setEmptyProduct(null, capture(emptySearchViewModelSlot))
        }

        val emptySearchViewModel = emptySearchViewModelSlot.captured
        emptySearchViewModel.isFilterActive shouldBe true
        emptySearchViewModel.isLocalSearch shouldBe false
        emptySearchViewModel.globalSearchApplink shouldBe ""
        emptySearchViewModel.keyword shouldBe ""
        emptySearchViewModel.pageTitle shouldBe ""
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
        val emptySearchViewModelSlot = slot<EmptySearchProductViewModel>()

        verify {
            productListView.setEmptyProduct(null, capture(emptySearchViewModelSlot))
        }

        val emptySearchViewModel = emptySearchViewModelSlot.captured
        emptySearchViewModel.isFilterActive shouldBe false
        emptySearchViewModel.isLocalSearch shouldBe false
        emptySearchViewModel.globalSearchApplink shouldBe ""
        emptySearchViewModel.keyword shouldBe ""
        emptySearchViewModel.pageTitle shouldBe ""
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
        visitableList.last().shouldBeInstanceOf<SearchInTokopediaViewModel>()
        (visitableList.last() as SearchInTokopediaViewModel).applink shouldBe "${ApplinkConstInternalDiscovery.SEARCH_RESULT}?q=asus"
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