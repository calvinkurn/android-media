package com.tokopedia.sellerorder.detail.domain.usecase

import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.sellerorder.detail.data.model.GetSomDetailResponse
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class SomGetOrderDetailWithResolutionUseCase @Inject constructor(
    private val somGetOrderDetailUseCase: SomGetOrderDetailUseCase,
    private val somResolutionGetTicketStatusUseCase: SomResolutionGetTicketStatusUseCase
): UseCase<Result<GetSomDetailResponse>>() {

    companion object {
        const val ORDER_ID_PARAM = "orderID"
    }

    override suspend fun executeOnBackground(): Result<GetSomDetailResponse> {
        return try {
            val orderId: String = useCaseRequestParams.parameters[ORDER_ID_PARAM] as String
            val detailResponse = somGetOrderDetailUseCase.execute(orderId)
            if (detailResponse.getSomDetail?.hasResoStatus.orFalse()) {
                val orderIdLong = orderId.toLongOrZero()
                val params = HashMap<String, Any?>()
                params[ORDER_ID_PARAM] = orderIdLong
                somResolutionGetTicketStatusUseCase.setRequestParams(params)
                val resolutionResponse = somResolutionGetTicketStatusUseCase.execute()
                detailResponse.somResolution = resolutionResponse.resolutionGetTicketStatus?.data
            }
            Success(detailResponse)
        } catch (e: Exception) {
            return Fail(e)
        }
    }

    suspend fun execute(orderId: String): Result<GetSomDetailResponse> {
        useCaseRequestParams.parameters[ORDER_ID_PARAM] = orderId
        return executeOnBackground()
    }
}
