package com.tokopedia.purchase_platform.features.express_checkout.data.entity.response.profile

import com.google.gson.annotations.SerializedName
import com.tokopedia.purchase_platform.features.express_checkout.data.entity.response.Header

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

data class ProfileResponse(
        @SerializedName("header")
        val header: Header,

        @SerializedName("status")
        val status: String,

        @SerializedName("error_message")
        val errorMessage: ArrayList<String>,

        @SerializedName("data")
        val data: ProfileData
)