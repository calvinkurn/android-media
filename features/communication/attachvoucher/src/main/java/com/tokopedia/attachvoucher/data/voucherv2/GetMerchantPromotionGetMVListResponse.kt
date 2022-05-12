package com.tokopedia.attachvoucher.data.voucherv2


import com.google.gson.annotations.SerializedName

data class GetMerchantPromotionGetMVListResponse(
    @SerializedName("MerchantPromotionGetMVList")
    val merchantPromotionGetMVList: MerchantPromotionGetMVList = MerchantPromotionGetMVList()
)