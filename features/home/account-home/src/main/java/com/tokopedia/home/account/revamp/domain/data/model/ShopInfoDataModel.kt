package com.tokopedia.home.account.revamp.domain.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ShopInfoDataModel(
        @SerializedName("shop_name")
        @Expose
        var shopName: String = "",
        @SerializedName("shop_avatar")
        @Expose
        var shopAvatar: String = "",
        @SerializedName("shop_id")
        @Expose
        var shopId: String = "",
        @SerializedName("shop_is_official")
        @Expose
        var shopIsOfficial: String = ""
)