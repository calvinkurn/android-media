package com.tokopedia.topads.sdk.domain.model

import com.google.gson.annotations.SerializedName

class WishlistModel {
    @SerializedName("data")
    var data: Data? = null

    class Data {
        @SerializedName("success")
        var isSuccess = false
    }
}
