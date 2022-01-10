package com.tokopedia.tokopedianow.search.presentation.viewmodel

import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_CLICK_TOKONOW
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_BUSINESS_UNIT
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_CURRENT_SITE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.BUSINESS_UNIT_PHYSICAL_GOODS
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.CURRENT_SITE_TOKOPEDIA_MARKET_PLACE
import com.tokopedia.tokopedianow.search.analytics.SearchTracking.Action.GENERAL_SEARCH
import com.tokopedia.tokopedianow.search.analytics.SearchTracking.Category.TOP_NAV
import com.tokopedia.tokopedianow.search.domain.model.SearchModel
import com.tokopedia.tokopedianow.searchcategory.analytics.SearchCategoryTrackingConst.Misc.LOCAL_SEARCH
import com.tokopedia.tokopedianow.searchcategory.analytics.SearchCategoryTrackingConst.Misc.NONE
import com.tokopedia.tokopedianow.searchcategory.analytics.SearchCategoryTrackingConst.Misc.PAGESOURCE
import com.tokopedia.tokopedianow.searchcategory.analytics.SearchCategoryTrackingConst.Misc.RELATEDKEYWORD
import com.tokopedia.tokopedianow.searchcategory.analytics.SearchCategoryTrackingConst.Misc.TOKOPEDIA_NOW
import com.tokopedia.tokopedianow.searchcategory.domain.model.AceSearchProductModel.SearchProduct
import com.tokopedia.tokopedianow.searchcategory.jsonToObject
import com.tokopedia.tokopedianow.searchcategory.utils.TOKONOW
import com.tokopedia.track.TrackAppUtils.EVENT
import com.tokopedia.track.TrackAppUtils.EVENT_ACTION
import com.tokopedia.track.TrackAppUtils.EVENT_CATEGORY
import com.tokopedia.track.TrackAppUtils.EVENT_LABEL
import org.junit.Assert.assertNull
import org.junit.Assert.assertThat
import org.junit.Test
import org.hamcrest.CoreMatchers.`is` as shouldBe

class SearchGeneralSearchTrackingTest: SearchTestFixtures() {

    @Test
    fun `test general search tracking`() {
        val searchModel = "search/first-page-8-products.json".jsonToObject<SearchModel>()
        `Given get search first page use case will be successful`(searchModel)

        `When view created`()

        `Then assert general search tracking`(
            searchProduct = searchModel.searchProduct,
            keyword = defaultKeyword,
        )
    }

    private fun `Then assert general search tracking`(
        searchProduct: SearchProduct,
        keyword: String,
    ) {
        val searchProductHeader = searchProduct.header
        val keywordProcess = searchProductHeader.keywordProcess
        val responseCode = searchProductHeader.responseCode
        val totalData = searchProductHeader.totalData.toString()
        val eventLabel = keyword +
            "|$keywordProcess" +
            "|$responseCode" +
            "|$BUSINESS_UNIT_PHYSICAL_GOODS" +
            "|$TOKONOW" +
            "|$TOKOPEDIA_NOW" +
            "|$totalData"
        val relatedKeyword = "$NONE - ${searchProduct.getAlternativeKeyword()}"
        val warehouseId = tokoNowSearchViewModel.warehouseId
        val pageSource = "$TOKOPEDIA_NOW.$TOKONOW.$LOCAL_SEARCH.$warehouseId"

        val generalSearch = tokoNowSearchViewModel.generalSearchEventLiveData.value!!

        assertThat(generalSearch[EVENT], shouldBe(EVENT_CLICK_TOKONOW))
        assertThat(generalSearch[EVENT_ACTION], shouldBe(GENERAL_SEARCH))
        assertThat(generalSearch[EVENT_CATEGORY], shouldBe(TOP_NAV))
        assertThat(generalSearch[EVENT_LABEL], shouldBe(eventLabel))
        assertThat(generalSearch[KEY_BUSINESS_UNIT], shouldBe(BUSINESS_UNIT_PHYSICAL_GOODS))
        assertThat(generalSearch[KEY_CURRENT_SITE], shouldBe(CURRENT_SITE_TOKOPEDIA_MARKET_PLACE))
        assertThat(generalSearch[RELATEDKEYWORD], shouldBe(relatedKeyword))
        assertThat(generalSearch[PAGESOURCE], shouldBe(pageSource))
    }

    @Test
    fun `test general search tracking with previous keyword`() {
        val searchModel = "search/first-page-8-products.json".jsonToObject<SearchModel>()
        val previousKeyword = "iphone"

        `Given search view model`(mapOf(
            SearchApiConst.Q to defaultKeyword,
            SearchApiConst.PREVIOUS_KEYWORD to previousKeyword
        ))
        `Given get search first page use case will be successful`(searchModel)

        `When view created`()

        `Then assert related keyword in general search has previous keyword`(
            searchModel.searchProduct,
            previousKeyword,
        )
    }

    private fun `Then assert related keyword in general search has previous keyword`(
        searchProduct: SearchProduct,
        previousKeyword: String,
    ) {
        val alternativeKeyword = searchProduct.getAlternativeKeyword()
        val relatedKeyword = "$previousKeyword - $alternativeKeyword"

        val generalSearch = tokoNowSearchViewModel.generalSearchEventLiveData.value!!
        assertThat(generalSearch[RELATEDKEYWORD], shouldBe(relatedKeyword))
    }

    @Test
    fun `test general search tracking with empty keyword`() {
        val searchModel = "search/first-page-8-products.json".jsonToObject<SearchModel>()

        val keyword = ""

        `Given search view model`(mapOf(
                SearchApiConst.Q to keyword
        ))
        `Given get search first page use case will be successful`(searchModel)

        `When view created`()

        assertNull(tokoNowSearchViewModel.generalSearchEventLiveData.value)
    }
}