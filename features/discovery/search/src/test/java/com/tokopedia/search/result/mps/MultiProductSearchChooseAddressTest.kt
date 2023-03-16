package com.tokopedia.search.result.mps

import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.mps.chooseaddress.ChooseAddressDataView
import com.tokopedia.search.result.mps.domain.model.MPSModel
import com.tokopedia.search.shouldBe
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.verify
import org.hamcrest.core.Is.`is`
import org.junit.Assert.assertThat
import org.junit.Test

class MultiProductSearchChooseAddressTest: MultiProductSearchTestFixtures() {

    private val dummyChooseAddressData = LocalCacheModel(
        address_id = "123",
        city_id = "45",
        district_id = "123",
        lat = "10.2131",
        long = "12.01324",
        postal_code = "12345",
        warehouse_id = "2215",
    )

    private val dummyChooseAddressDataUpdated = LocalCacheModel(
        address_id = "125",
        city_id = "11",
        district_id = "999",
        lat = "19.2167",
        long = "17.01374",
        postal_code = "53241",
        warehouse_id = "2256",
    )

    @Test
    fun `on view created will update state with current choose address`() {
        val viewModel = mpsViewModel()

        `Given choose address`(dummyChooseAddressData)

        `When view created`(viewModel)

        `Then verify state is updated with current choose address data`(viewModel)
    }

    private fun `Given choose address`(chooseAddressData: LocalCacheModel) {
        every { chooseAddressWrapper.getChooseAddressData() } returns chooseAddressData
    }

    private fun `When view created`(viewModel: MPSViewModel) {
        viewModel.onViewCreated()
    }

    private fun `Then verify state is updated with current choose address data`(viewModel: MPSViewModel) {
        assertThat(
            viewModel.stateValue.chooseAddressModel,
            `is`(dummyChooseAddressData)
        )
    }

    @Test
    fun `on view created will send param from choose address`() {
        val viewModel = mpsViewModel()

        `Given MPS Use Case success`(MPSSuccessJSON.jsonToObject(), requestParamsSlot)
        `Given choose address`(dummyChooseAddressData)

        `When view created`(viewModel)

        `Then verify parameters contains choose address data`(
            requestParamParameters,
            dummyChooseAddressData
        )
    }

    private fun `Then verify parameters contains choose address data`(
        parameters: Map<String, String>,
        expectedChooseAddressData: LocalCacheModel
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
    fun `on view created success will show choose address`() {
        val viewModel = mpsViewModel()
        `Given MPS Use Case success`(MPSSuccessJSON.jsonToObject())

        `When view created`(viewModel)

        `Then assert visitable list contains choose address`(viewModel)
    }

    private fun `Then assert visitable list contains choose address`(viewModel: MPSViewModel) {
        viewModel.stateValue.result.data
            ?.find { it is ChooseAddressDataView } as? ChooseAddressDataView
            ?: throw AssertionError("Choose Address not found")
    }

    @Test
    fun `on localizing address changed will reload data with latest choose address`() {
        val mpsState = MPSState(chooseAddressModel = dummyChooseAddressData)
        val mpsViewModel = mpsViewModel(mpsState)

        `Given MPS Use Case success`(MPSModel(), requestParamsSlot)
        `Given choose address`(dummyChooseAddressDataUpdated)

        `When localizing address selected`(mpsViewModel)

        `Then verify parameters contains choose address data`(
            requestParamParameters,
            dummyChooseAddressDataUpdated
        )
    }

    private fun `When localizing address selected`(mpsViewModel: MPSViewModel) {
        mpsViewModel.onLocalizingAddressSelected()
    }

    @Test
    fun `on view resumed will check choose address changes and reload data`() {
        val mpsModel = MPSSuccessJSON.jsonToObject<MPSModel>()
        val mpsState = MPSState()
            .chooseAddress(dummyChooseAddressData)
            .success(mpsModel)
        val mpsViewModel = mpsViewModel(mpsState)

        `Given MPS Use Case success`(mpsModel, requestParamsSlot)
        `Given choose address is updated`(dummyChooseAddressDataUpdated)

        `When view resumed`(mpsViewModel)

        `Then verify check choose address updates is called`(dummyChooseAddressData)
        `Then verify parameters contains choose address data`(
            requestParamParameters,
            dummyChooseAddressDataUpdated,
        )
    }

    private fun `Given choose address is updated`(chooseAddressModel: LocalCacheModel) {
        every { chooseAddressWrapper.isChooseAddressUpdated(any()) } returns true
        every { chooseAddressWrapper.getChooseAddressData() } returns chooseAddressModel
    }

    private fun `When view resumed`(mpsViewModel: MPSViewModel) {
        mpsViewModel.onViewResumed()
    }

    private fun `Then verify check choose address updates is called`(
        dummyChooseAddressData: LocalCacheModel
    ) {
        verify(exactly = 1) {
            chooseAddressWrapper.isChooseAddressUpdated(dummyChooseAddressData)
        }
    }

    @Test
    fun `on view resumed will not reload data if choose address not changed`() {
        val mpsModel = MPSSuccessJSON.jsonToObject<MPSModel>()
        val mpsState = MPSState()
            .chooseAddress(dummyChooseAddressData)
            .success(mpsModel)
        val mpsViewModel = mpsViewModel(mpsState)

        `When view resumed`(mpsViewModel)

        `Then verify check choose address updates is called`(dummyChooseAddressData)

        confirmVerified(mpsFirstPageUseCase)
    }
}
