package com.tokopedia.tokopedianow.search.presentation.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_CLICK_TOKONOW
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_BUSINESS_UNIT
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_CURRENT_SITE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.BUSINESS_UNIT_PHYSICAL_GOODS
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.CURRENT_SITE_TOKOPEDIA_MARKET_PLACE
import com.tokopedia.tokopedianow.search.domain.model.SearchModel
import com.tokopedia.tokopedianow.search.analytics.SearchTracking.Action.GENERAL_SEARCH
import com.tokopedia.tokopedianow.search.analytics.SearchTracking.Category.TOKONOW_TOP_NAV
import com.tokopedia.tokopedianow.search.analytics.SearchTracking.Misc.HASIL_PENCARIAN_DI_TOKONOW
import com.tokopedia.tokopedianow.searchcategory.assertBannerDataView
import com.tokopedia.tokopedianow.searchcategory.assertCategoryFilterDataView
import com.tokopedia.tokopedianow.searchcategory.assertChooseAddressDataView
import com.tokopedia.tokopedianow.searchcategory.assertProductCountDataView
import com.tokopedia.tokopedianow.searchcategory.assertQuickFilterDataView
import com.tokopedia.tokopedianow.searchcategory.assertTitleDataView
import com.tokopedia.tokopedianow.searchcategory.jsonToObject
import com.tokopedia.tokopedianow.searchcategory.presentation.model.ProductItemDataView
import com.tokopedia.tokopedianow.searchcategory.utils.TOKONOW
import com.tokopedia.tokopedianow.searchcategory.verifyProductItemDataViewList
import com.tokopedia.track.TrackAppUtils.EVENT
import com.tokopedia.track.TrackAppUtils.EVENT_ACTION
import com.tokopedia.track.TrackAppUtils.EVENT_CATEGORY
import com.tokopedia.track.TrackAppUtils.EVENT_LABEL
import org.junit.Assert.assertThat
import org.junit.Test
import org.hamcrest.CoreMatchers.`is` as shouldBe

class SearchFirstPageTest: BaseSearchPageLoadTest() {

    @Test
    fun `test first page is last page`() {
        val searchModel = "search/first-page-8-products.json".jsonToObject<SearchModel>()
        `Given get search first page use case will be successful`(searchModel, requestParamsSlot)

        `When view created`()

        val visitableList = tokoNowSearchViewModel.visitableListLiveData.value!!

        `Then assert request params map`(createExpectedMandatoryTokonowQueryParams(1))
        `Then assert first page visitables`(visitableList, searchModel)
        `Then assert visitable list does not end with loading more model`(visitableList)
        `Then assert has next page value`(false)
        `Then assert get first page success interactions`(searchModel)
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
            searchModel: SearchModel,
    ) {
        val mapParameter = tokoNowSearchViewModel.queryParam

        visitableList[0].assertChooseAddressDataView()
        visitableList[1].assertBannerDataView()
        visitableList[2].assertTitleDataView(title = "", hasSeeAllCategoryButton = false)
        visitableList[3].assertCategoryFilterDataView(searchModel.categoryFilter)
        visitableList[4].assertQuickFilterDataView(searchModel.quickFilter, mapParameter)
        visitableList[5].assertProductCountDataView(searchModel.searchProduct.header.totalDataText)
    }

    private fun `Then assert visitable list contents`(
            visitableList: List<Visitable<*>>,
            searchModel: SearchModel,
    ) {
        val expectedProductList = searchModel.searchProduct.data.productList
        val actualProductItemDataViewList = visitableList.filterIsInstance<ProductItemDataView>()

        verifyProductItemDataViewList(expectedProductList, actualProductItemDataViewList, 1)
    }

    private fun `Then assert get first page success interactions`(searchModel: SearchModel) {
        `Then assert auto complete applink from API`(searchModel)
        `Then assert header background is shown`()
        `Then assert content is not loading`()
        `Then assert general search tracking`(searchModel)
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

    private fun `Then assert general search tracking`(searchModel: SearchModel) {
        val searchProductHeader = searchModel.searchProduct.header
        val keywordProcess = searchProductHeader.keywordProcess
        val responseCode = searchProductHeader.responseCode
        val totalData = searchProductHeader.totalData.toString()
        val eventLabel = defaultKeyword +
                "|$keywordProcess" +
                "|$responseCode" +
                "|$BUSINESS_UNIT_PHYSICAL_GOODS" +
                "|$TOKONOW" +
                "|$HASIL_PENCARIAN_DI_TOKONOW" +
                "|$totalData"

        val generalSearch = tokoNowSearchViewModel.generalSearchEventLiveData.value!!

        assertThat(generalSearch[EVENT], shouldBe(EVENT_CLICK_TOKONOW))
        assertThat(generalSearch[EVENT_ACTION], shouldBe(GENERAL_SEARCH))
        assertThat(generalSearch[EVENT_CATEGORY], shouldBe(TOKONOW_TOP_NAV))
        assertThat(generalSearch[EVENT_LABEL], shouldBe(eventLabel))
        assertThat(generalSearch[KEY_BUSINESS_UNIT], shouldBe(BUSINESS_UNIT_PHYSICAL_GOODS))
        assertThat(generalSearch[KEY_CURRENT_SITE], shouldBe(CURRENT_SITE_TOKOPEDIA_MARKET_PLACE))
    }

    @Test
    fun `test first page has next page`() {
        val searchModel = "search/first-page-16-products.json".jsonToObject<SearchModel>()
        `Given get search first page use case will be successful`(searchModel, requestParamsSlot)

        `When view created`()

        val visitableList = tokoNowSearchViewModel.visitableListLiveData.value!!

        `Then assert request params map`(createExpectedMandatoryTokonowQueryParams(1))
        `Then assert first page visitables`(visitableList, searchModel)
        `Then assert visitable list end with loading more model`(visitableList)
        `Then assert has next page value`(true)
        `Then assert get first page success interactions`(searchModel)
    }
}