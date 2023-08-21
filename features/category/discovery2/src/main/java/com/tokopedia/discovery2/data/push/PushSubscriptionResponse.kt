package com.tokopedia.discovery2.data.push

import com.google.gson.annotations.SerializedName
import com.tokopedia.discovery2.data.notifier.NotifierSetReminder

data class PushSubscriptionResponse(
        @SerializedName("notifier_setReminder")
        val notifierSetReminder: NotifierSetReminder? = null
) {
    fun isSuccess() = notifierSetReminder?.isSuccess == 1 || notifierSetReminder?.isSuccess == 2

    fun getErrorMessage(): String = notifierSetReminder?.errorMessage.orEmpty()
}
