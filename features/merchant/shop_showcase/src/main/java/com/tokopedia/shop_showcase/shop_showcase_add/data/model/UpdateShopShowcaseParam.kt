package com.tokopedia.shop_showcase.shop_showcase_add.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UpdateShopShowcaseParam(
        @Expose
        @SerializedName("id") var id: String? = "0",
        @Expose
        @SerializedName("name") var name: String = ""
)