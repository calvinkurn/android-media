package com.tokopedia.home.beranda.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by frenzel
 */
data class HomeThematicResponse (
    @Expose
    @SerializedName("getHomeThematic")
    val getHomeThematic: GetHomeThematic

) {
    data class GetHomeThematic(
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
