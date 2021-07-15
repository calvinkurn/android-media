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
    val priceInt: Long = 0
    @SerializedName("category_breadcrumb")
    @Expose
    val category: String = ""
    @SerializedName("category_id")
    @Expose
    val categoryId: Long = 0
    @SerializedName("variant")
    @Expose
    val variant: List<AttachmentVariant>? = listOf()
    @SerializedName("drop_percentage")
    @Expose
    var dropPercentage: String = ""
    @SerializedName("price_before")
    @Expose
    var priceBefore: String = ""
    @SerializedName("shop_id")
    @Expose
    val shopId: Long = 0
    @SerializedName("free_ongkir")
    @Expose
    val freeShipping: FreeShipping = FreeShipping()
    @SerializedName("playstore_product_data")
    @Expose
    val playStoreData: PlayStoreData = PlayStoreData()
    @SerializedName("remaining_stock")
    @Expose
    var remainingStock: Int = 1
    @SerializedName("status")
    @Expose
    var status: Int = 1
    @SerializedName("wishlist")
    @Expose
    val wishList: Boolean = false
    @SerializedName("list_image_url")
    @Expose
    val images: List<String> = emptyList()
    @SerializedName("rating")
    @Expose
    val rating: TopchatProductRating = TopchatProductRating()
    @SerializedName("is_preorder")
    @Expose
    val isPreOrder: Boolean = false
    @SerializedName("campaign_id")
    @Expose
    var campaignId: Long = 0
    @SerializedName("is_fulfillment")
    @Expose
    var isFulFillment: Boolean = false
    @SerializedName("icon_tokocabang")
    @Expose
    var urlTokocabang: String = ""
    @SerializedName("parent_id")
    @Expose
    var parentId: String = "0"
}
