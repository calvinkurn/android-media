package com.tokopedia.cartcommon.data.response.common

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class OutOfService(
        @SerializedName("buttons")
        val buttons: List<Button> = emptyList(),
        @SerializedName("code")
        val code: String = "",
        @SerializedName("description")
        val description: String = "",
        @SerializedName("id")
        val id: String = "",
        @SerializedName("image")
        val image: String = "",
        @SerializedName("title")
        val title: String = ""
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

    fun isOutOfService(): Boolean {
        return id.isNotBlank() && id != "0"
    }

    fun getErrorType(): String {
        return when (id) {
            ID_MAINTENANCE -> return TYPE_MAINTENANCE
            ID_OVERLOAD -> return TYPE_RATE_LIMITER
            ID_TIMEOUT -> return TYPE_SERVER_DOWN
            else -> TYPE_NO_INTERNET_CONNECTION
        }
    }
}