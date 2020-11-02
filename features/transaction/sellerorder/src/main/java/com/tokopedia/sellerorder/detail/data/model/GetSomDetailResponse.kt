package com.tokopedia.sellerorder.detail.data.model

data class GetSomDetailResponse(
        var somDynamicPriceResponse: SomDynamicPriceResponse? = null,
        var getSomDetail: SomDetailOrder.Data.GetSomDetail? = null
)