package com.tokopedia.gm.subscribe.membership.data.model

data class Subscription(
        var subscription_type: Int = 0,
        var name: String = "",
        var notes: String = "",
        var days_limit: Int = 0
)