package com.tokopedia.home.beranda.domain.gql.feed

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Fikry on 2019-09-30
 */
class FreeOngkirInformation(
    @SerializedName("is_active", alternate = ["isActive"])
    @Expose
    val isActive: Boolean = false,
    @SerializedName("image_url", alternate = ["imageUrl"])
    @Expose
    val imageUrl: String = ""
)
