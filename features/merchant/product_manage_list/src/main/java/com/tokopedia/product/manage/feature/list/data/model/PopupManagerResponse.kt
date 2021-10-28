package com.tokopedia.product.manage.feature.list.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PopupManagerResponse(
    @SerializedName("getShopManagerPopups")
    @Expose
    val shopManagerPopups: ShopManagerPopups = ShopManagerPopups()
)

data class ShopManagerPopups(
    @SerializedName("data")
    @Expose
    val popupsData: PopupsData = PopupsData()
)

data class PopupsData(
    @SerializedName("showPopup")
    @Expose
    val isShowPopup: Boolean = false
)