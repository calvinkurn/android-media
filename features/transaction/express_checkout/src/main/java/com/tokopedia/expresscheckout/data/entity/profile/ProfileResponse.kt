package com.tokopedia.expresscheckout.data.entity.profile

import com.google.gson.annotations.SerializedName
import com.tokopedia.expresscheckout.data.entity.Header

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

data class ProfileResponse(
        @SerializedName("header")
    val header: Header,

        @SerializedName("data")
    val data: ProfileData
)