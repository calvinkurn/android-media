package com.tokopedia.feedcomponent.shoprecom.model

import com.tokopedia.kotlin.model.ImpressHolder

/**
 * created by fachrizalmrsln on 13/07/22
 **/
data class ShopRecomUiModel(
    val isShown: Boolean = false,
    val items: List<ShopRecomUiModelItem> = emptyList(),
    val nextCursor: String = "",
    val title: String = "",
    val loadNextPage: Boolean = false,
    val isRefresh: Boolean = false
)

data class ShopRecomUiModelItem(
    val badgeImageURL: String = "",
    val encryptedID: String = "",
    val id: Long = 0,
    val logoImageURL: String = "",
    val name: String = "",
    val nickname: String = "",
    val type: Int = 0,
    val applink: String = "",
    val state: ShopRecomFollowState = ShopRecomFollowState.UNFOLLOW
) {
    val impressHolder = ImpressHolder()

    companion object {
        const val FOLLOW_TYPE_SHOP = 2
        const val FOLLOW_TYPE_BUYER = 3
    }
}

enum class ShopRecomFollowState {
    FOLLOW, UNFOLLOW, LOADING_FOLLOW, LOADING_UNFOLLOW
}
