package com.tokopedia.buyerorderdetail.presentation.mapper

import com.tokopedia.buyerorderdetail.domain.models.GetOrderResolutionRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetP0DataRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetResolutionTicketStatusResponse
import com.tokopedia.buyerorderdetail.presentation.model.OrderResolutionUIModel
import com.tokopedia.buyerorderdetail.presentation.uistate.OrderResolutionTicketStatusUiState
import com.tokopedia.kotlin.extensions.orFalse

object OrderResolutionTicketStatusUiStateMapper {

    private fun mapOrderResolutionTicketStatus(
        resolutionData: GetResolutionTicketStatusResponse.ResolutionGetTicketStatus.ResolutionData?
    ): OrderResolutionUIModel {
        return OrderResolutionUIModel(
            title = resolutionData?.cardTitle.orEmpty(),
            status = resolutionData?.resolutionStatus?.text.orEmpty(),
            description = resolutionData?.description.orEmpty(),
            picture = resolutionData?.profilePicture.orEmpty(),
            showDeadline = resolutionData?.deadline?.showDeadline.orFalse(),
            deadlineDateTime = resolutionData?.deadline?.datetime.orEmpty(),
            backgroundColor = resolutionData?.deadline?.backgroundColor.orEmpty(),
            redirectPath = resolutionData?.redirectPath?.android.orEmpty(),
            resolutionStatusFontColor = resolutionData?.resolutionStatus?.fontColor.orEmpty()
        )
    }

    fun mapGetP0DataResultToOrderResolutionTicketStatusUiState(
        getP0DataRequestState: GetP0DataRequestState
    ): OrderResolutionTicketStatusUiState {
        return when (getP0DataRequestState) {
            is GetP0DataRequestState.Success -> {
                if (getP0DataRequestState.getBuyerOrderDetailRequestState.result.hasResoStatus.orFalse()) {
                    val getOrderResolutionRequestState = getP0DataRequestState.getOrderResolutionRequestState
                    if (getOrderResolutionRequestState is GetOrderResolutionRequestState.Success) {
                        OrderResolutionTicketStatusUiState.Showing(
                            mapOrderResolutionTicketStatus(getOrderResolutionRequestState.result)
                        )
                    } else {
                        OrderResolutionTicketStatusUiState.Hidden
                    }
                } else {
                    OrderResolutionTicketStatusUiState.Hidden
                }
            }
            is GetP0DataRequestState.Error -> {
                OrderResolutionTicketStatusUiState.Loading
            }
            else -> {
                OrderResolutionTicketStatusUiState.Loading
            }
        }
    }
}
