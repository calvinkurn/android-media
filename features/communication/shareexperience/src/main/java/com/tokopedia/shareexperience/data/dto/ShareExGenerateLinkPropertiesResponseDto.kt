package com.tokopedia.shareexperience.data.dto

import com.google.gson.annotations.SerializedName

data class ShareExGenerateLinkPropertiesResponseDto(
    @SerializedName("message")
    val message: String = "",
    @SerializedName("ogTitle")
    val ogTitle: String = "",
    @SerializedName("ogDescription")
    val ogDescription: String = "",
    @SerializedName("ogType")
    val ogType: String = "",
    @SerializedName("ogImageUrl")
    val ogImageUrl: String = "",
    @SerializedName("ogVideo")
    val ogVideo: String = "",
    @SerializedName("desktopUrl")
    val desktopUrl: String = "",
    @SerializedName("androidUrl")
    val androidUrl: String = "",
    @SerializedName("iosUrl")
    val iosUrl: String = "",
    @SerializedName("androidDeeplinkPath")
    val androidDeeplinkPath: String = "",
    @SerializedName("iosDeeplinkPath")
    val iosDeeplinkPath: String = "",
    @SerializedName("anMinVersion")
    val androidMinVersion: String = "",
    @SerializedName("iosMinVersion")
    val iosMinVersion: String = "",
    @SerializedName("canonicalUrl")
    val canonicalUrl: String = ""
)
