package com.tokopedia.notifications.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class PushPayloadExtra(
    @SerializedName("isReview_Notif")
    @ColumnInfo(name = "isReview_Notif")
    @Expose
    var isReviewNotif: Boolean? = false,

) : Parcelable