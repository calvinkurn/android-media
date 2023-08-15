package com.tokopedia.checkout.revamp.view

import com.tokopedia.checkout.domain.model.cartshipmentform.CartShipmentAddressFormData
import com.tokopedia.checkout.domain.model.cartshipmentform.GroupAddress
import com.tokopedia.checkout.domain.model.cartshipmentform.NewUpsellData
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutAddressModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutButtonPaymentModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutCostModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutCrossSellGroupModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutEpharmacyModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutOrderModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutOrderShipment
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutProductModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutPromoModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutTickerErrorModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutTickerModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutUpsellModel
import com.tokopedia.checkout.view.uimodel.ShipmentNewUpsellModel
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticCommon.data.entity.address.UserAddress
import com.tokopedia.logisticcart.shipping.model.CourierItemData
import com.tokopedia.purchase_platform.common.feature.bometadata.BoMetadata
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.domain.model.UploadPrescriptionUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.clearpromo.ClearPromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.clearpromo.SuccessDataUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerAnnouncementHolderData
import io.mockk.coEvery
import io.mockk.coVerify
import org.junit.Test

class CheckoutViewModelUpsellTest : BaseCheckoutViewModelTest() {

    @Test
    fun `WHEN cancel upsell THEN should try clear all BO`() {
        // Given
        viewModel.listData.value = listOf(
            CheckoutTickerErrorModel(errorMessage = ""),
            CheckoutTickerModel(ticker = TickerAnnouncementHolderData()),
            CheckoutAddressModel(recipientAddressModel = RecipientAddressModel()),
            CheckoutUpsellModel(upsell = ShipmentNewUpsellModel()),
            CheckoutProductModel("123"),
            CheckoutOrderModel("123", boMetadata = BoMetadata(), shipment = CheckoutOrderShipment(courierItemData = CourierItemData(logPromoCode = "BOCODE"))),
            CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )
        coEvery { clearCacheAutoApplyStackUseCase.setParams(any()).executeOnBackground() } returns
            ClearPromoUiModel(
                successDataModel = SuccessDataUiModel(
                    success = true
                )
            )
        coEvery { getShipmentAddressFormV4UseCase(any()) } returns CartShipmentAddressFormData()

        viewModel.isPlusSelected = false

        // When
        viewModel.cancelUpsell(
            true,
            true,
            false
        )

        // Then
        coVerify {
            clearCacheAutoApplyStackUseCase.setParams(any()).executeOnBackground()
            getShipmentAddressFormV4UseCase(any())
        }
    }

    @Test
    fun `WHEN clear all BO on temporary state THEN should try clear all BO`() {
        // Given
        val groupAddress = GroupAddress().apply {
            userAddress = UserAddress(state = 0)
        }
        val upsell = NewUpsellData(
            isShow = true,
            description = "desc",
            appLink = "applink",
            image = "image",
            isSelected = true,
            price = 100,
            priceWording = "Rp100",
            duration = "duration",
            summaryInfo = "wording",
            buttonText = "button"
        )
        coEvery { getShipmentAddressFormV4UseCase(any()) } returns
            CartShipmentAddressFormData(
                groupAddress = listOf(groupAddress),
                newUpsell = upsell
            )
        viewModel.isPlusSelected = true

        viewModel.loadSAF(
            true,
            false,
            false
        )

        viewModel.listData.value = listOf(
            CheckoutTickerErrorModel(errorMessage = ""),
            CheckoutTickerModel(ticker = TickerAnnouncementHolderData()),
            CheckoutAddressModel(recipientAddressModel = RecipientAddressModel()),
            CheckoutUpsellModel(upsell = ShipmentNewUpsellModel(isShow = true, isSelected = true)),
            CheckoutProductModel("123"),
            CheckoutOrderModel("123", boMetadata = BoMetadata(), shipment = CheckoutOrderShipment(courierItemData = CourierItemData(logPromoCode = "BOCODE"))),
            CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )
        coEvery { clearCacheAutoApplyStackUseCase.setParams(any()).executeOnBackground() } returns
            ClearPromoUiModel(
                successDataModel = SuccessDataUiModel(
                    success = true
                )
            )
        coEvery { getShipmentAddressFormV4UseCase(any()) } returns CartShipmentAddressFormData()

        // When
        viewModel.clearAllBoOnTemporaryUpsell()

        // Then
        coVerify {
            clearCacheAutoApplyStackUseCase.setParams(any()).executeOnBackground()
            getShipmentAddressFormV4UseCase(any())
        }
    }

    @Test
    fun `WHEN clear all BO on temporary state with no BO THEN should not try clear all BO`() {
        // Given
        val groupAddress = GroupAddress().apply {
            userAddress = UserAddress(state = 0)
        }
        val upsell = NewUpsellData(
            isShow = true,
            description = "desc",
            appLink = "applink",
            image = "image",
            isSelected = true,
            price = 100,
            priceWording = "Rp100",
            duration = "duration",
            summaryInfo = "wording",
            buttonText = "button"
        )
        coEvery { getShipmentAddressFormV4UseCase(any()) } returns
            CartShipmentAddressFormData(
                groupAddress = listOf(groupAddress),
                newUpsell = upsell
            )
        viewModel.isPlusSelected = true

        viewModel.loadSAF(
            true,
            false,
            false
        )

        viewModel.listData.value = listOf(
            CheckoutTickerErrorModel(errorMessage = ""),
            CheckoutTickerModel(ticker = TickerAnnouncementHolderData()),
            CheckoutAddressModel(recipientAddressModel = RecipientAddressModel()),
            CheckoutUpsellModel(upsell = ShipmentNewUpsellModel()),
            CheckoutProductModel("123"),
            CheckoutOrderModel("123", boMetadata = BoMetadata()),
            CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )
        coEvery { clearCacheAutoApplyStackUseCase.setParams(any()).executeOnBackground() } returns
            ClearPromoUiModel(
                successDataModel = SuccessDataUiModel(
                    success = true
                )
            )
        coEvery { getShipmentAddressFormV4UseCase(any()) } returns CartShipmentAddressFormData()

        // When
        viewModel.clearAllBoOnTemporaryUpsell()

        // Then
        coVerify(inverse = true) {
            clearCacheAutoApplyStackUseCase.setParams(any()).executeOnBackground()
        }
    }

    @Test
    fun `WHEN clear all BO on temporary state with no BO code THEN should not try clear all BO`() {
        // Given
        val groupAddress = GroupAddress().apply {
            userAddress = UserAddress(state = 0)
        }
        val upsell = NewUpsellData(
            isShow = true,
            description = "desc",
            appLink = "applink",
            image = "image",
            isSelected = true,
            price = 100,
            priceWording = "Rp100",
            duration = "duration",
            summaryInfo = "wording",
            buttonText = "button"
        )
        coEvery { getShipmentAddressFormV4UseCase(any()) } returns
            CartShipmentAddressFormData(
                groupAddress = listOf(groupAddress),
                newUpsell = upsell
            )
        viewModel.isPlusSelected = true

        viewModel.loadSAF(
            true,
            false,
            false
        )

        viewModel.listData.value = listOf(
            CheckoutTickerErrorModel(errorMessage = ""),
            CheckoutTickerModel(ticker = TickerAnnouncementHolderData()),
            CheckoutAddressModel(recipientAddressModel = RecipientAddressModel()),
            CheckoutUpsellModel(upsell = ShipmentNewUpsellModel()),
            CheckoutProductModel("123"),
            CheckoutOrderModel("123", boMetadata = BoMetadata(), shipment = CheckoutOrderShipment(courierItemData = CourierItemData())),
            CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )
        coEvery { clearCacheAutoApplyStackUseCase.setParams(any()).executeOnBackground() } returns
            ClearPromoUiModel(
                successDataModel = SuccessDataUiModel(
                    success = true
                )
            )
        coEvery { getShipmentAddressFormV4UseCase(any()) } returns CartShipmentAddressFormData()

        // When
        viewModel.clearAllBoOnTemporaryUpsell()

        // Then
        coVerify(inverse = true) {
            clearCacheAutoApplyStackUseCase.setParams(any()).executeOnBackground()
        }
    }

    @Test
    fun `WHEN clear all BO on temporary state with no shipment data THEN should not try clear all BO`() {
        // Given
        val groupAddress = GroupAddress().apply {
            userAddress = UserAddress(state = 0)
        }
        val upsell = NewUpsellData(
            isShow = true,
            description = "desc",
            appLink = "applink",
            image = "image",
            isSelected = true,
            price = 100,
            priceWording = "Rp100",
            duration = "duration",
            summaryInfo = "wording",
            buttonText = "button"
        )
        coEvery { getShipmentAddressFormV4UseCase(any()) } returns
            CartShipmentAddressFormData(
                groupAddress = listOf(groupAddress),
                newUpsell = upsell
            )
        viewModel.isPlusSelected = true

        viewModel.loadSAF(
            true,
            false,
            false
        )

        viewModel.listData.value = listOf(
            CheckoutTickerErrorModel(errorMessage = ""),
            CheckoutTickerModel(ticker = TickerAnnouncementHolderData()),
            CheckoutAddressModel(recipientAddressModel = RecipientAddressModel()),
            CheckoutUpsellModel(upsell = ShipmentNewUpsellModel()),
            CheckoutProductModel("123"),
            CheckoutOrderModel("123"),
            CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )
        coEvery { clearCacheAutoApplyStackUseCase.setParams(any()).executeOnBackground() } returns
            ClearPromoUiModel(
                successDataModel = SuccessDataUiModel(
                    success = true
                )
            )
        coEvery { getShipmentAddressFormV4UseCase(any()) } returns CartShipmentAddressFormData()

        // When
        viewModel.clearAllBoOnTemporaryUpsell()

        // Then
        coVerify(inverse = true) {
            clearCacheAutoApplyStackUseCase.setParams(any()).executeOnBackground()
        }
    }

    @Test
    fun `WHEN clear all BO not on temporary state THEN should not try clear all BO`() {
        // Given
        val groupAddress = GroupAddress().apply {
            userAddress = UserAddress(state = 0)
        }
        val upsell = NewUpsellData(
            isShow = true,
            description = "desc",
            appLink = "applink",
            image = "image",
            isSelected = false,
            price = 100,
            priceWording = "Rp100",
            duration = "duration",
            summaryInfo = "wording",
            buttonText = "button"
        )
        coEvery { getShipmentAddressFormV4UseCase(any()) } returns
            CartShipmentAddressFormData(
                groupAddress = listOf(groupAddress),
                newUpsell = upsell
            )
        viewModel.isPlusSelected = true

        viewModel.loadSAF(
            true,
            false,
            false
        )

        viewModel.listData.value = listOf(
            CheckoutTickerErrorModel(errorMessage = ""),
            CheckoutTickerModel(ticker = TickerAnnouncementHolderData()),
            CheckoutAddressModel(recipientAddressModel = RecipientAddressModel()),
            CheckoutUpsellModel(upsell = ShipmentNewUpsellModel()),
            CheckoutProductModel("123"),
            CheckoutOrderModel("123", boMetadata = BoMetadata(), shipment = CheckoutOrderShipment(courierItemData = CourierItemData(logPromoCode = "BOCODE"))),
            CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )
        coEvery { clearCacheAutoApplyStackUseCase.setParams(any()).executeOnBackground() } returns
            ClearPromoUiModel(
                successDataModel = SuccessDataUiModel(
                    success = true
                )
            )
        coEvery { getShipmentAddressFormV4UseCase(any()) } returns CartShipmentAddressFormData()

        // When
        viewModel.clearAllBoOnTemporaryUpsell()

        // Then
        coVerify(inverse = true) {
            clearCacheAutoApplyStackUseCase.setParams(any()).executeOnBackground()
        }
    }

    @Test
    fun `WHEN clear all BO not on upsell THEN should not try clear all BO`() {
        // Given
        val groupAddress = GroupAddress().apply {
            userAddress = UserAddress(state = 0)
        }
        val upsell = NewUpsellData(
            isShow = false,
            description = "desc",
            appLink = "applink",
            image = "image",
            isSelected = false,
            price = 100,
            priceWording = "Rp100",
            duration = "duration",
            summaryInfo = "wording",
            buttonText = "button"
        )
        coEvery { getShipmentAddressFormV4UseCase(any()) } returns
            CartShipmentAddressFormData(
                groupAddress = listOf(groupAddress),
                newUpsell = upsell
            )
        viewModel.isPlusSelected = true

        viewModel.loadSAF(
            true,
            false,
            false
        )

        viewModel.listData.value = listOf(
            CheckoutTickerErrorModel(errorMessage = ""),
            CheckoutTickerModel(ticker = TickerAnnouncementHolderData()),
            CheckoutAddressModel(recipientAddressModel = RecipientAddressModel()),
            CheckoutUpsellModel(upsell = ShipmentNewUpsellModel()),
            CheckoutProductModel("123"),
            CheckoutOrderModel("123", boMetadata = BoMetadata(), shipment = CheckoutOrderShipment(courierItemData = CourierItemData(logPromoCode = "BOCODE"))),
            CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )
        coEvery { clearCacheAutoApplyStackUseCase.setParams(any()).executeOnBackground() } returns
            ClearPromoUiModel(
                successDataModel = SuccessDataUiModel(
                    success = true
                )
            )
        coEvery { getShipmentAddressFormV4UseCase(any()) } returns CartShipmentAddressFormData()

        // When
        viewModel.clearAllBoOnTemporaryUpsell()

        // Then
        coVerify(inverse = true) {
            clearCacheAutoApplyStackUseCase.setParams(any()).executeOnBackground()
        }
    }
}
