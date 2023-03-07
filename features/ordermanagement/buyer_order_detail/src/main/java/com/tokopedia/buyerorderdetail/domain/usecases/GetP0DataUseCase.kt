package com.tokopedia.buyerorderdetail.domain.usecases

import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailParams
import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetP0DataParams
import com.tokopedia.buyerorderdetail.domain.models.GetP0DataRequestState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetP0DataUseCase @Inject constructor(
    private val getBuyerOrderDetailUseCase: GetBuyerOrderDetailUseCase
) {

    suspend operator fun invoke(params: GetP0DataParams) = execute(params).flowOn(Dispatchers.IO)
    private fun mapToGetP0DataRequestState(
        getBuyerOrderDetailRequestState: GetBuyerOrderDetailRequestState
    ) = flow {
        when (getBuyerOrderDetailRequestState) {
            is GetBuyerOrderDetailRequestState.Requesting -> {
                emit(GetP0DataRequestState.Requesting(getBuyerOrderDetailRequestState))
            }
            is GetBuyerOrderDetailRequestState.Complete -> {
                emit(GetP0DataRequestState.Complete(getBuyerOrderDetailRequestState))
            }
        }
    }

    private suspend fun execute(params: GetP0DataParams) = getBuyerOrderDetailUseCase(
        GetBuyerOrderDetailParams(
            params.cart, params.orderId, params.paymentId, params.shouldCheckCache
        )
    ).flatMapConcat(::mapToGetP0DataRequestState).catch {
        emit(GetP0DataRequestState.Complete(GetBuyerOrderDetailRequestState.Complete.Error(it)))
    }
}
