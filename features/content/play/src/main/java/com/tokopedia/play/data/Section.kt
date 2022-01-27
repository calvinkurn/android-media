package com.tokopedia.play.data

import com.google.gson.annotations.SerializedName

/**
 * @author by astidhiyaa on 27/01/22
 */
data class Section (
    @SerializedName("type")
    val sectionType: String = "",

    @SerializedName("title")
    val sectionTitle: String = "",

    @SerializedName("startTime")
    val timerStartTime: String = "",

    @SerializedName("serverTime")
    val timerEndTime: String = "",

    @SerializedName("type")
    val serverTime: String = "",

    @SerializedName("products")
    val listOfProducts: List<Product> = emptyList(),
){
    data class Background(
        @SerializedName("gradient")
        val gradientList: List<String> = emptyList(),

        @SerializedName("imageUrl")
        val imageUrl: String = ""
    )

    data class Countdown(
        @SerializedName("copy")
        val countdownInfo: String = ""
    )
}