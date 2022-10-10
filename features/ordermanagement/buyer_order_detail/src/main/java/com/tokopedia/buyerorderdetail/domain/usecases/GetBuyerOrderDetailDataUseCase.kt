package com.tokopedia.buyerorderdetail.domain.usecases

import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailDataParams
import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailDataRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetInsuranceDetailRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetOrderResolutionRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetP0DataParams
import com.tokopedia.buyerorderdetail.domain.models.GetP0DataRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetP1DataRequestState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetBuyerOrderDetailDataUseCase @Inject constructor(
    private val getP0DataUseCase: GetP0DataUseCase,
    private val getP1DataUseCase: GetP1DataUseCase
) {

    private fun mapGetP0DataRequestStateToGetAllDataRequestState(
        p0DataRequestState: GetP0DataRequestState
    ): Flow<GetBuyerOrderDetailDataRequestState> = flow {
        when (p0DataRequestState) {
            is GetP0DataRequestState.Requesting -> {
                emit(
                    GetBuyerOrderDetailDataRequestState.Requesting(
                        p0DataRequestState,
                        GetP1DataRequestState.Requesting()
                    )
                )
            }
            is GetP0DataRequestState.Success -> {
                emitAll(combineWithP1(p0DataRequestState))
            }
            is GetP0DataRequestState.Error -> {
                emit(
                    GetBuyerOrderDetailDataRequestState.Error(
                        p0DataRequestState,
                        GetP1DataRequestState.Complete(
                            GetOrderResolutionRequestState.Error(p0DataRequestState.getThrowable()),
                            GetInsuranceDetailRequestState.Error(p0DataRequestState.getThrowable()),
                        )
                    )
                )
            }
        }
    }

    private suspend fun combineWithP1(
        p0DataRequestState: GetP0DataRequestState.Success
    ): Flow<GetBuyerOrderDetailDataRequestState> = getP1DataUseCase.getP1Data(p0DataRequestState).flatMapLatest { p1DataRequestState ->
        flow {
            when (p1DataRequestState) {
                is GetP1DataRequestState.Requesting -> {
                    emit(
                        GetBuyerOrderDetailDataRequestState.Requesting(
                            p0DataRequestState,
                            p1DataRequestState
                        )
                    )
                }
                is GetP1DataRequestState.Complete -> {
                    emit(
                        GetBuyerOrderDetailDataRequestState.Success(
                            p0DataRequestState,
                            p1DataRequestState
                        )
                    )
                }
            }
        }
    }

    suspend fun getBuyerOrderDetailData(params: GetBuyerOrderDetailDataParams) = getP0DataUseCase
        .getP0Data(GetP0DataParams(params.cart, params.orderId, params.paymentId))
        .flatMapLatest(::mapGetP0DataRequestStateToGetAllDataRequestState)
        .catch {
            emit(
                GetBuyerOrderDetailDataRequestState.Error(
                    GetP0DataRequestState.Error(
                        GetBuyerOrderDetailRequestState.Error(it)
                    ),
                    GetP1DataRequestState.Complete(
                        GetOrderResolutionRequestState.Error(it),
                        GetInsuranceDetailRequestState.Error(it)
                    )
                )
            )
        }
        .flowOn(Dispatchers.IO)
}
