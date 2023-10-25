package com.tokopedia.oneclickcheckout.order.view.processor

import android.annotation.SuppressLint
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.oneclickcheckout.common.PAYMENT_CC_TYPE_TENOR_FULL
import com.tokopedia.oneclickcheckout.common.idling.OccIdlingResource
import com.tokopedia.oneclickcheckout.order.data.creditcard.CartDetailsItem
import com.tokopedia.oneclickcheckout.order.data.creditcard.CreditCardTenorListRequest
import com.tokopedia.oneclickcheckout.order.data.gocicil.GoCicilInstallmentOption
import com.tokopedia.oneclickcheckout.order.data.gocicil.GoCicilInstallmentRequest
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
import com.tokopedia.oneclickcheckout.order.data.payment.DetailsItemData
import com.tokopedia.oneclickcheckout.order.data.payment.PaymentData
import com.tokopedia.oneclickcheckout.order.data.payment.PaymentFeeRequest
import com.tokopedia.oneclickcheckout.order.data.payment.PaymentRequest
import com.tokopedia.oneclickcheckout.order.data.payment.PromoDetail
import com.tokopedia.oneclickcheckout.order.data.payment.SummariesItemData
import com.tokopedia.oneclickcheckout.order.data.payment.UsageSummariesData
import com.tokopedia.oneclickcheckout.order.data.payment.VoucherOrderItemData
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
import com.tokopedia.oneclickcheckout.order.view.model.OrderPromo
import com.tokopedia.oneclickcheckout.order.view.model.OrderShipment
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
        if (tenor.type != PAYMENT_CC_TYPE_TENOR_FULL) intTerm = tenor.type.toIntOrZero()
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

    @SuppressLint("PII Data Exposure")
    fun generatePaymentRequest(
        orderCart: OrderCart,
        orderShipment: OrderShipment,
        orderPayment: OrderPayment,
        orderCost: OrderCost,
        orderProfile: OrderProfile,
        orderPromo: OrderPromo
    ): PaymentRequest {
        return PaymentRequest(
            payment = PaymentData(
                gatewayCode = orderPayment.gatewayCode,
                profileCode = orderPayment.creditCard.additionalData.profileCode,
                paymentAmount = orderCost.totalPriceWithoutPaymentFees
            ),
            CartDetail(
                cart = CartData(
                    data = listOf(
                        CartDetailData(
                            address = CartAddressData(
                                id = orderProfile.address.addressId,
                                address = orderProfile.address.addressStreet,
                                state = orderProfile.address.provinceName,
                                city = orderProfile.address.cityName,
                                country = orderProfile.address.country,
                                postalCode = orderProfile.address.postalCode
                            ),
                            listOf(
                                CartGroupData(
                                    cartStringGroup = orderCart.cartString,
                                    shippingInfo = CartShippingInfoData(
                                        spId = orderShipment.getRealShipperProductId().toString(),
                                        originalShippingPrice = orderShipment.getRealOriginalPrice().toDouble(),
                                        serviceName = orderShipment.getRealServiceName(),
                                        shipperName = orderShipment.getRealShipperName(),
                                        eta = orderShipment.getRealServiceEta(),
                                        insurancePrice = orderShipment.getRealInsurancePrice().toDouble()
                                    ),
                                    shopOrders = listOf(
                                        CartShopOrderData(
                                            shopId = orderCart.shop.shopId,
                                            warehouseId = orderCart.shop.warehouseId.toLongOrZero(),
                                            shopTier = orderCart.shop.shopTier.toLong(),
                                            products = orderCart.products.map { orderProduct ->
                                                CartProductData(
                                                    productId = orderProduct.productId,
                                                    name = orderProduct.productName,
                                                    price = orderProduct.finalPrice,
                                                    quantity = orderProduct.orderQuantity.toLong(),
                                                    totalPrice = orderProduct.finalPrice * orderProduct.orderQuantity,
                                                    bundleGroupId = 0,
                                                    addonItems = emptyList(),
                                                    category = CartProductCategoryData(
                                                        id = orderProduct.categoryId,
                                                        name = orderProduct.lastLevelCategory,
                                                        identifier = orderProduct.categoryIdentifier
                                                    )
                                                )
                                            },
                                            bundle = emptyList(),
                                            addonItems = emptyList(),
                                            cartStringOrder = orderCart.cartString
                                        )
                                    )
                                )
                            )
                        )
                    )
                )
            ),
            PromoDetail(
                benefitSummaryInfo = BenefitSummaryInfoData(
                    orderPromo.lastApply.benefitSummaryInfo.summaries.map { summary ->
                        SummariesItemData(
                            type = summary.type,
                            details = summary.details.map { detail ->
                                DetailsItemData(
                                    amount = detail.amount,
                                    type = detail.type
                                )
                            }
                        )
                    }
                ),
                voucherOrders = orderPromo.lastApply.voucherOrders.map { voucher ->
                    VoucherOrderItemData(
                        code = voucher.code,
                        uniqueId = voucher.uniqueId,
                        shippingId = voucher.shippingId.toLong(),
                        spId = voucher.spId.toLong(),
                        type = voucher.type,
                        success = voucher.success,
                        cartStringGroup = voucher.cartStringGroup
                    )
                },
                additionalInfo = AdditionalInfoData(
                    usageSummaries = orderPromo.lastApply.additionalInfo.usageSummaries.map { usage ->
                        UsageSummariesData(
                            type = usage.type,
                            amountString = usage.amountStr,
                            amount = usage.amount
                        )
                    }
                )
            ),
            ""
        )
    }
}

class ResultGetGoCicilInstallment(
    val selectedInstallment: OrderPaymentGoCicilTerms,
    val installmentList: List<OrderPaymentGoCicilTerms>,
    val tickerMessage: String,
    val shouldUpdateCart: Boolean
)
