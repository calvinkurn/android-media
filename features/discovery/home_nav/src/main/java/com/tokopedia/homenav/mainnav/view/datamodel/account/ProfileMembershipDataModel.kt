package com.tokopedia.homenav.mainnav.view.datamodel.account

data class ProfileMembershipDataModel(
    var tokopointPointAmount: String = "",
    var tokopointExternalAmount: String = "",
    var isTokopointExternalAmountError: Boolean = false,
    var tokopointBadgeUrl: String = "",
    var tierBadgeUrl: String = "",
    var isGetUserMembershipError: Boolean = false,
    var badge: String = ""
)
