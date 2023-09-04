package com.tokopedia.checkout.view.presenter

import com.tokopedia.checkout.domain.model.cartshipmentform.CartShipmentAddressFormData
import com.tokopedia.checkout.domain.model.cartshipmentform.GroupAddress
import com.tokopedia.checkout.domain.model.cartshipmentform.NewUpsellData
import com.tokopedia.logisticCommon.data.entity.address.UserAddress
import com.tokopedia.logisticcart.shipping.model.CartItemModel
import com.tokopedia.logisticcart.shipping.model.ShipmentCartData
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel
import com.tokopedia.promocheckout.common.view.uimodel.VoucherLogisticItemUiModel
import com.tokopedia.purchase_platform.common.feature.bometadata.BoMetadata
import com.tokopedia.purchase_platform.common.feature.promo.view.model.clearpromo.ClearPromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.clearpromo.SuccessDataUiModel
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import org.junit.Test

class ShipmentViewModelUpsellTest : BaseShipmentViewModelTest() {

    @Test
    fun `WHEN cancel upsell THEN should try clear all BO`() {
        // Given
        viewModel.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel(cartStringGroup = "cartString").apply {
                cartItemModels = listOf(CartItemModel(cartStringGroup = "cartString"))
                shipmentCartData = ShipmentCartData(boMetadata = BoMetadata())
                voucherLogisticItemUiModel = VoucherLogisticItemUiModel("BOCODE")
            }
        )
        coEvery { clearCacheAutoApplyStackUseCase.setParams(any()).executeOnBackground() } returns
            ClearPromoUiModel(
                successDataModel = SuccessDataUiModel(
                    success = true
                )
            )
        coEvery { getShipmentAddressFormV4UseCase(any()) } returns CartShipmentAddressFormData()
        every {
            shipmentAnalyticsActionListener.sendAnalyticsViewInformationAndWarningTickerInCheckout(
                any()
            )
        } just Runs
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

        viewModel.processInitialLoadCheckoutPage(
            true,
            false,
            false
        )

        viewModel.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel(cartStringGroup = "cartString").apply {
                cartItemModels = listOf(CartItemModel(cartStringGroup = "cartString"))
                shipmentCartData = ShipmentCartData(boMetadata = BoMetadata())
                voucherLogisticItemUiModel = VoucherLogisticItemUiModel("BOCODE")
            }
        )
        coEvery { clearCacheAutoApplyStackUseCase.setParams(any()).executeOnBackground() } returns
            ClearPromoUiModel(
                successDataModel = SuccessDataUiModel(
                    success = true
                )
            )
        coEvery { getShipmentAddressFormV4UseCase(any()) } returns CartShipmentAddressFormData()
        every {
            shipmentAnalyticsActionListener.sendAnalyticsViewInformationAndWarningTickerInCheckout(
                any()
            )
        } just Runs

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

        viewModel.processInitialLoadCheckoutPage(
            true,
            false,
            false
        )

        viewModel.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel(cartStringGroup = "cartString").apply {
                cartItemModels = listOf(CartItemModel(cartStringGroup = "cartString"))
                shipmentCartData = ShipmentCartData(boMetadata = BoMetadata())
            }
        )
        coEvery { clearCacheAutoApplyStackUseCase.setParams(any()).executeOnBackground() } returns
            ClearPromoUiModel(
                successDataModel = SuccessDataUiModel(
                    success = true
                )
            )
        coEvery { getShipmentAddressFormV4UseCase(any()) } returns CartShipmentAddressFormData()
        every {
            shipmentAnalyticsActionListener.sendAnalyticsViewInformationAndWarningTickerInCheckout(
                any()
            )
        } just Runs

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

        viewModel.processInitialLoadCheckoutPage(
            true,
            false,
            false
        )

        viewModel.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel(cartStringGroup = "cartString").apply {
                cartItemModels = listOf(CartItemModel(cartStringGroup = "cartString"))
                shipmentCartData = ShipmentCartData(boMetadata = BoMetadata())
                voucherLogisticItemUiModel = VoucherLogisticItemUiModel()
            }
        )
        coEvery { clearCacheAutoApplyStackUseCase.setParams(any()).executeOnBackground() } returns
            ClearPromoUiModel(
                successDataModel = SuccessDataUiModel(
                    success = true
                )
            )
        coEvery { getShipmentAddressFormV4UseCase(any()) } returns CartShipmentAddressFormData()
        every {
            shipmentAnalyticsActionListener.sendAnalyticsViewInformationAndWarningTickerInCheckout(
                any()
            )
        } just Runs

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

        viewModel.processInitialLoadCheckoutPage(
            true,
            false,
            false
        )

        viewModel.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel(cartStringGroup = "cartString").apply {
                cartItemModels = listOf(CartItemModel(cartStringGroup = "cartString"))
            }
        )
        coEvery { clearCacheAutoApplyStackUseCase.setParams(any()).executeOnBackground() } returns
            ClearPromoUiModel(
                successDataModel = SuccessDataUiModel(
                    success = true
                )
            )
        coEvery { getShipmentAddressFormV4UseCase(any()) } returns CartShipmentAddressFormData()
        every {
            shipmentAnalyticsActionListener.sendAnalyticsViewInformationAndWarningTickerInCheckout(
                any()
            )
        } just Runs

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

        viewModel.processInitialLoadCheckoutPage(
            true,
            false,
            false
        )

        viewModel.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel(cartStringGroup = "cartString").apply {
                cartItemModels = listOf(CartItemModel(cartStringGroup = "cartString"))
                shipmentCartData = ShipmentCartData(boMetadata = BoMetadata())
                voucherLogisticItemUiModel = VoucherLogisticItemUiModel("BOCODE")
            }
        )
        coEvery { clearCacheAutoApplyStackUseCase.setParams(any()).executeOnBackground() } returns
            ClearPromoUiModel(
                successDataModel = SuccessDataUiModel(
                    success = true
                )
            )
        coEvery { getShipmentAddressFormV4UseCase(any()) } returns CartShipmentAddressFormData()
        every {
            shipmentAnalyticsActionListener.sendAnalyticsViewInformationAndWarningTickerInCheckout(
                any()
            )
        } just Runs

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

        viewModel.processInitialLoadCheckoutPage(
            true,
            false,
            false
        )

        viewModel.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel(cartStringGroup = "cartString").apply {
                cartItemModels = listOf(CartItemModel(cartStringGroup = "cartString"))
                shipmentCartData = ShipmentCartData(boMetadata = BoMetadata())
                voucherLogisticItemUiModel = VoucherLogisticItemUiModel("BOCODE")
            }
        )
        coEvery { clearCacheAutoApplyStackUseCase.setParams(any()).executeOnBackground() } returns
            ClearPromoUiModel(
                successDataModel = SuccessDataUiModel(
                    success = true
                )
            )
        coEvery { getShipmentAddressFormV4UseCase(any()) } returns CartShipmentAddressFormData()
        every {
            shipmentAnalyticsActionListener.sendAnalyticsViewInformationAndWarningTickerInCheckout(
                any()
            )
        } just Runs

        // When
        viewModel.clearAllBoOnTemporaryUpsell()

        // Then
        coVerify(inverse = true) {
            clearCacheAutoApplyStackUseCase.setParams(any()).executeOnBackground()
        }
    }
}
