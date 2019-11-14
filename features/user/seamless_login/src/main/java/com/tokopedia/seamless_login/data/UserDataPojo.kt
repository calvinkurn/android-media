package com.tokopedia.seamless_login.data

import com.google.gson.annotations.SerializedName

/**
 * Created by Yoris Prayogo on 2019-11-08.
 * Copyright (c) 2019 PT. Tokopedia All rights reserved.
 */

data class UserDataPojo(
        val date: String = "",

        @SerializedName("content-md5")
        val content_md5: String = "",

        val authorization: String = "",

        @SerializedName("accounts-authorization")
        val accountAuth: String = ""
)