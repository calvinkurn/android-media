package com.tokopedia.discovery2.data

import com.google.gson.annotations.SerializedName
import javax.annotation.Generated

@Generated("com.robohorse.robopojogenerator")
data class LayoutInfo(

        @SerializedName("base_layout")
        val baseLayout: Int = 0,

        @SerializedName("name")
        val name: String? = "",

        @SerializedName("id")
        val id: Int = 0,

        @SerializedName("column_type")
        val columnType: Int = 0
)