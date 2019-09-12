package com.tokopedia.affiliate.feature.dashboard.view.viewmodel

/**
 * Created by jegul on 2019-09-12.
 */
data class ShareableByMeProfileViewModel(
        val userName: String,
        val avatar: String,
        val link: String
) {
    val formattedUserName = "@$userName"
}