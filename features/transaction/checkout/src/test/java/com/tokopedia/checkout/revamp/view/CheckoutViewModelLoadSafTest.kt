package com.tokopedia.checkout.revamp.view

import com.tokopedia.checkout.domain.model.cartshipmentform.CartShipmentAddressFormData
import com.tokopedia.checkout.domain.model.cartshipmentform.CartShipmentAddressFormData.Companion.ERROR_CODE_TO_OPEN_ADD_NEW_ADDRESS
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutPageState
import com.tokopedia.logisticCommon.data.response.EligibleForAddressFeature
import com.tokopedia.logisticCommon.data.response.KeroAddrIsEligibleForAddressFeatureData
import io.mockk.coEvery
import io.mockk.every
import org.junit.Assert.assertEquals
import org.junit.Test

class CheckoutViewModelLoadSafTest : BaseCheckoutViewModelTest() {

    @Test
    fun load_SAF_empty_data() {
        // given
        val response = CartShipmentAddressFormData()
        coEvery {
            getShipmentAddressFormV4UseCase.invoke(any())
        } returns response

        // when
        viewModel.loadSAF(false, false, false)

        // then
        assertEquals(CheckoutPageState.EmptyData, viewModel.pageState.value)
    }

    @Test
    fun load_SAF_cache_expired() {
        // given
        val response = CartShipmentAddressFormData(isError = true, isOpenPrerequisiteSite = true, errorMessage = "error Message")
        coEvery {
            getShipmentAddressFormV4UseCase.invoke(any())
        } returns response

        // when
        viewModel.loadSAF(false, false, false)

        // then
        assertEquals(CheckoutPageState.CacheExpired(response.errorMessage), viewModel.pageState.value)
    }

    @Test
    fun load_SAF_no_address() {
        // given
        val response = CartShipmentAddressFormData(isError = false, errorCode = ERROR_CODE_TO_OPEN_ADD_NEW_ADDRESS)
        coEvery {
            getShipmentAddressFormV4UseCase.invoke(any())
        } returns response

        every {
            eligibleForAddressUseCase.get().eligibleForAddressFeature(any(), any(), any())
        } answers {
            (firstArg() as (KeroAddrIsEligibleForAddressFeatureData) -> Unit).invoke(
                KeroAddrIsEligibleForAddressFeatureData(eligibleForRevampAna = EligibleForAddressFeature(true))
            )
        }

        // when
        viewModel.loadSAF(false, false, false)

        // then
        assertEquals(CheckoutPageState.NoAddress(response, true), viewModel.pageState.value)
    }
}
