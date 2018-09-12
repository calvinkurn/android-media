package com.tokopedia.gm.subscribe.membership.data.model

data class GetMembershipData(
        var shop_id: Int = 0,
        var expired_date: String = "",
        var auto_withdrawal_date: String = "",
        var auto_extend: Int = 0,
        var subscription: Subscription = Subscription()
)