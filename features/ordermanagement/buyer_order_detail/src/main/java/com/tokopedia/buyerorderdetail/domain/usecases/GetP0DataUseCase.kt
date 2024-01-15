package com.tokopedia.buyerorderdetail.domain.usecases

import com.tokopedia.analytics.performance.util.EmbraceMonitoring
import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailParams
import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetP0DataParams
import com.tokopedia.buyerorderdetail.domain.models.GetP0DataRequestState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import javax.inject.Inject

class GetP0DataUseCase @Inject constructor(
    private val getBuyerOrderDetailUseCase: GetBuyerOrderDetailUseCase
) {

    suspend operator fun invoke(params: GetP0DataParams): Flow<GetP0DataRequestState> {
        logInvocationBreadcrumb(params)
        return execute(params).flowOn(Dispatchers.IO)
    }
    private fun mapToGetP0DataRequestState(
        getBuyerOrderDetailRequestState: GetBuyerOrderDetailRequestState
    ): GetP0DataRequestState {
        logMapperBreadcrumb(getBuyerOrderDetailRequestState)
        return when (getBuyerOrderDetailRequestState) {
            is GetBuyerOrderDetailRequestState.Requesting -> {
                GetP0DataRequestState.Requesting(getBuyerOrderDetailRequestState)
            }
            else -> GetP0DataRequestState.Complete(getBuyerOrderDetailRequestState)
        }
    }

    private suspend fun execute(params: GetP0DataParams) = getBuyerOrderDetailUseCase(
        with(params) { GetBuyerOrderDetailParams(cart, orderId, paymentId, shouldCheckCache) }
    ).map(::mapToGetP0DataRequestState).catch {
        EmbraceMonitoring.logBreadcrumb("Error fetching P0 data with error: ${it.stackTraceToString()}")
        emit(GetP0DataRequestState.Complete(GetBuyerOrderDetailRequestState.Complete.Error(it)))
    }.onCompletion {
        EmbraceMonitoring.logBreadcrumb("Finish fetching P0 data")
    }

    private fun logInvocationBreadcrumb(params: GetP0DataParams) {
        runCatching {
            EmbraceMonitoring.logBreadcrumb("Fetching P0 data with params: $params")
        }
    }

    private fun logMapperBreadcrumb(buyerOrderDetailRequestState: GetBuyerOrderDetailRequestState) {
        EmbraceMonitoring.logBreadcrumb("Mapping P0 data: ${buyerOrderDetailRequestState::class.java.simpleName}")
    }
}
