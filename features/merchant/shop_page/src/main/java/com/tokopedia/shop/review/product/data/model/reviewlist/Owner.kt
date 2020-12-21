package com.tokopedia.shop.review.product.data.model.reviewlist

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Owner {
    @SerializedName("shop")
    @Expose
    var shop: Shop? = null
    @SerializedName("user")
    @Expose
    var user: UserOwner? = null

}