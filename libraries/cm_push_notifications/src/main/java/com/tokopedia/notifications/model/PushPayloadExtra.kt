package com.tokopedia.notifications.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.notifications.common.CMConstant.PayloadKeys.REPLY_TYPE
import kotlinx.android.parcel.Parcelize


@Parcelize
data class PushPayloadExtra(

    @SerializedName("isReview_Notif")
    @Expose
    var isReviewNotif: Boolean? = false,


    @SerializedName(REPLY_TYPE)
    @Expose
    var replyType: String? = null,

    @SerializedName("user_type")
    var userType: String? = null

) : Parcelable
