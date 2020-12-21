package com.tokopedia.shop.review.product.data.model.reviewlist

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class User {
    @SerializedName("user_id")
    @Expose
    var userId = 0
    @SerializedName("full_name")
    @Expose
    var fullName: String? = null

}