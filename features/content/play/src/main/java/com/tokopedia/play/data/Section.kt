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

    @SerializedName("endTime")
    val timerEndTime: String = "",

    @SerializedName("serverTime")
    val serverTime: String = "",

    @SerializedName("products")
    val listOfProducts: List<Product> = emptyList(),

    @SerializedName("background")
    val background: Background = Background(),

    @SerializedName("countdown")
    val countdown: Countdown = Countdown(),

    @SerializedName("sourceID")
    val id: String = ""

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