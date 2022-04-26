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

    @SerializedName("start_time")
    val timerStartTime: String = "",

    @SerializedName("end_time")
    val timerEndTime: String = "",

    @SerializedName("server_time")
    val serverTime: String = "",

    @SerializedName("products")
    val listOfProducts: List<Product> = emptyList(),

    @SerializedName("background")
    val background: Background = Background(),

    @SerializedName("countdown")
    val countdown: Countdown = Countdown(),

    @SerializedName("source_id")
    val id: String = "",
){
    data class Background(
        @SerializedName("gradient")
        val gradientList: List<String>? = null,

        @SerializedName("image_url")
        val imageUrl: String = ""
    )

    data class Countdown(
        @SerializedName("copy")
        val countdownInfo: String = ""
    )
}