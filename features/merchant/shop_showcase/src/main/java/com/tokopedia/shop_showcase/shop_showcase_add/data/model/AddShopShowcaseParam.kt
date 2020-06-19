package com.tokopedia.shop_showcase.shop_showcase_add.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AddShopShowcaseParam(
        @Expose
        @SerializedName("name") var name: String = "",
        @Expose
        @SerializedName("productIDs") var productIDs: MutableList<String> = mutableListOf()
)