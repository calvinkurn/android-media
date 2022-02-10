package com.tokopedia.search.result.shop.presentation.viewmodel

import com.tokopedia.discovery.common.State
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.search.result.isExecuted
import com.tokopedia.search.result.shop.presentation.viewmodel.testinstance.searchShopModel
import com.tokopedia.search.result.stubExecute
import com.tokopedia.search.shouldBe
import com.tokopedia.search.shouldNotContain
import com.tokopedia.usecase.RequestParams
import io.mockk.every
import io.mockk.slot
import org.junit.Test

internal class SearchShopChooseAddressTest: SearchShopDataViewTestFixtures() {
    private val requestParamsSlot = slot<RequestParams>()
    private val warehouseId = "2216"
    private val dummyChooseAddressData = LocalCacheModel(
        address_id = "123",
        city_id = "45",
        district_id = "123",
        lat = "10.2131",
        long = "12.01324",
        postal_code = "12345",
        warehouse_id = warehouseId,
    )

    @Test
    fun `Show choose address widget in first page`() {
        `Given search shop API call will be successful with request param`()
        `Given chosen address data`(dummyChooseAddressData)

        `Given already view created`()
        `Given handle view is visible and added`()

        `Then verify parameters contains choose address data`(
            requestParamsSlot.captured.parameters as Map<String, String>,
            dummyChooseAddressData,
        )
        `Then assert search shop state is success and contains choose address model at first index`()
    }

    private fun `Given search shop API call will be successful with request param`() {
        searchShopFirstPageUseCase.stubExecute(requestParamsSlot).returns(searchShopModel)
    }

    private fun `Given chosen address data`(chooseAddressModel: LocalCacheModel?) {
        every { chooseAddressWrapper.getChooseAddressData() } returns chooseAddressModel
    }

    private fun `Given handle view is visible and added`() {
        searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
    }

    private fun `Given already view created`() {
        searchShopViewModel.onViewCreated()
    }

    private fun `Then assert search shop state is success and contains choose address model at first index`() {
        val searchShopState = searchShopViewModel.getSearchShopLiveData().value
        val query = searchShopViewModel.getSearchParameterQuery()

        searchShopState.shouldBeInstanceOf<State.Success<*>>()
        searchShopState.shouldHaveCorrectVisitableListWithLoadingMoreViewModel(query)
        searchShopState.shouldHaveChooseAddressViewModel(0)
    }

    private fun `Then verify parameters contains choose address data`(
        parameters: Map<String, String>, expectedChooseAddressData: LocalCacheModel
    ) {
        parameters[SearchApiConst.USER_LAT] shouldBe expectedChooseAddressData.lat
        parameters[SearchApiConst.USER_LONG] shouldBe expectedChooseAddressData.long
        parameters[SearchApiConst.USER_ADDRESS_ID] shouldBe expectedChooseAddressData.address_id
        parameters[SearchApiConst.USER_CITY_ID] shouldBe expectedChooseAddressData.city_id
        parameters[SearchApiConst.USER_DISTRICT_ID] shouldBe expectedChooseAddressData.district_id
        parameters[SearchApiConst.USER_POST_CODE] shouldBe expectedChooseAddressData.postal_code
        parameters[SearchApiConst.USER_WAREHOUSE_ID] shouldBe expectedChooseAddressData.warehouse_id
    }

    @Test
    fun `Null or empty choose address data will not send choose address parameter`() {
        `Given search shop API call will be successful with request param`()
        `Given chosen address data`(null)

        `Given already view created`()
        `Given handle view is visible and added`()

        `Then verify choose address data is not sent`(requestParamsSlot.captured.parameters as Map<String, String>)
    }

    @Test
    fun `Empty Choose Address data will not send choose address parameter`() {
        `Given search shop API call will be successful with request param`()
        `Given chosen address data`(LocalCacheModel())

        `Given already view created`()
        `Given handle view is visible and added`()

        `Then verify choose address data is not sent`(requestParamsSlot.captured.parameters as Map<String, String>)
    }

    private fun `Then verify choose address data is not sent`(parameters: Map<String, String>) {
        parameters.shouldNotContain(SearchApiConst.USER_LAT)
        parameters.shouldNotContain(SearchApiConst.USER_LONG)
        parameters.shouldNotContain(SearchApiConst.USER_ADDRESS_ID)
        parameters.shouldNotContain(SearchApiConst.USER_CITY_ID)
        parameters.shouldNotContain(SearchApiConst.USER_DISTRICT_ID)
        parameters.shouldNotContain(SearchApiConst.USER_POST_CODE)
        parameters.shouldNotContain(SearchApiConst.USER_WAREHOUSE_ID)
    }

    @Test
    fun `Reload search page after choosing new address`() {
        val newChooseAddressData = LocalCacheModel(
            address_id = "125",
            city_id = "11",
            district_id = "999",
            lat = "19.2167",
            long = "17.01374",
            postal_code = "53241",
            warehouse_id = warehouseId,
        )

        `Given search shop API call will be successful with request param`()
        `Given chosen address data`(dummyChooseAddressData)

        `Given already view created`()
        `Given handle view is visible and added`()

        `Given chosen address data`(newChooseAddressData)

        `Given chosen address data has changed`()

        `Then verify view will fetch new chosen address and reload data`()

        `Then verify parameters contains choose address data`(
            requestParamsSlot.captured.parameters as Map<String, String>,
            newChooseAddressData,
        )
    }

    private fun `Given chosen address data has changed`() {
        searchShopViewModel.onLocalizingAddressSelected()
    }

    private fun `Then verify view will fetch new chosen address and reload data`() {
        searchShopFirstPageUseCase.isExecuted(2)
    }

    @Test
    fun `Choose address data has changed on view resume`() {
        val newChooseAddressData = LocalCacheModel(
            address_id = "125",
            city_id = "11",
            district_id = "999",
            lat = "19.2167",
            long = "17.01374",
            postal_code = "53241",
            warehouse_id = warehouseId,
        )

        `Given search shop API call will be successful with request param`()
        `Given chosen address data`(dummyChooseAddressData)

        `Given already view created`()
        `Given handle view is visible and added`()

        `Given chosen address data`(newChooseAddressData)
        `Given choose address data has updated`()

        `Given view is already resumed`()

        `Then verify view will fetch new chosen address and reload data`()

        `Then verify parameters contains choose address data`(
            requestParamsSlot.captured.parameters as Map<String, String>,
            newChooseAddressData,
        )
    }

    private fun `Given choose address data has updated`() {
        every { chooseAddressWrapper.isChooseAddressUpdated(dummyChooseAddressData) } returns true
    }

    @Test
    fun `Choose address data does not change on view resume`() {
        `Given search shop API call will be successful with request param`()
        `Given chosen address data`(dummyChooseAddressData)

        `Given already view created`()
        `Given handle view is visible and added`()

        `Given view is already resumed`()

        `Then verify fetch data only once`()
    }

    private fun `Given view is already resumed`() {
        searchShopViewModel.onViewResume()
    }

    private fun `Then verify fetch data only once`() {
        searchShopFirstPageUseCase.isExecuted(1)
    }
}