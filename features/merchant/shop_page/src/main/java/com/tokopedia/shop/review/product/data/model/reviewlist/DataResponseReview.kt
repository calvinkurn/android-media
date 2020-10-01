package com.tokopedia.shop.review.product.data.model.reviewlist

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by zulfikarrahman on 1/15/18.
 */
abstract class DataResponseReview {
    @SerializedName("paging")
    @Expose
    var paging: Paging? = null
    @SerializedName("is_owner")
    @Expose
    var isOwner = 0
    @SerializedName("owner")
    @Expose
    private var owner: Owner? = null

    fun getOwner(): Owner? {
        return owner
    }

    fun setOwner(owner: Owner?) {
        this.owner = owner
    }
}