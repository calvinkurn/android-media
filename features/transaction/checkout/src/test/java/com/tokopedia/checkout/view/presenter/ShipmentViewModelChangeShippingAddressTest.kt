package com.tokopedia.checkout.view.presenter

import com.tokopedia.akamai_bot_lib.exception.AkamaiErrorException
import com.tokopedia.checkout.domain.model.changeaddress.SetShippingAddressData
import com.tokopedia.localizationchooseaddress.domain.model.ChosenAddressModel
import com.tokopedia.logisticCommon.data.entity.address.LocationDataModel
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticcart.shipping.model.CartItemModel
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel
import io.mockk.coEvery
import io.mockk.verifySequence
import org.junit.Test

class ShipmentViewModelChangeShippingAddressTest : BaseShipmentViewModelTest() {

    @Test
    fun `WHEN change shipping address for normal checkout flow success THEN should render success`() {
        // Given
        val chosenAddress = ChosenAddressModel(addressId = 123)
        val recipientAddressModel = RecipientAddressModel().apply {
            id = "1"
        }
        viewModel.shipmentCartItemModelList = ArrayList<ShipmentCartItemModel>().apply {
            add(
                ShipmentCartItemModel(cartStringGroup = "").apply {
                    cartItemModels = ArrayList<CartItemModel>().apply {
                        add(
                            CartItemModel(
                                cartStringGroup = "",
                                quantity = 1,
                                productId = 1,
                                noteToSeller = "note",
                                cartId = 123
                            )
                        )
                    }
                }
            )
        }
        coEvery { changeShippingAddressGqlUseCase(any()) } returns SetShippingAddressData(isSuccess = true)

        // When
        viewModel.changeShippingAddress(
            recipientAddressModel,
            chosenAddress,
            false,
            false,
            true,
            true
        )

        // Then
        verifySequence {
            view.showLoading()
            view.setHasRunningApiCall(true)
            view.hideLoading()
            view.setHasRunningApiCall(false)
            view.getStringResource(any())
            view.showToastNormal(any())
            view.renderChangeAddressSuccess(true)
        }
    }

    @Test
    fun changeShippingAddressFailed_ShouldShowError() {
        // Given
        val chosenAddress = ChosenAddressModel(addressId = 123)
        coEvery { changeShippingAddressGqlUseCase(any()) } returns SetShippingAddressData(isSuccess = false)

        // When
        viewModel.changeShippingAddress(
            RecipientAddressModel(),
            chosenAddress,
            false,
            false,
            true,
            true
        )

        // Then
        verifySequence {
            view.showLoading()
            view.setHasRunningApiCall(true)
            view.hideLoading()
            view.setHasRunningApiCall(false)
            view.getStringResource(any())
            view.showToastError(any())
            view.renderChangeAddressFailed(any())
        }
    }

    @Test
    fun changeShippingAddressError_ShouldShowError() {
        // Given
        val chosenAddress = ChosenAddressModel(addressId = 123)
        coEvery { changeShippingAddressGqlUseCase(any()) } throws Throwable()

        // When
        viewModel.changeShippingAddress(
            RecipientAddressModel(),
            chosenAddress,
            false,
            false,
            true,
            true
        )

        // Then
        verifySequence {
            view.showLoading()
            view.setHasRunningApiCall(true)
            view.hideLoading()
            view.setHasRunningApiCall(false)
            view.activity
            view.showToastError(any())
            view.renderChangeAddressFailed(any())
        }
    }

    @Test
    fun `WHEN change address for trade in indopaket flow success THEN should render success`() {
        // Given
        val chosenAddress = null
        val recipientAddressModel = RecipientAddressModel().apply {
            id = "1"
            locationDataModel = LocationDataModel().apply {
                addrId = "1"
            }
        }
        viewModel.shipmentCartItemModelList = ArrayList<ShipmentCartItemModel>().apply {
            add(
                ShipmentCartItemModel(cartStringGroup = "").apply {
                    cartItemModels = ArrayList<CartItemModel>().apply {
                        add(
                            CartItemModel(
                                cartStringGroup = "",
                                quantity = 1,
                                productId = 1,
                                noteToSeller = "note",
                                cartId = 123
                            )
                        )
                    }
                }
            )
        }
        coEvery { changeShippingAddressGqlUseCase(any()) } returns SetShippingAddressData(isSuccess = true)

        // When
        viewModel.changeShippingAddress(
            recipientAddressModel,
            chosenAddress,
            false,
            true,
            true,
            true
        )

        // Then
        verifySequence {
            view.showLoading()
            view.setHasRunningApiCall(true)
            view.hideLoading()
            view.setHasRunningApiCall(false)
            view.getStringResource(any())
            view.showToastNormal(any())
            view.renderChangeAddressSuccess(true)
        }
    }

    @Test
    fun `WHEN change shipping address error with error message THEN should show error message`() {
        // Given
        val chosenAddress = ChosenAddressModel(addressId = 123)
        val errorMessages = ArrayList<String>().apply {
            add("Error Message")
        }
        coEvery { changeShippingAddressGqlUseCase(any()) } returns SetShippingAddressData(isSuccess = false, messages = errorMessages)

        // When
        viewModel.changeShippingAddress(
            RecipientAddressModel(),
            chosenAddress,
            false,
            false,
            true,
            true
        )

        // Then
        verifySequence {
            view.showLoading()
            view.setHasRunningApiCall(true)
            view.hideLoading()
            view.setHasRunningApiCall(false)
            view.showToastError(any())
            view.renderChangeAddressFailed(any())
        }
    }

    @Test
    fun `WHEN change shipping address get akamai error THEN should show error message`() {
        // Given
        val chosenAddress = ChosenAddressModel(addressId = 123)
        coEvery { changeShippingAddressGqlUseCase(any()) } throws AkamaiErrorException("error")

        // When
        viewModel.changeShippingAddress(
            RecipientAddressModel(),
            chosenAddress,
            false,
            false,
            true,
            true
        )

        // Then
        verifySequence {
            view.showLoading()
            view.setHasRunningApiCall(true)
            view.hideLoading()
            view.setHasRunningApiCall(false)
            view.showToastError(any())
            view.renderChangeAddressFailed(any())
        }
    }
}
