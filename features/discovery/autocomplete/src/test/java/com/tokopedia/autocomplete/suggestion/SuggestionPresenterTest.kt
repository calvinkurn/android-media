package com.tokopedia.autocomplete.suggestion

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.autocomplete.*
import com.tokopedia.autocomplete.shouldBe
import com.tokopedia.autocomplete.shouldBeInstanceOf
import com.tokopedia.autocomplete.suggestion.domain.model.SuggestionItem
import com.tokopedia.autocomplete.suggestion.domain.model.SuggestionTopShop
import com.tokopedia.autocomplete.suggestion.domain.model.SuggestionUniverse
import com.tokopedia.autocomplete.suggestion.doubleline.SuggestionDoubleLineDataDataView
import com.tokopedia.autocomplete.suggestion.doubleline.SuggestionDoubleLineWithoutImageDataDataView
import com.tokopedia.autocomplete.suggestion.productline.SuggestionProductLineDataDataView
import com.tokopedia.autocomplete.suggestion.singleline.SuggestionSingleLineDataDataView
import com.tokopedia.autocomplete.suggestion.title.SuggestionTitleDataView
import com.tokopedia.autocomplete.suggestion.topshop.SuggestionTopShopCardDataView
import com.tokopedia.autocomplete.suggestion.topshop.SuggestionTopShopWidgetDataView
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.usecase.RequestParams
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.slot
import io.mockk.verify
import org.junit.Test
import rx.Subscriber

private const val suggestionCommonResponse = "autocomplete/suggestion/suggestion-common-response.json"
private const val suggestionTopShopResponse = "autocomplete/suggestion/suggestion-top-shop-response.json"
private const val suggestionCampaignResponse = "autocomplete/suggestion/local-global-response.json"
private const val suggestionCampaignAtTopResponse = "autocomplete/suggestion/local-global-at-top-response.json"

internal class SuggestionPresenterTest: SuggestionPresenterTestFixtures() {

    private val searchProductPageTitle = "Waktu Indonesia Belanja"
    private val searchParameter = mapOf(
            SearchApiConst.NAVSOURCE to "clp",
            SearchApiConst.SRP_PAGE_TITLE to searchProductPageTitle,
            SearchApiConst.SRP_PAGE_ID to "1234"
    )
    private val expectedDefaultDimension90 = SearchConstant.CustomDimension.DEFAULT_VALUE_CUSTOM_DIMENSION_90_GLOBAL
    private val expectedLocalDimension90 =
            "${searchParameter[SearchApiConst.SRP_PAGE_TITLE]}.${searchParameter[SearchApiConst.NAVSOURCE]}." +
                    "local_search.${searchParameter[SearchApiConst.SRP_PAGE_ID]}"

    private val requestParamsSlot = slot<RequestParams>()

    @Test
    fun `Test suggestion presenter has set parameter`() {
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
        `Given chosen address data`(dummyChooseAddressData)
        `Given getSuggestionUseCase will be successful`(suggestionCommonResponse.jsonToObject())

        `when presenter get suggestion data (search)`()

        `Then verify search parameter has warehouseId`(warehouseId)
    }

    private fun `Given chosen address data`(chooseAddressModel: LocalCacheModel?) {
        every { suggestionView.chooseAddressData } returns chooseAddressModel
    }

    private fun `Given getSuggestionUseCase will be successful`(suggestionUniverse: SuggestionUniverse) {
        every { getSuggestionUseCase.execute(capture(requestParamsSlot), any()) }.answers {
            secondArg<Subscriber<SuggestionUniverse>>().complete(suggestionUniverse)
        }
    }

    private fun `Then verify search parameter has warehouseId`(warehouseId: String) {
        val requestParams = requestParamsSlot.captured

        requestParams.parameters[SearchApiConst.USER_WAREHOUSE_ID] shouldBe warehouseId
    }

    @Test
    fun `test get suggestion data`() {
        val suggestionUniverse = suggestionCommonResponse.jsonToObject<SuggestionUniverse>()
        `given suggestion use case capture request params`(suggestionUniverse)

        `when presenter get suggestion data (search)`()

        `then verify suggestion API is called`()
        `then verify suggestion view will call showSuggestionResult behavior`()
        `then verify visitable list`(suggestionUniverse)
    }

    private fun `given suggestion use case capture request params`(suggestionUniverse: SuggestionUniverse) {
        every { getSuggestionUseCase.execute(any(), any()) }.answers {
            secondArg<Subscriber<SuggestionUniverse>>().onStart()
            secondArg<Subscriber<SuggestionUniverse>>().onNext(suggestionUniverse)
        }
    }

    private fun `when presenter get suggestion data (search)`() {
        suggestionPresenter.search()
    }

    private fun `then verify suggestion API is called`() {
        verify { getSuggestionUseCase.execute(any(), any()) }
    }

    private fun `then verify suggestion view will call showSuggestionResult behavior`() {
        verify {
            suggestionView.showSuggestionResult(capture(slotVisitableList))
        }
    }

    private fun `then verify visitable list`(suggestionUniverse: SuggestionUniverse) {
        val visitableList = slotVisitableList.captured

        visitableList[0].shouldBeInstanceOf<SuggestionSingleLineDataDataView>()
        visitableList[1].shouldBeInstanceOf<SuggestionSingleLineDataDataView>()
        visitableList[2].shouldBeInstanceOf<SuggestionSingleLineDataDataView>()
        visitableList[3].shouldBeInstanceOf<SuggestionTitleDataView>()
        visitableList[4].shouldBeInstanceOf<SuggestionDoubleLineDataDataView>()
        visitableList[5].shouldBeInstanceOf<SuggestionDoubleLineDataDataView>()
        visitableList[6].shouldBeInstanceOf<SuggestionTitleDataView>()
        visitableList[7].shouldBeInstanceOf<SuggestionDoubleLineDataDataView>()
        visitableList[8].shouldBeInstanceOf<SuggestionDoubleLineDataDataView>()
        visitableList.size shouldBe suggestionUniverse.data.items.size

        assertVisitableListData(visitableList, suggestionUniverse, expectedDefaultDimension90)
    }

    private fun assertVisitableListData(visitableList: List<Visitable<*>>, suggestionUniverse: SuggestionUniverse, dimension90: String = "") {
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
                is BaseSuggestionDataView -> {
                    (visitable as BaseSuggestionDataView).assertBaseSuggestionDataView(expectedItem, dimension90)
                    expectedPosition++
                }
            }
        }
    }

    private fun SuggestionTitleDataView.assertSuggestionTitleDataView(item: SuggestionItem) {
        title shouldBe item.title
    }

    private fun SuggestionTopShopWidgetDataView.assertTopShopWidgetDataView(item: SuggestionItem, listExpectedData: List<SuggestionTopShop>) {
        template shouldBe item.template
        title shouldBe item.title

        listSuggestionTopShopCardData.forEachIndexed { index, suggestionTopShopCardDataView ->
            suggestionTopShopCardDataView.assertSuggestionTopShopCardDataView(listExpectedData[index])
        }
    }

    private fun SuggestionTopShopCardDataView.assertSuggestionTopShopCardDataView(suggestionTopShop: SuggestionTopShop) {
        type shouldBe suggestionTopShop.type
        id shouldBe suggestionTopShop.id
        applink shouldBe suggestionTopShop.applink
        url shouldBe suggestionTopShop.url
        title shouldBe suggestionTopShop.title
        subtitle shouldBe suggestionTopShop.subtitle
        iconTitle shouldBe suggestionTopShop.iconTitle
        iconSubtitle shouldBe suggestionTopShop.iconSubtitle
        urlTracker shouldBe suggestionTopShop.urlTracker
        imageUrl shouldBe suggestionTopShop.imageUrl

        productData.forEachIndexed { index, suggestionTopShopProductDataView ->
            suggestionTopShopProductDataView.imageUrl shouldBe suggestionTopShop.topShopProducts[index].imageUrl
        }
    }

    private fun BaseSuggestionDataView.assertBaseSuggestionDataView(item: SuggestionItem, dimension90: String = "") {
        template shouldBe item.template
        type shouldBe item.type
        applink shouldBe item.applink
        url shouldBe item.url
        title shouldBe item.title
        subtitle shouldBe item.subtitle
        iconTitle shouldBe item.iconTitle
        iconSubtitle shouldBe item.iconSubtitle
        shortcutUrl shouldBe item.shortcutUrl
        shortcutImage shouldBe item.shortcutImage
        imageUrl shouldBe item.imageUrl
        label shouldBe item.label
        labelType shouldBe item.labelType
        urlTracker shouldBe item.urlTracker
        trackingCode shouldBe item.tracking.code
        this.dimension90 shouldBe dimension90
    }

    @Test
    fun `test fail to get suggestion data`() {
        `given suggestion API will return error`()
        `when presenter get suggestion data (search)`()
        `then verify suggestion API is called`()
        `then verify view interaction for load data failed with exception`()
    }

    private fun `given suggestion API will return error`() {
        every { getSuggestionUseCase.execute(any(), any()) }.answers {
            secondArg<Subscriber<SuggestionUniverse>>().onStart()
            secondArg<Subscriber<SuggestionUniverse>>().onError(testException)
        }
    }

    private fun `then verify view interaction for load data failed with exception`() {
        verify {
            suggestionView.chooseAddressData
        }
        confirmVerified(suggestionView)
    }

    @Test
    fun `test get suggestion data with top shop`() {
        val suggestionUniverse = suggestionTopShopResponse.jsonToObject<SuggestionUniverse>()
        `given suggestion use case capture request params with top shop`(suggestionUniverse)

        `when presenter get suggestion data (search)`()

        `then verify suggestion API is called`()
        `then verify suggestion view will call showSuggestionResult behavior`()
        `then verify visitable list with top shop`(suggestionUniverse)
    }

    private fun `given suggestion use case capture request params with top shop`(suggestionUniverse: SuggestionUniverse) {
        every { getSuggestionUseCase.execute(any(), any()) }.answers {
            secondArg<Subscriber<SuggestionUniverse>>().onStart()
            secondArg<Subscriber<SuggestionUniverse>>().onNext(suggestionUniverse)
        }
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
        `Given presenter will return searchParameter`(searchParameter)
        `given suggestion use case capture request params`(suggestionUniverse)

        `when presenter get suggestion data (search)`()

        `then verify suggestion API is called`()
        `then verify suggestion view will call showSuggestionResult behavior`()
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
        `Given presenter will return searchParameter`(searchParameter)
        `given suggestion use case capture request params`(suggestionUniverse)

        `when presenter get suggestion data (search)`()

        `then verify suggestion API is called`()
        `then verify suggestion view will call showSuggestionResult behavior`()
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
    fun `Test suggestion presenter has set parameter and no warehouseId`() {
        `Given chosen address data`(LocalCacheModel())
        `Given getSuggestionUseCase will be successful`(suggestionCommonResponse.jsonToObject())

        `when presenter get suggestion data (search)`()

        `Then verify search parameter has no warehouseId`()
    }

    private fun `Then verify search parameter has no warehouseId`() {
        val requestParams = requestParamsSlot.captured

        requestParams.parameters.shouldNotContain(SearchApiConst.USER_WAREHOUSE_ID)
    }
}