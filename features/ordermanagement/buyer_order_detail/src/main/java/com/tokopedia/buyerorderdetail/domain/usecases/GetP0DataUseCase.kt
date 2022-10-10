package com.tokopedia.buyerorderdetail.domain.usecases

import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailParams
import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetP0DataParams
import com.tokopedia.buyerorderdetail.domain.models.GetP0DataRequestState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetP0DataUseCase @Inject constructor(
    private val getBuyerOrderDetailUseCase: GetBuyerOrderDetailUseCase
) {
    private fun mapToGetP0DataRequestState(
        getBuyerOrderDetailRequestState: GetBuyerOrderDetailRequestState
    ) = flow {
        when (getBuyerOrderDetailRequestState) {
            is GetBuyerOrderDetailRequestState.Requesting -> {
                emit(GetP0DataRequestState.Requesting(getBuyerOrderDetailRequestState))
            }
            is GetBuyerOrderDetailRequestState.Success -> {
                emit(GetP0DataRequestState.Success(getBuyerOrderDetailRequestState))
            }
            is GetBuyerOrderDetailRequestState.Error -> {
                emit(GetP0DataRequestState.Error(getBuyerOrderDetailRequestState))
            }
        }
    }

    suspend fun getP0Data(params: GetP0DataParams) = getBuyerOrderDetailUseCase(
        GetBuyerOrderDetailParams(params.cart, params.orderId, params.paymentId)
    ).flatMapLatest(::mapToGetP0DataRequestState).catch {
        emit(GetP0DataRequestState.Error(GetBuyerOrderDetailRequestState.Error(it)))
    }.flowOn(Dispatchers.IO)
}
