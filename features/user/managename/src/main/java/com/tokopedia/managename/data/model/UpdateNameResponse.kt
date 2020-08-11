package com.tokopedia.managename.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Yoris Prayogo on 04/06/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

data class UpdateNameResponse(
        @SerializedName("userProfileUpdate")
        @Expose
        val data: UpdateNameModel? = null
)