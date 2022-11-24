package com.tokopedia.notifications.model.payload_extra

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TokoChat(
    @SerializedName("orderIdTkpd")
    var orderIdTkpd: String? = "",

    @SerializedName("orderIdGojek")
    var orderIdGojek: String? = ""
) : Parcelable
