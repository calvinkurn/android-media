package com.tokopedia.review.feature.review.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductrevGetFormUserData(
        @SerializedName("userName")
        @Expose
        val userName: String = "",
        @SerializedName("isAnonym")
        @Expose
        val isAnonym: Boolean = false
)