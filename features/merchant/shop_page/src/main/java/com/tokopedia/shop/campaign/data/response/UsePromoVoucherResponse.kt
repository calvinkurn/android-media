package com.tokopedia.shop.campaign.data.response

import com.google.gson.annotations.SerializedName

data class UsePromoVoucherResponse(
    @SerializedName("save_cache_auto_apply")
    val saveCacheAutoReply: SaveCacheAutoReply
) {
    data class SaveCacheAutoReply(
        @SerializedName("Success")
        val success: Boolean
    )
}
