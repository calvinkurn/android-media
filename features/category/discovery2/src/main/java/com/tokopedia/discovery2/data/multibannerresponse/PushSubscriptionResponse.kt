package com.tokopedia.discovery2.data.multibannerresponse

import com.google.gson.annotations.SerializedName

data class PushSubscriptionResponse(
        @SerializedName("notifier_setReminder")
        val notifierSetReminder: NotifierSetReminder? = null
)
