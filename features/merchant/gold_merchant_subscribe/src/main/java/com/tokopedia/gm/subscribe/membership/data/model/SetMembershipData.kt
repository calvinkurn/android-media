package com.tokopedia.gm.subscribe.membership.data.model

data class SetMembershipData(
        var data: String = "",
        var status: String = "",
        var error_message: List<Any> = listOf()
)