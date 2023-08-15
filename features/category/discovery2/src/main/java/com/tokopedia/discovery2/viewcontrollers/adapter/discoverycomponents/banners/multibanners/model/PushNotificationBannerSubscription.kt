package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.banners.multibanners.model

import com.tokopedia.discovery2.Utils.Companion.BANNER_SUBSCRIPTION_DEFAULT_POSITION
import com.tokopedia.kotlin.extensions.view.EMPTY

data class PushNotificationBannerSubscription(
    val position: Int = BANNER_SUBSCRIPTION_DEFAULT_POSITION,
    val errorMessage: String = String.EMPTY,
    val isSubscribed: Boolean = false
)
