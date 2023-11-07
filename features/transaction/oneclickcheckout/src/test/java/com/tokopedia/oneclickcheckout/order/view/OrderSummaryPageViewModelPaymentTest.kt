package com.tokopedia.oneclickcheckout.order.view

import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.InsuranceData
import com.tokopedia.oneclickcheckout.order.data.payment.AdditionalInfoData
import com.tokopedia.oneclickcheckout.order.data.payment.BenefitSummaryInfoData
import com.tokopedia.oneclickcheckout.order.data.payment.CartAddressData
import com.tokopedia.oneclickcheckout.order.data.payment.CartData
import com.tokopedia.oneclickcheckout.order.data.payment.CartDetail
import com.tokopedia.oneclickcheckout.order.data.payment.CartDetailData
import com.tokopedia.oneclickcheckout.order.data.payment.CartGroupData
import com.tokopedia.oneclickcheckout.order.data.payment.CartProductCategoryData
import com.tokopedia.oneclickcheckout.order.data.payment.CartProductData
import com.tokopedia.oneclickcheckout.order.data.payment.CartShippingInfoData
import com.tokopedia.oneclickcheckout.order.data.payment.CartShopOrderData
import com.tokopedia.oneclickcheckout.order.data.payment.PaymentData
import com.tokopedia.oneclickcheckout.order.data.payment.PaymentRequest
import com.tokopedia.oneclickcheckout.order.data.payment.PromoDetail
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
import com.tokopedia.oneclickcheckout.order.view.model.OrderShipment
import com.tokopedia.oneclickcheckout.order.view.model.OrderShop
import com.tokopedia.oneclickcheckout.order.view.model.OrderTotal
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
            OrderShop(shopId = "234", warehouseId = "345", shopTier = 456)
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
                categoryIdentifier = "12asdf"
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
                                                        price = 4.0,
                                                        quantity = 2,
                                                        totalPrice = 8.0,
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
                    benefitSummaryInfo = BenefitSummaryInfoData(),
                    voucherOrders = emptyList(),
                    additionalInfo = AdditionalInfoData()
                )
            ),
            paymentRequest
        )
    }
}
