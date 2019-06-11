package com.tokopedia.product.manage.item.main.base.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PopUpManagerViewModel(

        @SerializedName("getShopManagerPopups")
        @Expose
        val shopManagerPopups: ShopManagerPopups  = ShopManagerPopups()
)

data class ShopManagerPopups(
        @SerializedName("data")
        @Expose
        val data : PopupsData = PopupsData()
)

data class PopupsData(
        @SerializedName("showPopUp")
        @Expose
        val showPopUp:Boolean = false
)