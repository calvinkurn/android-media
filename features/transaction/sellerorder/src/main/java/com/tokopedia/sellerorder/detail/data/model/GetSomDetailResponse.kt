package com.tokopedia.sellerorder.detail.data.model

data class GetSomDetailResponse(
    var somDynamicPriceResponse: SomDynamicPriceResponse.GetSomDynamicPrice? = null,
    var getSomDetail: SomDetailOrder.GetSomDetail? = null,
    var somResolution: GetResolutionTicketStatusResponse.ResolutionGetTicketStatus.ResolutionData? = null
)
