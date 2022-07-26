package com.tokopedia.wishlistcommon.data.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GetIsWishlistedV2Response {
    @SerializedName("ProductWishlistQuery")
    @Expose
    var isWishlisted = false
}
