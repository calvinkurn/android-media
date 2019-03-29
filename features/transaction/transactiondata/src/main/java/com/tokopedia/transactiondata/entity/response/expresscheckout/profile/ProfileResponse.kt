package com.tokopedia.transactiondata.entity.response.expresscheckout.profile

import com.google.gson.annotations.SerializedName
import com.tokopedia.transactiondata.entity.response.expresscheckout.Header

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