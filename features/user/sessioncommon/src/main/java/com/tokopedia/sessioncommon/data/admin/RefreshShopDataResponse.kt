package com.tokopedia.sessioncommon.data.admin

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.sessioncommon.data.profile.ShopBasicData

data class RefreshShopDataResponse(
        @SerializedName("shopBasicData")
        @Expose
        var shopInfo: ShopBasicData = ShopBasicData()
)