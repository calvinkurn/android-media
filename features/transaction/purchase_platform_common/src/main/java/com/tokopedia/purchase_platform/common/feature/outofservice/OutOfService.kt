package com.tokopedia.purchase_platform.common.feature.outofservice

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OutOfServiceData(
        @SerializedName("id")
        val id: String = "",
        @SerializedName("image")
        val image: String = "",
        @SerializedName("title")
        val title: String = "",
        @SerializedName("description")
        val description: String = "",
        @SerializedName("buttons")
        val buttons: List<OutOfServiceButtonData> = emptyList()
) : Parcelable {

    companion object {
        const val ID_MAINTENANCE = "1"
        const val ID_OVERLOAD = "2"
        const val ID_TIMEOUT = "3"
        const val TYPE_MAINTENANCE = "Maintenance"
        const val TYPE_SERVER_DOWN = "Server Down"
        const val TYPE_RATE_LIMITER = "Got Rate Limiter"
        const val TYPE_NO_INTERNET_CONNECTION = "No internet connection"
    }

    val isOutOfService: Boolean
        get() = id.isNotBlank() && id != "0"

    fun getErrorType(): String {
        return when (id) {
            ID_MAINTENANCE -> return TYPE_MAINTENANCE
            ID_OVERLOAD -> return TYPE_RATE_LIMITER
            ID_TIMEOUT -> return TYPE_SERVER_DOWN
            else -> TYPE_NO_INTERNET_CONNECTION
        }
    }
}

@Parcelize
data class OutOfServiceButtonData(
        @SerializedName("id")
        val id: String = "",
        @SerializedName("code")
        val code: String = "",
        @SerializedName("message")
        val message: String = "",
        @SerializedName("color")
        val color: String = ""
) : Parcelable {
    companion object {
        const val ID_START_SHOPPING = "1"
        const val ID_RETRY = "2"
        const val ID_HOMEPAGE = "3"
        const val ID_SETTING = "4"
    }
}