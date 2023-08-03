package com.tokopedia.shop.pageheader.presentation.uimodel

data class ShopFollowButtonUiModel(
    val isFollowing: Boolean = false,
    var isShowLoading: Boolean = false,
    val textLabel: String = "Follow",
    var leftDrawableUrl: String = "",
    val isNeverFollow: Boolean = false
)
