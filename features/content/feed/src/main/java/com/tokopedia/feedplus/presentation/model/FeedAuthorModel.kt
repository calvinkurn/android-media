package com.tokopedia.feedplus.presentation.model

import com.tokopedia.feedplus.presentation.model.type.AuthorType

/**
 * Created By : Muhammad Furqan on 28/02/23
 */
data class FeedAuthorModel(
    val id: String,
    val type: AuthorType,
    val name: String,
    val badgeUrl: String,
    val logoUrl: String,
    val appLink: String,
    val encryptedUserId: String,
    val isLive: Boolean
)
