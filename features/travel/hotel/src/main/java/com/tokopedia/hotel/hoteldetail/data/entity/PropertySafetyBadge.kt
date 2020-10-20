package com.tokopedia.hotel.hoteldetail.data.entity

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * @author by jessica on 07/10/20
 */

@Parcelize
data class PropertySafetyBadge(
        @SerializedName("show")
        @Expose
        val isShow: Boolean = false,
        @SerializedName("title")
        @Expose
        val title: String = "",
        @SerializedName("content")
        @Expose
        val content: String = "",
        @SerializedName("icon")
        @Expose
        val icon: SafetyBadgeIcon = SafetyBadgeIcon()
): Parcelable {
    @Parcelize
    data class SafetyBadgeIcon(
            @SerializedName("dark")
            @Expose
            val dark: String = "",

            @SerializedName("light")
            @Expose
            val light: String = ""
    ): Parcelable
}