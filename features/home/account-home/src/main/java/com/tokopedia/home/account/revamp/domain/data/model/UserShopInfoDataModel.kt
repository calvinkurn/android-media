package com.tokopedia.home.account.revamp.domain.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UserShopInfoDataModel(
        @SerializedName("info")
        @Expose
        var info: ShopInfoDataModel = ShopInfoDataModel(),
        @SerializedName("owner")
        @Expose
        var owner: ShopOwnerDataModel = ShopOwnerDataModel()
)