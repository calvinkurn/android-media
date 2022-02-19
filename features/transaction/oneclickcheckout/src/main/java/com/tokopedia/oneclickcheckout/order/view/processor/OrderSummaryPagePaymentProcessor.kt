package com.tokopedia.oneclickcheckout.order.view.processor

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.oneclickcheckout.common.PAYMENT_CC_TYPE_TENOR_FULL
import com.tokopedia.oneclickcheckout.common.idling.OccIdlingResource
import com.tokopedia.oneclickcheckout.order.data.creditcard.CartDetailsItem
import com.tokopedia.oneclickcheckout.order.data.creditcard.CreditCardTenorListRequest
import com.tokopedia.oneclickcheckout.order.domain.CreditCardTenorListUseCase
import com.tokopedia.oneclickcheckout.order.view.model.*
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class OrderSummaryPagePaymentProcessor @Inject constructor(private val creditCardTenorListUseCase: CreditCardTenorListUseCase,
                                                           private val executorDispatchers: CoroutineDispatchers) {

    suspend fun getAdminFee(orderPaymentCreditCard: OrderPaymentCreditCard, userId: String,
                            orderCost: OrderCost, orderCart: OrderCart): List<OrderPaymentInstallmentTerm>? {
        OccIdlingResource.increment()
        val result = withContext(executorDispatchers.io) {
            try {
                val creditCardData = creditCardTenorListUseCase.executeSuspend(generateCreditCardTenorListRequest(orderPaymentCreditCard, userId, orderCost, orderCart))
                if (creditCardData.errorMsg.isNotEmpty()) {
                    return@withContext null
                } else {
                    return@withContext creditCardData.tenorList.map { mapAfpbToInstallmentTerm(it) }
                }
            } catch (t: Throwable) {
                Timber.d(t)
                return@withContext null
            }
        }
        OccIdlingResource.decrement()
        return result
    }

    private fun generateCreditCardTenorListRequest(orderPaymentCreditCard: OrderPaymentCreditCard,
                                                   userId: String, orderCost: OrderCost, orderCart: OrderCart): CreditCardTenorListRequest {
        val cartDetailsItemList = ArrayList<CartDetailsItem>()
        val paymentAmount = orderCost.totalItemPrice + orderCost.shippingFee
        val cartDetailsItem = CartDetailsItem(shopType = orderCart.shop.shopTier, paymentAmount = paymentAmount)
        cartDetailsItemList.add(cartDetailsItem)
        val totalDiscount = orderCost.productDiscountAmount + orderCost.shippingDiscountAmount
        val totalOtherAmount = orderCost.purchaseProtectionPrice + orderCost.insuranceFee + orderCost.addOnPrice

        return CreditCardTenorListRequest(
                tokenId = orderPaymentCreditCard.tokenId,
                userId = userId.toLong(),
                totalAmount = orderPaymentCreditCard.additionalData.totalProductPrice.toDouble(),
                profileCode = orderPaymentCreditCard.additionalData.profileCode,
                ccfeeSignature = orderPaymentCreditCard.tenorSignature,
                timestamp = orderPaymentCreditCard.unixTimestamp,
                otherAmount = totalOtherAmount,
                discountAmount = totalDiscount.toDouble(),
                cartDetails = cartDetailsItemList
        )
    }

    private fun mapAfpbToInstallmentTerm(tenor: TenorListData): OrderPaymentInstallmentTerm {
        var intTerm = 0
        if (tenor.type != PAYMENT_CC_TYPE_TENOR_FULL) intTerm = tenor.type.toInt()
        return OrderPaymentInstallmentTerm(
                term = intTerm,
                isEnable = !tenor.disable,
                fee = tenor.fee,
                monthlyAmount = tenor.amount,
                description = tenor.desc
        )
    }
}