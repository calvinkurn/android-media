package com.tokopedia.seamless_login_common.data

import com.google.gson.annotations.SerializedName

/**
 * Created by Yoris Prayogo on 2019-11-08.
 * Copyright (c) 2019 PT. Tokopedia All rights reserved.
 */

data class UserDataPojo(
        @SerializedName("date")
        val date: String = "",

        @SerializedName("content-md5")
        val content_md5: String = "",

        @SerializedName("authorization")
        val authorization: String = "",

        @SerializedName("accounts-authorization")
        val accountAuth: String = ""
)