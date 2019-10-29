package com.tokopedia.shop.common.data.viewmodel

import com.tokopedia.shop.common.graphql.data.stampprogress.MembershipQuests

data class ItemRegisteredViewModel(
        val membershipProgram: MembershipQuests = MembershipQuests(),
        val url: String = ""
) : BaseMembershipViewModel