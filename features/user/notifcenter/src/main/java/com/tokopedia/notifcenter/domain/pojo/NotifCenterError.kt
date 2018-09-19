package com.tokopedia.notifcenter.domain.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class NotifCenterError(
        @SerializedName("errors")
        @Expose
        val errors: List<ErrorsItem> = ArrayList()
)