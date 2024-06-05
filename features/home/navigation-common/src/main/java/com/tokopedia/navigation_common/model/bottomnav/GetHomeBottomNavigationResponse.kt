package com.tokopedia.navigation_common.model.bottomnav

import com.google.gson.annotations.SerializedName

data class GetHomeBottomNavigationResponse(
    @SerializedName("getHomeBottomNavigation")
    val data: Data = Data()
) {

    data class Data(
        @SerializedName("bottomNavigations")
        val bottomNavigations: List<BottomNavigation> = emptyList()
    )

    data class BottomNavigation(
        @SerializedName("id")
        val id: Long = 0L,

        @SerializedName("name")
        val name: String = "",

        @SerializedName("type")
        val type: String = "",

        @SerializedName("imageList")
        val imageList: List<Image> = emptyList(),

        @SerializedName("jumper")
        val jumper: Jumper? = null,

        @SerializedName("discoID")
        val discoId: String = "",

        @SerializedName("queryParams")
        val queryParams: String = ""
    )

    data class Image(
        @SerializedName("type")
        val type: String = "",

        @SerializedName("imageUrl")
        val imageUrl: String = "",

        @SerializedName("leftPadding")
        val leftPadding: Float = 0f,

        @SerializedName("rightPadding")
        val rightPadding: Float = 0f,

        @SerializedName("imageType")
        val imageType: String = "",
    )

    data class Jumper(
        @SerializedName("id")
        val id: Long = 0L,

        @SerializedName("name")
        val name: String = "",

        @SerializedName("imageList")
        val imageList: List<Image> = emptyList(),
    )
}
