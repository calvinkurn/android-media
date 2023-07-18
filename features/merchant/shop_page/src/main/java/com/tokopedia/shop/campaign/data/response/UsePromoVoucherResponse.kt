package com.tokopedia.shop.campaign.data.response

import com.google.gson.annotations.SerializedName

data class UsePromoVoucherResponse(
    @SerializedName("saveCacheAutoApplyStack")
    val saveCacheAutoApplyStack: SaveCacheAutoApplyStack
) {
    data class SaveCacheAutoApplyStack(
        @SerializedName("Success")
        val success: Boolean
    )
}
