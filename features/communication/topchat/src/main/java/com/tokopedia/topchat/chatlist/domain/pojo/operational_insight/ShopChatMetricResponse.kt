package com.tokopedia.topchat.chatlist.domain.pojo.operational_insight

import com.google.gson.annotations.SerializedName

data class ShopChatMetricResponse(
    @SerializedName("GetShopChatTicker")
    var shopChatTicker: ShopChatTicker? = ShopChatTicker()
)
