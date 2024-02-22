package com.tokopedia.home_component.usecase.thematic

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class ThematicResponse(
    @Expose
    @SerializedName("getHomeThematic")
    val getThematic: GetThematic

) {
    data class GetThematic(
        @Expose
        @SerializedName("thematic")
        val thematic: Thematic
    )

    data class Thematic(
        @Expose
        @SerializedName("isShown")
        val isShown: Boolean,
        @Expose
        @SerializedName("colorMode")
        val colorMode: String,
        @Expose
        @SerializedName("heightPercentage")
        val heightPercentage: Int,
        @Expose
        @SerializedName("backgroundImageURL")
        val backgroundImageURL: String,
        @Expose
        @SerializedName("foregroundImageURL")
        val foregroundImageURL: String,
    )
}
