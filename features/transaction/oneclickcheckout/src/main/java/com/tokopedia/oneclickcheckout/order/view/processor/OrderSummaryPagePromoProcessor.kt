package com.tokopedia.oneclickcheckout.order.view.processor

import com.tokopedia.oneclickcheckout.common.STATUS_OK
import com.tokopedia.oneclickcheckout.common.dispatchers.ExecutorDispatchers
import com.tokopedia.oneclickcheckout.common.idling.OccIdlingResource
import com.tokopedia.oneclickcheckout.common.view.model.OccGlobalEvent
import com.tokopedia.oneclickcheckout.order.analytics.OrderSummaryAnalytics
import com.tokopedia.oneclickcheckout.order.view.OrderSummaryPageViewModel
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

    suspend fun validateUseLogisticPromo(validateUsePromoRequest: ValidateUsePromoRequest, logisticPromoCode: String): Triple<Boolean, ValidateUsePromoRevampUiModel?, OccGlobalEvent> {
        OccIdlingResource.increment()
        val resultValidateUse = withContext(executorDispatchers.io) {
            try {
                val response = validateUsePromoRevampUseCase.createObservable(RequestParams.create().apply {
                    putObject(ValidateUsePromoRevampUseCase.PARAM_VALIDATE_USE, validateUsePromoRequest)
                }).toBlocking().single()
                if (response.status.equals(STATUS_OK, true)) {
                    val voucherOrderUiModel = response.promoUiModel.voucherOrderUiModels.firstOrNull { it?.code == logisticPromoCode }
                    if (voucherOrderUiModel != null && voucherOrderUiModel.messageUiModel.state != "red") {
                        return@withContext Triple(true, response, OccGlobalEvent.Normal)
                    }
                }
                return@withContext Triple(false, response, OccGlobalEvent.Error(errorMessage = OrderSummaryPageViewModel.FAIL_APPLY_BBO_ERROR_MESSAGE))
            } catch (t: Throwable) {
                return@withContext Triple(false, null, OccGlobalEvent.Error(t.cause ?: t))
            }
        }
        OccIdlingResource.decrement()
        return resultValidateUse
    }

    suspend fun finalValidateUse(validateUsePromoRequest: ValidateUsePromoRequest): Pair<ValidateUsePromoRevampUiModel?, OccGlobalEvent> {
        OccIdlingResource.increment()
        val resultValidateUse = withContext(executorDispatchers.io) {
            try {
                val response = validateUsePromoRevampUseCase.createObservable(RequestParams.create().apply {
                    putObject(ValidateUsePromoRevampUseCase.PARAM_VALIDATE_USE, validateUsePromoRequest)
                }).toBlocking().single()
                return@withContext response to OccGlobalEvent.Loading
            } catch (t: Throwable) {
                return@withContext null to OccGlobalEvent.TriggerRefresh(throwable = t)
            }
        }
        OccIdlingResource.decrement()
        return resultValidateUse
    }

    suspend fun cancelIneligiblePromoCheckout(promoCodeList: ArrayList<String>): Pair<Boolean, OccGlobalEvent> {
        OccIdlingResource.increment()
        val result = withContext(executorDispatchers.io) {
            try {
                clearCacheAutoApplyStackUseCase.setParams(ClearCacheAutoApplyStackUseCase.PARAM_VALUE_MARKETPLACE, promoCodeList, true)
                clearCacheAutoApplyStackUseCase.createObservable(RequestParams.EMPTY).toBlocking().single()
                return@withContext true to OccGlobalEvent.Loading
            } catch (t: Throwable) {
                return@withContext false to OccGlobalEvent.Error(t)
            }
        }
        OccIdlingResource.decrement()
        return result
    }
}