package com.tokopedia.oneclickcheckout.order.view.processor

import com.tokopedia.oneclickcheckout.common.dispatchers.ExecutorDispatchers
import com.tokopedia.oneclickcheckout.common.idling.OccIdlingResource
import com.tokopedia.oneclickcheckout.order.analytics.OrderSummaryAnalytics
import com.tokopedia.promocheckout.common.domain.ClearCacheAutoApplyStackUseCase
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.ValidateUsePromoRevampUseCase
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel
import com.tokopedia.usecase.RequestParams
import kotlinx.coroutines.withContext
import javax.inject.Inject

class OrderSummaryPagePromoProcessor @Inject constructor(private val validateUsePromoRevampUseCase: ValidateUsePromoRevampUseCase,
                                                         private val clearCacheAutoApplyStackUseCase: ClearCacheAutoApplyStackUseCase,
                                                         private val orderSummaryAnalytics: OrderSummaryAnalytics,
                                                         private val executorDispatchers: ExecutorDispatchers) {

    suspend fun validateUsePromo(validateUsePromoRequest: ValidateUsePromoRequest, lastValidateUsePromoRevampUiModel: ValidateUsePromoRevampUiModel?): ValidateUsePromoRevampUiModel? {
        OccIdlingResource.increment()
        val resultValidateUse = withContext(executorDispatchers.io) {
            try {
                val result = validateUsePromoRevampUseCase.createObservable(RequestParams.create().apply {
                    putObject(ValidateUsePromoRevampUseCase.PARAM_VALIDATE_USE, validateUsePromoRequest)
                }).toBlocking().single()
                var isPromoReleased = false
                if (!lastValidateUsePromoRevampUiModel?.promoUiModel?.codes.isNullOrEmpty() && result.promoUiModel.codes.isNotEmpty() && result.promoUiModel.messageUiModel.state == "red") {
                    isPromoReleased = true
                } else {
                    result.promoUiModel.voucherOrderUiModels.firstOrNull { it?.messageUiModel?.state == "red" }?.let {
                        isPromoReleased = true
                    }
                }
                if (isPromoReleased) {
                    orderSummaryAnalytics.eventViewPromoDecreasedOrReleased(true)
                } else if (lastValidateUsePromoRevampUiModel != null && result.promoUiModel.benefitSummaryInfoUiModel.finalBenefitAmount < lastValidateUsePromoRevampUiModel.promoUiModel.benefitSummaryInfoUiModel.finalBenefitAmount) {
                    orderSummaryAnalytics.eventViewPromoDecreasedOrReleased(false)
                }
                return@withContext result
            } catch (t: Throwable) {
                return@withContext null
            }
        }
        OccIdlingResource.decrement()
        return resultValidateUse
    }

    suspend fun clearOldLogisticPromo(oldPromoCode: String) {
        withContext(executorDispatchers.io) {
            try {
                clearCacheAutoApplyStackUseCase.setParams(ClearCacheAutoApplyStackUseCase.PARAM_VALUE_MARKETPLACE, arrayListOf(oldPromoCode), true)
                clearCacheAutoApplyStackUseCase.createObservable(RequestParams.EMPTY).toBlocking().single()
            } catch (t: Throwable) {
                //ignore throwable
            }
        }
    }
}