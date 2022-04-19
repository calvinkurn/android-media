package com.tokopedia.category.navbottomsheet.model

import com.google.gson.annotations.SerializedName

data class ChildItem(

        @field:SerializedName("n")
        val name: String? = null,

        @field:SerializedName("id")
        val id: String? = null,

        @field:SerializedName("a")
        val appLink:String? = null,

        @field:SerializedName("c")
        val child: List<ChildItem?>? = null,

        @field:SerializedName("viewType")
        var viewType:Int = 1
)