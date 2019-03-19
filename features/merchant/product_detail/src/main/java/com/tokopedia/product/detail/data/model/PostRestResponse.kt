package com.tokopedia.product.detail.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PostRestResponse(
        @SerializedName("is_success")
        @Expose
        private val _isSuccess: Int = 0
){
    val isSuccess: Boolean
        get() = _isSuccess == 1
}