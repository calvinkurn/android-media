package com.tokopedia.notifcenter.data.entity.filter

import com.google.gson.annotations.SerializedName

data class NotifcenterFilterResponse(
        @SerializedName("notifcenter_filter_v2")
        val notifcenterFilterV2: NotifcenterFilterV2 = NotifcenterFilterV2()
)