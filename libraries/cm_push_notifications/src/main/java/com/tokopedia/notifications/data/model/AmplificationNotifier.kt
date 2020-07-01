package com.tokopedia.notifications.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AmplificationNotifier(
        @Expose
        @SerializedName("notifier_getPushDataToRender")
        val webhookAttributionNotifier: Amplification = Amplification()
)