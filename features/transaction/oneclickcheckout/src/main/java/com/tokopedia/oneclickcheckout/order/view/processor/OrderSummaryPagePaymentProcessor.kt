package com.tokopedia.oneclickcheckout.order.view.processor

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.oneclickcheckout.common.PAYMENT_CC_TYPE_TENOR_FULL
import com.tokopedia.oneclickcheckout.common.idling.OccIdlingResource
import com.tokopedia.oneclickcheckout.order.data.creditcard.CartDetailsItem
import com.tokopedia.oneclickcheckout.order.data.creditcard.CreditCardTenorListRequest
import com.tokopedia.oneclickcheckout.order.data.gocicil.GoCicilInstallmentOption
import com.tokopedia.oneclickcheckout.order.data.gocicil.GoCicilInstallmentRequest
import com.tokopedia.oneclickcheckout.order.data.payment.PaymentFeeRequest
import com.tokopedia.oneclickcheckout.order.domain.CreditCardTenorListUseCase
import com.tokopedia.oneclickcheckout.order.domain.DynamicPaymentFeeUseCase
import com.tokopedia.oneclickcheckout.order.domain.GoCicilInstallmentOptionUseCase
import com.tokopedia.oneclickcheckout.order.view.model.OrderCart
import com.tokopedia.oneclickcheckout.order.view.model.OrderCost
import com.tokopedia.oneclickcheckout.order.view.model.OrderPayment
import com.tokopedia.oneclickcheckout.order.view.model.OrderPaymentCreditCard
import com.tokopedia.oneclickcheckout.order.view.model.OrderPaymentFee
import com.tokopedia.oneclickcheckout.order.view.model.OrderPaymentGoCicilTerms
import com.tokopedia.oneclickcheckout.order.view.model.OrderPaymentInstallmentTerm
import com.tokopedia.oneclickcheckout.order.view.model.OrderProfile
import com.tokopedia.oneclickcheckout.order.view.model.TenorListData
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class OrderSummaryPagePaymentProcessor @Inject constructor(
    private val creditCardTenorListUseCase: CreditCardTenorListUseCase,
    private val goCicilInstallmentOptionUseCase: GoCicilInstallmentOptionUseCase,
    private val dynamicPaymentFeeUseCase: DynamicPaymentFeeUseCase,
    private val executorDispatchers: CoroutineDispatchers
) {

    suspend fun getCreditCardAdminFee(
        orderPaymentCreditCard: OrderPaymentCreditCard,
        userId: String,
        orderCost: OrderCost,
        orderCart: OrderCart
    ): List<OrderPaymentInstallmentTerm>? {
        OccIdlingResource.increment()
        val result = withContext(executorDispatchers.io) {
            try {
                val creditCardData = creditCardTenorListUseCase.executeSuspend(
                    generateCreditCardTenorListRequest(
                        orderPaymentCreditCard,
                        userId,
                        orderCost,
                        orderCart
                    )
                )
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

    private fun generateCreditCardTenorListRequest(
        orderPaymentCreditCard: OrderPaymentCreditCard,
        userId: String,
        orderCost: OrderCost,
        orderCart: OrderCart
    ): CreditCardTenorListRequest {
        val cartDetailsItemList = ArrayList<CartDetailsItem>()
        val cartDetailsItem = CartDetailsItem(
            shopType = orderCart.shop.shopTier,
            paymentAmount = orderCost.totalItemPriceAndShippingFee
        )
        cartDetailsItemList.add(cartDetailsItem)

        return CreditCardTenorListRequest(
            tokenId = orderPaymentCreditCard.tokenId,
            userId = userId.toLong(),
            totalAmount = orderPaymentCreditCard.additionalData.totalProductPrice.toDouble(),
            profileCode = orderPaymentCreditCard.additionalData.profileCode,
            ccfeeSignature = orderPaymentCreditCard.tenorSignature,
            timestamp = orderPaymentCreditCard.unixTimestamp,
            otherAmount = orderCost.totalAdditionalFee,
            discountAmount = orderCost.totalDiscounts.toDouble(),
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

    fun generateGoCicilInstallmentRequest(
        orderPayment: OrderPayment,
        userId: String,
        orderCost: OrderCost,
        orderCart: OrderCart,
        orderProfile: OrderProfile,
        promoCodes: List<String>
    ): GoCicilInstallmentRequest {
        return GoCicilInstallmentRequest(
            gatewayCode = orderPayment.gatewayCode,
            merchantCode = orderPayment.creditCard.additionalData.merchantCode,
            profileCode = orderPayment.creditCard.additionalData.profileCode,
            userId = userId,
            paymentAmount = orderCost.totalPriceWithoutPaymentFees,
            merchantType = orderCart.shop.merchantType,
            address = orderProfile.address,
            shop = orderCart.shop,
            products = orderCart.products,
            promoCodes = promoCodes
        )
    }

    suspend fun getGopayAdminFee(
        request: GoCicilInstallmentRequest,
        orderPayment: OrderPayment
    ): ResultGetGoCicilInstallment? {
        OccIdlingResource.increment()
        val result = withContext(executorDispatchers.io) {
            try {
                val response = goCicilInstallmentOptionUseCase.executeSuspend(
                    request
                    )
                val installmentList = mapInstallmentOptions(
                    response.installmentOptions
                )
                var selectedTerm = orderPayment.walletData.goCicilData.selectedTerm
                var shouldUpdateCart = false
                if (selectedTerm == null) {
                    shouldUpdateCart = true
                    selectedTerm = autoSelectGoCicilTerm(
                        orderPayment.walletData.goCicilData.selectedTenure,
                        installmentList
                    )
                }
                val selectedInstallment =
                    installmentList.first { it.installmentTerm == selectedTerm.installmentTerm }
                return@withContext ResultGetGoCicilInstallment(
                    selectedInstallment,
                    installmentList,
                    response.ticker.message,
                    shouldUpdateCart
                )
            } catch (t: Throwable) {
                Timber.d(t)
                return@withContext null
            }
        }
        OccIdlingResource.decrement()
        return result
    }

    private fun mapInstallmentOptions(
        installmentOptions: List<GoCicilInstallmentOption>
    ): List<OrderPaymentGoCicilTerms> {
        return installmentOptions.map {
            OrderPaymentGoCicilTerms(
                installmentTerm = it.installmentTerm,
                optionId = it.optionId,
                firstInstallmentDate = it.firstInstallmentTime,
                lastInstallmentDate = it.estInstallmentEnd,
                firstDueMessage = it.firstDueMessage,
                interestAmount = it.interestAmount,
                feeAmount = it.feeAmount,
                installmentAmountPerPeriod = it.installmentAmountPerPeriod,
                labelType = it.labelType,
                labelMessage = it.labelMessage,
                isActive = it.isActive,
                description = it.description,
                isRecommended = it.isRecommended
            )
        }
    }

    private fun autoSelectGoCicilTerm(
        selectedTenure: Int,
        installmentTerms: List<OrderPaymentGoCicilTerms>
    ): OrderPaymentGoCicilTerms {
        var selectedTerm: OrderPaymentGoCicilTerms?
        if (selectedTenure > 0) {
            selectedTerm = installmentTerms.firstOrNull { it.installmentTerm == selectedTenure }
            if (selectedTerm != null) {
                return selectedTerm
            }
        }
        selectedTerm = installmentTerms.lastOrNull { it.isRecommended && it.isActive }
        if (selectedTerm == null) {
            selectedTerm = installmentTerms.lastOrNull { it.isActive }
        }
        if (selectedTerm == null) {
            selectedTerm = installmentTerms.last()
        }
        return selectedTerm
    }

    suspend fun getPaymentFee(
        orderPayment: OrderPayment,
        orderCost: OrderCost
    ): List<OrderPaymentFee>? {
        OccIdlingResource.increment()
        val result = withContext(executorDispatchers.io) {
            try {
                return@withContext dynamicPaymentFeeUseCase(
                    PaymentFeeRequest(
                        orderPayment.creditCard.additionalData.profileCode,
                        orderPayment.gatewayCode,
                        orderCost.totalPriceWithoutPaymentFees
                    )
                )
            } catch (t: Throwable) {
                Timber.d(t)
                return@withContext null
            }
        }
        OccIdlingResource.decrement()
        return result
    }
}

class ResultGetGoCicilInstallment(
    val selectedInstallment: OrderPaymentGoCicilTerms,
    val installmentList: List<OrderPaymentGoCicilTerms>,
    val tickerMessage: String,
    val shouldUpdateCart: Boolean
)
