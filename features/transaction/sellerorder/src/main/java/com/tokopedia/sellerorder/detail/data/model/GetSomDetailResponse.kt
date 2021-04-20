package com.tokopedia.sellerorder.detail.data.model

data class GetSomDetailResponse(
        var somDynamicPriceResponse: SomDynamicPriceResponse.GetSomDynamicPrice? = null,
        var getSomDetail: SomDetailOrder.Data.GetSomDetail? = null
)