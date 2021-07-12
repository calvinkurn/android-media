package com.tokopedia.autocomplete.initialstate

import com.tokopedia.autocomplete.complete
import com.tokopedia.autocomplete.initialstate.curatedcampaign.CuratedCampaignDataView
import com.tokopedia.autocomplete.initialstate.data.InitialStateUniverse
import com.tokopedia.autocomplete.initialstate.recentsearch.RecentSearchDataView
import com.tokopedia.autocomplete.jsonToObject
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.autocomplete.shouldBe
import com.tokopedia.autocomplete.shouldNotContain
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

    private fun `Given getInitialStateUseCase will be successful`(list: List<InitialStateData>) {
        every { getInitialStateUseCase.execute(capture(requestParamsSlot), any()) }.answers {
            secondArg<Subscriber<List<InitialStateData>>>().complete(list)
        }
    }

    private fun `Then verify search parameter has warehouseId`(warehouseId: String) {
        val requestParams = requestParamsSlot.captured

        requestParams.parameters[SearchApiConst.USER_WAREHOUSE_ID] shouldBe warehouseId
    }

    private fun `Test Initial State Data`(list: List<InitialStateData>) {
        `Given initial state use case capture request params`(list)
        `When presenter get initial state data`()
        `Then verify initial state API is called`()
    }

    private fun `When presenter get initial state data`() {
        initialStatePresenter.getInitialStateData()
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

    private fun `Then verify visitable list`(expectedData: List<InitialStateData>) {
        val visitableList = slotVisitableList.captured

        `Then verify RecentViewDataView`(visitableList, expectedData, expectedDefaultDimension90)
        `Then verify RecentSearchDataView`(visitableList, expectedData, expectedDefaultDimension90)
        `Then verify PopularSearchDataView`(visitableList, expectedData, expectedDefaultDimension90)
        `Then verify DynamicInitialStateSearchDataView`(visitableList, expectedData, expectedDefaultDimension90)

        `Then verify RecentSearchDataView only have n items`(3, visitableList[3] as RecentSearchDataView)
    }

    private fun `Then verify RecentSearchDataView only have n items`(expectedRecentSearchSize: Int, dataView: RecentSearchDataView) {
        assert(dataView.list.size == expectedRecentSearchSize) {
            "RecentSearchDataView should only have $expectedRecentSearchSize items, actual size is ${dataView.list.size}"
        }
    }

    @Test
    fun `Test get initial state data with see more button`() {
        val initialStateData = initialStateWithSeeMoreRecentSearch.jsonToObject<InitialStateUniverse>().data
        `Test Initial State Data`(initialStateData)

        `Then verify initial state view will call showInitialStateResult behavior`()
        `Then verify visitable list has RecentSearchSeeMoreDataView`(initialStateData)
    }

    private fun `Then verify visitable list has RecentSearchSeeMoreDataView`(expectedData: List<InitialStateData>) {
        val visitableList = slotVisitableList.captured

        `Then verify CuratedCampaignDataView`(visitableList, expectedData)
        `Then verify RecentViewDataView`(visitableList, expectedData, expectedDefaultDimension90)
        `Then verify RecentSearchDataView`(visitableList, expectedData, expectedDefaultDimension90)
        `Then verify PopularSearchDataView`(visitableList, expectedData, expectedDefaultDimension90)
        `Then verify DynamicInitialStateSearchDataView`(visitableList, expectedData, expectedDefaultDimension90)
        `Then verify InitialStateProductListDataView`(visitableList, expectedData, expectedDefaultDimension90)

        `Then verify RecentSearchDataView only have n items`(3, visitableList[4] as RecentSearchDataView)
    }

    @Test
    fun `Test get local initial state`() {
        `Given presenter will return searchParameter`(searchParameter)

        `Test Initial State Data`(initialStateCommonData)

        `Then verify initial state view will call showInitialStateResult behavior`()
        `Then verify visitable list`(initialStateCommonData, expectedLocalDimension90)
    }

    private fun `Then verify visitable list`(expectedData: List<InitialStateData>, expectedDimension90: String) {
        val visitableList = slotVisitableList.captured

        `Then verify RecentViewDataView`(visitableList, expectedData, expectedDimension90)
        `Then verify RecentSearchDataView`(visitableList, expectedData, expectedDimension90)
        `Then verify PopularSearchDataView`(visitableList, expectedData, expectedDimension90)
        `Then verify DynamicInitialStateSearchDataView`(visitableList, expectedData, expectedDimension90)

        `Then verify RecentSearchDataView only have n items`(3, visitableList[3] as RecentSearchDataView)
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