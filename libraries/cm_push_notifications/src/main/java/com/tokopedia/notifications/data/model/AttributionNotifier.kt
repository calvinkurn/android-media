package com.tokopedia.notifications.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AttributionNotifier(
        @Expose
        @SerializedName("notifier_sendWebhookPushNotification")
        val webhookAttributionNotifier: Attribution = Attribution()
)