package com.tokopedia.autocompletecomponent.suggestion

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.autocompletecomponent.jsonToObject
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
private const val suggestionTopShopResponse = "autocomplete/suggestion/suggestion-top-shop-response.json"
private const val suggestionCampaignResponse = "autocomplete/suggestion/local-global-response.json"
private const val suggestionCampaignAtTopResponse = "autocomplete/suggestion/local-global-at-top-response.json"
private const val suggestionShopAdsResponse = "autocomplete/suggestion/shopads/suggestion-shop-ads-response.json"
private const val suggestionWithoutShopAdsResponse = "autocomplete/suggestion/shopads/suggestion-without-shop-ads-response.json"
private const val suggestionWithoutShopAdsTemplateResponse = "autocomplete/suggestion/shopads/suggestion-shop-ads-without-template.json"

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
        `then verify suggestion view will call showSuggestionResult`()
        `then verify visitable list`(suggestionUniverse)
    }

    private fun `when presenter get suggestion data`(
        searchParameter: Map<String, String> = mapOf(
            SearchApiConst.Q to keyword,
        ),
    ) {
        suggestionPresenter.getSuggestion(searchParameter)
    }

    private fun `then verify suggestion API is called`() {
        verify { getSuggestionUseCase.execute(any(), any(), any()) }
    }

    private fun `then verify suggestion view will call showSuggestionResult`() {
        verify {
            suggestionView.showSuggestionResult(capture(slotVisitableList))
        }
    }

    private fun `then verify visitable list`(suggestionUniverse: SuggestionUniverse) {
        val visitableList = slotVisitableList.captured

        visitableList[0].shouldBeSuggestionDoubleLineDataView(false)
        visitableList[1].shouldBeSuggestionSingleLineDataDataView()
        visitableList[2].shouldBeSuggestionSingleLineDataDataView()
        visitableList[3].shouldBeSuggestionSingleLineDataDataView()
        visitableList[4].shouldBeSuggestionTitleDataView()
        visitableList[5].shouldBeSuggestionDoubleLineDataView(true)
        visitableList[6].shouldBeSuggestionDoubleLineDataView(true)
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
        }
        confirmVerified(suggestionView)
    }

    @Test
    fun `test get suggestion data with top shop`() {
        val suggestionUniverse = suggestionTopShopResponse.jsonToObject<SuggestionUniverse>()
        `Given Suggestion API will return SuggestionUniverse`(suggestionUniverse)

        `when presenter get suggestion data`()

        `then verify suggestion API is called`()
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
    fun `Suggestion request will include warehouse id if exists`() {
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

        `Then verify search parameter has warehouseId`(warehouseId)
    }

    private fun `Given chosen address data`(chooseAddressModel: LocalCacheModel?) {
        every { suggestionView.chooseAddressData } returns chooseAddressModel
    }

    private fun `Then verify search parameter has warehouseId`(warehouseId: String) {
        requestParams.parameters[SearchApiConst.USER_WAREHOUSE_ID] shouldBe warehouseId
    }

    @Test
    fun `Suggestion request will not include warehouse id if not exists`() {
        val suggestionUniverse = suggestionCommonResponse.jsonToObject<SuggestionUniverse>()

        `Given chosen address data`(LocalCacheModel())
        `Given Suggestion API will return SuggestionUniverse`(suggestionUniverse, requestParamsSlot)

        `when presenter get suggestion data`()

        `Then verify search parameter has no warehouseId`()
    }

    private fun `Then verify search parameter has no warehouseId`() {
        requestParams.parameters.shouldNotContain(SearchApiConst.USER_WAREHOUSE_ID)
    }

    @Test
    fun `Suggestion will show shop ads if both template and shop ads GQL is successful`() {
        val suggestionUniverse = suggestionShopAdsResponse.jsonToObject<SuggestionUniverse>()
        `Given Suggestion API will return SuggestionUniverse`(suggestionUniverse, requestParamsSlot)

        `when presenter get suggestion data`()

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

        `then verify suggestion view will call showSuggestionResult`()
        `then verify visitable list is empty`()
    }
}