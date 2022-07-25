package com.tokopedia.tokofood.merchant

import com.tokopedia.localizationchooseaddress.domain.response.GetStateChosenAddressResponse
import com.tokopedia.logisticCommon.data.response.KeroAddrIsEligibleForAddressFeatureData
import com.tokopedia.tokofood.data.*
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import junit.framework.Assert.assertEquals
import org.junit.Assert
import org.junit.Test

class ManageLocationViewModelTest : ManageLocationViewModelTestFixture() {

    @Test
    fun `when updatePinPoint is success expect the boolean result to be true`() {
        coEvery {
            keroEditAddressUseCase.execute("", "", "")
        } returns generateTestKeroEditAddressResponse().keroEditAddress.data.isEditSuccess()
        viewModel.updatePinPoint("", "", "")
        val expectedResult = generateTestKeroEditAddressResponse()
        viewModel.updatePinPoint("", "", "")
        coVerify { keroEditAddressUseCase.execute("", "", "") }
        assertEquals(expectedResult.keroEditAddress.data.isEditSuccess(), viewModel.updatePinPointState.value)
    }

    @Test
    fun `when updatePinPoint is failed expect error message`() {
        coEvery {
            keroEditAddressUseCase.execute("", "", "")
        } throws Throwable("error_message")
        viewModel.updatePinPoint("", "", "")
        val actualResponse = viewModel.errorMessage.value
        assertEquals(actualResponse, "error_message")
    }

    @Test
    fun `when checkUserEligibilityForAnaRevamp is success expect eligibility data`() {
        coEvery {
            eligibleForAddressUseCase.eligibleForAddressFeature(any(), any(), any())
        } answers {
            val testData = generateTestKeroAddrIsEligibleForAddressFeature()
            firstArg<(KeroAddrIsEligibleForAddressFeatureData) -> Unit>().invoke(testData.data)
        }
        val expectedResult = generateTestKeroAddrIsEligibleForAddressFeature()
        viewModel.checkUserEligibilityForAnaRevamp()
        val actualResult = viewModel.eligibleForAnaRevamp.value
        Assert.assertEquals(expectedResult.data.eligibleForRevampAna.eligible, (actualResult as Success).data.eligible)
    }

    @Test
    fun `when checkUserEligibilityForAnaRevamp is failed expect fail response`() {
        coEvery {
            eligibleForAddressUseCase.eligibleForAddressFeature(any(), any(), any())
        } answers {
            secondArg<(Throwable) -> Unit>().invoke(Throwable())
        }
        viewModel.checkUserEligibilityForAnaRevamp()
        val actualResult = viewModel.eligibleForAnaRevamp.value
        Assert.assertTrue(actualResult is Fail)
    }

    @Test
    fun `when getChooseAddress is success expect chosen address data`() {
        coEvery {
            getChooseAddressWarehouseLocUseCase.getStateChosenAddress(any(), any(), any())
        } answers {
            val testData = generateTestGetStateChosenAddressQglResponse()
            firstArg<(GetStateChosenAddressResponse) -> Unit>().invoke(testData.response)
        }
        val expectedResult = generateTestGetStateChosenAddressQglResponse()
        viewModel.getChooseAddress("tokofood")
        val actualResult = viewModel.chooseAddress.value
        Assert.assertEquals(expectedResult.response, (actualResult as Success).data)
    }

    @Test
    fun `when getChooseAddress is success expect fail response`() {
        coEvery {
            getChooseAddressWarehouseLocUseCase.getStateChosenAddress(any(), any(), any())
        } answers {
            secondArg<(Throwable) -> Unit>().invoke(Throwable())
        }
        viewModel.getChooseAddress("tokofood")
        val actualResult = viewModel.chooseAddress.value
        Assert.assertTrue(actualResult is Fail)
    }

    @Test
    fun `when checkDeliveryCoverage is success expect checkDeliveryCoverageResult`() {
        coEvery {
            checkDeliveryCoverageUseCase.executeOnBackground()
        } returns generateTestDeliveryCoverageResult()
        viewModel.checkDeliveryCoverage("merchantId","latlong","timezone")
        val expectedResponse = generateTestDeliveryCoverageResult()
        val actualResponse = viewModel.checkDeliveryCoverageResult.value
        Assert.assertEquals(expectedResponse, (actualResponse as Success).data)
    }

    @Test
    fun `when checkDeliveryCoverage is failed expect fail response`() {
        coEvery {
            checkDeliveryCoverageUseCase.executeOnBackground()
        } throws Throwable()
        viewModel.checkDeliveryCoverage("merchantId","latlong","timezone")
        coVerify { checkDeliveryCoverageUseCase.executeOnBackground() }
        val actualResponse = viewModel.checkDeliveryCoverageResult.value
        assert(actualResponse is Fail)
    }
}