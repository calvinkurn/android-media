package com.tokopedia.home_account.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Yoris Prayogo on 22/10/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

data class Cta(

        @SerializedName("appLink")
        @Expose
        val appLink: String = "",

        @SerializedName("text")
        @Expose
        val text: String = "",

        @SerializedName("url")
        @Expose
        val url: String = ""
)