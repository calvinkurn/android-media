package com.tokopedia.feedcomponent.data.pojo.shoprecom

data class ShopRecomUiModel(
    val isShown: Boolean = false,
    val items: List<ShopRecomUiModelItem> = emptyList(),
    val nextCursor: String = "",
    val title: String = "",
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
