package com.tokopedia.common_category.model.topAds

import com.google.gson.annotations.SerializedName

data class LabelsItem(

        @field:SerializedName("color")
        val color: String? = null,

        @field:SerializedName("title")
        val title: String? = null
)