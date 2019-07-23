package com.tokopedia.home.beranda.domain.gql.feed

import com.google.gson.annotations.SerializedName

/**
 * Created by Lukas on 2019-07-18
 */
class Label (
        @SerializedName("title")
        val title: String,
        @SerializedName("color")
        val color: String
)