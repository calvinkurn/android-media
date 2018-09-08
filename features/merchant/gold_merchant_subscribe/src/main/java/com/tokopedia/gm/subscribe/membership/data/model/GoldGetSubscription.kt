package com.tokopedia.gm.subscribe.membership.data.model

data class GoldGetSubscription(
        var data: MembershipData = MembershipData(),
        var error_message: List<Any> = listOf()
)