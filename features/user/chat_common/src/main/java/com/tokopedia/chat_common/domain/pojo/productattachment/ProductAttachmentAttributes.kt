package com.tokopedia.chat_common.domain.pojo.productattachment

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ProductAttachmentAttributes {

    @SerializedName("product_id")
    @Expose
    val productId: Int = 0
    @SerializedName("product_profile")
    @Expose
    val productProfile: ProductProfile = ProductProfile()

}
