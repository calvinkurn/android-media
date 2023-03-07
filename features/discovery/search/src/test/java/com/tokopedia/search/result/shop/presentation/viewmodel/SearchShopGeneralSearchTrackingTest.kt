package com.tokopedia.search.result.shop.presentation.viewmodel

import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.utils.Dimension90Utils
import com.tokopedia.search.analytics.GeneralSearchTrackingShop
import com.tokopedia.search.analytics.SearchEventTracking
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.shop.domain.model.SearchShopModel
import com.tokopedia.search.result.stubExecute
import com.tokopedia.search.shouldBe
import org.junit.Test

internal class SearchShopGeneralSearchTrackingTest: SearchShopDataViewTestFixtures() {

    private val defaultParamGeneralSearchTracking = mapOf(
        SearchApiConst.Q to defaultKeyword
    )

    override fun setUp() { }

    private fun `Test general search tracking`(
            searchShopParameter: Map<String, Any> = defaultParamGeneralSearchTracking,
            searchShopModel: SearchShopModel,
            expectedGeneralSearchTracking: GeneralSearchTrackingShop,
    ) {
        searchShopViewModel = createSearchShopViewModel(searchShopParameter)
        `Given search shop will be successful`(searchShopModel)

        `When handle view is visible and added`()

        `Then assert general search tracking`(expectedGeneralSearchTracking)
    }

    private fun `Given search shop will be successful`(searchShopModel: SearchShopModel) {
        searchShopFirstPageUseCase.stubExecute().returns(searchShopModel)
    }

    private fun `When handle view is visible and added`() {
        searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
    }

    private fun `Then assert general search tracking`(
            expectedGeneralSearchTracking: GeneralSearchTrackingShop,
    ) {
        val actualGeneralSearchTracking = searchShopViewModel.generalSearchTrackingLiveData.value!!
        actualGeneralSearchTracking shouldBe expectedGeneralSearchTracking
    }

    @Test
    fun `general search tracking shop`() {
        val searchShopModel = "searchshop/generalsearch/general-search.json".jsonToObject<SearchShopModel>()

        val treatmentType = searchShopModel.aceSearchShop.header.keywordProcess
        val responseCode = searchShopModel.aceSearchShop.header.responseCode
        val businessUnit = SearchEventTracking.PHYSICAL_GOODS
        val totalData = searchShopModel.aceSearchShop.totalShop
        val eventLabel = "$defaultKeyword|" +
                "$treatmentType|" +
                "$responseCode|" +
                "$businessUnit|" +
                "${SearchEventTracking.NONE}|" +
                "${SearchEventTracking.NONE}|" +
                "$totalData"

        val relatedKeyword = "${SearchEventTracking.NONE} - ${SearchEventTracking.NONE}"

        val expectedGeneralSearchTracking = GeneralSearchTrackingShop(
            eventLabel = eventLabel,
            pageSource = Dimension90Utils.getDimension90(searchShopParameterCommon),
            relatedKeyword = relatedKeyword,
            searchFilter = "",
            externalReference = "",
        )

        `Test general search tracking`(
                searchShopModel = searchShopModel,
                expectedGeneralSearchTracking = expectedGeneralSearchTracking
        )
    }

    @Test
    fun `general search tracking shop with navsource`() {
        val navsource = "home"
        val searchShopParameter = mapOf(
                SearchApiConst.Q to defaultKeyword,
                SearchApiConst.NAVSOURCE to navsource,
        )
        val searchShopModel = "searchshop/generalsearch/general-search.json".jsonToObject<SearchShopModel>()

        val treatmentType = searchShopModel.aceSearchShop.header.keywordProcess
        val responseCode = searchShopModel.aceSearchShop.header.responseCode
        val businessUnit = SearchEventTracking.PHYSICAL_GOODS
        val totalData = searchShopModel.aceSearchShop.totalShop
        val eventLabel = "$defaultKeyword|" +
                "$treatmentType|" +
                "$responseCode|" +
                "$businessUnit|" +
                "$navsource|" +
                "${SearchEventTracking.NONE}|" +
                "$totalData"

        val relatedKeyword = "${SearchEventTracking.NONE} - ${SearchEventTracking.NONE}"

        val expectedGeneralSearchTracking = GeneralSearchTrackingShop(
            eventLabel = eventLabel,
            pageSource = Dimension90Utils.getDimension90(searchShopParameter),
            relatedKeyword = relatedKeyword,
            searchFilter = "",
            externalReference = "",
        )

        `Test general search tracking`(
                searchShopParameter = searchShopParameter,
                searchShopModel = searchShopModel,
                expectedGeneralSearchTracking = expectedGeneralSearchTracking
        )
    }

    @Test
    fun `general search tracking shop with srp_page_title`() {
        val navsource = "home"
        val srpPageTitle = "Page title"
        val searchShopParameter = mapOf(
                SearchApiConst.Q to defaultKeyword,
                SearchApiConst.NAVSOURCE to navsource,
                SearchApiConst.SRP_PAGE_TITLE to srpPageTitle,
        )
        val searchShopModel = "searchshop/generalsearch/general-search.json".jsonToObject<SearchShopModel>()

        val treatmentType = searchShopModel.aceSearchShop.header.keywordProcess
        val responseCode = searchShopModel.aceSearchShop.header.responseCode
        val businessUnit = SearchEventTracking.PHYSICAL_GOODS
        val totalData = searchShopModel.aceSearchShop.totalShop
        val eventLabel = "$defaultKeyword|" +
                "$treatmentType|" +
                "$responseCode|" +
                "$businessUnit|" +
                "$navsource|" +
                "$srpPageTitle|" +
                "$totalData"

        val relatedKeyword = "${SearchEventTracking.NONE} - ${SearchEventTracking.NONE}"

        val expectedGeneralSearchTracking = GeneralSearchTrackingShop(
            eventLabel = eventLabel,
            pageSource = Dimension90Utils.getDimension90(searchShopParameter),
            relatedKeyword = relatedKeyword,
            searchFilter = "",
            externalReference = "",
        )

        `Test general search tracking`(
                searchShopParameter = searchShopParameter,
                searchShopModel = searchShopModel,
                expectedGeneralSearchTracking = expectedGeneralSearchTracking
        )
    }

    @Test
    fun `general search tracking shop with previous keyword`() {
        val previousKeyword = "iphone"
        val searchShopParameter = mapOf(
                SearchApiConst.Q to defaultKeyword,
                SearchApiConst.PREVIOUS_KEYWORD to previousKeyword,
        )
        val searchShopModel = "searchshop/generalsearch/general-search.json".jsonToObject<SearchShopModel>()

        val treatmentType = searchShopModel.aceSearchShop.header.keywordProcess
        val responseCode = searchShopModel.aceSearchShop.header.responseCode
        val businessUnit = SearchEventTracking.PHYSICAL_GOODS
        val totalData = searchShopModel.aceSearchShop.totalShop
        val eventLabel = "$defaultKeyword|" +
                "$treatmentType|" +
                "$responseCode|" +
                "$businessUnit|" +
                "${SearchEventTracking.NONE}|" +
                "${SearchEventTracking.NONE}|" +
                "$totalData"

        val relatedKeyword = "$previousKeyword - ${SearchEventTracking.NONE}"

        val expectedGeneralSearchTracking = GeneralSearchTrackingShop(
            eventLabel = eventLabel,
            pageSource = Dimension90Utils.getDimension90(searchShopParameterCommon),
            relatedKeyword = relatedKeyword,
            searchFilter = "",
            externalReference = "",
        )

        `Test general search tracking`(
                searchShopParameter = searchShopParameter,
                searchShopModel = searchShopModel,
                expectedGeneralSearchTracking = expectedGeneralSearchTracking
        )
    }

    @Test
    fun `general search tracking shop with filter`() {
        val previousKeyword = "iphone"
        val searchShopParameter = mapOf(
            SearchApiConst.Q to defaultKeyword,
            SearchApiConst.PREVIOUS_KEYWORD to previousKeyword,
            SearchApiConst.OFFICIAL to true.toString(),
            SearchApiConst.FCITY to "123",
        )
        val searchShopModel = "searchshop/generalsearch/general-search.json".jsonToObject<SearchShopModel>()

        val treatmentType = searchShopModel.aceSearchShop.header.keywordProcess
        val responseCode = searchShopModel.aceSearchShop.header.responseCode
        val businessUnit = SearchEventTracking.PHYSICAL_GOODS
        val totalData = searchShopModel.aceSearchShop.totalShop
        val eventLabel = "$defaultKeyword|" +
            "$treatmentType|" +
            "$responseCode|" +
            "$businessUnit|" +
            "${SearchEventTracking.NONE}|" +
            "${SearchEventTracking.NONE}|" +
            "$totalData"

        val relatedKeyword = "$previousKeyword - ${SearchEventTracking.NONE}"

        val expectedGeneralSearchTracking = GeneralSearchTrackingShop(
            eventLabel = eventLabel,
            pageSource = Dimension90Utils.getDimension90(searchShopParameterCommon),
            relatedKeyword = relatedKeyword,
            searchFilter = "${SearchApiConst.OFFICIAL}=true&${SearchApiConst.FCITY}=123",
            externalReference = "",
        )

        `Test general search tracking`(
            searchShopParameter = searchShopParameter,
            searchShopModel = searchShopModel,
            expectedGeneralSearchTracking = expectedGeneralSearchTracking
        )
    }

    @Test
    fun `general search tracking shop external reference`() {
        val externalReference = "1234567"
        val searchShopParameter = mapOf(
            SearchApiConst.Q to defaultKeyword,
            SearchApiConst.SRP_EXT_REF to externalReference,
        )
        val searchShopModel = "searchshop/generalsearch/general-search.json".jsonToObject<SearchShopModel>()

        val treatmentType = searchShopModel.aceSearchShop.header.keywordProcess
        val responseCode = searchShopModel.aceSearchShop.header.responseCode
        val businessUnit = SearchEventTracking.PHYSICAL_GOODS
        val totalData = searchShopModel.aceSearchShop.totalShop
        val eventLabel = "$defaultKeyword|" +
                "$treatmentType|" +
                "$responseCode|" +
                "$businessUnit|" +
                "${SearchEventTracking.NONE}|" +
                "${SearchEventTracking.NONE}|" +
                "$totalData"

        val relatedKeyword = "${SearchEventTracking.NONE} - ${SearchEventTracking.NONE}"

        val expectedGeneralSearchTracking = GeneralSearchTrackingShop(
            eventLabel = eventLabel,
            pageSource = Dimension90Utils.getDimension90(searchShopParameterCommon),
            relatedKeyword = relatedKeyword,
            searchFilter = "",
            externalReference = externalReference,
        )

        `Test general search tracking`(
            searchShopParameter = searchShopParameter,
            searchShopModel = searchShopModel,
            expectedGeneralSearchTracking = expectedGeneralSearchTracking
        )
    }

}