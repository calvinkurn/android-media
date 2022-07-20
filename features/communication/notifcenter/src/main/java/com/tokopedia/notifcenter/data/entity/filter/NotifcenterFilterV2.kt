package com.tokopedia.notifcenter.data.entity.filter

import com.google.gson.annotations.SerializedName

data class NotifcenterFilterV2(
        @SerializedName("notifcenter_tagList")
        val notifcenterTagList: NotifcenterTagList = NotifcenterTagList()
)