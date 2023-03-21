package com.tokopedia.checkout.view.presenter

import com.tokopedia.akamai_bot_lib.exception.AkamaiErrorException
import com.tokopedia.checkout.domain.model.changeaddress.SetShippingAddressData
import com.tokopedia.localizationchooseaddress.domain.model.ChosenAddressModel
import com.tokopedia.logisticCommon.data.entity.address.LocationDataModel
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticcart.shipping.model.CartItemModel
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel
import io.mockk.every
import io.mockk.verifySequence
import org.junit.Test
import rx.Observable

class ShipmentPresenterChangeShippingAddressTest : BaseShipmentPresenterTest() {

    @Test
    fun `WHEN change shipping address for normal checkout flow success THEN should render success`() {
        // Given
        val chosenAddress = ChosenAddressModel(addressId = 123)
        val recipientAddressModel = RecipientAddressModel().apply {
            id = "1"
        }
        presenter.shipmentCartItemModelList = ArrayList<ShipmentCartItemModel>().apply {
            add(
                ShipmentCartItemModel().apply {
                    cartItemModels = ArrayList<CartItemModel>().apply {
                        add(
                            CartItemModel().apply {
                                quantity = 1
                                productId = 1
                                noteToSeller = "note"
                                cartId = 123
                            }
                        )
                    }
                }
            )
        }
        every { changeShippingAddressGqlUseCase.createObservable(any()) } returns Observable.just(
            SetShippingAddressData(isSuccess = true)
        )

        // When
        presenter.changeShippingAddress(
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
            view.activityContext
            view.showToastNormal(any())
            view.renderChangeAddressSuccess(true)
        }
    }

    @Test
    fun changeShippingAddressFailed_ShouldShowError() {
        // Given
        val chosenAddress = ChosenAddressModel(addressId = 123)
        every { changeShippingAddressGqlUseCase.createObservable(any()) } returns Observable.just(
            SetShippingAddressData(isSuccess = false)
        )

        // When
        presenter.changeShippingAddress(
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
            view.activityContext
            view.showToastError(any())
            view.renderChangeAddressFailed(any())
        }
    }

    @Test
    fun changeShippingAddressError_ShouldShowError() {
        // Given
        val chosenAddress = ChosenAddressModel(addressId = 123)
        every { changeShippingAddressGqlUseCase.createObservable(any()) } returns Observable.error(
            Throwable()
        )

        // When
        presenter.changeShippingAddress(
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
            view.activityContext
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
        presenter.shipmentCartItemModelList = ArrayList<ShipmentCartItemModel>().apply {
            add(
                ShipmentCartItemModel().apply {
                    cartItemModels = ArrayList<CartItemModel>().apply {
                        add(
                            CartItemModel().apply {
                                quantity = 1
                                productId = 1
                                noteToSeller = "note"
                                cartId = 123
                            }
                        )
                    }
                }
            )
        }
        every { changeShippingAddressGqlUseCase.createObservable(any()) } returns Observable.just(
            SetShippingAddressData(isSuccess = true)
        )

        // When
        presenter.changeShippingAddress(
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
            view.activityContext
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
        every { changeShippingAddressGqlUseCase.createObservable(any()) } returns Observable.just(
            SetShippingAddressData(isSuccess = false, messages = errorMessages)
        )

        // When
        presenter.changeShippingAddress(
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
        every { changeShippingAddressGqlUseCase.createObservable(any()) } returns Observable.error(
            AkamaiErrorException("error")
        )

        // When
        presenter.changeShippingAddress(
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
