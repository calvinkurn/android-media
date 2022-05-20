package com.tokopedia.tokopedianow.home.presentation.model

data class HomeReferralDataModel(
    val ogImage: String = "",
    val ogTitle: String = "",
    val ogDescription: String = "",
    val textDescription: String = "",
    val sharingUrlParam: String = "",
    val userStatus: String = "",
    val maxReward: String = "",
    val isSender: Boolean = false
)