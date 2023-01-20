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
    var badgeImageURL: String = "",
    var encryptedID: String = "",
    var id: Long = 0,
    var logoImageURL: String = "",
    var name: String = "",
    var nickname: String = "",
    var type: Int = 0,
    var applink: String = "",
    var state: ShopRecomFollowState = ShopRecomFollowState.UNFOLLOW,
)

enum class ShopRecomFollowState {
    FOLLOW, UNFOLLOW, LOADING_FOLLOW, LOADING_UNFOLLOW
}
