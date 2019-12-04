package com.tokopedia.shop.favourite.view.model

/**
 * Created by jegul on 2019-11-05
 */
data class ShopFollowerListResultUiModel(
        val isCanLoadMore: Boolean,
        val currentPage: Int,
        val followerUiModelList: List<ShopFollowerUiModel>
)