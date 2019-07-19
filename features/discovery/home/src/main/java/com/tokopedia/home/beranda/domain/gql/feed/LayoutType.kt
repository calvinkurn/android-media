package com.tokopedia.home.beranda.domain.gql.feed


import com.google.gson.annotations.SerializedName

data class LayoutType(
        @SerializedName("type")
        val type: String
)