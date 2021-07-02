package com.tokopedia.homenav.mainnav.data.pojo.notif

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Inbox(
        @SerializedName("inbox_ticket")
        @Expose
        val inbox_ticket: Int,

        @SerializedName("inbox_review")
        @Expose
        val review: Int
)