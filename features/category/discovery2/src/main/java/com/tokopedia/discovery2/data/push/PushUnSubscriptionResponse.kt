package com.tokopedia.discovery2.data.push

import com.google.gson.annotations.SerializedName
import com.tokopedia.discovery2.data.notifier.NotifierSetReminder

data class PushUnSubscriptionResponse(
        @SerializedName("notifier_unsetReminder")
        val notifierSetReminder: NotifierSetReminder? = null
)
