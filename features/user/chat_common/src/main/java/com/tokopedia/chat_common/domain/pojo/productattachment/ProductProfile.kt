package com.tokopedia.chat_common.domain.pojo.productattachment

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.chat_common.data.AttachmentVariant

class ProductProfile {
    @SerializedName("min_order")
    @Expose
    val minOrder: Int = 1
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
    @SerializedName("category_id")
    @Expose
    val categoryId: Int = 0
    @SerializedName("variant")
    @Expose
    val variant: List<AttachmentVariant> = listOf()
    @SerializedName("drop_percentage")
    @Expose
    val dropPercentage: String = ""
    @SerializedName("price_before")
    @Expose
    val priceBefore: String = ""
    @SerializedName("shop_id")
    @Expose
    val shopId: Int = 0
    @SerializedName("free_ongkir")
    @Expose
    val freeShipping: FreeShipping = FreeShipping()
    @SerializedName("playstore_product_data")
    @Expose
    val playStoreData: PlayStoreData = PlayStoreData()
    @SerializedName("remaining_stock")
    @Expose
    val remainingStock: Int = 1
}
