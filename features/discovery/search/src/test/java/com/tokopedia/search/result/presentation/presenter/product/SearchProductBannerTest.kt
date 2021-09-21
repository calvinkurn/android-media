package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.presentation.model.*
import com.tokopedia.search.shouldBe
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import io.mockk.every
import io.mockk.slot
import org.junit.Test
import rx.Subscriber

private const val bottomBannerResponse = "searchproduct/banner/banner-bottom.json"
private const val bottomBannerWithBroadMatchResponse = "searchproduct/banner/banner-bottom-with-broadmatch.json"
private const val topBannerResponse = "searchproduct/banner/banner-top.json"
private const val topBannerWithBroadMatchResponse = "searchproduct/banner/banner-top-with-broadmatch.json"
private const val bannerPosition8Response = "searchproduct/banner/banner-position-8.json"
private const val bannerPosition8WithBroadMatchResponse = "searchproduct/banner/banner-position-8.json"
private const val bannerPosition12Page1Response = "searchproduct/banner/banner-position-12-page-1.json"
private const val bannerPosition12WithBroadMatchPage1Response = "searchproduct/banner/banner-position-12-page-1-with-broadmatch.json"
private const val bottomBannerWithBroadMatchPage1Response = "searchproduct/banner/banner-bottom-page-1-with-broadmatch.json"
private const val bottomBannerPage1Response = "searchproduct/banner/banner-bottom-page-1.json"
private const val bannerPage2Response = "searchproduct/banner/banner-page-2.json"

internal class SearchProductBannerTest: ProductListPresenterTestFixtures() {

    private val visitableListSlot = slot<List<Visitable<*>>>()
    private val visitableList = mutableListOf<Visitable<*>>()

    @Test
    fun `Test show banner at bottom of page 1`() {
        val searchProductModel = bottomBannerResponse.jsonToObject<SearchProductModel>()
        val searchParameter = mapOf(
                SearchApiConst.Q to "hampers",
                SearchApiConst.NAVSOURCE to "thematic",
                SearchApiConst.SRP_PAGE_ID to "332"
        )

        `Given search product API will success`(searchProductModel)
        `Given view will set and add product list`()

        `When load data`(searchParameter)

        `Then assert banner at bottom of page`(searchProductModel)
    }

    private fun `Given search product API will success`(searchProductModel: SearchProductModel) {
        every {
            searchProductFirstPageUseCase.execute(any(), any())
        } answers {
            secondArg<Subscriber<SearchProductModel>>().complete(searchProductModel)
        }
    }

    private fun `Given view will set and add product list`() {
        every {
            productListView.setProductList(capture(visitableListSlot))
        } answers {
            visitableList.clear()
            visitableList.addAll(visitableListSlot.captured)
        }

        every {
            productListView.addProductList(capture(visitableListSlot))
        } answers {
            visitableList.addAll(visitableListSlot.captured)
        }
    }

    private fun `When load data`(searchParameter: Map<String, String>) {
        productListPresenter.loadData(searchParameter)
    }

    private fun `Then assert banner at bottom of page`(searchProductModel: SearchProductModel) {
        val indexedVisitableList = visitableList.withIndex().toList()
        val (bannerDataViewIndex, bannerDataView) = indexedVisitableList.getIndexedBannerDataView()

        (bannerDataView as BannerDataView).assert(searchProductModel.searchProduct.data.banner)

        val visitableListBelowBanner = indexedVisitableList.filter { it.index > bannerDataViewIndex }
        `Then assert banner is below all product items`(visitableListBelowBanner)
        `Then assert banner is above SearchInTokopedia`(visitableListBelowBanner)
    }

    private fun List<IndexedValue<Visitable<*>>>.getIndexedBannerDataView(): IndexedValue<Visitable<*>> {
        return find { it.value is BannerDataView }
                ?: throw AssertionError("Banner Data View not found")
    }

    private fun `Then assert banner is below all product items`(visitableListBelowBanner: List<IndexedValue<Visitable<*>>>) {
        val hasProductItemBelowBanner = visitableListBelowBanner.any { it.value is ProductItemDataView }
        hasProductItemBelowBanner.shouldBe(
                false,
                "Banner should be below all product items"
        )
    }

    private fun `Then assert banner is above SearchInTokopedia`(visitableListBelowBanner: List<IndexedValue<Visitable<*>>>) {
        val hasSearchInTokopediaDataView = visitableListBelowBanner.any { it.value is SearchInTokopediaDataView }
        hasSearchInTokopediaDataView.shouldBe(
                true,
                "Banner should be above Search in Tokopedia"
        )
    }

    private fun BannerDataView.assert(bannerModel: SearchProductModel.Banner) {
        this.position shouldBe bannerModel.position
        this.text shouldBe bannerModel.text
        this.applink shouldBe bannerModel.applink
        this.imageUrl shouldBe bannerModel.imageUrl
    }

    @Test
    fun `Test show banner at bottom of page 1 with broad match`() {
        val searchProductModel = bottomBannerWithBroadMatchResponse.jsonToObject<SearchProductModel>()

        `Given search product API will success`(searchProductModel)
        `Given view will set and add product list`()

        `When load data`(mapOf(SearchApiConst.Q to "hampers"))

        `Then assert banner at bottom of page with broad match`(searchProductModel)
    }

    private fun `Then assert banner at bottom of page with broad match`(searchProductModel: SearchProductModel) {
        val indexedVisitableList = visitableList.withIndex().toList()
        val (bannerDataViewIndex, bannerDataView) = indexedVisitableList.getIndexedBannerDataView()

        (bannerDataView as BannerDataView).assert(searchProductModel.searchProduct.data.banner)

        val visitableListBelowBanner = indexedVisitableList.filter { it.index > bannerDataViewIndex }
        `Then assert banner is below all product items`(visitableListBelowBanner)
        `Then assert banner is below broad match`(visitableListBelowBanner)
    }

    private fun `Then assert banner is below broad match`(visitableListBelowBanner: List<IndexedValue<Visitable<*>>>) {
        val hasBroadMatchTitleDataView = visitableListBelowBanner.any { it.value is SuggestionDataView }
        hasBroadMatchTitleDataView.shouldBe(
                false,
                "Banner should be below Broad Match"
        )

        val hasBroadMatchBelowBanner = visitableListBelowBanner.any { it.value is BroadMatchDataView }
        hasBroadMatchBelowBanner.shouldBe(
                false,
                "Banner should be below Broad Match"
        )
    }

    @Test
    fun `Test show banner at top of page`() {
        val searchProductModel = topBannerResponse.jsonToObject<SearchProductModel>()
        val dummyTDN = TopAdsImageViewModel(position = 1, bannerName = "Position 1")
        searchProductModel.setTopAdsImageViewModelList(listOf(dummyTDN))

        `Given search product API will success`(searchProductModel)
        `Given view will set and add product list`()

        `When load data`(mapOf(SearchApiConst.Q to "samsung"))

        `Then assert banner at top of page`(searchProductModel)
    }

    private fun `Then assert banner at top of page`(searchProductModel: SearchProductModel) {
        val indexedVisitableList = visitableList.withIndex().toList()
        val (bannerDataViewIndex, bannerDataView) = indexedVisitableList.getIndexedBannerDataView()

        (bannerDataView as BannerDataView).assert(searchProductModel.searchProduct.data.banner)

        val visitableListAboveBanner = indexedVisitableList.filter { it.index < bannerDataViewIndex }
        `Then assert banner is above all product items`(visitableListAboveBanner)
        `Then assert banner is below all top ads widget`(visitableListAboveBanner)
    }

    private fun `Then assert banner is above all product items`(visitableListAboveBanner: List<IndexedValue<Visitable<*>>>) {
        val hasProductItemAboveBanner = visitableListAboveBanner.any { it.value is ProductItemDataView }
        hasProductItemAboveBanner.shouldBe(
                false,
                "Banner should be above all product items"
        )
    }

    private fun `Then assert banner is below all top ads widget`(visitableListAboveBanner: List<IndexedValue<Visitable<*>>>) {
        val hasHeadlineAdsAboveBanner = visitableListAboveBanner.any { it.value is CpmDataView }
        hasHeadlineAdsAboveBanner.shouldBe(
                true,
                "Banner should be below headline ads"
        )

        val hasTDNAboveBanner = visitableListAboveBanner.any { it.value is SearchProductTopAdsImageDataView }
        hasTDNAboveBanner.shouldBe(
                true,
                "Banner should be below TDN"
        )
    }

    @Test
    fun `Test show banner at top of page with broad match`() {
        val searchProductModel = topBannerWithBroadMatchResponse.jsonToObject<SearchProductModel>()
        val dummyTDN = TopAdsImageViewModel(position = 1, bannerName = "Position 1")
        searchProductModel.setTopAdsImageViewModelList(listOf(dummyTDN))

        `Given search product API will success`(searchProductModel)
        `Given view will set and add product list`()

        `When load data`(mapOf(SearchApiConst.Q to "samsung"))

        `Then assert banner at top of page with broad match`(searchProductModel)
    }

    private fun `Then assert banner at top of page with broad match`(searchProductModel: SearchProductModel) {
        val indexedVisitableList = visitableList.withIndex().toList()
        val (bannerDataViewIndex, bannerDataView) = indexedVisitableList.getIndexedBannerDataView()

        (bannerDataView as BannerDataView).assert(searchProductModel.searchProduct.data.banner)

        val visitableListAboveBanner = indexedVisitableList.filter { it.index < bannerDataViewIndex }
        val visitableListBelowBanner = indexedVisitableList.filter { it.index > bannerDataViewIndex }
        `Then assert banner is above all product items`(visitableListAboveBanner)
        `Then assert banner is below all top ads widget`(visitableListAboveBanner)
        `Then assert banner is below broad match`(visitableListBelowBanner)
    }

    @Test
    fun `Test show banner in middle of page`() {
        val searchProductModel = bannerPosition8Response.jsonToObject<SearchProductModel>()

        `Given search product API will success`(searchProductModel)
        `Given view will set and add product list`()

        `When load data`(mapOf(SearchApiConst.Q to "samsung"))

        `Then assert banner in middle of page`(searchProductModel)
    }

    private fun `Then assert banner in middle of page`(searchProductModel: SearchProductModel) {
        val indexedVisitableList = visitableList.withIndex().toList()
        val (bannerDataViewIndex, bannerDataView) = indexedVisitableList.getIndexedBannerDataView()

        val bannerModel = searchProductModel.searchProduct.data.banner
        (bannerDataView as BannerDataView).assert(bannerModel)

        val visitableListAboveBanner = indexedVisitableList.filter { it.index < bannerDataViewIndex }

        val productItemCount = bannerModel.position
        `Then assert some product items above banner`(visitableListAboveBanner, productItemCount)
    }

    private fun `Then assert some product items above banner`(
            visitableListAboveBanner: List<IndexedValue<Visitable<*>>>,
            productItemCount: Int,
    ) {
        visitableListAboveBanner.count { it.value is ProductItemDataView }.shouldBe(
                productItemCount,
                "There should be $productItemCount product items above banner"
        )
    }

    @Test
    fun `Test show banner in middle of page - same position with broad match`() {
        val searchProductModel = bannerPosition8WithBroadMatchResponse.jsonToObject<SearchProductModel>()

        `Given search product API will success`(searchProductModel)
        `Given view will set and add product list`()

        `When load data`(mapOf(SearchApiConst.Q to "samsung"))

        `Then assert banner in middle of page with broad match`(searchProductModel)
    }

    private fun `Then assert banner in middle of page with broad match`(searchProductModel: SearchProductModel) {
        val indexedVisitableList = visitableList.withIndex().toList()
        val (bannerDataViewIndex, bannerDataView) = indexedVisitableList.getIndexedBannerDataView()

        val bannerModel = searchProductModel.searchProduct.data.banner
        (bannerDataView as BannerDataView).assert(bannerModel)

        val visitableListAboveBanner = indexedVisitableList.filter { it.index < bannerDataViewIndex }
        val visitableListBelowBanner = indexedVisitableList.filter { it.index > bannerDataViewIndex }

        val productItemCount = bannerModel.position
        `Then assert some product items above banner`(visitableListAboveBanner, productItemCount)
        `Then assert banner is below broad match`(visitableListBelowBanner)
    }

    @Test
    fun `Test show banner at bottom of page 2 with broad match`() {
        val searchProductModel = bottomBannerWithBroadMatchPage1Response.jsonToObject<SearchProductModel>()
        val searchProductModelPage2 = bannerPage2Response.jsonToObject<SearchProductModel>()
        val searchParameter = mapOf(
                SearchApiConst.Q to "hampers",
                SearchApiConst.NAVSOURCE to "thematic",
                SearchApiConst.SRP_PAGE_ID to "332"
        )

        `Given search product API will success`(searchProductModel)
        `Given search product load more API will success`(searchProductModelPage2)
        `Given view will set and add product list`()
        `Given view already load data`(searchParameter)

        `When load more data`(searchParameter)

        `Then assert banner at bottom of page with broad match`(searchProductModel)
    }

    private fun `Given search product load more API will success`(searchProductModel: SearchProductModel) {
        every {
            searchProductLoadMoreUseCase.execute(any(), any())
        } answers {
            secondArg<Subscriber<SearchProductModel>>().complete(searchProductModel)
        }
    }

    private fun `Given view already load data`(searchParameter: Map<String, String>) {
        productListPresenter.loadData(searchParameter)
    }

    private fun `When load more data`(searchParameter: Map<String, String>) {
        productListPresenter.loadMoreData(searchParameter)
    }

    @Test
    fun `Test show banner at bottom of page 2`() {
        val searchProductModel = bottomBannerPage1Response.jsonToObject<SearchProductModel>()
        val searchProductModelPage2 = bannerPage2Response.jsonToObject<SearchProductModel>()
        val searchParameter = mapOf(
                SearchApiConst.Q to "hampers",
                SearchApiConst.NAVSOURCE to "thematic",
                SearchApiConst.SRP_PAGE_ID to "332"
        )

        `Given search product API will success`(searchProductModel)
        `Given search product load more API will success`(searchProductModelPage2)
        `Given view will set and add product list`()
        `Given view already load data`(searchParameter)

        `When load more data`(searchParameter)

        `Then assert banner at bottom of page`(searchProductModel)
    }

    @Test
    fun `Test show banner in middle of page 2`() {
        val searchProductModel = bannerPosition12Page1Response.jsonToObject<SearchProductModel>()
        val searchProductModelPage2 = bannerPage2Response.jsonToObject<SearchProductModel>()
        val searchParameter = mapOf(
                SearchApiConst.Q to "hampers",
                SearchApiConst.NAVSOURCE to "thematic",
                SearchApiConst.SRP_PAGE_ID to "332"
        )

        `Given search product API will success`(searchProductModel)
        `Given search product load more API will success`(searchProductModelPage2)
        `Given view will set and add product list`()
        `Given view already load data`(searchParameter)

        `When load more data`(searchParameter)

        `Then assert banner in middle of page`(searchProductModel)
    }

    @Test
    fun `Test show banner in middle of page 2 - same position with broad match`() {
        val searchProductModel = bannerPosition12WithBroadMatchPage1Response.jsonToObject<SearchProductModel>()
        val searchProductModelPage2 = bannerPage2Response.jsonToObject<SearchProductModel>()
        val searchParameter = mapOf(
                SearchApiConst.Q to "hampers",
                SearchApiConst.NAVSOURCE to "thematic",
                SearchApiConst.SRP_PAGE_ID to "332"
        )

        `Given search product API will success`(searchProductModel)
        `Given search product load more API will success`(searchProductModelPage2)
        `Given view will set and add product list`()
        `Given view already load data`(searchParameter)

        `When load more data`(searchParameter)

        `Then assert banner in middle of page with broad match`(searchProductModel)
    }
}
