package com.tokopedia.tokopedianow.search.presentation.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.domain.model.LocalWarehouseModel
import com.tokopedia.remoteconfig.RollenceKey.TOKOPEDIA_NOW_PAGINATION
import com.tokopedia.tokopedianow.common.constant.ConstantKey.EXPERIMENT_DISABLED
import com.tokopedia.tokopedianow.common.constant.ConstantKey.EXPERIMENT_ENABLED
import com.tokopedia.tokopedianow.common.constant.ServiceType.NOW_2H
import com.tokopedia.tokopedianow.search.domain.model.SearchModel
import com.tokopedia.tokopedianow.search.presentation.model.CTATokopediaNowHomeDataView
import com.tokopedia.tokopedianow.searchcategory.assertBannerDataView
import com.tokopedia.tokopedianow.searchcategory.assertCategoryFilterDataView
import com.tokopedia.tokopedianow.searchcategory.assertChooseAddressDataView
import com.tokopedia.tokopedianow.searchcategory.assertProductAdsCarousel
import com.tokopedia.tokopedianow.searchcategory.assertProductCountDataView
import com.tokopedia.tokopedianow.searchcategory.assertQuickFilterDataView
import com.tokopedia.tokopedianow.searchcategory.jsonToObject
import com.tokopedia.tokopedianow.searchcategory.presentation.model.ProductItemDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.SearchTitle
import com.tokopedia.tokopedianow.searchcategory.presentation.model.TitleDataView
import com.tokopedia.tokopedianow.searchcategory.verifyProductItemDataViewList
import com.tokopedia.tokopedianow.util.SearchCategoryDummyUtils.dummyChooseAddressData
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert
import org.junit.Assert.assertThat
import org.junit.Test
import org.hamcrest.CoreMatchers.`is` as shouldBe

class SearchFirstPageTest : BaseSearchPageLoadTest() {

    override fun setUp() {}

    @Test
    fun `test first page is last page 2h`() {
        val searchModel = "search/first-page-8-products.json".jsonToObject<SearchModel>()

        val chooseAddressData = LocalCacheModel(
            address_id = "12257",
            city_id = "12345",
            district_id = "2274",
            lat = "1.1000",
            long = "37.002",
            postal_code = "15123",
            shop_id = "549621",
            warehouse_id = "11299001123",
            warehouses = listOf(
                LocalWarehouseModel(1, "2h"),
                LocalWarehouseModel(2, "fc")
            ),
            service_type = "2h"
        )

        `Given get search first page use case will be successful`(searchModel, requestParamsSlot)
        `Given choose address data`(chooseAddressData)
        `Given search view model`()
        `Given remote config`(
            defaultValue = EXPERIMENT_DISABLED,
            key = TOKOPEDIA_NOW_PAGINATION,
            value = EXPERIMENT_DISABLED
        )

        `When view created`()

        val visitableList = tokoNowSearchViewModel.visitableListLiveData.value!!

        `Then assert request params map`(createExpectedMandatoryTokonowQueryParams(1))
        `Then assert first page visitables`(visitableList, searchModel)
        `Then assert visitable list footer 2h`(visitableList, searchModel)
        `Then assert visitable list does not end with loading more model`(visitableList)
        `Then assert has next page value`(false)
        `Then assert get first page success interactions`(searchModel)
        `Then assert first page success trigger is Unit`()
        `Then assert isEmptyResult false`()
        `Then assert serviceType 2h`()
    }

    @Test
    fun `test first page is last page 15m`() {
        val searchModel = "search/first-page-8-products.json".jsonToObject<SearchModel>()

        `Given get search first page use case will be successful`(searchModel, requestParamsSlot)
        `Given choose address data`(dummyChooseAddressData.copy(service_type = NOW_2H))
        `Given search view model`()
        `Given remote config`(
            defaultValue = EXPERIMENT_DISABLED,
            key = TOKOPEDIA_NOW_PAGINATION,
            value = EXPERIMENT_ENABLED
        )

        `When view created`()

        val visitableList = tokoNowSearchViewModel.visitableListLiveData.value!!

        `Then assert request params map`(createExpectedMandatoryTokonowQueryParams(1))
        `Then assert first page visitables`(visitableList, searchModel)
        `Then assert visitable list does not end with loading more model`(visitableList)
        `Then assert has next page value`(false)
        `Then assert get first page success interactions`(searchModel)
        `Then assert first page success trigger is Unit`()
    }

    private fun `Then assert first page visitables`(
        visitableList: List<Visitable<*>>,
        searchModel: SearchModel
    ) {
        `Then assert visitable list header`(visitableList, searchModel)
        `Then assert visitable list contents`(visitableList, searchModel)
    }

    private fun `Then assert visitable list header`(
        visitableList: List<Visitable<*>>,
        searchModel: SearchModel
    ) {
        val mapParameter = tokoNowSearchViewModel.queryParam

        visitableList[0].assertChooseAddressDataView()
        visitableList[1].assertBannerDataView()
        visitableList[2].assertTitleDataView()
        visitableList[3].assertCategoryFilterDataView(searchModel.categoryFilter)
        visitableList[4].assertQuickFilterDataView(searchModel.quickFilter, mapParameter)
        visitableList[5].assertProductCountDataView(searchModel.searchProduct.header.totalDataText)
    }

    private fun Visitable<*>.assertTitleDataView() {
        assertThat(this, instanceOf(TitleDataView::class.java))

        val titleDataView = this as TitleDataView
        assertThat(titleDataView.hasSeeAllCategoryButton, shouldBe(false))
        assertThat(titleDataView.titleType, instanceOf(SearchTitle::class.java))
    }

    private fun `Then assert visitable list contents`(
        visitableList: List<Visitable<*>>,
        searchModel: SearchModel
    ) {
        val expectedProductList = searchModel.searchProduct.data.productList
        val actualProductItemDataViewList = visitableList.filterIsInstance<ProductItemDataView>()

        verifyProductAdsCarousel(visitableList, searchModel)
        verifyProductItemDataViewList(expectedProductList, actualProductItemDataViewList, 1)
    }

    private fun verifyProductAdsCarousel(
        visitableList: List<Visitable<*>>,
        searchModel: SearchModel
    ) {
        visitableList[6].assertProductAdsCarousel(searchModel.productAds)
    }

    private fun `Then assert visitable list footer 2h`(
        visitableList: List<Visitable<*>>,
        searchModel: SearchModel
    ) {
        val lastProductIndex = visitableList.indexOfLast { it is ProductItemDataView }
        val footerStartIndex = lastProductIndex + 1
        val footerList = visitableList.subList(footerStartIndex, visitableList.size)

        footerList.last().assertCTATokopediaNowHomeDataView()
    }

    private fun Visitable<*>.assertCTATokopediaNowHomeDataView() {
        assertThat(this, instanceOf(CTATokopediaNowHomeDataView::class.java))
    }

    private fun `Then assert get first page success interactions`(searchModel: SearchModel) {
        `Then assert auto complete applink from API`(searchModel)
        `Then assert header background is shown`()
        `Then assert content is not loading`()
    }

    private fun `Then assert auto complete applink from API`(searchModel: SearchModel) {
        val expectedApplink = searchModel.searchProduct.data.autocompleteApplink

        assertThat(tokoNowSearchViewModel.autoCompleteApplink, shouldBe(expectedApplink))
    }

    private fun `Then assert header background is shown`() {
        assertThat(tokoNowSearchViewModel.isHeaderBackgroundVisibleLiveData.value, shouldBe(true))
    }

    private fun `Then assert content is not loading`() {
        assertThat(tokoNowSearchViewModel.isContentLoadingLiveData.value, shouldBe(false))
    }

    @Test
    fun `test first page has next page`() {
        val searchModel = "search/first-page-16-products.json".jsonToObject<SearchModel>()
        `Given get search first page use case will be successful`(searchModel, requestParamsSlot)
        `Given choose address data`()
        `Given search view model`()

        `When view created`()

        val visitableList = tokoNowSearchViewModel.visitableListLiveData.value!!

        `Then assert request params map`(createExpectedMandatoryTokonowQueryParams(1))
        `Then assert first page visitables`(visitableList, searchModel)
        `Then assert visitable list end with loading more model`(visitableList)
        `Then assert has next page value`(true)
        `Then assert get first page success interactions`(searchModel)
    }

    private fun `Then assert first page success trigger is Unit`() {
        assertThat(tokoNowSearchViewModel.firstPageSuccessTriggerLiveData.value, shouldBe(Unit))
    }

    private fun `Then assert isEmptyResult false`() {
        MatcherAssert.assertThat(tokoNowSearchViewModel.isEmptyResult, shouldBe(false))
    }

    private fun `Then assert serviceType 2h`() {
        MatcherAssert.assertThat(tokoNowSearchViewModel.serviceType, shouldBe("2h"))
    }
}
