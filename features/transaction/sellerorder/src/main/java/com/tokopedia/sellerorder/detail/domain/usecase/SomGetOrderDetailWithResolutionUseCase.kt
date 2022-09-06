package com.tokopedia.sellerorder.detail.domain.usecase

import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sellerorder.detail.data.model.GetResolutionTicketStatusResponse
import com.tokopedia.sellerorder.detail.data.model.GetSomDetailResponse
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.usecase.coroutines.Result
import java.util.HashMap
import javax.inject.Inject

class SomGetOrderDetailWithResolutionUseCase @Inject constructor(
    private val somGetOrderDetailUseCase: SomGetOrderDetailUseCase,
    private val somResolutionGetTicketStatusUseCase: SomResolutionGetTicketStatusUseCase
): UseCase<Result<GetSomDetailResponse>>() {

    companion object {
        const val ORDER_ID_PARAM = "orderID"
    }

    suspend fun execute(orderId: String): Result<GetSomDetailResponse> {
        useCaseRequestParams.parameters[ORDER_ID_PARAM] = orderId
        return executeOnBackground()
    }

    override suspend fun executeOnBackground(): Result<GetSomDetailResponse> {
        try {
            val orderId: String = useCaseRequestParams.parameters[ORDER_ID_PARAM] as String
            val detailResponse = somGetOrderDetailUseCase.execute(orderId)
            if (detailResponse is Success) {
                if (detailResponse.data.getSomDetail?.hasResoStatus.orFalse()) {
                    val orderIdLong = orderId.toLongOrZero()
                    val params = HashMap<String, Any?>()
                    params[ORDER_ID_PARAM] = orderIdLong
                    somResolutionGetTicketStatusUseCase.setRequestParams(params)
                    val resolutionResponse = somResolutionGetTicketStatusUseCase.execute()
                    return if (resolutionResponse is Success) {
                        detailResponse.data.somResolution = resolutionResponse
                            .data.resolutionGetTicketStatus?.data
                        detailResponse
                    } else {
                        detailResponse.data.somResolution = GetResolutionTicketStatusResponse
                            .ResolutionGetTicketStatus.ResolutionData()
                        detailResponse
                    }
                } else {
                    return detailResponse
                }
            } else {
                return Fail(MessageErrorException("failed retrieving detail"))
            }
        } catch (e: Exception) {
            return Fail(e)
        }
    }
}