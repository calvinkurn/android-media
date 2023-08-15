package com.tokopedia.checkout.revamp.view

import com.tokopedia.checkout.data.model.response.releasebookingstock.ReleaseBookingResponse
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutAddressModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutButtonPaymentModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutCostModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutCrossSellGroupModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutEpharmacyModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutOrderModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutProductModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutPromoModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutTickerErrorModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutTickerModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutUpsellModel
import com.tokopedia.checkout.view.uimodel.ShipmentNewUpsellModel
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.domain.model.UploadPrescriptionUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerAnnouncementHolderData
import io.mockk.coEvery
import io.mockk.coVerify
import org.junit.Test
import java.io.IOException

class CheckoutViewModelReleaseBookingTest : BaseCheckoutViewModelTest() {

    @Test
    fun `WHEN release booking in revamp page THEN should hit release booking use case with first productId`() {
        // Given
        coEvery { releaseBookingUseCase.get().invoke(any()) } returns ReleaseBookingResponse()
        val productId = 300L
        viewModel.listData.value = listOf(
            CheckoutTickerErrorModel(errorMessage = ""),
            CheckoutTickerModel(ticker = TickerAnnouncementHolderData()),
            CheckoutAddressModel(recipientAddressModel = RecipientAddressModel()),
            CheckoutUpsellModel(upsell = ShipmentNewUpsellModel()),
            CheckoutProductModel("123", productId = productId),
            CheckoutProductModel("123", productId = productId + 1),
            CheckoutOrderModel("123"),
            CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        // When
        viewModel.releaseBooking()

        // Then
        coVerify { releaseBookingUseCase.get().invoke(productId) }
    }

    @Test
    fun `GIVEN release booking failed WHEN release booking THEN should hit release booking use case with first productId`() {
        // Given
        coEvery { releaseBookingUseCase.get().invoke(any()) } throws IOException()
        val productId = 300L
        viewModel.listData.value = listOf(
            CheckoutTickerErrorModel(errorMessage = ""),
            CheckoutTickerModel(ticker = TickerAnnouncementHolderData()),
            CheckoutAddressModel(recipientAddressModel = RecipientAddressModel()),
            CheckoutUpsellModel(upsell = ShipmentNewUpsellModel()),
            CheckoutProductModel("123", productId = productId),
            CheckoutProductModel("123", productId = productId + 1),
            CheckoutOrderModel("123"),
            CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        // When
        viewModel.releaseBooking()

        // Then
        coVerify { releaseBookingUseCase.get().invoke(productId) }
    }

    @Test
    fun `GIVEN no cart item WHEN release booking THEN should not hit release booking use case`() {
        // Given
        coEvery { releaseBookingUseCase.get().invoke(any()) } returns ReleaseBookingResponse()
        viewModel.listData.value = emptyList()

        // When
        viewModel.releaseBooking()

        // Then
        coVerify(inverse = true) { releaseBookingUseCase.get().invoke(any()) }
    }
}
