package com.tokopedia.feedcomponent.view.viewmodel.responsemodel

/**
 * @author by yoasfs on 2019-12-09
 */
data class FavoriteShopViewModel (
        var shopId: String = "",
        var rowNumber: Int = 0,
        var adapterPosition: Int = 0,
        var errorMessage: String = "",
        var isSuccess: Boolean = false,
        val isUnfollowFromShopsMenu: Boolean = false,
        val isFollowedFromFollowRestrictionBottomSheet: Boolean = false
)
