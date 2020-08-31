package com.tokopedia.managename.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Yoris Prayogo on 04/06/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

data class UpdateNameModel(
        @SerializedName("isSuccess")
        @Expose
        var isSuccess: Int = 0,
        @SerializedName("completionScore")
        @Expose
        var completionScore: Int = 0,
        @SerializedName("errors")
        @Expose
        var errors: ArrayList<String> = arrayListOf()
)