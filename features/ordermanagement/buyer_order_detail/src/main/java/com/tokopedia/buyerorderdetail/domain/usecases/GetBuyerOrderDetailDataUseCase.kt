package com.tokopedia.buyerorderdetail.domain.usecases

import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailDataParams
import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailDataRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetInsuranceDetailRequestState
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
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetBuyerOrderDetailDataUseCase @Inject constructor(
    private val getP0DataUseCase: GetP0DataUseCase,
    private val getP1DataUseCase: GetP1DataUseCase
) {

    suspend operator fun invoke(
        params: GetBuyerOrderDetailDataParams
    ) = execute(params).flowOn(Dispatchers.IO)

    private fun mapGetP0DataRequestStateToGetAllDataRequestState(
        p0DataRequestState: GetP0DataRequestState, shouldCheckCache: Boolean
    ): Flow<GetBuyerOrderDetailDataRequestState> = flow {
        when (p0DataRequestState) {
            is GetP0DataRequestState.Requesting -> {
                emit(
                    GetBuyerOrderDetailDataRequestState.Requesting(
                        p0DataRequestState, GetP1DataRequestState.Requesting()
                    )
                )
            }
            is GetP0DataRequestState.Complete -> {
                val getBuyerOrderDetailRequestState = p0DataRequestState
                    .getBuyerOrderDetailRequestState
                if (getBuyerOrderDetailRequestState is GetBuyerOrderDetailRequestState.Complete.Success) {
                    val getP1DataParams = GetP1DataParams(
                        getBuyerOrderDetailRequestState.result.hasResoStatus.orFalse(),
                        getBuyerOrderDetailRequestState.result.hasInsurance.orFalse(),
                        getBuyerOrderDetailRequestState.result.orderId.toLongOrZero(),
                        getBuyerOrderDetailRequestState.result.invoice,
                        shouldCheckCache
                    )
                    emitAll(combineWithP1(getP1DataParams, p0DataRequestState))
                } else {
                    emit(
                        GetBuyerOrderDetailDataRequestState.Complete(
                            p0DataRequestState, GetP1DataRequestState.Complete(
                                GetOrderResolutionRequestState.Complete.Error(null),
                                GetInsuranceDetailRequestState.Complete.Error(null),
                            )
                        )
                    )
                }
            }
        }
    }

    private fun combineWithP1(
        getP1DataParams: GetP1DataParams, p0DataRequestState: GetP0DataRequestState.Complete
    ): Flow<GetBuyerOrderDetailDataRequestState> =
        getP1DataUseCase(getP1DataParams).flatMapConcat { p1DataRequestState ->
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
                            GetBuyerOrderDetailDataRequestState.Complete(
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
    ).flatMapConcat { p0DataRequestState ->
        mapGetP0DataRequestStateToGetAllDataRequestState(
            p0DataRequestState, params.shouldCheckCache
        )
    }.catch {
        emit(
            GetBuyerOrderDetailDataRequestState.Complete(
                GetP0DataRequestState.Complete(GetBuyerOrderDetailRequestState.Complete.Error(it)),
                GetP1DataRequestState.Complete(
                    GetOrderResolutionRequestState.Complete.Error(it),
                    GetInsuranceDetailRequestState.Complete.Error(it)
                )
            )
        )
    }
}
