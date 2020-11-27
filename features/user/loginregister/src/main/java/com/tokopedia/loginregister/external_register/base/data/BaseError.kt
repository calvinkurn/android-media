package com.tokopedia.loginregister.external_register.base.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Yoris Prayogo on 23/11/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

data class BaseError(
        @Expose
        @SerializedName("code")
        val code: Int = 0,
        @Expose
        @SerializedName("title")
        val title: String = "",
        @Expose
        @SerializedName("message")
        val message: String = ""
)