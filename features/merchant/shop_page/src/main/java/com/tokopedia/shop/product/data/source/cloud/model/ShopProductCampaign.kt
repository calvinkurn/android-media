package com.tokopedia.shop.product.data.source.cloud.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by brilliant.oka on 29/03/17.
 */
class ShopProductCampaign {
    @SerializedName("product_id")
    @Expose
    var productId: String? = null

    @SerializedName("discounted_price")
    @Expose
    var discountedPrice: String? = null

    @SerializedName("end_date")
    @Expose
    var endDate: String? = null

    @SerializedName("original_price")
    @Expose
    var originalPrice: String? = null
}
