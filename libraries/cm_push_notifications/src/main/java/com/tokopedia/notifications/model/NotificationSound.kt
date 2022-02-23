package com.tokopedia.notifications.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NotificationSound(
    @SerializedName("nf_channel_name")
    @Expose
    val channelName : String?,
    @SerializedName("nf_sound")
    @Expose
    val channelSound : String?
) : Parcelable
