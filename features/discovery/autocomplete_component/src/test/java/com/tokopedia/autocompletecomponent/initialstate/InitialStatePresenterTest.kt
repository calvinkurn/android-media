package com.tokopedia.autocompletecomponent.initialstate

import com.tokopedia.autocompletecomponent.complete
import com.tokopedia.autocompletecomponent.initialstate.data.InitialStateUniverse
import com.tokopedia.autocompletecomponent.initialstate.domain.InitialStateData
import com.tokopedia.autocompletecomponent.initialstate.recentsearch.RecentSearchDataView
import com.tokopedia.autocompletecomponent.jsonToObject
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.autocompletecomponent.shouldBe
import com.tokopedia.autocompletecomponent.shouldNotContain
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.usecase.RequestParams
import io.mockk.every
import io.mockk.slot
import io.mockk.verify
import org.junit.Test
import rx.Subscriber

private const val initialStateWithSeeMoreRecentSearch = "autocomplete/initialstate/with-5-data-show-more-recent-search.json"

internal class InitialStatePresenterTest: InitialStatePresenterTestFixtures() {

    private val searchProductPageTitle = "Waktu Indonesia Belanja"
    private val searchParameter = mapOf(
            SearchApiConst.Q to keyword,
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
    fun `Test initial state presenter has set parameter`() {
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
        `Given getInitialStateUseCase will be successful`(initialStateCommonData)

        `When presenter get initial state data`()

        `Then verify search parameter has warehouseId`(warehouseId)
    }

    private fun `Given chosen address data`(chooseAddressModel: LocalCacheModel?) {
        every { initialStateView.chooseAddressData } returns chooseAddressModel
    }

    private fun `Given getInitialStateUseCase will be successful`(
        initialStateUniverse: InitialStateUniverse
    ) {
        every { getInitialStateUseCase.execute(capture(requestParamsSlot), any()) }.answers {
            secondArg<Subscriber<InitialStateUniverse>>().complete(initialStateUniverse)
        }
    }

    private fun `Then verify search parameter has warehouseId`(warehouseId: String) {
        val requestParams = requestParamsSlot.captured

        requestParams.parameters[SearchApiConst.USER_WAREHOUSE_ID] shouldBe warehouseId
    }

    private fun `Test Initial State Data`(
        initialStateUniverse: InitialStateUniverse,
        searchParameter: Map<String, String> = mapOf(
            SearchApiConst.Q to keyword
        ),
    ) {
        `Given initial state use case will be successful`(initialStateUniverse)
        `When presenter get initial state data`(searchParameter)
        `Then verify initial state API is called`()
    }

    private fun `When presenter get initial state data`(
        searchParameter: Map<String, String> = mapOf(
            SearchApiConst.Q to keyword
        ),
    ) {
        initialStatePresenter.showInitialState(searchParameter)
    }

    private fun `Then verify initial state API is called`() {
        verify { getInitialStateUseCase.execute(any(), any()) }
    }

    @Test
    fun `Test get initial state data without see more button`() {
        `Test Initial State Data`(initialStateCommonData)

        `Then verify initial state view will call showInitialStateResult behavior`()
        `Then verify visitable list`(initialStateCommonData)
    }

    private fun `Then verify initial state view will call showInitialStateResult behavior`() {
        verify {
            initialStateView.showInitialStateResult(capture(slotVisitableList))
        }
    }

    private fun `Then verify visitable list`(initialStateUniverse: InitialStateUniverse) {
        val expectedData = initialStateUniverse.data
        val visitableList = slotVisitableList.captured

        `Then verify RecentViewDataView`(
            visitableList, expectedData, expectedDefaultDimension90, keyword,
        )
        `Then verify RecentSearchDataView`(
            visitableList, expectedData, expectedDefaultDimension90, keyword,
        )
        `Then verify PopularSearchDataView`(
            visitableList, expectedData, expectedDefaultDimension90, keyword,
        )
        `Then verify DynamicInitialStateSearchDataView`(
            visitableList, expectedData, expectedDefaultDimension90, keyword,
        )

        `Then verify RecentSearchDataView only have n items`(
            3, visitableList[3] as RecentSearchDataView
        )
    }

    private fun `Then verify RecentSearchDataView only have n items`(expectedRecentSearchSize: Int, dataView: RecentSearchDataView) {
        assert(dataView.list.size == expectedRecentSearchSize) {
            "RecentSearchDataView should only have $expectedRecentSearchSize items, actual size is ${dataView.list.size}"
        }
    }

    @Test
    fun `Test get initial state data with see more button`() {
        val initialStateData = initialStateWithSeeMoreRecentSearch.jsonToObject<InitialStateUniverse>()
        `Test Initial State Data`(initialStateData)

        `Then verify initial state view will call showInitialStateResult behavior`()
        `Then verify visitable list has RecentSearchSeeMoreDataView`(initialStateData)
    }

    private fun `Then verify visitable list has RecentSearchSeeMoreDataView`(
        initialStateUniverse: InitialStateUniverse
    ) {
        val expectedData = initialStateUniverse.data
        val visitableList = slotVisitableList.captured

        `Then verify CuratedCampaignDataView`(
            visitableList, expectedData, expectedDefaultDimension90, keyword,
        )
        `Then verify RecentViewDataView`(
            visitableList, expectedData, expectedDefaultDimension90, keyword,
        )
        `Then verify RecentSearchDataView`(
            visitableList, expectedData, expectedDefaultDimension90, keyword,
        )
        `Then verify PopularSearchDataView`(
            visitableList, expectedData, expectedDefaultDimension90, keyword,
        )
        `Then verify DynamicInitialStateSearchDataView`(
            visitableList, expectedData, expectedDefaultDimension90, keyword,
        )
        `Then verify InitialStateProductListDataView`(
            visitableList, expectedData, expectedDefaultDimension90, keyword,
        )
        `Then verify InitialStateChipWidgetDataView`(
            visitableList, expectedData, expectedDefaultDimension90, keyword,
        )


        `Then verify RecentSearchDataView only have n items`(3, visitableList[4] as RecentSearchDataView)
    }

    @Test
    fun `Test get local initial state`() {
        `Test Initial State Data`(initialStateCommonData, searchParameter)

        `Then verify initial state view will call showInitialStateResult behavior`()
        `Then verify visitable list`(initialStateCommonData, expectedLocalDimension90)
    }

    private fun `Then verify visitable list`(
        initialStateUniverse: InitialStateUniverse,
        expectedDimension90: String,
    ) {
        val expectedData = initialStateUniverse.data
        val visitableList = slotVisitableList.captured

        `Then verify RecentViewDataView`(
            visitableList, expectedData, expectedDimension90, keyword
        )
        `Then verify RecentSearchDataView`(
            visitableList, expectedData, expectedDimension90, keyword
        )
        `Then verify PopularSearchDataView`(
            visitableList, expectedData, expectedDimension90, keyword
        )
        `Then verify DynamicInitialStateSearchDataView`(
            visitableList, expectedData, expectedDimension90, keyword
        )

        `Then verify RecentSearchDataView only have n items`(
            3, visitableList[3] as RecentSearchDataView
        )
    }

    @Test
    fun `Test fail to get initial state data`() {
        `Given initial state API will return error`()
        `When presenter get initial state data`()
        `Then verify initial state API is called`()
        `Then verify view interaction for load data failed with exception`()
    }

    private fun `Given initial state API will return error`() {
        every { getInitialStateUseCase.execute(any(), any()) }.answers {
            secondArg<Subscriber<List<InitialStateData>>>().onStart()
            secondArg<Subscriber<List<InitialStateData>>>().onError(testException)
        }
    }

    @Test
    fun `Test initial state presenter has set parameter and no warehouseId`() {
        `Given chosen address data`(LocalCacheModel())
        `Given getInitialStateUseCase will be successful`(initialStateCommonData)

        `When presenter get initial state data`()

        `Then verify search parameter has no warehouseId`()
    }

    private fun `Then verify search parameter has no warehouseId`() {
        val requestParams = requestParamsSlot.captured

        requestParams.parameters.shouldNotContain(SearchApiConst.USER_WAREHOUSE_ID)
    }
}