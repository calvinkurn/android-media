package com.tokopedia.discovery2.data.multibannerresponse

import com.google.gson.annotations.SerializedName

data class PushStatusResponse(
        @SerializedName("notifier_checkReminder")
        val notifierCheckReminder: NotifierCheckReminder? = null
)
