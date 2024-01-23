package com.tokopedia.tokofood.merchant

import com.tokopedia.logisticCommon.data.constant.ManageAddressSource
import com.tokopedia.logisticCommon.domain.param.KeroEditAddressParam
import com.tokopedia.tokofood.data.createKeroEditAddressResponse
import com.tokopedia.tokofood.data.createKeroEditAddressResponseFail
import com.tokopedia.tokofood.data.generateTestDeliveryCoverageResult
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import junit.framework.Assert.assertEquals
import org.junit.Assert
import org.junit.Test

class ManageLocationViewModelTest : ManageLocationViewModelTestFixture() {

    @Test
    fun `when merchantId value changed, should reflect to view model value`() {
        val expectedMerchantId = "123"

        viewModel.merchantId = expectedMerchantId

        assertEquals(expectedMerchantId, viewModel.merchantId)
    }

    @Test
    fun `when updatePinPoint is success expect the boolean result to be true`() {
        val latitude = "111"
        val longitude = "112"
        coEvery {
            keroEditAddressUseCase(KeroEditAddressParam("", latitude, longitude, ManageAddressSource.TOKOFOOD))
        } returns createKeroEditAddressResponse(latitude, longitude).keroEditAddress.data
        viewModel.updatePinPoint("", latitude, longitude)
        coVerify { keroEditAddressUseCase(KeroEditAddressParam("", latitude, longitude, ManageAddressSource.TOKOFOOD)) }
        assertEquals(viewModel.updatePinPointState.value?.isSuccess, 1)
    }

    @Test
    fun `when updatePinPoint is not success expect the boolean result to be false`() {
        coEvery {
            keroEditAddressUseCase(KeroEditAddressParam("", "", "", ManageAddressSource.TOKOFOOD))
        } returns createKeroEditAddressResponseFail().keroEditAddress.data
        viewModel.updatePinPoint("", "", "")
        viewModel.updatePinPoint("", "", "")
        coVerify { keroEditAddressUseCase(KeroEditAddressParam("", "", "", ManageAddressSource.TOKOFOOD)) }
        assertEquals(viewModel.updatePinPointState.value?.isSuccess, 0)
    }

    @Test
    fun `when updatePinPoint is failed expect error message`() {
        coEvery {
            keroEditAddressUseCase(KeroEditAddressParam("", "", "", ManageAddressSource.TOKOFOOD))
        } throws Throwable("error_message")
        viewModel.updatePinPoint("", "", "")
        val actualResponse = viewModel.errorMessage.value
        assertEquals(actualResponse, "error_message")
    }

    @Test
    fun `when checkDeliveryCoverage is success expect checkDeliveryCoverageResult`() {
        coEvery {
            checkDeliveryCoverageUseCase.executeOnBackground()
        } returns generateTestDeliveryCoverageResult()
        viewModel.checkDeliveryCoverage("merchantId", "latlong", "timezone")
        val expectedResponse = generateTestDeliveryCoverageResult()
        val actualResponse = viewModel.checkDeliveryCoverageResult.value
        Assert.assertEquals(expectedResponse, (actualResponse as Success).data)
    }

    @Test
    fun `when checkDeliveryCoverage is failed expect fail response`() {
        coEvery {
            checkDeliveryCoverageUseCase.executeOnBackground()
        } throws Throwable()
        viewModel.checkDeliveryCoverage("merchantId", "latlong", "timezone")
        coVerify { checkDeliveryCoverageUseCase.executeOnBackground() }
        val actualResponse = viewModel.checkDeliveryCoverageResult.value
        assert(actualResponse is Fail)
    }
}
