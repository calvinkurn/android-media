package com.tokopedia.watch.notification.model

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class NotificationCenterDetailResponse(
    @SerializedName("notifcenter_detail_v3")
    val notificationCenterDetail: NotificationCenterDetail = NotificationCenterDetail()
)

data class NotificationCenterDetail(
    @SerializedName("new_list")
    val newList: List<NotificationModel> = listOf()
)

@SuppressLint("Invalid Data Type")
data class NotificationModel(
    @SerializedName("notif_id")
    val notifId: String = "",
    @SerializedName("read_status")
    var readStatus: Int = 0,
    @SerializedName("short_description")
    val shortDescription: String = "",
    @SerializedName("status")
    val status: Int = 0,
    @SerializedName("title")
    val title: String = ""
)