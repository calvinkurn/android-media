package com.tokopedia.feedcomponent.shoprecom.model

/**
 * created by fachrizalmrsln on 13/07/22
 **/
data class ShopRecomUiModel(
    val isShown: Boolean = false,
    val items: List<ShopRecomUiModelItem> = emptyList(),
    val nextCursor: String = "",
    val title: String = "",
    val loadNextPage: Boolean = false,
    val isRefresh: Boolean = false,
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
    val state: ShopRecomFollowState = ShopRecomFollowState.UNFOLLOW,
)

enum class ShopRecomFollowState {
    FOLLOW, UNFOLLOW, LOADING_FOLLOW, LOADING_UNFOLLOW
}
