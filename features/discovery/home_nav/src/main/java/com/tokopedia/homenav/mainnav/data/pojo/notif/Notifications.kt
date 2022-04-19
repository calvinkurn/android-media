package com.tokopedia.homenav.mainnav.data.pojo.notif

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Notifications(
        @SerializedName("resolutionAs")
        @Expose
        val resolutionAs: ResolutionAs,
        @SerializedName("inbox")
        @Expose
        val inbox: Inbox
)