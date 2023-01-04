package com.tokopedia.similarsearch.getsimilarproducts.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LabelGroupVariant(
    @SerializedName("title")
    @Expose
    val title: String = "",

    @SerializedName("type")
    @Expose
    val type: String = "",

    @SerializedName("type_variant")
    @Expose
    val typeVariant: String = "",

    @SerializedName("hex_color")
    @Expose
    val hexColor: String = "",
)
