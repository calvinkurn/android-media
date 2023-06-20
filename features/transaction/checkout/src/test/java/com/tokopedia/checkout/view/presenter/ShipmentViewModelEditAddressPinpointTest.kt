package com.tokopedia.checkout.view.presenter

import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticCommon.data.entity.geolocation.autocomplete.LocationPass
import io.mockk.every
import io.mockk.verifyOrder
import org.junit.Test
import rx.Observable

class ShipmentViewModelEditAddressPinpointTest : BaseShipmentViewModelTest() {

    @Test
    fun pinpointSuccess_ShouldRenderEditAddressSuccess() {
        // Given
        viewModel.recipientAddressModel = RecipientAddressModel().apply {
            id = "1"
            addressName = "address 1"
            street = "street 1"
            postalCode = "12345"
            destinationDistrictId = "1"
            cityId = "1"
            provinceId = "1"
            recipientName = "user 1"
            recipientPhoneNumber = "1234567890"
        }

        val latitude = "123"
        val longitude = "456"

        every { editAddressUseCase.createObservable(any()) } returns Observable.just(
            """
            {
                "data": {
                    "is_success": 1
                }
            }
            """.trimIndent()
        )

        // When
        viewModel.editAddressPinpoint(latitude, longitude, LocationPass())

        // Then
        verifyOrder {
            view.showLoading()
            view.setHasRunningApiCall(true)
            view.setHasRunningApiCall(false)
            view.hideLoading()
            view.renderEditAddressSuccess(latitude, longitude)
        }
    }

    @Test
    fun pinpointFailed_ShouldNavigateToSetPinpointWithErrorMessage() {
        // Given
        viewModel.recipientAddressModel = RecipientAddressModel().apply {
            id = "1"
            addressName = "address 1"
            street = "street 1"
            postalCode = "12345"
            destinationDistrictId = "1"
            cityId = "1"
            provinceId = "1"
            recipientName = "user 1"
            recipientPhoneNumber = "1234567890"
        }

        val latitude = "123"
        val longitude = "456"
        val locationPass = LocationPass()

        val errorMessage = "error"

        every { editAddressUseCase.createObservable(any()) } returns Observable.just(
            """
            {
                "data": {
                    "is_success": 0
                },
                "message_error": ["$errorMessage"]
            }
            """.trimIndent()
        )

        // When
        viewModel.editAddressPinpoint(latitude, longitude, locationPass)

        // Then
        verifyOrder {
            view.showLoading()
            view.setHasRunningApiCall(true)
            view.setHasRunningApiCall(false)
            view.hideLoading()
            view.navigateToSetPinpoint(errorMessage, locationPass)
        }
    }

    @Test
    fun pinpointFailedWithoutErrorMessage_ShouldNavigateToSetPinpointWithDefaultErrorMessage() {
        // Given
        viewModel.recipientAddressModel = RecipientAddressModel().apply {
            id = "1"
            addressName = "address 1"
            street = "street 1"
            postalCode = "12345"
            destinationDistrictId = "1"
            cityId = "1"
            provinceId = "1"
            recipientName = "user 1"
            recipientPhoneNumber = "1234567890"
        }

        val latitude = "123"
        val longitude = "456"
        val locationPass = LocationPass()

        val errorMessage = "error"

        every { editAddressUseCase.createObservable(any()) } returns Observable.just(
            """
            {
                "data": {
                    "is_success": 0
                },
                "message_error": []
            }
            """.trimIndent()
        )

        every {
            view.getStringResource(com.tokopedia.abstraction.R.string.default_request_error_unknown)
        } returns errorMessage

        // When
        viewModel.editAddressPinpoint(latitude, longitude, locationPass)

        // Then
        verifyOrder {
            view.showLoading()
            view.setHasRunningApiCall(true)
            view.setHasRunningApiCall(false)
            view.hideLoading()
            view.navigateToSetPinpoint(errorMessage, locationPass)
        }
    }

    @Test
    fun pinpointError_ShouldShowToastError() {
        // Given
        viewModel.recipientAddressModel = RecipientAddressModel().apply {
            id = "1"
            addressName = "address 1"
            street = "street 1"
            postalCode = "12345"
            destinationDistrictId = "1"
            cityId = "1"
            provinceId = "1"
            recipientName = "user 1"
            recipientPhoneNumber = "1234567890"
        }

        val latitude = "123"
        val longitude = "456"

        every { editAddressUseCase.createObservable(any()) } returns Observable.error(Throwable())

        // When
        viewModel.editAddressPinpoint(latitude, longitude, LocationPass())

        // Then
        verifyOrder {
            view.showLoading()
            view.setHasRunningApiCall(true)
            view.setHasRunningApiCall(false)
            view.hideLoading()
            view.showToastError(any())
        }
    }
}
