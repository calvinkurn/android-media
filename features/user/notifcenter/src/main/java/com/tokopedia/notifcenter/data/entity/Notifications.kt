package com.tokopedia.notifcenter.data.entity

import com.google.gson.annotations.SerializedName

data class Notifications(
    @SerializedName("is_tab_update")
    val isTabUpdate: Boolean = false
)