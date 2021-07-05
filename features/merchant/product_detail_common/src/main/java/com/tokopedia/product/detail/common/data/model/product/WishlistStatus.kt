package com.tokopedia.product.detail.common.data.model.product

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Yehezkiel on 14/12/20
 */
data class WishlistStatus(
        @SerializedName("ProductWishlistQuery")
        @Expose
        var isWishlisted: Boolean? = null
)