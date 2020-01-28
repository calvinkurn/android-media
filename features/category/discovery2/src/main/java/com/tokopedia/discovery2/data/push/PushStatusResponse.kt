package com.tokopedia.discovery2.data.push

import com.google.gson.annotations.SerializedName
import com.tokopedia.discovery2.data.notifier.NotifierCheckReminder

data class PushStatusResponse(
        @SerializedName("notifier_checkReminder")
        val notifierCheckReminder: NotifierCheckReminder? = null
)
