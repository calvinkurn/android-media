package com.tokopedia.notifcenter.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class NotifCenterError(
        @SerializedName("errors")
        @Expose
        val errors: List<ErrorsItem> = ArrayList()
)