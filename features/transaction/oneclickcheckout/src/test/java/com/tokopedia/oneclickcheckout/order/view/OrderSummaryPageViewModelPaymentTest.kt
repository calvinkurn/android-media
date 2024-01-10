package com.tokopedia.oneclickcheckout.order.view

import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.EstimatedTimeArrivalPromo
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.InsuranceData
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ProductData
import com.tokopedia.logisticcart.shipping.model.LogisticPromoUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel
import com.tokopedia.oneclickcheckout.order.data.payment.AdditionalInfoData
import com.tokopedia.oneclickcheckout.order.data.payment.BenefitSummaryInfoData
import com.tokopedia.oneclickcheckout.order.data.payment.CartAddOnData
import com.tokopedia.oneclickcheckout.order.data.payment.CartAddressData
import com.tokopedia.oneclickcheckout.order.data.payment.CartData
import com.tokopedia.oneclickcheckout.order.data.payment.CartDetail
import com.tokopedia.oneclickcheckout.order.data.payment.CartDetailData
import com.tokopedia.oneclickcheckout.order.data.payment.CartGroupData
import com.tokopedia.oneclickcheckout.order.data.payment.CartProductCategoryData
import com.tokopedia.oneclickcheckout.order.data.payment.CartProductData
import com.tokopedia.oneclickcheckout.order.data.payment.CartShippingInfoData
import com.tokopedia.oneclickcheckout.order.data.payment.CartShopOrderData
import com.tokopedia.oneclickcheckout.order.data.payment.DetailsItemData
import com.tokopedia.oneclickcheckout.order.data.payment.PaymentData
import com.tokopedia.oneclickcheckout.order.data.payment.PaymentRequest
import com.tokopedia.oneclickcheckout.order.data.payment.PromoDetail
import com.tokopedia.oneclickcheckout.order.data.payment.SummariesItemData
import com.tokopedia.oneclickcheckout.order.data.payment.UsageSummariesData
import com.tokopedia.oneclickcheckout.order.data.payment.VoucherOrderItemData
import com.tokopedia.oneclickcheckout.order.view.model.OccButtonState
import com.tokopedia.oneclickcheckout.order.view.model.OrderCart
import com.tokopedia.oneclickcheckout.order.view.model.OrderCost
import com.tokopedia.oneclickcheckout.order.view.model.OrderInsurance
import com.tokopedia.oneclickcheckout.order.view.model.OrderPayment
import com.tokopedia.oneclickcheckout.order.view.model.OrderPaymentCreditCard
import com.tokopedia.oneclickcheckout.order.view.model.OrderPaymentCreditCardAdditionalData
import com.tokopedia.oneclickcheckout.order.view.model.OrderProduct
import com.tokopedia.oneclickcheckout.order.view.model.OrderProfile
import com.tokopedia.oneclickcheckout.order.view.model.OrderProfileAddress
import com.tokopedia.oneclickcheckout.order.view.model.OrderPromo
import com.tokopedia.oneclickcheckout.order.view.model.OrderShipment
import com.tokopedia.oneclickcheckout.order.view.model.OrderShop
import com.tokopedia.oneclickcheckout.order.view.model.OrderTotal
import com.tokopedia.purchase_platform.common.feature.addonsproduct.data.model.AddOnsProductDataModel
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnGiftingButtonModel
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnGiftingDataItemModel
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnGiftingDataModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyAdditionalInfoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyMessageUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUsageSummariesUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyVoucherOrdersItemUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.BenefitSummaryInfoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.DetailsItemUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.SummariesItemUiModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class OrderSummaryPageViewModelPaymentTest : BaseOrderSummaryPageViewModelTest() {

    @Test
    fun generatePaymentRequest() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(
            buttonState = OccButtonState.NORMAL,
            orderCost = OrderCost(totalPriceWithoutPaymentFees = 1.1)
        )
        orderSummaryPageViewModel.orderShop.value =
            OrderShop(
                shopId = "234",
                warehouseId = "345",
                shopTier = 456,
                addOn = AddOnGiftingDataModel(
                    status = 0,
                    addOnsDataItemModelList = listOf(
                        AddOnGiftingDataItemModel(
                            addOnPrice = 13.0,
                            addOnQty = 2
                        )
                    ),
                    addOnsButtonModel = AddOnGiftingButtonModel(
                        title = "gift"
                    )
                )
            )
        orderSummaryPageViewModel.orderCart =
            OrderCart(cartString = "123", shop = orderSummaryPageViewModel.orderShop.value)
        orderSummaryPageViewModel.orderProducts.value = listOf(
            OrderProduct(
                productId = Long.MAX_VALUE.toString(),
                productName = "asdf",
                finalPrice = 4.0,
                orderQuantity = 2,
                categoryId = "12",
                lastLevelCategory = "asdfasdf",
                categoryIdentifier = "12asdf",
                addOn = AddOnGiftingDataModel(
                    status = 1,
                    addOnsDataItemModelList = listOf(
                        AddOnGiftingDataItemModel(
                            addOnPrice = 12.0,
                            addOnQty = 2
                        )
                    ),
                    addOnsButtonModel = AddOnGiftingButtonModel(
                        title = "gift"
                    )
                ),
                addOnsProductData = AddOnsProductDataModel(
                    data = listOf(
                        AddOnsProductDataModel.Data(
                            status = 0,
                            name = "addon0",
                            price = 20
                        ),
                        AddOnsProductDataModel.Data(
                            status = 1,
                            name = "addon1",
                            price = 21
                        ),
                        AddOnsProductDataModel.Data(
                            status = 2,
                            name = "addon2",
                            price = 22
                        ),
                        AddOnsProductDataModel.Data(
                            status = 3,
                            name = "addon3",
                            price = 23
                        ),
                        AddOnsProductDataModel.Data(
                            status = 3,
                            name = "addon4",
                            price = 24,
                            fixedQuantity = true
                        )
                    )
                )
            ),
            OrderProduct(
                isError = true,
                productId = Long.MIN_VALUE.toString(),
                productName = "qwer",
                finalPrice = 3.0,
                orderQuantity = 1,
                categoryId = "23",
                lastLevelCategory = "qwerqwer",
                categoryIdentifier = "23qwer"
            )
        )
        orderSummaryPageViewModel.orderProfile.value = OrderProfile(
            address = OrderProfileAddress(
                addressId = "1",
                addressStreet = "a",
                provinceName = "b",
                cityName = "c",
                country = "d",
                postalCode = "3"
            )
        )
        orderSummaryPageViewModel.orderShipment.value = OrderShipment(
            shipperProductId = 1,
            shippingPrice = 10,
            serviceName = "durasi (1 hari)",
            shipperName = "kirimin",
            shippingEta = "1 hari lagi",
            insurance = OrderInsurance(
                insuranceData = InsuranceData(
                    insurancePrice = 3.0
                ),
                isCheckInsurance = true
            )
        )
        orderSummaryPageViewModel.orderPayment.value = OrderPayment(
            gatewayCode = "gatewayC",
            creditCard = OrderPaymentCreditCard(
                additionalData = OrderPaymentCreditCardAdditionalData(
                    profileCode = "profileC"
                )
            )
        )
        orderSummaryPageViewModel.orderPromo.value = OrderPromo(
            lastApply = LastApplyUiModel(
                benefitSummaryInfo = BenefitSummaryInfoUiModel(
                    summaries = listOf(
                        SummariesItemUiModel(
                            type = "type1",
                            details = listOf(
                                DetailsItemUiModel(
                                    amount = 1,
                                    type = "subtype1"
                                ),
                                DetailsItemUiModel(
                                    amount = 3,
                                    type = "subtype3"
                                )
                            )
                        ),
                        SummariesItemUiModel(
                            type = "type2",
                            details = listOf(
                                DetailsItemUiModel(
                                    amount = 2,
                                    type = "subtype2"
                                )
                            )
                        )
                    )
                ),
                voucherOrders = listOf(
                    LastApplyVoucherOrdersItemUiModel(
                        uniqueId = "123",
                        message = LastApplyMessageUiModel(state = "green"),
                        code = "a",
                        type = "a",
                        cartStringGroup = "1231",
                        shippingId = 1,
                        spId = 1,
                        success = true
                    ),
                    LastApplyVoucherOrdersItemUiModel(
                        uniqueId = "123",
                        message = LastApplyMessageUiModel(state = "green"),
                        code = "",
                        type = "a",
                        cartStringGroup = "1231",
                        shippingId = 1,
                        spId = 1,
                        success = true
                    ),
                    LastApplyVoucherOrdersItemUiModel(
                        uniqueId = "123",
                        message = LastApplyMessageUiModel(state = "green"),
                        code = "a",
                        type = "",
                        cartStringGroup = "1231",
                        shippingId = 1,
                        spId = 1,
                        success = true
                    ),
                    LastApplyVoucherOrdersItemUiModel(
                        uniqueId = "123",
                        message = LastApplyMessageUiModel(state = "red"),
                        code = "a",
                        type = "a",
                        cartStringGroup = "1231",
                        shippingId = 1,
                        spId = 1,
                        success = true
                    ),
                    LastApplyVoucherOrdersItemUiModel(
                        uniqueId = "321",
                        message = LastApplyMessageUiModel(state = "green"),
                        code = "a",
                        type = "a",
                        cartStringGroup = "1231",
                        shippingId = 1,
                        spId = 1,
                        success = true
                    )
                ),
                additionalInfo = LastApplyAdditionalInfoUiModel(
                    usageSummaries = listOf(
                        LastApplyUsageSummariesUiModel(
                            type = "type1",
                            amount = 1,
                            amountStr = "1.0"
                        ),
                        LastApplyUsageSummariesUiModel(
                            type = "type2",
                            amount = 2,
                            amountStr = "2.0"
                        )
                    )
                )
            )
        )

        // When
        val paymentRequest =
            orderSummaryPageViewModel.generatePaymentRequest(orderSummaryPageViewModel.orderTotal.value.orderCost)

        // Then
        assertEquals(
            PaymentRequest(
                payment = PaymentData(
                    profileCode = "profileC",
                    gatewayCode = "gatewayC",
                    paymentAmount = 1.1
                ),
                cartDetail = CartDetail(
                    cart = CartData(
                        data = listOf(
                            CartDetailData(
                                address = CartAddressData(
                                    id = "1",
                                    address = "a",
                                    state = "b",
                                    city = "c",
                                    country = "d",
                                    postalCode = "3"
                                ),
                                groupOrders = listOf(
                                    CartGroupData(
                                        cartStringGroup = "123",
                                        shippingInfo = CartShippingInfoData(
                                            spId = "1",
                                            originalShippingPrice = 10.0,
                                            serviceName = "durasi (1 hari)",
                                            shipperName = "kirimin",
                                            eta = "1 hari lagi",
                                            insurancePrice = 3.0
                                        ),
                                        shopOrders = listOf(
                                            CartShopOrderData(
                                                shopId = "234",
                                                warehouseId = 345,
                                                shopTier = 456,
                                                products = listOf(
                                                    CartProductData(
                                                        productId = "9223372036854775807",
                                                        name = "asdf",
                                                        price = 4,
                                                        quantity = 2,
                                                        totalPrice = 8,
                                                        bundleGroupId = "",
                                                        addonItems = listOf(
                                                            CartAddOnData(
                                                                name = "gift",
                                                                price = 6,
                                                                quantity = 2,
                                                                totalPrice = 12
                                                            ),
                                                            CartAddOnData(
                                                                name = "addon1",
                                                                price = 21,
                                                                quantity = 2,
                                                                totalPrice = 42
                                                            ),
                                                            CartAddOnData(
                                                                name = "addon3",
                                                                price = 23,
                                                                quantity = 2,
                                                                totalPrice = 46
                                                            ),
                                                            CartAddOnData(
                                                                name = "addon4",
                                                                price = 24,
                                                                quantity = 1,
                                                                totalPrice = 24
                                                            )
                                                        ),
                                                        category = CartProductCategoryData(
                                                            id = "12",
                                                            name = "asdfasdf",
                                                            identifier = "12asdf"
                                                        )
                                                    )
                                                ),
                                                bundle = emptyList(),
                                                addonItems = emptyList(),
                                                cartStringOrder = "123"
                                            )
                                        )
                                    )
                                )
                            )
                        )
                    )
                ),
                promoDetail = PromoDetail(
                    benefitSummaryInfo = BenefitSummaryInfoData(
                        summaries = listOf(
                            SummariesItemData(
                                details = listOf(
                                    DetailsItemData(
                                        amount = 1.0,
                                        type = "subtype1"
                                    ),
                                    DetailsItemData(
                                        amount = 3.0,
                                        type = "subtype3"
                                    )
                                ),
                                type = "type1"
                            ),
                            SummariesItemData(
                                details = listOf(
                                    DetailsItemData(
                                        amount = 2.0,
                                        type = "subtype2"
                                    )
                                ),
                                type = "type2"
                            )
                        )
                    ),
                    voucherOrders = listOf(
                        VoucherOrderItemData(
                            code = "a",
                            uniqueId = "123",
                            shippingId = 1,
                            spId = 1,
                            type = "a",
                            success = true,
                            cartStringGroup = "1231"
                        )
                    ),
                    additionalInfo = AdditionalInfoData(
                        usageSummaries = listOf(
                            UsageSummariesData(
                                type = "type1",
                                amount = 1.0,
                                amountString = "1.0"
                            ),
                            UsageSummariesData(
                                type = "type2",
                                amount = 2.0,
                                amountString = "2.0"
                            )
                        )
                    )
                )
            ),
            paymentRequest
        )
    }

    @Test
    fun generatePaymentRequestWithBO() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(
            buttonState = OccButtonState.NORMAL,
            orderCost = OrderCost(totalPriceWithoutPaymentFees = 1.1)
        )
        orderSummaryPageViewModel.orderShop.value =
            OrderShop(
                shopId = "234",
                warehouseId = "345",
                shopTier = 456,
                addOn = AddOnGiftingDataModel(
                    status = 1,
                    addOnsDataItemModelList = listOf(
                        AddOnGiftingDataItemModel(
                            addOnPrice = 12.0,
                            addOnQty = 2
                        )
                    ),
                    addOnsButtonModel = AddOnGiftingButtonModel(
                        title = "gift"
                    )
                )
            )
        orderSummaryPageViewModel.orderCart =
            OrderCart(cartString = "123", shop = orderSummaryPageViewModel.orderShop.value)
        orderSummaryPageViewModel.orderProducts.value = listOf(
            OrderProduct(
                productId = Long.MAX_VALUE.toString(),
                productName = "asdf",
                finalPrice = 4.0,
                orderQuantity = 2,
                categoryId = "12",
                lastLevelCategory = "asdfasdf",
                categoryIdentifier = "12asdf",
                addOn = AddOnGiftingDataModel(
                    status = 1,
                    addOnsDataItemModelList = emptyList(),
                    addOnsButtonModel = AddOnGiftingButtonModel(
                        title = "gift"
                    )
                )
            ),
            OrderProduct(
                isError = true,
                productId = Long.MIN_VALUE.toString(),
                productName = "qwer",
                finalPrice = 3.0,
                orderQuantity = 1,
                categoryId = "23",
                lastLevelCategory = "qwerqwer",
                categoryIdentifier = "23qwer"
            )
        )
        orderSummaryPageViewModel.orderProfile.value = OrderProfile(
            address = OrderProfileAddress(
                addressId = "1",
                addressStreet = "a",
                provinceName = "b",
                cityName = "c",
                country = "d",
                postalCode = "3"
            )
        )
        orderSummaryPageViewModel.orderShipment.value = OrderShipment(
            shipperProductId = 1,
            shippingPrice = 10,
            serviceName = "durasi (1 hari)",
            shipperName = "kirimin",
            shippingEta = "1 hari lagi",
            insurance = OrderInsurance(
                insuranceData = InsuranceData(
                    insurancePrice = 3.0
                ),
                isCheckInsurance = false
            ),
            isApplyLogisticPromo = true,
            logisticPromoViewModel = LogisticPromoUiModel(
                shippingRate = 11,
                title = "BebasOngkir",
                shipperName = "BOKurir",
                etaData = EstimatedTimeArrivalPromo(textEta = "2 hari")
            ),
            logisticPromoShipping = ShippingCourierUiModel(
                productData = ProductData(
                    shipperProductId = 2
                )
            )
        )
        orderSummaryPageViewModel.orderPayment.value = OrderPayment(
            gatewayCode = "gatewayC",
            creditCard = OrderPaymentCreditCard(
                additionalData = OrderPaymentCreditCardAdditionalData(
                    profileCode = "profileC"
                )
            )
        )

        // When
        val paymentRequest =
            orderSummaryPageViewModel.generatePaymentRequest(orderSummaryPageViewModel.orderTotal.value.orderCost)

        // Then
        assertEquals(
            PaymentRequest(
                payment = PaymentData(
                    profileCode = "profileC",
                    gatewayCode = "gatewayC",
                    paymentAmount = 1.1
                ),
                cartDetail = CartDetail(
                    cart = CartData(
                        data = listOf(
                            CartDetailData(
                                address = CartAddressData(
                                    id = "1",
                                    address = "a",
                                    state = "b",
                                    city = "c",
                                    country = "d",
                                    postalCode = "3"
                                ),
                                groupOrders = listOf(
                                    CartGroupData(
                                        cartStringGroup = "123",
                                        shippingInfo = CartShippingInfoData(
                                            spId = "2",
                                            originalShippingPrice = 11.0,
                                            serviceName = "BebasOngkir",
                                            shipperName = "BOKurir",
                                            eta = "2 hari",
                                            insurancePrice = 0.0
                                        ),
                                        shopOrders = listOf(
                                            CartShopOrderData(
                                                shopId = "234",
                                                warehouseId = 345,
                                                shopTier = 456,
                                                products = listOf(
                                                    CartProductData(
                                                        productId = "9223372036854775807",
                                                        name = "asdf",
                                                        price = 4,
                                                        quantity = 2,
                                                        totalPrice = 8,
                                                        bundleGroupId = "",
                                                        addonItems = emptyList(),
                                                        category = CartProductCategoryData(
                                                            id = "12",
                                                            name = "asdfasdf",
                                                            identifier = "12asdf"
                                                        )
                                                    )
                                                ),
                                                bundle = emptyList(),
                                                addonItems = listOf(
                                                    CartAddOnData(
                                                        name = "gift",
                                                        price = 6,
                                                        quantity = 2,
                                                        totalPrice = 12
                                                    )
                                                ),
                                                cartStringOrder = "123"
                                            )
                                        )
                                    )
                                )
                            )
                        )
                    )
                ),
                promoDetail = PromoDetail(
                    benefitSummaryInfo = BenefitSummaryInfoData(),
                    voucherOrders = emptyList(),
                    additionalInfo = AdditionalInfoData()
                )
            ),
            paymentRequest
        )
    }
}
