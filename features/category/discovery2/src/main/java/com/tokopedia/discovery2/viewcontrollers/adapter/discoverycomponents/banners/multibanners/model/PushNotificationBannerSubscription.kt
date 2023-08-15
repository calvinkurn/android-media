package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.banners.multibanners.model

import com.tokopedia.discovery2.Utils.Companion.BANNER_SUBSCRIPTION_DEFAULT_POSITION
import com.tokopedia.discovery2.data.push.PushSubscriptionResponse
import com.tokopedia.discovery2.data.push.PushUnSubscriptionResponse
import com.tokopedia.kotlin.extensions.view.EMPTY

data class PushNotificationBannerSubscription(
    val position: Int = BANNER_SUBSCRIPTION_DEFAULT_POSITION,
    val errorMessage: String = String.EMPTY,
    val isSubscribed: Boolean = false
)

fun PushSubscriptionResponse?.isSuccess() = this?.notifierSetReminder?.isSuccess == 1 || this?.notifierSetReminder?.isSuccess == 2

fun PushUnSubscriptionResponse?.isSuccess() = this?.notifierSetReminder?.isSuccess == 1 || this?.notifierSetReminder?.isSuccess == 2
