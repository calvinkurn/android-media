package com.tokopedia.similarsearch.getsimilarproducts.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

internal data class FreeOngkirBadge(
        @SerializedName("is_active")
        @Expose
        val isActive: Boolean = false,


        @SerializedName("img_url")
        @Expose
        val imgUrl: String = ""
)