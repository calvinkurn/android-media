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
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ProductData
import com.tokopedia.logisticcart.shipping.model.CashOnDeliveryProduct
import com.tokopedia.logisticcart.shipping.model.CourierItemData
import com.tokopedia.logisticcart.shipping.model.LogisticPromoUiModel
import com.tokopedia.logisticcart.shipping.model.MerchantVoucherProductModel
import com.tokopedia.logisticcart.shipping.model.OntimeDelivery
import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingDurationUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingRecommendationData
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.domain.model.UploadPrescriptionUiModel
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.Order
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.ProductDetail
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.PromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.OrdersItem
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.view.model.clearpromo.ClearPromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.clearpromo.SuccessDataUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyMessageUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyVoucherOrdersItemUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.MessageUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoCheckoutVoucherOrdersItemUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerAnnouncementHolderData
import io.mockk.coEvery
import org.junit.Assert.assertEquals
import org.junit.Test

class CheckoutViewModelPromoTest : BaseCheckoutViewModelTest() {

    @Test
    fun generate_coupon_list_recom_request() {
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
        val result = viewModel.generateCouponListRecommendationRequest()

        // then
        assertEquals(
            PromoRequest(
                cartType = "default",
                state = "checkout",
                orders = listOf(
                    Order(
                        cartStringGroup = "123",
                        product_details = listOf(ProductDetail(quantity = 0)),
                        isChecked = true
                    )
                )
            ),
            result
        )
    }

    @Test
    fun validate_clear_all_bo() {
        // given
        coEvery {
            clearCacheAutoApplyStackUseCase.setParams(any()).executeOnBackground()
        } returns ClearPromoUiModel(
            SuccessDataUiModel(true)
        )

        coEvery {
            validateUsePromoRevampUseCase.setParam(any()).executeOnBackground()
        } returns ValidateUsePromoRevampUiModel(
            status = "OK",
            errorCode = "200"
        )

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
            CheckoutProductModel("123", cartStringOrder = "12"),
            CheckoutOrderModel(
                "123",
                boCode = "boCode",
                boUniqueId = "12",
                shipment = CheckoutOrderShipment(courierItemData = CourierItemData(logPromoCode = "boCode"))
            ),
            CheckoutProductModel("234", cartStringOrder = "23"),
            CheckoutOrderModel(
                "234",
                shipment = CheckoutOrderShipment(courierItemData = CourierItemData())
            ),
            CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
            CheckoutPromoModel(
                promo = LastApplyUiModel(
                    voucherOrders = listOf(
                        LastApplyVoucherOrdersItemUiModel(
                            code = "boCode",
                            message = LastApplyMessageUiModel(state = "green")
                        )
                    )
                )
            ),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        // when
        viewModel.validateClearAllBoPromo(
            ValidateUsePromoRequest(
                orders = listOf(
                    OrdersItem(cartStringGroup = "123"),
                    OrdersItem(cartStringGroup = "234")
                )
            ),
            PromoUiModel()
        )

        // then
        assertEquals(
            listOf(
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
                CheckoutProductModel("123", cartStringOrder = "12"),
                CheckoutOrderModel(
                    "123",
                    boCode = "boCode",
                    boUniqueId = "12",
                    shipment = CheckoutOrderShipment()
                ),
                CheckoutProductModel("234", cartStringOrder = "23"),
                CheckoutOrderModel(
                    "234",
                    shipment = CheckoutOrderShipment(courierItemData = CourierItemData())
                ),
                CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
                CheckoutPromoModel(promo = LastApplyUiModel()),
                CheckoutCostModel(totalPriceString = "Rp0"),
                CheckoutCrossSellGroupModel(),
                CheckoutButtonPaymentModel(enable = true, totalPrice = "Rp0")
            ),
            viewModel.listData.value
        )
    }

    @Test
    fun validate_bo() {
        // given
        coEvery {
            ratesUseCase.invoke(any())
        } returns ShippingRecommendationData(
            shippingDurationUiModels = listOf(
                ShippingDurationUiModel(
                    shippingCourierViewModelList = listOf(
                        ShippingCourierUiModel(
                            productData = ProductData(
                                shipperProductId = 1,
                                shipperId = 1
                            )
                        )
                    )
                )
            ),
            listLogisticPromo = listOf(
                LogisticPromoUiModel(promoCode = "boCode", shipperId = 1, shipperProductId = 1)
            )
        )

        val validateUsePromoRevampUiModel = ValidateUsePromoRevampUiModel(
            status = "OK",
            errorCode = "200",
            promoUiModel = PromoUiModel(
                voucherOrderUiModels = listOf(
                    PromoCheckoutVoucherOrdersItemUiModel(
                        code = "boCode",
                        uniqueId = "12",
                        cartStringGroup = "123",
                        shippingId = 1,
                        spId = 1,
                        type = "logistic",
                        messageUiModel = MessageUiModel(state = "green")
                    )
                )
            )
        )
        coEvery {
            validateUsePromoRevampUseCase.setParam(any()).executeOnBackground()
        } returns validateUsePromoRevampUiModel

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
            CheckoutProductModel("123", cartStringOrder = "12"),
            CheckoutOrderModel(
                "123"
            ),
            CheckoutProductModel("234", cartStringOrder = "23"),
            CheckoutOrderModel(
                "234",
                shipment = CheckoutOrderShipment(courierItemData = CourierItemData())
            ),
            CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
            CheckoutPromoModel(
                promo = LastApplyUiModel()
            ),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        // when
        viewModel.validateBoPromo(
            validateUsePromoRevampUiModel
        )

        // then
        assertEquals(
            listOf(
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
                CheckoutProductModel("123", cartStringOrder = "12"),
                CheckoutOrderModel(
                    "123",
                    boUniqueId = "12",
                    shipment = CheckoutOrderShipment(
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
                            ontimeDelivery = OntimeDelivery(
                                textLabel = "",
                                textDetail = "",
                                urlDetail = ""
                            ),
                            codProductData = CashOnDeliveryProduct(0, "", 0, "", "", ""),
                            etaText = "",
                            etaErrorCode = -1,
                            merchantVoucherProductModel = MerchantVoucherProductModel(0),
                            isSelected = true,
                            shipperProductId = 1,
                            shipperId = 1,
                            shipperName = "",
                            logPromoCode = "boCode",
                            logPromoMsg = "",
                            promoTitle = "",
                            logPromoDesc = ""
                        )
                    )
                ),
                CheckoutProductModel("234", cartStringOrder = "23"),
                CheckoutOrderModel(
                    "234",
                    shipment = CheckoutOrderShipment(courierItemData = CourierItemData())
                ),
                CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
                CheckoutPromoModel(
                    promo = LastApplyUiModel(
                        voucherOrders = listOf(
                            LastApplyVoucherOrdersItemUiModel(
                                code = "boCode",
                                uniqueId = "12",
                                message = LastApplyMessageUiModel(state = "green"),
                                cartStringGroup = "123",
                                type = "logistic"
                            )
                        )
                    )
                ),
                CheckoutCostModel(totalPriceString = "Rp0", hasSelectAllShipping = true),
                CheckoutCrossSellGroupModel(),
                CheckoutButtonPaymentModel(enable = true, totalPrice = "Rp0")
            ),
            viewModel.listData.value
        )
    }

    @Test
    fun cancel_promo() {
        // given
        coEvery {
            clearCacheAutoApplyStackUseCase.setParams(any()).executeOnBackground()
        } returns ClearPromoUiModel(
            SuccessDataUiModel(true)
        )

        coEvery {
            validateUsePromoRevampUseCase.setParam(any()).executeOnBackground()
        } returns ValidateUsePromoRevampUiModel(
            status = "OK",
            errorCode = "200"
        )

        val orderModel = CheckoutOrderModel(
            "123",
            boUniqueId = "12",
            shipment = CheckoutOrderShipment(
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
                    ontimeDelivery = OntimeDelivery(
                        textLabel = "",
                        textDetail = "",
                        urlDetail = ""
                    ),
                    codProductData = CashOnDeliveryProduct(0, "", 0, "", "", ""),
                    etaText = "",
                    etaErrorCode = -1,
                    merchantVoucherProductModel = MerchantVoucherProductModel(0),
                    isSelected = true,
                    shipperProductId = 1,
                    shipperId = 1,
                    shipperName = "",
                    logPromoCode = "boCode",
                    logPromoMsg = "",
                    promoTitle = "",
                    logPromoDesc = ""
                )
            )
        )
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
            CheckoutProductModel("123", cartStringOrder = "12"),
            orderModel,
            CheckoutProductModel("234", cartStringOrder = "23"),
            CheckoutOrderModel(
                "234",
                shipment = CheckoutOrderShipment(courierItemData = CourierItemData())
            ),
            CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
            CheckoutPromoModel(
                promo = LastApplyUiModel(
                    voucherOrders = listOf(
                        LastApplyVoucherOrdersItemUiModel(
                            code = "boCode",
                            uniqueId = "12",
                            message = LastApplyMessageUiModel(state = "green"),
                            cartStringGroup = "123"
                        )
                    )
                )
            ),
            CheckoutCostModel(totalPriceString = "Rp0", hasSelectAllShipping = true),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel(enable = true, totalPrice = "Rp0")
        )

        // when
        viewModel.cancelAutoApplyPromoStackLogistic(5, "boCode", orderModel)

        // then
        assertEquals(
            listOf(
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
                CheckoutProductModel("123", cartStringOrder = "12"),
                CheckoutOrderModel(
                    "123",
                    boUniqueId = "12",
                    shipment = CheckoutOrderShipment()
                ),
                CheckoutProductModel("234", cartStringOrder = "23"),
                CheckoutOrderModel(
                    "234",
                    shipment = CheckoutOrderShipment(courierItemData = CourierItemData())
                ),
                CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
                CheckoutPromoModel(promo = LastApplyUiModel()),
                CheckoutCostModel(totalPriceString = "Rp0"),
                CheckoutCrossSellGroupModel(),
                CheckoutButtonPaymentModel(enable = true, totalPrice = "Rp0")
            ),
            viewModel.listData.value
        )
    }
}
