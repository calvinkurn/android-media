package com.tokopedia.createpost.common.data.pojo.getcontentform

import com.google.gson.annotations.SerializedName

/**
 * @author by milhamj on 05/03/19.
 */
data class Author(
        @SerializedName("type")
        val type: String = "",
        @SerializedName("id")
        val id: String = "",
        @SerializedName("name")
        val name: String = "",
        @SerializedName("thumbnail")
        val thumbnail: String = "",
        @SerializedName("badge")
        val badge: String = ""
)