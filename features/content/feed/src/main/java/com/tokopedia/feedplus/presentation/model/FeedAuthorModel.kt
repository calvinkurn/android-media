package com.tokopedia.feedplus.presentation.model

/**
 * Created By : Muhammad Furqan on 28/02/23
 */
data class FeedAuthorModel(
    val id: String = "",
    val type: Int = 0,
    val name: String = "",
    val description: String = "",
    val badgeUrl: String = "",
    val logoUrl: String = "",
    val applink: String = "",
    val encryptedUserId: String = "",
    val isLive: Boolean = false
)
