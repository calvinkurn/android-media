package com.tokopedia.profilecompletion.changepin.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Yoris Prayogo on 23/07/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

data class ResetPin2FaPojo(
        @SerializedName("resetUserPin")
        @Expose
        var data: ChangePin2FAData = ChangePin2FAData()
)

data class ChangePin2FAData(
        @SerializedName("is_success")
        @Expose
        var is_success: Int = 0,
        @SerializedName("user_id")
        @Expose
        var userId: String = "",
        @SerializedName("access_token")
        @Expose
        var accessToken: String = "",
        @SerializedName("sid")
        @Expose
        var sid: String = "",
        @SerializedName("refresh_token")
        @Expose
        var refreshToken: String = "",
        @SerializedName("expires_in")
        @Expose
        var expires: Int = 0,
        @SerializedName("error")
        @Expose
        var error: String = ""
)