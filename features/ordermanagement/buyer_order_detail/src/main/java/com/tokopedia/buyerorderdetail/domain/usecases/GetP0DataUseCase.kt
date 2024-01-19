package com.tokopedia.buyerorderdetail.domain.usecases

import com.tokopedia.analytics.performance.util.EmbraceMonitoring
import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailParams
import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetP0DataParams
import com.tokopedia.buyerorderdetail.domain.models.GetP0DataRequestState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import javax.inject.Inject

class GetP0DataUseCase @Inject constructor(
    private val getBuyerOrderDetailUseCase: GetBuyerOrderDetailUseCase
) {

    suspend operator fun invoke(params: GetP0DataParams) = execute(params).flowOn(Dispatchers.IO)

    private fun mapToGetP0DataRequestState(
        getBuyerOrderDetailRequestState: GetBuyerOrderDetailRequestState
    ): GetP0DataRequestState {
        return when (getBuyerOrderDetailRequestState) {
            is GetBuyerOrderDetailRequestState.Requesting -> {
                GetP0DataRequestState.Requesting(getBuyerOrderDetailRequestState)
            }
            else -> {
                GetP0DataRequestState.Complete(getBuyerOrderDetailRequestState)
            }
        }
    }

    private suspend fun execute(params: GetP0DataParams) = getBuyerOrderDetailUseCase(
        with(params) { GetBuyerOrderDetailParams(cart, orderId, paymentId, shouldCheckCache) }
    ).map {
        mapToGetP0DataRequestState(it)
    }.catch {
        emit(GetP0DataRequestState.Complete(GetBuyerOrderDetailRequestState.Complete.Error(it)))
    }.onCompletion {
        logCompletionBreadcrumb(params, it)
    }

    private fun logCompletionBreadcrumb(params: GetP0DataParams, throwable: Throwable?) {
        runCatching {
            if (throwable == null) {
                EmbraceMonitoring.logBreadcrumb("GetP0DataUseCase - Success: $params")
            } else {
                EmbraceMonitoring.logBreadcrumb("GetP0DataUseCase - Error: ${throwable.stackTraceToString()}")
            }
        }
    }
}
