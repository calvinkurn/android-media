package com.tokopedia.product.manage.feature.list.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PopupManagerResponse(
    @SerializedName("getShopManagerPopups")
    @Expose
    val getShopManagerPopups: GetShopManagerPopups = GetShopManagerPopups()
)

data class GetShopManagerPopups(
    @SerializedName("data")
    @Expose
    val shopManagerPopupsData: ShopManagerPopupsData = ShopManagerPopupsData()
)

data class ShopManagerPopupsData(
    @SerializedName("showPopup")
    @Expose
    val isShowPopup: Boolean = false
)