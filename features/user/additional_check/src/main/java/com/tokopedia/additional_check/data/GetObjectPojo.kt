package com.tokopedia.additional_check.data

import com.google.gson.annotations.SerializedName

/**
 * Created by Yoris Prayogo on 2019-11-08.
 * Copyright (c) 2019 PT. Tokopedia All rights reserved.
 */

data class GetObjectPojo(
        @SerializedName("status")
        val status: Boolean = false,

        @SerializedName("data")
        val bottomSheetModel: BottomSheetModel? = null
)

data class BottomSheetModel(
        @SerializedName("image")
        val image: String = "",
        @SerializedName("text")
        val text: String = "",
        @SerializedName("applink")
        val applink: String = "",
        @SerializedName("title")
        val title: String = "",
        @SerializedName("btn_text")
        val btnText: String = "")