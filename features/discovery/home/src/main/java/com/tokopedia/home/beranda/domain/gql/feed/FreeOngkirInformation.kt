package com.tokopedia.home.beranda.domain.gql.feed

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Fikry on 2019-09-30
 */
class FreeOngkirInformation (
        @SerializedName("is_active")
        @Expose
        val isActive: Boolean,
        @SerializedName("image_url")
        @Expose
        val imageUrl: String
)