package com.tokopedia.chat_common.domain.pojo.productattachment

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ProductAttachmentAttributes constructor(
    @SerializedName("product_id")
    @Expose
    val productId: String = "0",
    @SerializedName("product_profile")
    @Expose
    val productProfile: ProductProfile = ProductProfile()
) {

    fun isBannedProduct(): Boolean {
        return productProfile.playStoreData.isBanned()
    }
}
