package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.presentation.model.ChooseAddressDataView
import com.tokopedia.search.result.presentation.model.ProductItemDataView
import com.tokopedia.search.result.presentation.model.SearchProductTopAdsImageDataView
import com.tokopedia.search.result.product.cpm.CpmDataView
import com.tokopedia.search.shouldBe
import com.tokopedia.search.shouldBeInstanceOf
import com.tokopedia.topads.sdk.domain.model.TopAdsImageUiModel
import io.mockk.every
import io.mockk.slot
import org.junit.Test
import rx.Subscriber

private const val tdnWithHeadlineAdsPosition14JSON = "searchproduct/tdn/tdn-with-headline-ads-position-14.json"
private const val tdnWithHeadlineAdsPosition28JSON = "searchproduct/tdn/tdn-with-headline-ads-position-28.json"

internal class SearchProductTDNTest: ProductListPresenterTestFixtures() {

    private val searchProductModelFirstPage = searchProductCommonResponseJSON.jsonToObject<SearchProductModel>()
    private val searchProductModelLoadMore = searchProductCommonResponseJSON.jsonToObject<SearchProductModel>()
    private val tdn0 = TopAdsImageUiModel(position = 0, bannerName = "Position 0")
    private val tdn1 = TopAdsImageUiModel(position = 1, bannerName = "Position 1")
    private val tdn4 = TopAdsImageUiModel(position = 4, bannerName = "Position 4")
    private val tdn8 = TopAdsImageUiModel(position = 8, bannerName = "Position 8")
    private val tdn12 = TopAdsImageUiModel(position = 12, bannerName = "Position 12")
    private val tdn14 = TopAdsImageUiModel(position = 14, bannerName = "Position 14")
    private val tdn16 = TopAdsImageUiModel(position = 16, bannerName = "Position 16")
    private val tdn28 = TopAdsImageUiModel(position = 28, bannerName = "Position 28")

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
        visitableList.size shouldBe 12

        visitableList[1].shouldBeInstanceOf<SearchProductTopAdsImageDataView>()
        (visitableList[1] as SearchProductTopAdsImageDataView).topAdsImageUiModel shouldBe tdn1
        visitableList[6].shouldBeInstanceOf<SearchProductTopAdsImageDataView>()
        (visitableList[6] as SearchProductTopAdsImageDataView).topAdsImageUiModel shouldBe tdn4
        visitableList[11].shouldBeInstanceOf<SearchProductTopAdsImageDataView>()
        (visitableList[11] as SearchProductTopAdsImageDataView).topAdsImageUiModel shouldBe tdn8
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

    private fun `Then verify TDN on second page`() { visitableList.size shouldBe 22

        visitableList[16].shouldBeInstanceOf<SearchProductTopAdsImageDataView>()
        (visitableList[16] as SearchProductTopAdsImageDataView).topAdsImageUiModel shouldBe tdn12
        visitableList[21].shouldBeInstanceOf<SearchProductTopAdsImageDataView>()
        (visitableList[21] as SearchProductTopAdsImageDataView).topAdsImageUiModel shouldBe tdn16
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
        val searchProductModelWithCPM = tdnWithHeadlineAdsPosition14JSON.jsonToObject<SearchProductModel>()
        searchProductModelWithCPM.setTopAdsImageViewModelList(listOf(tdn1, tdn14))

        `Given top ads headline helper will process headline ads`(searchProductModelWithCPM)
        `Given Search Product API will return SearchProductModel`(searchProductModelWithCPM)
        `Given product list will be captured`()

        `When load data`()

        `Then verify visitable list with TDN above headline ads in first page`()
    }

    private fun `Then verify visitable list with TDN above headline ads in first page`() {
        visitableList.size shouldBe 19

        visitableList.forEachIndexed { index, visitable ->
            when (index) {
                0 -> visitable.shouldBeInstanceOf<ChooseAddressDataView>()
                1, 17 -> visitable.shouldBeInstanceOf<SearchProductTopAdsImageDataView>()
                2, 18 -> visitable.shouldBeInstanceOf<CpmDataView>()
                else -> visitable.shouldBeInstanceOf<ProductItemDataView>()
            }
        }
    }

    @Test
    fun `TDN should be above headline ads in the same position after load more`() {
        val searchProductModelWithCPM = tdnWithHeadlineAdsPosition28JSON.jsonToObject<SearchProductModel>()
        searchProductModelWithCPM.setTopAdsImageViewModelList(listOf(tdn1, tdn14, tdn28))

        val searchProductWithTopAds = "searchproduct/with-topads.json".jsonToObject<SearchProductModel>()

        `Given top ads headline helper will process headline ads`(searchProductModelWithCPM)
        `Given Search Product API will return SearchProductModel`(searchProductModelWithCPM)
        `Given Search Product Load More API will return SearchProductModel`(searchProductWithTopAds)
        `Given product list will be captured`()
        `Given view already load first page`()

        `When load more data`()

        `Then verify visitable list with TDN above headline ads after load more`()
    }

    private fun `Then verify visitable list with TDN above headline ads after load more`() {
        visitableList.size shouldBe 37

        visitableList.forEachIndexed { index, visitable ->
            when (index) {
                0 -> visitable.shouldBeInstanceOf<ChooseAddressDataView>()
                1, 17, 33 -> visitable.shouldBeInstanceOf<SearchProductTopAdsImageDataView>()
                2, 18, 35, 36 -> visitable.shouldBeInstanceOf<CpmDataView>()
                else -> visitable.shouldBeInstanceOf<ProductItemDataView>()
            }
        }
    }
}
