package com.tokopedia.attachvoucher.data.voucherv2


import com.google.gson.annotations.SerializedName

data class MerchantPromotionGetMVList(
    @SerializedName("data")
    val `data`: Data = Data(),
    @SerializedName("header")
    val header: Header = Header()
)