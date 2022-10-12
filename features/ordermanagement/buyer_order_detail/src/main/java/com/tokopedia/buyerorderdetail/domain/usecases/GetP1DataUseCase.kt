package com.tokopedia.buyerorderdetail.domain.usecases

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
import javax.inject.Inject

class GetP1DataUseCase @Inject constructor(
    private val getOrderResolutionUseCase: GetOrderResolutionUseCase,
    private val getInsuranceDetailUseCase: GetInsuranceDetailUseCase
) {
    private fun getOrderResolutionUseCaseRequestStates(params: GetP1DataParams) = flow {
        if (params.hasResoStatus) {
            emitAll(
                executeOrderResolutionUseCase(
                    GetOrderResolutionParams(params.orderId, params.shouldCheckCache)
                )
            )
        } else {
            emit(GetOrderResolutionRequestState.Success(null))
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
            emit(GetInsuranceDetailRequestState.Success(null))
        }
    }

    private suspend fun executeInsuranceDetailUseCase(params: GetInsuranceDetailParams) = getInsuranceDetailUseCase(params)

    private fun mapP1UseCasesRequestState(
        orderResolutionRequestState: GetOrderResolutionRequestState,
        insuranceDetailRequestState: GetInsuranceDetailRequestState
    ): GetP1DataRequestState {
        return if (
            orderResolutionRequestState is GetOrderResolutionRequestState.Requesting ||
            insuranceDetailRequestState is GetInsuranceDetailRequestState.Requesting
        ) {
            GetP1DataRequestState.Requesting(orderResolutionRequestState, insuranceDetailRequestState)
        } else {
            GetP1DataRequestState.Complete(orderResolutionRequestState, insuranceDetailRequestState)
        }
    }

    private fun execute(params: GetP1DataParams): Flow<GetP1DataRequestState> {
        return combine(
            getOrderResolutionUseCaseRequestStates(params),
            getInsuranceDetailUseCaseRequestStates(params)
        ) { flows ->
            mapP1UseCasesRequestState(
                flows[0] as GetOrderResolutionRequestState,
                flows[1] as GetInsuranceDetailRequestState
            )
        }.catch {
            emit(
                GetP1DataRequestState.Complete(
                    GetOrderResolutionRequestState.Error(it),
                    GetInsuranceDetailRequestState.Error(it)
                )
            )
        }
    }

    operator fun invoke(params: GetP1DataParams) = execute(params).flowOn(Dispatchers.IO)
}
