package com.tokopedia.search.result.mps.shopwidget

import com.tokopedia.search.result.mps.domain.model.MPSModel.SearchShopMPS.Shop.Badge

data class MPSShopBadgeDataView(
    val imageUrl: String = "",
    val isShow: Boolean = false,
) {
    fun willShow(): Boolean = imageUrl.isNotEmpty() && isShow

    companion object {
        fun create(badge: Badge): MPSShopBadgeDataView = MPSShopBadgeDataView(
            imageUrl = badge.imageUrl,
            isShow = badge.isShow,
        )
    }
}
