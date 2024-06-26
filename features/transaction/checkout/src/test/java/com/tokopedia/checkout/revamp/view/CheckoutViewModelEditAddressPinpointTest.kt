package com.tokopedia.checkout.revamp.view

import com.tokopedia.checkout.domain.model.cartshipmentform.CartShipmentAddressFormData
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutAddressModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutButtonPaymentModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutCostModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutCrossSellGroupModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutEpharmacyModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutOrderModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutPageToaster
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutProductModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutPromoModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutTickerErrorModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutTickerModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutUpsellModel
import com.tokopedia.checkout.view.uimodel.ShipmentNewUpsellModel
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticCommon.data.entity.geolocation.autocomplete.LocationPass
import com.tokopedia.logisticCommon.data.response.KeroEditAddressResponse
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.domain.model.UploadPrescriptionUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerAnnouncementHolderData
import com.tokopedia.unifycomponents.Toaster
import io.mockk.coEvery
import io.mockk.coVerify
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.IOException

class CheckoutViewModelEditAddressPinpointTest : BaseCheckoutViewModelTest() {

    @Test
    fun pinpointSuccess_ShouldRenderEditAddressSuccess() {
        // Given
        viewModel.listData.value = listOf(
            CheckoutTickerErrorModel(errorMessage = ""),
            CheckoutTickerModel(ticker = TickerAnnouncementHolderData()),
            CheckoutAddressModel(
                recipientAddressModel = RecipientAddressModel().apply {
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
            ),
            CheckoutUpsellModel(upsell = ShipmentNewUpsellModel()),
            CheckoutProductModel("123"),
            CheckoutOrderModel("123"),
            CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        val latitude = "123"
        val longitude = "456"

        coEvery { pinpointUseCase(any()) } returns KeroEditAddressResponse.Data(
            KeroEditAddressResponse.Data.KeroEditAddress(
                KeroEditAddressResponse.Data.KeroEditAddress.KeroEditAddressSuccessResponse(
                    isSuccess = 1
                )
            )
        )

        coEvery { getShipmentAddressFormV4UseCase(any()) } returns CartShipmentAddressFormData()

        // When
        viewModel.editAddressPinpoint(
            latitude,
            longitude,
            LocationPass()
        ) { errorMessage, locationPass ->
        }

        // Then
        coVerify { getShipmentAddressFormV4UseCase(any()) }
    }

    @Test
    fun pinpointFailedWithoutErrorMessage_ShouldNavigateToSetPinpointWithDefaultErrorMessage() {
        // Given
        viewModel.listData.value = listOf(
            CheckoutTickerErrorModel(errorMessage = ""),
            CheckoutTickerModel(ticker = TickerAnnouncementHolderData()),
            CheckoutAddressModel(
                recipientAddressModel = RecipientAddressModel().apply {
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
            ),
            CheckoutUpsellModel(upsell = ShipmentNewUpsellModel()),
            CheckoutProductModel("123"),
            CheckoutOrderModel("123"),
            CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        val latitude = "123"
        val longitude = "456"
        val locationPass = LocationPass()

        coEvery { pinpointUseCase(any()) } returns KeroEditAddressResponse.Data(
            KeroEditAddressResponse.Data.KeroEditAddress(
                KeroEditAddressResponse.Data.KeroEditAddress.KeroEditAddressSuccessResponse(
                    isSuccess = 0
                )
            )
        )

        var resultErrorMessage = ""

        // When
        viewModel.editAddressPinpoint(
            latitude,
            longitude,
            locationPass
        ) { errorMessage, locationPass ->
            resultErrorMessage = errorMessage
        }

        // Then
        assertEquals("Terjadi kesalahan. Ulangi beberapa saat lagi", resultErrorMessage)
    }

    @Test
    fun pinpointError_ShouldShowToastError() {
        // Given
        viewModel.listData.value = listOf(
            CheckoutTickerErrorModel(errorMessage = ""),
            CheckoutTickerModel(ticker = TickerAnnouncementHolderData()),
            CheckoutAddressModel(
                recipientAddressModel = RecipientAddressModel().apply {
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
            ),
            CheckoutUpsellModel(upsell = ShipmentNewUpsellModel()),
            CheckoutProductModel("123"),
            CheckoutOrderModel("123"),
            CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        val latitude = "123"
        val longitude = "456"

        val throwable = IOException("testing")
        coEvery { pinpointUseCase(any()) } throws throwable

        var resultErrorMessage = ""

        // When
        viewModel.editAddressPinpoint(
            latitude,
            longitude,
            LocationPass()
        ) { errorMessage, locationPass ->
            resultErrorMessage = errorMessage
        }

        // Then
        assertEquals("", resultErrorMessage)
        assertEquals(
            CheckoutPageToaster(
                Toaster.TYPE_ERROR,
                "",
                throwable
            ),
            latestToaster
        )
    }
}
