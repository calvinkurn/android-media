package com.tokopedia.accountprofile.settingprofile.changepin.data

import com.google.gson.annotations.SerializedName

/**
 * Created by Yoris Prayogo on 23/07/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

data class ResetPin2FaPojo(
    @SerializedName("resetUserPin")
    var data: ChangePin2FAData = ChangePin2FAData()
)

data class ChangePin2FAData(
    @SerializedName("is_success")
    var is_success: Int = 0,
    @SerializedName("user_id")
    var userId: String = "",
    @SerializedName("access_token")
    var accessToken: String = "",
    @SerializedName("sid")
    var sid: String = "",
    @SerializedName("refresh_token")
    var refreshToken: String = "",
    @SerializedName("expires_in")
    var expires: Int = 0,
    @SerializedName("error")
    var error: String = ""
)
