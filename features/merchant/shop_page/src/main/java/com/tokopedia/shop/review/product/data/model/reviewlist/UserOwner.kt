package com.tokopedia.shop.review.product.data.model.reviewlist

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UserOwner {
    @SerializedName("user_id")
    @Expose
    var userId = 0
    @SerializedName("user_url")
    @Expose
    var userUrl: String? = null
    @SerializedName("user_image")
    @Expose
    var userImage: String? = null
    @SerializedName("user_full_name")
    @Expose
    var userFullName: String? = null
    @SerializedName("user_label")
    @Expose
    var userLabel: String? = null

}