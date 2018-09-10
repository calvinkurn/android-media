package com.tokopedia.gm.subscribe.membership.data.model

data class GoldSetSubscription(
        var data: String = "",
        var status: String = "",
        var error_message: List<Any> = listOf()
)