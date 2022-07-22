package com.tokopedia.tokofood.merchant

import io.mockk.coEvery
import io.mockk.coVerify
import junit.framework.Assert.assertTrue
import org.junit.Test

class ManageLocationViewModelTest : ManageLocationViewModelTestFixture() {

    @Test
    fun `when updatePinPoint is success expect the boolean result to be true`() {
        coEvery {
            keroEditAddressUseCase.execute("","","")
        } returns true
        viewModel.updatePinPoint("","","")
        val actualResponse = viewModel.updatePinPoint("","","")
        coVerify { keroEditAddressUseCase.execute("","","") }
        assertTrue(actualResponse)
    }

    @Test
    fun `when checkUserEligibilityForAnaRevamp is success expect eligibility data`() {
        eligibleForAddressUseCase
    }

    @Test
    fun `when getChooseAddress is success expect chosen address data`() {
        getChooseAddressWarehouseLocUseCase
    }

    @Test
    fun `when checkDeliveryCoverage is success expect checkDeliveryCoverageResult`() {
        checkDeliveryCoverageUseCase
    }
}