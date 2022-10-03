package com.tokopedia.buyerorderdetail.domain.models

data class GetP0DataResponse(
    val getBuyerOrderDetailResponse: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail,
    val getOrderResolutionResponse: GetResolutionTicketStatusResponse.ResolutionGetTicketStatus.ResolutionData? = null
)
