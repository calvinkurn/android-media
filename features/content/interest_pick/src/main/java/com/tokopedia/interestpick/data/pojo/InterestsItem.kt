package com.tokopedia.interestpick.data.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class InterestsItem(
        @SerializedName("relationships")
        @Expose
        val relationships: Relationships = Relationships(),

        @SerializedName("image_url")
        @Expose
        val imageUrl: String = "",

        @SerializedName("name")
        @Expose
        val name: String = "",

        @SerializedName("id")
        @Expose
        val id: Int = 0
)