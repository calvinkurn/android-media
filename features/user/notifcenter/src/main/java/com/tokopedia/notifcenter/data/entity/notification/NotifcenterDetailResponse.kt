package com.tokopedia.notifcenter.data.entity.notification


import com.google.gson.annotations.SerializedName

data class NotifcenterDetailResponse(
        @SerializedName("notifcenter_detail_v3")
        val notifcenterDetail: NotifcenterDetail = NotifcenterDetail()
)