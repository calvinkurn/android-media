package com.tokopedia.buyerorderdetail.domain.usecases

import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailDataParams
import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailDataRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetOrderResolutionRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetP0DataParams
import com.tokopedia.buyerorderdetail.domain.models.GetP0DataRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetP1DataParams
import com.tokopedia.buyerorderdetail.domain.models.GetP1DataRequestState
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetBuyerOrderDetailDataUseCase @Inject constructor(
    private val getP0DataUseCase: GetP0DataUseCase, private val getP1DataUseCase: GetP1DataUseCase
) {

    private fun mapGetP0DataRequestStateToGetAllDataRequestState(
        p0DataRequestState: GetP0DataRequestState,
        shouldCheckCache: Boolean
    ): Flow<GetBuyerOrderDetailDataRequestState> = flow {
        when (p0DataRequestState) {
            is GetP0DataRequestState.Requesting -> {
                emit(
                    GetBuyerOrderDetailDataRequestState.Requesting(
                        p0DataRequestState, GetP1DataRequestState.Requesting()
                    )
                )
            }
            is GetP0DataRequestState.Success -> {
                val getP1DataParams = GetP1DataParams(
                    p0DataRequestState.getBuyerOrderDetailRequestState.result.hasResoStatus.orFalse(),
                    p0DataRequestState.getBuyerOrderDetailRequestState.result.orderId.toLongOrZero(),
                    shouldCheckCache
                )
                emitAll(combineWithP1(getP1DataParams, p0DataRequestState))
            }
            is GetP0DataRequestState.Error -> {
                emit(
                    GetBuyerOrderDetailDataRequestState.Error(
                        p0DataRequestState, GetP1DataRequestState.Complete(
                            GetOrderResolutionRequestState.Error(p0DataRequestState.getThrowable())
                        )
                    )
                )
            }
        }
    }

    private suspend fun combineWithP1(
        getP1DataParams: GetP1DataParams, p0DataRequestState: GetP0DataRequestState.Success
    ): Flow<GetBuyerOrderDetailDataRequestState> =
        getP1DataUseCase(getP1DataParams).flatMapLatest { p1DataRequestState ->
            flow {
                when (p1DataRequestState) {
                    is GetP1DataRequestState.Requesting -> {
                        emit(
                            GetBuyerOrderDetailDataRequestState.Requesting(
                                p0DataRequestState, p1DataRequestState
                            )
                        )
                    }
                    is GetP1DataRequestState.Complete -> {
                        emit(
                            GetBuyerOrderDetailDataRequestState.Success(
                                p0DataRequestState, p1DataRequestState
                            )
                        )
                    }
                }
            }
        }

    private suspend fun execute(
        params: GetBuyerOrderDetailDataParams
    ) = getP0DataUseCase(
        GetP0DataParams(params.cart, params.orderId, params.paymentId, params.shouldCheckCache)
    ).flatMapLatest { p0DataRequestState ->
        mapGetP0DataRequestStateToGetAllDataRequestState(p0DataRequestState, params.shouldCheckCache)
    }.catch {
        emit(
            GetBuyerOrderDetailDataRequestState.Error(
                GetP0DataRequestState.Error(GetBuyerOrderDetailRequestState.Error(it)),
                GetP1DataRequestState.Complete(GetOrderResolutionRequestState.Error(it))
            )
        )
    }

    suspend operator fun invoke(
        params: GetBuyerOrderDetailDataParams
    ) = execute(params).flowOn(Dispatchers.IO)
}
