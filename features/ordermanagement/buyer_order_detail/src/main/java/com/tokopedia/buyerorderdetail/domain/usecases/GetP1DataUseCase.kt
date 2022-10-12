package com.tokopedia.buyerorderdetail.domain.usecases

import com.tokopedia.buyerorderdetail.domain.models.GetInsuranceDetailParams
import com.tokopedia.buyerorderdetail.domain.models.GetInsuranceDetailRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetOrderResolutionRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetP1DataParams
import com.tokopedia.buyerorderdetail.domain.models.GetP1DataRequestState
import com.tokopedia.kotlin.extensions.orFalse
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
    private fun getOrderResolutionUseCaseRequestStates(hasResoStatus: Boolean, orderId: Long) =
        flow {
            if (hasResoStatus) {
                emitAll(executeOrderResolutionUseCase(orderId))
            } else {
                emit(GetOrderResolutionRequestState.Success(null))
            }
        }

    private suspend fun executeOrderResolutionUseCase(orderId: Long) = getOrderResolutionUseCase(orderId)

    private fun getInsuranceDetailUseCaseRequestStates(hasInsurance: Boolean, invoice: String) = flow {
        if (hasInsurance) {
            emitAll(executeInsuranceDetailUseCase(GetInsuranceDetailParams(invoice)))
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
            getOrderResolutionUseCaseRequestStates(params.hasResoStatus.orFalse(), params.orderId),
            getInsuranceDetailUseCaseRequestStates(params.hasInsurance, params.invoice)
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
