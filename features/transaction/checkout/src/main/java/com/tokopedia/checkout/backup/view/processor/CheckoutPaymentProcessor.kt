package com.tokopedia.checkout.backup.view.processor

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.checkout.backup.view.BackupCheckoutVM
import com.tokopedia.checkout.domain.model.cartshipmentform.ShipmentPlatformFeeData
import com.tokopedia.checkout.domain.model.platformfee.PaymentFeeCheckoutRequest
import com.tokopedia.checkout.domain.model.platformfee.PaymentFeeResponse
import com.tokopedia.checkout.domain.usecase.GetPaymentFeeCheckoutUseCase
import com.tokopedia.checkout.backup.view.uimodel.CheckoutCostModel
import com.tokopedia.checkout.view.uimodel.ShipmentPaymentFeeModel
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsCourierSelection
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.currency.CurrencyFormatUtil
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class CheckoutPaymentProcessor @Inject constructor(
    private val getPaymentFeeCheckoutUseCase: GetPaymentFeeCheckoutUseCase,
    private val checkoutAnalyticsCourierSelection: CheckoutAnalyticsCourierSelection,
    private val userSessionInterface: UserSessionInterface,
    private val dispatchers: CoroutineDispatchers
) {

    suspend fun checkPlatformFee(
        shipmentPlatformFeeData: ShipmentPlatformFeeData,
        cost: CheckoutCostModel,
        request: PaymentFeeCheckoutRequest
    ): CheckoutCostModel {
        if (!cost.hasSelectAllShipping) {
            return cost.copy(dynamicPlatformFee = ShipmentPaymentFeeModel(isLoading = false))
        }
        if (shipmentPlatformFeeData.isEnable) {
            val platformFeeModel = cost.dynamicPlatformFee
            if (cost.totalPrice > platformFeeModel.minRange &&
                cost.totalPrice < platformFeeModel.maxRange
            ) {
                return cost.copy(dynamicPlatformFee = platformFeeModel.copy(isLoading = false))
            } else {
                val paymentFee = getDynamicPaymentFee(request)
                if (paymentFee != null) {
                    val platformFee = ShipmentPaymentFeeModel()
                    for (fee in paymentFee.data) {
                        if (fee.code.equals(
                                BackupCheckoutVM.PLATFORM_FEE_CODE,
                                ignoreCase = true
                            )
                        ) {
                            platformFee.title = fee.title
                            platformFee.fee = fee.fee
                            platformFee.minRange = fee.minRange
                            platformFee.maxRange = fee.maxRange
                            platformFee.isShowTooltip = fee.showTooltip
                            platformFee.tooltip = fee.tooltipInfo
                            platformFee.isShowSlashed = fee.showSlashed
                            platformFee.slashedFee = fee.slashedFee.toDouble()
                        }
                    }
                    checkoutAnalyticsCourierSelection.eventViewPlatformFeeInCheckoutPage(
                        userSessionInterface.userId,
                        CurrencyFormatUtil.convertPriceValueToIdrFormat(
                            platformFee.fee.toLong(),
                            false
                        ).removeDecimalSuffix()
                    )
                    return cost.copy(dynamicPlatformFee = platformFee)
                } else {
                    return cost.copy(
                        dynamicPlatformFee = ShipmentPaymentFeeModel(
                            isShowTicker = true,
                            ticker = shipmentPlatformFeeData.errorWording
                        )
                    )
                }
            }
        }
        return cost.copy(dynamicPlatformFee = cost.dynamicPlatformFee.copy(isLoading = false))
    }

    private suspend fun getDynamicPaymentFee(request: PaymentFeeCheckoutRequest): PaymentFeeResponse? {
        return withContext(dispatchers.io) {
            try {
                getPaymentFeeCheckoutUseCase.setParams(request)
                val paymentFeeGqlResponse = getPaymentFeeCheckoutUseCase.executeOnBackground()
                if (paymentFeeGqlResponse.response.success) {
                    return@withContext paymentFeeGqlResponse.response
                } else {
                    return@withContext null
                }
            } catch (t: Throwable) {
                Timber.d(t)
                return@withContext null
            }
        }
    }
}
