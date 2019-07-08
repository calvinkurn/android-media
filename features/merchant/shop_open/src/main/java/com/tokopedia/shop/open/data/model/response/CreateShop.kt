package com.tokopedia.shop.open.data.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CreateShop(
        @SerializedName("createShop")
        @Expose
        val createShop : CreateShopData = CreateShopData()
)