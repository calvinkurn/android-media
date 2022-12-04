package com.tokopedia.home.beranda.domain.gql.feed

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Lukas on 2019-07-01
 */
data class Badge(
    @SerializedName("title")
    @Expose
    val title: String = "",
    @SerializedName("image_url", alternate = ["imageUrl"])
    @Expose
    val imageUrl: String = ""
)
