package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.presentation.model.CpmDataView
import com.tokopedia.search.result.presentation.model.ProductItemDataView
import com.tokopedia.search.result.presentation.model.SearchProductTopAdsImageDataView
import com.tokopedia.search.result.presentation.model.SeparatorDataView
import com.tokopedia.search.shouldBe
import com.tokopedia.search.shouldBeInstanceOf
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import io.mockk.every
import io.mockk.slot
import org.junit.Test
import rx.Subscriber

private const val tdnWithHeadlineAdsPosition12JSON = "searchproduct/tdn/tdn-with-headline-ads-position-12.json"
private const val tdnWithHeadlineAdsPosition24JSON = "searchproduct/tdn/tdn-with-headline-ads-position-24.json"

internal class SearchProductTDNTest: ProductListPresenterTestFixtures() {

    private val searchProductModelFirstPage = searchProductCommonResponseJSON.jsonToObject<SearchProductModel>()
    private val searchProductModelLoadMore = searchProductCommonResponseJSON.jsonToObject<SearchProductModel>()
    private val tdn0 = TopAdsImageViewModel(position = 0, bannerName = "Position 0")
    private val tdn1 = TopAdsImageViewModel(position = 1, bannerName = "Position 1")
    private val tdn4 = TopAdsImageViewModel(position = 4, bannerName = "Position 4")
    private val tdn8 = TopAdsImageViewModel(position = 8, bannerName = "Position 8")
    private val tdn12 = TopAdsImageViewModel(position = 12, bannerName = "Position 12")
    private val tdn16 = TopAdsImageViewModel(position = 16, bannerName = "Position 16")
    private val tdn24 = TopAdsImageViewModel(position = 24, bannerName = "Position 24")

    private val visitableList = mutableListOf<Visitable<*>>()

    @Test
    fun `Test TDN on first page`() {
        val tdnList = listOf(tdn1, tdn4, tdn8)
        searchProductModelFirstPage.setTopAdsImageViewModelList(tdnList)

        `Given Search Product API will return SearchProductModel`(searchProductModelFirstPage)
        `Given product list will be captured`()

        `When load data`()

        `Then verify TDN on first page`()
    }

    private fun `Given Search Product API will return SearchProductModel`(searchProductModel: SearchProductModel) {
        every { searchProductFirstPageUseCase.execute(any(), any()) }.answers {
            secondArg<Subscriber<SearchProductModel>>().complete(searchProductModel)
        }
    }

    private fun `Given product list will be captured`() {
        val firstPageVisitableListSlot = slot<List<Visitable<*>>>()
        every { productListView.setProductList(capture(firstPageVisitableListSlot)) } answers {
            visitableList.addAll(firstPageVisitableListSlot.captured)
        }

        val secondPageVisitableListSlot = slot<List<Visitable<*>>>()
        every { productListView.addProductList(capture(secondPageVisitableListSlot)) } answers {
            visitableList.addAll(secondPageVisitableListSlot.captured)
        }
    }

    private fun `When load data`() {
        productListPresenter.loadData(mapOf())
    }

    private fun `Then verify TDN on first page`() {
        visitableList.size shouldBe 11

        visitableList[0].shouldBeInstanceOf<SearchProductTopAdsImageDataView>()
        (visitableList[0] as SearchProductTopAdsImageDataView).topAdsImageViewModel shouldBe tdn1
        visitableList[5].shouldBeInstanceOf<SearchProductTopAdsImageDataView>()
        (visitableList[5] as SearchProductTopAdsImageDataView).topAdsImageViewModel shouldBe tdn4
        visitableList[10].shouldBeInstanceOf<SearchProductTopAdsImageDataView>()
        (visitableList[10] as SearchProductTopAdsImageDataView).topAdsImageViewModel shouldBe tdn8
    }

    @Test
    fun `Test TDN on load more`() {
        val tdnList = listOf(tdn1, tdn4, tdn8, tdn12, tdn16)
        searchProductModelFirstPage.setTopAdsImageViewModelList(tdnList)

        `Given Search Product API will return SearchProductModel`(searchProductModelFirstPage)
        `Given Search Product Load More API will return SearchProductModel`(searchProductModelLoadMore)
        `Given product list will be captured`()
        `Given view already load first page`()

        `When load more data`()

        `Then verify TDN on second page`()
    }

    private fun `Given Search Product Load More API will return SearchProductModel`(searchProductModel: SearchProductModel) {
        every { searchProductLoadMoreUseCase.execute(any(), any()) }.answers {
            secondArg<Subscriber<SearchProductModel>>().complete(searchProductModel)
        }
    }

    private fun `Given view already load first page`() {
        productListPresenter.loadData(mapOf())
    }

    private fun `When load more data`() {
        productListPresenter.loadMoreData(mapOf())
    }

    private fun `Then verify TDN on second page`() {
        visitableList.size shouldBe 21

        visitableList[15].shouldBeInstanceOf<SearchProductTopAdsImageDataView>()
        (visitableList[15] as SearchProductTopAdsImageDataView).topAdsImageViewModel shouldBe tdn12
        visitableList[20].shouldBeInstanceOf<SearchProductTopAdsImageDataView>()
        (visitableList[20] as SearchProductTopAdsImageDataView).topAdsImageViewModel shouldBe tdn16
    }

    @Test
    fun `Test TDN position 0`() {
        val tdnList = listOf(tdn0)
        searchProductModelFirstPage.setTopAdsImageViewModelList(tdnList)

        `Given Search Product API will return SearchProductModel`(searchProductModelFirstPage)
        `Given product list will be captured`()

        `When load data`()

        `Then verify TDN is not shown`()
    }

    private fun `Then verify TDN is not shown`() {
        visitableList.any { it is SearchProductTopAdsImageDataView } shouldBe false
    }

    @Test
    fun `TDN should be above headline ads in the same position in first page`() {
        val searchProductModelWithCPM = tdnWithHeadlineAdsPosition12JSON.jsonToObject<SearchProductModel>()
        searchProductModelWithCPM.setTopAdsImageViewModelList(listOf(tdn1, tdn12))

        `Given Search Product API will return SearchProductModel`(searchProductModelWithCPM)
        `Given product list will be captured`()

        `When load data`()

        `Then verify visitable list with TDN above headline ads in first page`()
    }

    private fun `Then verify visitable list with TDN above headline ads in first page`() {
        visitableList.size shouldBe 20

        visitableList.forEachIndexed { index, visitable ->
            if (index == 0 || index == 14) {
                visitable.shouldBeInstanceOf<SearchProductTopAdsImageDataView>()
            }
            else if (index == 15 || index == 17) {
                visitable.shouldBeInstanceOf<SeparatorDataView>()
            }
            else if (index == 1 || index == 16) {
                visitable.shouldBeInstanceOf<CpmDataView>()
            }
            else {
                visitable.shouldBeInstanceOf<ProductItemDataView>()
            }
        }
    }

    @Test
    fun `TDN should be above headline ads in the same position after load more`() {
        val searchProductModelWithCPM = tdnWithHeadlineAdsPosition24JSON.jsonToObject<SearchProductModel>()
        searchProductModelWithCPM.setTopAdsImageViewModelList(listOf(tdn1, tdn12, tdn24))

        val searchProductWithTopAds = "searchproduct/with-topads.json".jsonToObject<SearchProductModel>()

        `Given Search Product API will return SearchProductModel`(searchProductModelWithCPM)
        `Given Search Product Load More API will return SearchProductModel`(searchProductWithTopAds)
        `Given product list will be captured`()
        `Given view already load first page`()

        `When load more data`()

        `Then verify visitable list with TDN above headline ads after load more`()
    }

    private fun `Then verify visitable list with TDN above headline ads after load more`() {
        visitableList.size shouldBe 36

        visitableList.forEachIndexed { index, visitable ->
            if (index == 0 || index == 14 || index == 27) {
                visitable.shouldBeInstanceOf<SearchProductTopAdsImageDataView>()
            }
            else if (index == 28 || index == 30) {
                visitable.shouldBeInstanceOf<SeparatorDataView>()
            }
            else if (index == 1 || index == 29) {
                visitable.shouldBeInstanceOf<CpmDataView>()
            }
            else {
                visitable.shouldBeInstanceOf<ProductItemDataView>()
            }
        }
    }
}