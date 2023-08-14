package com.tokopedia.checkout.revamp.view

import com.tokopedia.checkout.domain.model.cartshipmentform.CartShipmentAddressFormData
import com.tokopedia.checkout.domain.model.changeaddress.SetShippingAddressData
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutPageToaster
import com.tokopedia.localizationchooseaddress.domain.model.ChosenAddressModel
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.unifycomponents.Toaster
import io.mockk.coEvery
import io.mockk.coVerify
import org.junit.Test
import java.io.IOException

class CheckoutViewModelAddressTest : BaseCheckoutViewModelTest() {

    @Test
    fun change_address_failed() {
        // given
        val exception = IOException()
        coEvery {
            changeShippingAddressGqlUseCase.get().invoke(any())
        } throws exception

        // when
        viewModel.changeAddress(RecipientAddressModel(), ChosenAddressModel(), false)

        // then
        coVerify(inverse = true) {
            getShipmentAddressFormV4UseCase.invoke(any())
        }

        coVerify {
            toasterProcessor.commonToaster.emit(CheckoutPageToaster(Toaster.TYPE_ERROR, throwable = exception))
        }
    }

    @Test
    fun change_address_error_with_default_message() {
        // given
        coEvery {
            changeShippingAddressGqlUseCase.get().invoke(any())
        } returns SetShippingAddressData(isSuccess = false, messages = emptyList())

        // when
        viewModel.changeAddress(RecipientAddressModel(), ChosenAddressModel(), false)

        // then
        coVerify(inverse = true) {
            getShipmentAddressFormV4UseCase.invoke(any())
        }

        coVerify {
            toasterProcessor.commonToaster.emit(CheckoutPageToaster(Toaster.TYPE_ERROR, toasterMessage = "Gagal mengubah alamat"))
        }
    }

    @Test
    fun change_address_error() {
        // given
        val errors = listOf("error", "message")
        coEvery {
            changeShippingAddressGqlUseCase.get().invoke(any())
        } returns SetShippingAddressData(isSuccess = false, messages = errors)

        // when
        viewModel.changeAddress(RecipientAddressModel(), ChosenAddressModel(), false)

        // then
        coVerify(inverse = true) {
            getShipmentAddressFormV4UseCase.invoke(any())
        }

        coVerify {
            toasterProcessor.commonToaster.emit(CheckoutPageToaster(Toaster.TYPE_ERROR, toasterMessage = "error message"))
        }
    }

    @Test
    fun change_address_success_with_default_message() {
        // given
        coEvery {
            changeShippingAddressGqlUseCase.get().invoke(any())
        } returns SetShippingAddressData(isSuccess = true, messages = emptyList())

        coEvery {
            getShipmentAddressFormV4UseCase.invoke(any())
        } returns CartShipmentAddressFormData()

        // when
        viewModel.changeAddress(RecipientAddressModel(), ChosenAddressModel(), false)

        // then
        coVerify {
            getShipmentAddressFormV4UseCase.invoke(any())
            toasterProcessor.commonToaster.emit(
                CheckoutPageToaster(
                    Toaster.TYPE_NORMAL,
                    toasterMessage = "Berhasil mengubah alamat"
                )
            )
        }
    }
}
