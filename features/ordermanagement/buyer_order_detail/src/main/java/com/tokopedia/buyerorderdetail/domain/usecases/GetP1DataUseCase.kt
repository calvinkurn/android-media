package com.tokopedia.buyerorderdetail.domain.usecases

import com.tokopedia.buyerorderdetail.domain.models.GetInsuranceDetailParams
import com.tokopedia.buyerorderdetail.domain.models.GetInsuranceDetailRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetOrderResolutionRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetP0DataRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetP1DataRequestState
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.toLongOrZero
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
    private fun getOrderResolutionUseCaseRequestStates(hasResoStatus: Boolean, orderId: Long) = flow {
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
        getInsuranceDetailRequestState: GetInsuranceDetailRequestState
    ): GetP1DataRequestState {
        return if (
            orderResolutionRequestState is GetOrderResolutionRequestState.Requesting ||
            getInsuranceDetailRequestState is GetInsuranceDetailRequestState.Requesting
        ) {
            GetP1DataRequestState.Requesting(orderResolutionRequestState, getInsuranceDetailRequestState)
        } else {
            GetP1DataRequestState.Complete(orderResolutionRequestState, getInsuranceDetailRequestState)
        }
    }

    fun getP1Data(p0DataRequestState: GetP0DataRequestState.Success): Flow<GetP1DataRequestState> = combine(
        getOrderResolutionUseCaseRequestStates(
            p0DataRequestState.getBuyerOrderDetailRequestState.result.hasResoStatus.orFalse(),
            p0DataRequestState.getBuyerOrderDetailRequestState.result.orderId.toLongOrZero()
        ),
        getInsuranceDetailUseCaseRequestStates(
            p0DataRequestState.getBuyerOrderDetailRequestState.result.hasInsurance.orFalse(),
            p0DataRequestState.getBuyerOrderDetailRequestState.result.invoice
        ),
        ::mapP1UseCasesRequestState
    ).catch {
        emit(GetP1DataRequestState.Complete(
            GetOrderResolutionRequestState.Error(it),
            GetInsuranceDetailRequestState.Error(it)
        ))
    }.flowOn(Dispatchers.IO)
}
