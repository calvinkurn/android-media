package com.tokopedia.seller.menu.common.domain.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.sessioncommon.data.profile.ShopBasicData

data class RefreshShopDataResponse(
        @SerializedName("shopBasicData")
        @Expose
        var shopInfo: ShopBasicData = ShopBasicData()
)