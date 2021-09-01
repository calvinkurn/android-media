package com.tokopedia.home_component.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * created by Dhaba
 */
data class ChannelViewAllCard(
    val id: String = "",
    val contentType: String = "",
    val title: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val gradientColor: ArrayList<String> = arrayListOf()
)