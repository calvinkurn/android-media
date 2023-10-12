package com.tokopedia.autocompletecomponent.initialstate

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.autocompletecomponent.complete
import com.tokopedia.autocompletecomponent.initialstate.data.InitialStateUniverse
import com.tokopedia.autocompletecomponent.initialstate.domain.InitialStateData
import com.tokopedia.autocompletecomponent.initialstate.recentsearch.RecentSearchDataView
import com.tokopedia.autocompletecomponent.initialstate.recentsearch.RecentSearchTitleDataView
import com.tokopedia.autocompletecomponent.jsonToObject
import com.tokopedia.autocompletecomponent.shouldBe
import com.tokopedia.autocompletecomponent.shouldNotContain
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.USER_ADDRESS_ID
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.USER_CITY_ID
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.USER_DISTRICT_ID
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.USER_LAT
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.USER_LONG
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.USER_POST_CODE
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.USER_WAREHOUSE_ID
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.usecase.RequestParams
import io.mockk.every
import io.mockk.slot
import io.mockk.verify
import org.junit.Test
import rx.Subscriber

private const val initialStateWithSeeMoreRecentSearch = "autocomplete/initialstate/with-5-data-show-more-recent-search.json"
private const val initialStateWithSearchBarEducation = "autocomplete/initialstate/with-searchbar-education.json"
private const val initialStateRecentSearchEmptyHeaderResponse = "autocomplete/initialstate/recent-search-empty-header-response.json"
private const val initialStateMpsEnabledResponse = "autocomplete/initialstate/mps-enabled.json"
private const val initialStateMpsK2KResponse = "autocomplete/initialstate/mps-k2k.json"

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
    private val requestParams by lazy { requestParamsSlot.captured }

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

        `Then verify search parameter has choose address params`(dummyChooseAddressData)
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

    private fun `Then verify search parameter has choose address params`(
        localCacheModel: LocalCacheModel
    ) {
        val initialStateParams = requestParams.parameters
        initialStateParams[USER_LAT] shouldBe localCacheModel.lat
        initialStateParams[USER_LONG] shouldBe localCacheModel.long
        initialStateParams[USER_ADDRESS_ID] shouldBe localCacheModel.address_id
        initialStateParams[USER_CITY_ID] shouldBe localCacheModel.city_id
        initialStateParams[USER_DISTRICT_ID] shouldBe localCacheModel.district_id
        initialStateParams[USER_POST_CODE] shouldBe localCacheModel.postal_code
        initialStateParams[USER_WAREHOUSE_ID] shouldBe localCacheModel.warehouse_id
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
        `Given rollance is off`()
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
    fun `Test initial state presenter has set parameter and no choose address data`() {
        `Given chosen address data`(LocalCacheModel())
        `Given getInitialStateUseCase will be successful`(initialStateCommonData)

        `When presenter get initial state data`()

        `Then verify search parameter has no choose address params`()
    }

    private fun `Then verify search parameter has no choose address params`() {
        val initialStateParams = requestParams.parameters

        initialStateParams.shouldNotContain(USER_LAT)
        initialStateParams.shouldNotContain(USER_LONG)
        initialStateParams.shouldNotContain(USER_ADDRESS_ID)
        initialStateParams.shouldNotContain(USER_CITY_ID)
        initialStateParams.shouldNotContain(USER_DISTRICT_ID)
        initialStateParams.shouldNotContain(USER_POST_CODE)
        initialStateParams.shouldNotContain(USER_WAREHOUSE_ID)
    }

    @Test
    fun `Test initial state with searchbar education`() {
        val initialStateData = initialStateWithSearchBarEducation.jsonToObject<InitialStateUniverse>()
        `Test Initial State Data`(initialStateData)

        `Then verify initial state view will call showInitialStateResult behavior`()
        `Then verify visitable list has SearchBarEducationDataView`(initialStateData)
    }

    private fun `Then verify visitable list has SearchBarEducationDataView`(
        initialStateUniverse: InitialStateUniverse
    ) {
        val expectedData = initialStateUniverse.data
        val visitableList = slotVisitableList.captured

        `Then verify PopularSearchDataView`(
            visitableList, expectedData, expectedDefaultDimension90, keyword,
        )

        `Then verify SearchBarEducationDataView`(
            visitableList, expectedData, expectedDefaultDimension90, keyword,
        )
    }

    @Test
    fun `Test get initial state data with empty header on recent search`() {
        val initialStateData = initialStateRecentSearchEmptyHeaderResponse.jsonToObject<InitialStateUniverse>()
        `Test Initial State Data`(initialStateData)

        `Then verify initial state view will call showInitialStateResult behavior`()
        `Then verify visitable list has no RecentSearchTitleDataView`(initialStateData)
    }

    private fun `Then verify visitable list has no RecentSearchTitleDataView`(
        initialStateUniverse: InitialStateUniverse
    ) {
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

        `Then verify RecentSearchDataView only have n items`(3, visitableList[2] as RecentSearchDataView)
        `Then verify no RecentSearchTitleDataView`(visitableList)
    }

    private fun `Then verify no RecentSearchTitleDataView`(
        actualData: List<Visitable<*>>,
    ) {
        actualData.none { it is RecentSearchTitleDataView } shouldBe true
    }

    @Test
    fun `Test mps enabled initial search`() {
        val initialStateData = initialStateMpsEnabledResponse.jsonToObject<InitialStateUniverse>()
        `Test Initial State Data`(initialStateData)

        `Then verify initial state view will call showInitialStateResult behavior`()
    }

    @Test
    fun `Test mps k2k initial search`() {
        val initialStateData = initialStateMpsK2KResponse.jsonToObject<InitialStateUniverse>()
        `Test Initial State Data`(initialStateData)

        `Then verify initial state view will call showInitialStateResult behavior`()
        `Then verify visitable list has MpsDataView`(initialStateData)
    }


    private fun `Then verify visitable list has MpsDataView`(
        initialStateUniverse: InitialStateUniverse
    ) {
        val expectedData = initialStateUniverse.data
        val visitableList = slotVisitableList.captured

        `Then verify MpsDataView`(
            visitableList, expectedData, expectedDefaultDimension90, keyword,
        )
    }
}
