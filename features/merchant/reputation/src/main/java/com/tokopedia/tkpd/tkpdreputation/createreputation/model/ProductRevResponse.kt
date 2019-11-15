package com.tokopedia.tkpd.tkpdreputation.createreputation.model


import com.google.gson.annotations.SerializedName

data class ProductRevResponse(
    @SerializedName("productData")
    val productData: ProductData = ProductData(),
    @SerializedName("reputationID")
    val reputationID: Int = 0,
    @SerializedName("shopData")
    val shopData: ShopData = ShopData(),
    @SerializedName("userData")
    val userData: UserData = UserData()
)