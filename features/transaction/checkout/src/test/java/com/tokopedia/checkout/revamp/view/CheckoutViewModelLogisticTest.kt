package com.tokopedia.checkout.revamp.view

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
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.InsuranceData
import com.tokopedia.logisticcart.shipping.model.CashOnDeliveryProduct
import com.tokopedia.logisticcart.shipping.model.CourierItemData
import com.tokopedia.logisticcart.shipping.model.MerchantVoucherProductModel
import com.tokopedia.logisticcart.shipping.model.OntimeDelivery
import com.tokopedia.logisticcart.shipping.model.Product
import com.tokopedia.logisticcart.shipping.model.RatesParam
import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingDurationUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingParam
import com.tokopedia.logisticcart.shipping.model.ShippingRecommendationData
import com.tokopedia.purchase_platform.common.feature.bometadata.BoMetadata
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.domain.model.UploadPrescriptionUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.clearpromo.ClearPromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.clearpromo.SuccessDataUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerAnnouncementHolderData
import io.mockk.coEvery
import io.mockk.coVerify
import org.junit.Assert.assertEquals
import org.junit.Test

class CheckoutViewModelLogisticTest : BaseCheckoutViewModelTest() {

    @Test
    fun generate_rates_param() {
        // given
        viewModel.listData.value = listOf(
            CheckoutTickerErrorModel(errorMessage = ""),
            CheckoutTickerModel(ticker = TickerAnnouncementHolderData()),
            CheckoutAddressModel(
                recipientAddressModel = RecipientAddressModel().apply {
                    id = "1"
                    destinationDistrictId = "1"
                    addressName = "jakarta"
                    postalCode = "123"
                    latitude = "123"
                    longitude = "321"
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

        // when
        val ratesParam = viewModel.generateRatesParam(CheckoutOrderModel("123"), "")

        // then
        assertEquals(
            RatesParam.Builder(
                emptyList(),
                ShippingParam(
                    uniqueId = "123",
                    destinationDistrictId = "1",
                    destinationLatitude = "123",
                    destinationLongitude = "321",
                    destinationPostalCode = "123",
                    shopId = "0",
                    insurance = 1,
                    categoryIds = "0",
                    addressId = "1",
                    products = listOf(Product()),
                    boMetadata = BoMetadata()
                )
            ).apply { warehouseId("0") }.build(),
            ratesParam
        )
    }

    @Test
    fun set_selected_courier() {
        // given
        coEvery {
            validateUsePromoRevampUseCase.setParam(any()).executeOnBackground()
        } returns ValidateUsePromoRevampUiModel(status = "OK", errorCode = "200")

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
        val courierItemData = CourierItemData()

        // when
        viewModel.setSelectedCourier(
            5,
            courierItemData,
            listOf(ShippingCourierUiModel()),
            InsuranceData()
        )

        // then
        assertEquals(
            courierItemData,
            (viewModel.listData.value[5] as CheckoutOrderModel).shipment.courierItemData
        )
    }

    @Test
    fun set_selected_courier_with_previous_promo_code() {
        // given
        coEvery {
            validateUsePromoRevampUseCase.setParam(any()).executeOnBackground()
        } returns ValidateUsePromoRevampUiModel(status = "OK", errorCode = "200")

        coEvery {
            clearCacheAutoApplyStackUseCase.setParams(match { it.orderData.orders.first().codes.size == 1 && it.orderData.orders.first().codes.first() == "promoBO" }).executeOnBackground()
        } returns ClearPromoUiModel(
            SuccessDataUiModel(success = true)
        )

        viewModel.listData.value = listOf(
            CheckoutTickerErrorModel(errorMessage = ""),
            CheckoutTickerModel(ticker = TickerAnnouncementHolderData()),
            CheckoutAddressModel(recipientAddressModel = RecipientAddressModel()),
            CheckoutUpsellModel(upsell = ShipmentNewUpsellModel()),
            CheckoutProductModel("123"),
            CheckoutOrderModel("123", shipment = CheckoutOrderShipment(courierItemData = CourierItemData(logPromoCode = "promoBO"))),
            CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )
        val courierItemData = CourierItemData()

        // when
        viewModel.setSelectedCourier(
            5,
            courierItemData,
            listOf(ShippingCourierUiModel()),
            InsuranceData()
        )

        // then
        assertEquals(
            courierItemData,
            (viewModel.listData.value[5] as CheckoutOrderModel).shipment.courierItemData
        )
        coVerify {
            clearCacheAutoApplyStackUseCase.setParams(any()).executeOnBackground()
        }
    }

    @Test
    fun load_shipping_normal() {
        // given
        viewModel.listData.value = listOf(
            CheckoutTickerErrorModel(errorMessage = ""),
            CheckoutTickerModel(ticker = TickerAnnouncementHolderData()),
            CheckoutAddressModel(
                recipientAddressModel = RecipientAddressModel().apply {
                    id = "1"
                    destinationDistrictId = "1"
                    addressName = "jakarta"
                    postalCode = "123"
                    latitude = "123"
                    longitude = "321"
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

        val shippingCourierUiModel = ShippingCourierUiModel()
        coEvery { ratesUseCase.invoke(any()) } returns ShippingRecommendationData(
            shippingDurationUiModels = listOf(
                ShippingDurationUiModel(
                    shippingCourierViewModelList = listOf(
                        shippingCourierUiModel
                    )
                )
            )
        )

        // when
        viewModel.loadShipping(CheckoutOrderModel("123"), 5)

        // then
        assertEquals(
            CheckoutOrderShipment(
                courierItemData = CourierItemData(
                    name = "",
                    estimatedTimeDelivery = "",
                    shipperFormattedPrice = "",
                    insuranceUsedInfo = "",
                    promoCode = "",
                    checksum = "",
                    ut = "",
                    now = false,
                    priorityInnactiveMessage = "",
                    priorityFormattedPrice = "",
                    priorityDurationMessage = "",
                    priorityCheckboxMessage = "",
                    priorityWarningboxMessage = "",
                    priorityFeeMessage = "",
                    priorityPdpMessage = "",
                    ontimeDelivery = OntimeDelivery(textLabel = "", textDetail = "", urlDetail = ""),
                    codProductData = CashOnDeliveryProduct(0, "", 0, "", "", ""),
                    etaText = "",
                    etaErrorCode = -1,
                    merchantVoucherProductModel = MerchantVoucherProductModel(0),
                    isSelected = true
                ),
                shippingCourierUiModels = listOf(shippingCourierUiModel)
            ),
            (viewModel.listData.value[5] as CheckoutOrderModel).shipment
        )
    }
}
