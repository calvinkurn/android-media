package com.tokopedia.autocompletecomponent.suggestion

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.autocompletecomponent.jsonToObject
import com.tokopedia.autocompletecomponent.searchbar.SearchBarKeyword
import com.tokopedia.autocompletecomponent.shouldBe
import com.tokopedia.autocompletecomponent.shouldBeInstanceOf
import com.tokopedia.autocompletecomponent.shouldNotContain
import com.tokopedia.autocompletecomponent.suggestion.chips.SuggestionChipWidgetDataView
import com.tokopedia.autocompletecomponent.suggestion.domain.model.SuggestionUniverse
import com.tokopedia.autocompletecomponent.suggestion.doubleline.SuggestionDoubleLineDataDataView
import com.tokopedia.autocompletecomponent.suggestion.doubleline.SuggestionDoubleLineWithoutImageDataDataView
import com.tokopedia.autocompletecomponent.suggestion.productline.SuggestionProductLineDataDataView
import com.tokopedia.autocompletecomponent.suggestion.separator.SuggestionSeparatorDataView
import com.tokopedia.autocompletecomponent.suggestion.singleline.SuggestionSingleLineDataDataView
import com.tokopedia.autocompletecomponent.suggestion.title.SuggestionTitleDataView
import com.tokopedia.autocompletecomponent.suggestion.topshop.SuggestionTopShopWidgetDataView
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.USER_ADDRESS_ID
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.USER_CITY_ID
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.USER_DISTRICT_ID
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.USER_LAT
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.USER_LONG
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.USER_POST_CODE
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.USER_WAREHOUSE_ID
import com.tokopedia.discovery.common.constants.SearchConstant.CustomDimension.DEFAULT_VALUE_CUSTOM_DIMENSION_90_GLOBAL
import com.tokopedia.discovery.common.utils.Dimension90Utils
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.usecase.RequestParams
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.slot
import io.mockk.verify
import org.junit.Test

private const val suggestionCommonResponse = "autocomplete/suggestion/suggestion-common-response.json"
private const val suggestionEmptyHeaderTitleResponse = "autocomplete/suggestion/suggestion-empty-header-title-response.json"
private const val suggestionTopShopResponse = "autocomplete/suggestion/suggestion-top-shop-response.json"
private const val suggestionCampaignResponse = "autocomplete/suggestion/local-global-response.json"
private const val suggestionCampaignAtTopResponse = "autocomplete/suggestion/local-global-at-top-response.json"
private const val suggestionShopAdsResponse = "autocomplete/suggestion/shopads/suggestion-shop-ads-response.json"
private const val suggestionWithoutShopAdsResponse = "autocomplete/suggestion/shopads/suggestion-without-shop-ads-response.json"
private const val suggestionWithoutShopAdsTemplateResponse = "autocomplete/suggestion/shopads/suggestion-shop-ads-without-template.json"
private const val suggestionMultipleShopAdsNoMatchResponse = "autocomplete/suggestion/shopads/suggestion-multiple-shop-ads-no-match-response.json"
private const val suggestionMultipleShopAdsMatchFirstResponse = "autocomplete/suggestion/shopads/suggestion-multiple-shop-ads-match-first.json"
private const val suggestionSingleShopAdsMatchFirstResponse = "autocomplete/suggestion/shopads/suggestion-single-shop-ads-match-first.json"
private const val suggestionMultipleShopAdsMatchSecondResponse = "autocomplete/suggestion/shopads/suggestion-multiple-shop-ads-match-second.json"
private const val suggestionMultipleShopAdsMatchBothResponse = "autocomplete/suggestion/shopads/suggestion-multiple-shop-ads-match-both.json"

internal class SuggestionPresenterTest: SuggestionPresenterTestFixtures() {

    private val keyword: String = "asus"
    private val searchProductPageTitle = "Waktu Indonesia Belanja"
    private val searchParameter = mapOf(
        SearchApiConst.Q to keyword,
        SearchApiConst.NAVSOURCE to "clp",
        SearchApiConst.SRP_PAGE_TITLE to searchProductPageTitle,
        SearchApiConst.SRP_PAGE_ID to "1234"
    )
    private val expectedDefaultDimension90 = DEFAULT_VALUE_CUSTOM_DIMENSION_90_GLOBAL
    private val expectedLocalDimension90 = Dimension90Utils.getDimension90(searchParameter)

    private val requestParamsSlot = slot<RequestParams>()
    private val requestParams by lazy { requestParamsSlot.captured }

    @Test
    fun `get suggestion data success will show suggestion`() {
        val suggestionUniverse = suggestionCommonResponse.jsonToObject<SuggestionUniverse>()
        `Given Suggestion API will return SuggestionUniverse`(suggestionUniverse, requestParamsSlot)

        `when presenter get suggestion data`()

        `then verify suggestion API is called`()
        `then verify suggestion view will call hideExceedKeywordLimit`()
        `then verify suggestion view will call showSuggestionResult`()
        `then verify visitable list`(suggestionUniverse)
    }

    private fun `when presenter get suggestion data`(
        searchParameter: Map<String, String> = mapOf(
            SearchApiConst.Q to keyword,
        ),
        activeKeyword: SearchBarKeyword = SearchBarKeyword(
            keyword = keyword
        ),
    ) {
        suggestionPresenter.getSuggestion(searchParameter, activeKeyword)
    }

    private fun `then verify suggestion API is called`() {
        verify { getSuggestionUseCase.execute(any(), any(), any()) }
    }
    private fun `then verify suggestion API is not called`() {
        verify(exactly = 0) { getSuggestionUseCase.execute(any(), any(), any()) }
    }

    private fun `then verify suggestion view will call showExceedKeywordLimit`() {
        verify {
            suggestionView.showExceedKeywordLimit()
        }
    }
    private fun `then verify suggestion view will call hideExceedKeywordLimit`() {
        verify {
            suggestionView.hideExceedKeywordLimit()
        }
    }

    private fun `then verify suggestion view will call showSuggestionResult`() {
        verify {
            suggestionView.showSuggestionResult(capture(slotVisitableList))
        }
    }
    private fun `then verify suggestion view will call hideSuggestionResult`() {
        verify {
            suggestionView.hideSuggestionResult()
        }
    }

    private fun `then verify visitable list`(suggestionUniverse: SuggestionUniverse) {
        val visitableList = slotVisitableList.captured

        visitableList[0].shouldBeSuggestionDoubleLineDataView()
        visitableList[1].shouldBeSuggestionSingleLineDataDataView(
            isBoldAllText = true
        )
        visitableList[2].shouldBeSuggestionSingleLineDataDataView()
        visitableList[3].shouldBeSuggestionSingleLineDataDataView(
            isBoldAllText = false
        )
        visitableList[4].shouldBeSuggestionTitleDataView()
        visitableList[5].shouldBeSuggestionDoubleLineDataView(
            isBoldAllText = false,
            isCircle = true
        )
        visitableList[6].shouldBeSuggestionDoubleLineDataView(
            isBoldAllText = false,
            isCircle = true
        )
        visitableList[7].shouldBeSuggestionTitleDataView()
        visitableList[8].shouldBeSuggestionChipWidgetDataView()
        visitableList.size shouldBe suggestionUniverse.data.items.size

        assertVisitableListData(visitableList, suggestionUniverse, expectedDefaultDimension90)
    }

    private fun assertVisitableListData(
        visitableList: List<Visitable<*>>,
        suggestionUniverse: SuggestionUniverse,
        dimension90: String = ""
    ) {
        var expectedPosition = 0
        visitableList.forEach { visitable ->
            val expectedItem = suggestionUniverse.data.items[expectedPosition]
            when(visitable) {
                is SuggestionTitleDataView -> {
                    visitable.assertSuggestionTitleDataView(expectedItem)
                    expectedPosition++
                }
                is SuggestionTopShopWidgetDataView -> {
                    visitable.assertTopShopWidgetDataView(expectedItem, suggestionUniverse.topShop)
                    expectedPosition++
                }
                is SuggestionSingleLineDataDataView -> {
                    visitable.data.assertBaseSuggestionDataView(
                        SUGGESTION_SINGLE_LINE,
                        expectedItem,
                        dimension90,
                        keyword,
                    )
                    expectedPosition++
                }
                is SuggestionDoubleLineDataDataView -> {
                    visitable.data.assertBaseSuggestionDataView(
                        SUGGESTION_DOUBLE_LINE,
                        expectedItem,
                        dimension90,
                        keyword,
                    )
                    expectedPosition++
                }
                is SuggestionChipWidgetDataView -> {
                    visitable.data.assertBaseSuggestionDataView(
                        SUGGESTION_CHIP_WIDGET,
                        expectedItem,
                        dimension90,
                        keyword,
                    )
                    visitable.assertSuggestionChipWidgetDataView(expectedItem.suggestionChildItems)
                    expectedPosition++
                }
            }
        }
    }

    @Test
    fun `get suggestion data error will not show anything`() {
        `given suggestion API will return error`()
        `when presenter get suggestion data`()
        `then verify suggestion API is called`()
        `then verify view not showing anything`()
    }

    private fun `given suggestion API will return error`() {
        every { getSuggestionUseCase.execute(any(), any(), any()) }.answers {
            secondArg<(Throwable) -> Unit>().invoke(testException)
        }
    }

    private fun `then verify view not showing anything`() {
        verify {
            suggestionView.chooseAddressData
            suggestionView.hideExceedKeywordLimit()
        }
        confirmVerified(suggestionView)
    }

    @Test
    fun `test get suggestion data with top shop`() {
        val suggestionUniverse = suggestionTopShopResponse.jsonToObject<SuggestionUniverse>()
        `Given Suggestion API will return SuggestionUniverse`(suggestionUniverse)

        `when presenter get suggestion data`()

        `then verify suggestion API is called`()
        `then verify suggestion view will call hideExceedKeywordLimit`()
        `then verify suggestion view will call showSuggestionResult`()
        `then verify visitable list with top shop`(suggestionUniverse)
    }

    private fun `then verify visitable list with top shop`(suggestionUniverse: SuggestionUniverse) {
        val visitableList = slotVisitableList.captured

        visitableList[0].shouldBeInstanceOf<SuggestionSingleLineDataDataView>()
        visitableList[1].shouldBeInstanceOf<SuggestionSingleLineDataDataView>()
        visitableList[2].shouldBeInstanceOf<SuggestionSingleLineDataDataView>()
        visitableList[3].shouldBeInstanceOf<SuggestionSingleLineDataDataView>()
        visitableList[4].shouldBeInstanceOf<SuggestionSingleLineDataDataView>()
        visitableList[5].shouldBeInstanceOf<SuggestionTitleDataView>()
        visitableList[6].shouldBeInstanceOf<SuggestionDoubleLineDataDataView>()
        visitableList[7].shouldBeInstanceOf<SuggestionDoubleLineDataDataView>()
        visitableList[8].shouldBeInstanceOf<SuggestionDoubleLineDataDataView>()
        visitableList[9].shouldBeInstanceOf<SuggestionTopShopWidgetDataView>()
        visitableList.size shouldBe suggestionUniverse.data.items.size

        assertVisitableListData(visitableList, suggestionUniverse, expectedDefaultDimension90)
    }

    @Test
    fun `test get suggestion data with campaign local global component`() {
        val suggestionUniverse = suggestionCampaignResponse.jsonToObject<SuggestionUniverse>()
        `Given Suggestion API will return SuggestionUniverse`(suggestionUniverse, requestParamsSlot)

        `when presenter get suggestion data`(searchParameter)

        `then verify suggestion API is called`()
        `then verify suggestion view will call hideExceedKeywordLimit`()
        `then verify suggestion view will call showSuggestionResult`()
        `then verify visitable list should have SuggestionDoubleLineWithoutImageDataView`(suggestionUniverse)
    }

    private fun `then verify visitable list should have SuggestionDoubleLineWithoutImageDataView`(suggestionUniverse: SuggestionUniverse) {
        val visitableList = slotVisitableList.captured

        visitableList[0].shouldBeInstanceOf<SuggestionSingleLineDataDataView>()
        visitableList[1].shouldBeInstanceOf<SuggestionSingleLineDataDataView>()
        visitableList[2].shouldBeInstanceOf<SuggestionTitleDataView>()
        visitableList[3].shouldBeInstanceOf<SuggestionDoubleLineDataDataView>()
        visitableList[4].shouldBeInstanceOf<SuggestionDoubleLineDataDataView>()
        visitableList[5].shouldBeInstanceOf<SuggestionTitleDataView>()
        visitableList[6].shouldBeInstanceOf<SuggestionProductLineDataDataView>()
        visitableList[7].shouldBeInstanceOf<SuggestionProductLineDataDataView>()
        visitableList[8].shouldBeInstanceOf<SuggestionSeparatorDataView>()
        visitableList[9].shouldBeInstanceOf<SuggestionDoubleLineWithoutImageDataDataView>()
        visitableList[10].shouldBeInstanceOf<SuggestionDoubleLineWithoutImageDataDataView>()
        visitableList.size shouldBe suggestionUniverse.data.items.size + 1

        assertVisitableListData(visitableList, suggestionUniverse, expectedLocalDimension90)
    }

    @Test
    fun `test get suggestion data with campaign local global component at the top`() {
        val suggestionUniverse = suggestionCampaignAtTopResponse.jsonToObject<SuggestionUniverse>()
        `Given Suggestion API will return SuggestionUniverse`(suggestionUniverse, requestParamsSlot)

        `when presenter get suggestion data`(searchParameter)

        `then verify suggestion API is called`()
        `then verify suggestion view will call hideExceedKeywordLimit`()
        `then verify suggestion view will call showSuggestionResult`()
        `then verify visitable list should only have SuggestionDoubleLineWithoutImageDataView`(suggestionUniverse)
    }

    private fun `then verify visitable list should only have SuggestionDoubleLineWithoutImageDataView`(suggestionUniverse: SuggestionUniverse) {
        val visitableList = slotVisitableList.captured

        visitableList[0].shouldBeInstanceOf<SuggestionDoubleLineWithoutImageDataDataView>()
        visitableList[1].shouldBeInstanceOf<SuggestionDoubleLineWithoutImageDataDataView>()
        visitableList.size shouldBe suggestionUniverse.data.items.size

        assertVisitableListData(visitableList, suggestionUniverse, expectedLocalDimension90)
    }

    @Test
    fun `Suggestion request will include choose address params if exists`() {
        val warehouseId = "2216"
        val dummyChooseAddressData = LocalCacheModel(
            address_id = "123",
            city_id = "45",
            district_id = "123",
            lat = "10.2131",
            long = "12.01324",
            postal_code = "12345",
            warehouse_id = warehouseId
        )
        val suggestionUniverse = suggestionCommonResponse.jsonToObject<SuggestionUniverse>()

        `Given chosen address data`(dummyChooseAddressData)
        `Given Suggestion API will return SuggestionUniverse`(suggestionUniverse, requestParamsSlot)

        `when presenter get suggestion data`()

        `Then verify search parameter has choose address params`(dummyChooseAddressData)
    }

    private fun `Given chosen address data`(chooseAddressModel: LocalCacheModel?) {
        every { suggestionView.chooseAddressData } returns chooseAddressModel
    }

    private fun `Then verify search parameter has choose address params`(
        localCacheModel: LocalCacheModel
    ) {
        val suggestionParams = requestParams.parameters
        suggestionParams[USER_LAT] shouldBe localCacheModel.lat
        suggestionParams[USER_LONG] shouldBe localCacheModel.long
        suggestionParams[USER_ADDRESS_ID] shouldBe localCacheModel.address_id
        suggestionParams[USER_CITY_ID] shouldBe localCacheModel.city_id
        suggestionParams[USER_DISTRICT_ID] shouldBe localCacheModel.district_id
        suggestionParams[USER_POST_CODE] shouldBe localCacheModel.postal_code
        suggestionParams[USER_WAREHOUSE_ID] shouldBe localCacheModel.warehouse_id
    }

    @Test
    fun `Suggestion request will not include choose address params if not exists`() {
        val suggestionUniverse = suggestionCommonResponse.jsonToObject<SuggestionUniverse>()

        `Given chosen address data`(LocalCacheModel())
        `Given Suggestion API will return SuggestionUniverse`(suggestionUniverse, requestParamsSlot)

        `when presenter get suggestion data`()

        `Then verify search parameter has no choose address params`()
    }

    private fun `Then verify search parameter has no choose address params`() {
        val suggestionParams = requestParams.parameters
        suggestionParams.shouldNotContain(USER_LAT)
        suggestionParams.shouldNotContain(USER_LONG)
        suggestionParams.shouldNotContain(USER_ADDRESS_ID)
        suggestionParams.shouldNotContain(USER_CITY_ID)
        suggestionParams.shouldNotContain(USER_DISTRICT_ID)
        suggestionParams.shouldNotContain(USER_POST_CODE)
        suggestionParams.shouldNotContain(USER_WAREHOUSE_ID)
    }

    @Test
    fun `Suggestion will show shop ads if both template and shop ads GQL is successful`() {
        val suggestionUniverse = suggestionShopAdsResponse.jsonToObject<SuggestionUniverse>()
        `Given Suggestion API will return SuggestionUniverse`(suggestionUniverse, requestParamsSlot)

        `when presenter get suggestion data`()

        `then verify suggestion view will call hideExceedKeywordLimit`()
        `then verify suggestion view will call showSuggestionResult`()
        `Then verify shop ads double line data view`(suggestionUniverse)
    }

    private fun `Then verify shop ads double line data view`(suggestionUniverse: SuggestionUniverse) {
        val shopAdsDoubleLineDataView = (visitableList.first() as SuggestionDoubleLineDataDataView)

        shopAdsDoubleLineDataView.data.assertShopAdsSuggestionData(
            suggestionUniverse.data.items.first(),
            suggestionUniverse.cpmModel.data.first(),
            1,
            expectedDefaultDimension90,
            keyword,
        )
    }

    @Test
    fun `Suggestion will not show shop ads if shop ads GQL is not successful`() {
        val suggestionUniverse = suggestionWithoutShopAdsResponse.jsonToObject<SuggestionUniverse>()
        `Given Suggestion API will return SuggestionUniverse`(suggestionUniverse, requestParamsSlot)

        `when presenter get suggestion data`()

        `then verify suggestion view will call hideExceedKeywordLimit`()
        `then verify suggestion view will call showSuggestionResult`()
        `then verify visitable list is empty`()
    }

    private fun `then verify visitable list is empty`() {
        visitableList.size shouldBe 0
    }

    @Test
    fun `Suggestion will not show shop ads if there is no shop ads template`() {
        val suggestionUniverse = suggestionWithoutShopAdsTemplateResponse.jsonToObject<SuggestionUniverse>()
        `Given Suggestion API will return SuggestionUniverse`(suggestionUniverse, requestParamsSlot)

        `when presenter get suggestion data`()

        `then verify suggestion view will call hideExceedKeywordLimit`()
        `then verify suggestion view will call showSuggestionResult`()
        `then verify visitable list is empty`()
    }

    @Test
    fun `Suggestion will show main ads and all organic shop`() {
        val suggestionUniverse = suggestionMultipleShopAdsNoMatchResponse.jsonToObject<SuggestionUniverse>()
        val expectedSize = suggestionUniverse.data.items.size

        `Given Suggestion API will return SuggestionUniverse`(suggestionUniverse, requestParamsSlot)

        `when presenter get suggestion data`()

        `then verify suggestion view will call hideExceedKeywordLimit`()
        `then verify suggestion view will call showSuggestionResult`()
        `Then verify shop ads shown is main ads`(suggestionUniverse)
        `Then verify shops visitable list size`(expectedSize)
    }

    private fun `Then verify shop ads shown is main ads`(suggestionUniverse: SuggestionUniverse) {
        val shopAdsDoubleLineDataView = (visitableList.first() as SuggestionDoubleLineDataDataView)
        val mainAdsApplink = suggestionUniverse.cpmModel.data.first().applinks

        shopAdsDoubleLineDataView.data.applink shouldBe mainAdsApplink
    }

    private fun `Then verify shops visitable list size`(expectedSize: Int) {
        visitableList.size shouldBe expectedSize
    }

    @Test
    fun `Suggestion will show sub ads and all organic shop`() {
        val suggestionUniverse = suggestionMultipleShopAdsMatchFirstResponse.jsonToObject<SuggestionUniverse>()
        val expectedSize = suggestionUniverse.data.items.size

        `Given Suggestion API will return SuggestionUniverse`(suggestionUniverse, requestParamsSlot)

        `when presenter get suggestion data`()

        `then verify suggestion view will call hideExceedKeywordLimit`()
        `then verify suggestion view will call showSuggestionResult`()
        `Then verify shop ads shown is sub ads`(suggestionUniverse)
        `Then verify shops visitable list size`(expectedSize)
    }

    private fun `Then verify shop ads shown is sub ads`(suggestionUniverse: SuggestionUniverse) {
        val shopAdsDoubleLineDataView = (visitableList.first() as SuggestionDoubleLineDataDataView)
        val subAdsApplink = suggestionUniverse.cpmModel.data[1].applinks

        shopAdsDoubleLineDataView.data.applink shouldBe subAdsApplink
    }

    @Test
    fun `Suggestion will not show shop ads and show all organic shop`() {
        val suggestionUniverse = suggestionSingleShopAdsMatchFirstResponse.jsonToObject<SuggestionUniverse>()
        val expectedSize = 2

        `Given Suggestion API will return SuggestionUniverse`(suggestionUniverse, requestParamsSlot)

        `when presenter get suggestion data`()

        `then verify suggestion view will call hideExceedKeywordLimit`()
        `then verify suggestion view will call showSuggestionResult`()
        `Then verify first visitable list is not shop ads`()
        `Then verify shops visitable list size`(expectedSize)
    }

    private fun `Then verify first visitable list is not shop ads`() {
        val suggestionDoubleLineDataView = (visitableList.first() as SuggestionDoubleLineDataDataView)

        suggestionDoubleLineDataView.data.template shouldBe SUGGESTION_DOUBLE_LINE
    }

    @Test
    fun `Suggestion will show main ads and exclude one organic shop`() {
        val suggestionUniverse = suggestionMultipleShopAdsMatchSecondResponse.jsonToObject<SuggestionUniverse>()
        val expectedSize = 2
        val excludedApplink = "tokopedia://shop/2386090?source=universe&st=product"

        `Given Suggestion API will return SuggestionUniverse`(suggestionUniverse, requestParamsSlot)

        `when presenter get suggestion data`()

        `then verify suggestion view will call hideExceedKeywordLimit`()
        `then verify suggestion view will call showSuggestionResult`()
        `Then verify shop ads shown is main ads`(suggestionUniverse)
        `Then verify shops visitable list size`(expectedSize)
        `Then verify shown shops is excluding main ads`(excludedApplink)
    }

    private fun `Then verify shown shops is excluding main ads`(excludedApplink: String) {
        val isExcludedApplinkExists =
            visitableList.any {
                (it as SuggestionDoubleLineDataDataView).data.applink == excludedApplink
            }

        isExcludedApplinkExists shouldBe false
    }

    @Test
    fun `Suggestion will not show any organic shop`() {
        val suggestionUniverse = suggestionMultipleShopAdsMatchBothResponse.jsonToObject<SuggestionUniverse>()
        val expectedSize = 2

        `Given Suggestion API will return SuggestionUniverse`(suggestionUniverse, requestParamsSlot)

        `when presenter get suggestion data`()
        `then verify suggestion view will call hideExceedKeywordLimit`()
        `then verify suggestion view will call showSuggestionResult`()
        `Then verify shop ads shown is sub ads`(suggestionUniverse)
        `Then verify shops visitable list size`(expectedSize)
    }

    @Test
    fun `get suggestion data with empty header title will hide header`() {
        val suggestionUniverse = suggestionEmptyHeaderTitleResponse.jsonToObject<SuggestionUniverse>()
        `Given Suggestion API will return SuggestionUniverse`(suggestionUniverse, requestParamsSlot)

        `when presenter get suggestion data`()

        `then verify suggestion API is called`()
        `then verify suggestion view will call hideExceedKeywordLimit`()
        `then verify suggestion view will call showSuggestionResult`()
        `then verify no suggestion title header`()
    }

    private fun `then verify no suggestion title header`() {
        val visitableList = slotVisitableList.captured
        visitableList.none { it is SuggestionTitleDataView } shouldBe true
    }

    @Test
    fun `get mps suggestion test`() {
        val suggestionUniverse = suggestionCommonResponse.jsonToObject<SuggestionUniverse>()
        `Given Suggestion API will return SuggestionUniverse`(suggestionUniverse, requestParamsSlot)

        `when presenter get suggestion data`(
            searchParameter = mapOf(
                SearchApiConst.ACTIVE_TAB to SearchApiConst.ACTIVE_TAB_MPS,
                SearchApiConst.Q1 to "apple",
                SearchApiConst.Q2 to "samsung",
            ),
            activeKeyword = SearchBarKeyword(
                position = 2,
                keyword = keyword,
            )
        )

        `then verify suggestion API is called`()
        `then verify suggestion view will call hideExceedKeywordLimit`()
        `Then verify search parameter is mps suggestion`(keyword)
    }

    private fun `Then verify search parameter is mps suggestion`(
        expectedQuery : String
    ) {
        val suggestionParams = requestParams.parameters
        suggestionParams[SearchApiConst.ACTIVE_TAB] shouldBe SearchApiConst.ACTIVE_TAB_MPS
        suggestionParams[SearchApiConst.Q] shouldBe expectedQuery
    }

    @Test
    fun `mps exceed keyword limit`() {
        `when presenter get suggestion data`(
            activeKeyword = SearchBarKeyword(
                position = 3,
                keyword = keyword,
            )
        )

        `then verify suggestion API is not called`()
        `then verify suggestion view will call showExceedKeywordLimit`()
        `then verify suggestion view will call hideSuggestionResult`()
    }

}
