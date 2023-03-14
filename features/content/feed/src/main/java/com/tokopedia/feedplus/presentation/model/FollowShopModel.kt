package com.tokopedia.feedplus.presentation.model

/**
 * Created By : Muhammad Furqan on 07/03/23
 */
data class FollowShopModel(
    val id: String,
    val encryptedId: String,
    val isShop: Boolean,
    val success: Boolean,
    val isFollowing: Boolean
)
