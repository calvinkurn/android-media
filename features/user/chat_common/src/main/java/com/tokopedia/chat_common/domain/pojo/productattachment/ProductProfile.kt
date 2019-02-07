package com.tokopedia.chat_common.domain.pojo.productattachment

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ProductProfile {

    @SerializedName("image_url")
    @Expose
    val imageUrl: String = ""
    @SerializedName("name")
    @Expose
    val name: String = ""
    @SerializedName("price")
    @Expose
    val price: String = ""
    @SerializedName("url")
    @Expose
    val url: String = ""
    @SerializedName("price_int")
    @Expose
    val priceInt: Int = 0
    @SerializedName("category_breadcrumb")
    @Expose
    val category: String = ""
    @SerializedName("variant")
    @Expose
    val variant: Any? = null
}
