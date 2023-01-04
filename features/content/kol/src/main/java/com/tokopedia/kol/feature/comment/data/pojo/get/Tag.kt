package com.tokopedia.kol.feature.comment.data.pojo.get

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

/**
 * Created By : Muhammad Furqan on 05/12/22
 */
data class Tag (
    @SerializedName("id")
val id: String = "",
    @SerializedName("type")
val type: String = "",
    @SerializedName("link")
val link: String = "",
    @SuppressLint("Invalid Data Type")
    @SerializedName("price")
    val price: String = "",
    @SerializedName("url")
val url: String = "",
    @SerializedName("caption")
val caption: String = ""
)
