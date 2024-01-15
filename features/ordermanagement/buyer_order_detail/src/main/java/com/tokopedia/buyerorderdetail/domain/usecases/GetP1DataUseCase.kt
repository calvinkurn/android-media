package com.tokopedia.buyerorderdetail.domain.usecases

import com.tokopedia.analytics.performance.util.EmbraceMonitoring
import com.tokopedia.buyerorderdetail.domain.models.GetInsuranceDetailParams
import com.tokopedia.buyerorderdetail.domain.models.GetInsuranceDetailRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetOrderResolutionParams
import com.tokopedia.buyerorderdetail.domain.models.GetOrderResolutionRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetP1DataParams
import com.tokopedia.buyerorderdetail.domain.models.GetP1DataRequestState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import javax.inject.Inject

class GetP1DataUseCase @Inject constructor(
    private val getOrderResolutionUseCase: GetOrderResolutionUseCase,
    private val getInsuranceDetailUseCase: GetInsuranceDetailUseCase
) {

    operator fun invoke(params: GetP1DataParams): Flow<GetP1DataRequestState> {
        logInvocationBreadcrumb(params)
        return execute(params).flowOn(Dispatchers.IO)
    }
    private fun getOrderResolutionUseCaseRequestStates(params: GetP1DataParams) = flow {
        if (params.hasResoStatus) {
            emitAll(
                executeOrderResolutionUseCase(
                    GetOrderResolutionParams(params.orderId, params.shouldCheckCache)
                )
            )
        } else {
            emit(GetOrderResolutionRequestState.Complete.Success(null))
        }
    }

    private suspend fun executeOrderResolutionUseCase(
        params: GetOrderResolutionParams
    ) = getOrderResolutionUseCase(params)

    private fun getInsuranceDetailUseCaseRequestStates(params: GetP1DataParams) = flow {
        if (params.hasInsurance) {
            emitAll(
                executeInsuranceDetailUseCase(
                    GetInsuranceDetailParams(params.invoice, params.shouldCheckCache)
                )
            )
        } else {
            emit(GetInsuranceDetailRequestState.Complete.Success(null))
        }
    }

    private suspend fun executeInsuranceDetailUseCase(params: GetInsuranceDetailParams) = getInsuranceDetailUseCase(params)

    private fun mapP1UseCasesRequestState(
        orderResolutionRequestState: GetOrderResolutionRequestState,
        insuranceDetailRequestState: GetInsuranceDetailRequestState
    ): GetP1DataRequestState {
        logMapperBreadcrumb(orderResolutionRequestState, insuranceDetailRequestState)
        return if (
            orderResolutionRequestState is GetOrderResolutionRequestState.Requesting ||
            insuranceDetailRequestState is GetInsuranceDetailRequestState.Requesting
        ) {
            GetP1DataRequestState.Requesting(orderResolutionRequestState, insuranceDetailRequestState)
        } else {
            GetP1DataRequestState.Complete(orderResolutionRequestState, insuranceDetailRequestState)
        }.logBreadcrumb()
    }

    private fun execute(params: GetP1DataParams): Flow<GetP1DataRequestState> {
        return combine(
            getOrderResolutionUseCaseRequestStates(params),
            getInsuranceDetailUseCaseRequestStates(params)
        ) { flows ->
            mapP1UseCasesRequestState(
                flows[0] as GetOrderResolutionRequestState, // please make sure that flow[0] is GetOrderResolutionRequestState after editing the flow source
                flows[1] as GetInsuranceDetailRequestState // please make sure that flow[1] is GetInsuranceDetailRequestState after editing the flow source
            )
        }.catch {
            EmbraceMonitoring.logBreadcrumb("Error fetching P1 data with error: ${it.stackTraceToString()}")
            emit(
                GetP1DataRequestState.Complete(
                    GetOrderResolutionRequestState.Complete.Error(it),
                    GetInsuranceDetailRequestState.Complete.Error(it)
                )
            )
        }.onCompletion {
            EmbraceMonitoring.logBreadcrumb("Finish fetching P1 data")
        }
    }

    private fun logInvocationBreadcrumb(params: GetP1DataParams) {
        runCatching {
            EmbraceMonitoring.logBreadcrumb("Fetching P1 data with params: $params")
        }
    }

    private fun logMapperBreadcrumb(
        orderResolutionRequestState: GetOrderResolutionRequestState,
        insuranceDetailRequestState: GetInsuranceDetailRequestState
    ) {
        runCatching {
            EmbraceMonitoring.logBreadcrumb("Mapping P1 data: ${orderResolutionRequestState::class.java.simpleName}, ${insuranceDetailRequestState::class.java.simpleName}")
        }
    }

    private fun GetP1DataRequestState.logBreadcrumb() = also {
        EmbraceMonitoring.logBreadcrumb("Finish mapping P1 data into ${this::class.java.simpleName}")
    }
}
