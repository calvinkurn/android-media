package com.tokopedia.interestpick.data.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Relationships(
        @SerializedName("is_selected")
        @Expose
        val isSelected: Boolean = false
)