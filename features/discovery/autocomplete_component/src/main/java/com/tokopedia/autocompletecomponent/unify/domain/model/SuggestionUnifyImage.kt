package com.tokopedia.autocompletecomponent.unify.domain.model


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class SuggestionUnifyImage(
    @SerializedName("icon_image_url")
    @Expose
    val iconImageUrl: String = "",

    @SerializedName("image_url")
    @Expose
    val imageUrl: String = "",

    @SerializedName("is_border")
    @Expose
    val isBorder: Boolean = false,

    @SerializedName("radius")
    @Expose
    val radius: String = ""
)
