package com.tokopedia.shop_showcase.shop_showcase_add.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RemovedProduct(
        @Expose
        @SerializedName("product_id") var product_id: String? = "",
        @Expose
        @SerializedName("menu_id") var menu_id: String? = ""
)