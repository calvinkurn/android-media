package com.tokopedia.shop.review.product.data.model.reviewlist

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ReviewShop : Review() {
    @SerializedName("product")
    @Expose
    var product: Product? = null

}