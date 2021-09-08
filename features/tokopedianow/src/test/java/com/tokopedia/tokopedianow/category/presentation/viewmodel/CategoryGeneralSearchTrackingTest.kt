package com.tokopedia.tokopedianow.category.presentation.viewmodel

import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.tokopedianow.category.domain.model.CategoryModel
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_CLICK_TOKONOW
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_BUSINESS_UNIT
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_CURRENT_SITE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.BUSINESS_UNIT_PHYSICAL_GOODS
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.CURRENT_SITE_TOKOPEDIA_MARKET_PLACE
import com.tokopedia.tokopedianow.search.analytics.SearchTracking.Action.GENERAL_SEARCH
import com.tokopedia.tokopedianow.search.analytics.SearchTracking.Category.TOP_NAV
import com.tokopedia.tokopedianow.searchcategory.analytics.SearchCategoryTrackingConst.Misc.LOCAL_SEARCH
import com.tokopedia.tokopedianow.searchcategory.analytics.SearchCategoryTrackingConst.Misc.NONE
import com.tokopedia.tokopedianow.searchcategory.analytics.SearchCategoryTrackingConst.Misc.PAGESOURCE
import com.tokopedia.tokopedianow.searchcategory.analytics.SearchCategoryTrackingConst.Misc.RELATEDKEYWORD
import com.tokopedia.tokopedianow.searchcategory.analytics.SearchCategoryTrackingConst.Misc.TOKOPEDIA_NOW
import com.tokopedia.tokopedianow.searchcategory.domain.model.AceSearchProductModel
import com.tokopedia.tokopedianow.searchcategory.jsonToObject
import com.tokopedia.tokopedianow.searchcategory.utils.TOKONOW
import com.tokopedia.tokopedianow.searchcategory.utils.TOKONOW_CATEGORY
import com.tokopedia.track.TrackAppUtils.EVENT
import com.tokopedia.track.TrackAppUtils.EVENT_ACTION
import com.tokopedia.track.TrackAppUtils.EVENT_CATEGORY
import com.tokopedia.track.TrackAppUtils.EVENT_LABEL
import org.junit.Assert.assertThat
import org.junit.Test
import org.hamcrest.CoreMatchers.`is` as shouldBe

class CategoryGeneralSearchTrackingTest: CategoryTestFixtures() {

    @Test
    fun `test general search tracking`() {
        val categoryModel = "category/first-page-8-products.json".jsonToObject<CategoryModel>()
        `Given get category first page use case will be successful`(categoryModel)

        `When view created`()

        `Then assert general search tracking`(
            searchProduct = categoryModel.searchProduct,
            expectedCategoryId = defaultCategoryL1,
        )
    }

    private fun `Then assert general search tracking`(
        searchProduct: AceSearchProductModel.SearchProduct,
        expectedPreviousKeyword: String = NONE,
        expectedCategoryId: String,
    ) {
        val searchProductHeader = searchProduct.header
        val keywordProcess = searchProductHeader.keywordProcess
        val responseCode = searchProductHeader.responseCode
        val totalData = searchProductHeader.totalData.toString()
        val eventLabel = "" +
            "|$keywordProcess" +
            "|$responseCode" +
            "|$BUSINESS_UNIT_PHYSICAL_GOODS" +
            "|$TOKONOW" +
            "|$TOKOPEDIA_NOW" +
            "|$totalData"
        val relatedKeyword = "$expectedPreviousKeyword - ${searchProduct.getAlternativeKeyword()}"
        val warehouseId = tokoNowCategoryViewModel.warehouseId
        val pageSource = "$TOKOPEDIA_NOW.$TOKONOW_CATEGORY.$LOCAL_SEARCH.$warehouseId"

        val generalSearch = tokoNowCategoryViewModel.generalSearchEventLiveData.value!!

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
        val categoryModel = "category/first-page-8-products.json".jsonToObject<CategoryModel>()
        val previousKeyword = "iphone"

        `Given category view model`(
            defaultCategoryL1,
            defaultCategoryL2,
            mapOf(
                SearchApiConst.PREVIOUS_KEYWORD to previousKeyword
            )
        )
        `Given get category first page use case will be successful`(categoryModel)

        `When view created`()

        `Then assert general search tracking`(
            searchProduct = categoryModel.searchProduct,
            expectedPreviousKeyword = previousKeyword,
            expectedCategoryId = defaultCategoryL1
        )
    }
}