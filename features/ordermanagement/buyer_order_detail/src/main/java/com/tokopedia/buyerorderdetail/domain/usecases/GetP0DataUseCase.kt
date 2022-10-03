package com.tokopedia.buyerorderdetail.domain.usecases

import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailParams
import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetOrderResolutionRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetP0DataParams
import com.tokopedia.buyerorderdetail.domain.models.GetP0DataRequestState
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetP0DataUseCase @Inject constructor(
    private val getBuyerOrderDetailUseCase: GetBuyerOrderDetailUseCase,
    private val getOrderResolutionUseCase: GetOrderResolutionUseCase
) {

    private fun mapToGetP0DataRequestState(
        getBuyerOrderDetailRequestState: GetBuyerOrderDetailRequestState
    ) = flow {
        when (getBuyerOrderDetailRequestState) {
            is GetBuyerOrderDetailRequestState.Requesting -> {
                emit(GetP0DataRequestState.Requesting(getBuyerOrderDetailRequestState))
            }
            is GetBuyerOrderDetailRequestState.Success -> {
                if (getBuyerOrderDetailRequestState.result.hasResoStatus == true) {
                    emitAll(combineWithOrderResolutionData(getBuyerOrderDetailRequestState))
                } else {
                    emit(
                        GetP0DataRequestState.Success(getBuyerOrderDetailRequestState)
                    )
                }
            }
            is GetBuyerOrderDetailRequestState.Error -> {
                emit(GetP0DataRequestState.Error(
                    getBuyerOrderDetailRequestState,
                    GetOrderResolutionRequestState.Error(getBuyerOrderDetailRequestState.throwable)
                ))
            }
        }
    }

    private fun combineWithOrderResolutionData(
        buyerOrderDetailRequestState: GetBuyerOrderDetailRequestState.Success
    ) = getOrderResolutionUseCase.getOrderResolution(
        buyerOrderDetailRequestState.result.orderId.toLongOrZero()
    ).flatMapLatest { getOrderResolutionRequestState ->
        flow {
            when (getOrderResolutionRequestState) {
                is GetOrderResolutionRequestState.Requesting -> {
                    emit(
                        GetP0DataRequestState.Requesting(
                            buyerOrderDetailRequestState,
                            getOrderResolutionRequestState
                        )
                    )
                }
                is GetOrderResolutionRequestState.Success -> {
                    emit(
                        GetP0DataRequestState.Success(
                            buyerOrderDetailRequestState,
                            getOrderResolutionRequestState
                        )
                    )
                }
                is GetOrderResolutionRequestState.Error -> {
                    emit(
                        GetP0DataRequestState.Success(
                            buyerOrderDetailRequestState,
                            getOrderResolutionRequestState
                        )
                    )
                }
            }
        }
    }

    fun getP0Data(params: GetP0DataParams) = getBuyerOrderDetailUseCase.getBuyerOrderDetail(
        GetBuyerOrderDetailParams(params.cart, params.orderId, params.paymentId)
    ).flatMapLatest(::mapToGetP0DataRequestState).flowOn(Dispatchers.IO)
}
