package com.tokopedia.topchat.chatroom.domain.pojo.product_bundling

import com.google.gson.annotations.SerializedName

data class ProductBundlingPojo(
    @SerializedName("product_bundling")
    var listProductBundling: ArrayList<ProductBundlingData> = arrayListOf()
)