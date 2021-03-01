package com.tokopedia.shop.pageheader.presentation.uimodel

import com.tokopedia.feedcomponent.data.pojo.whitelist.Whitelist
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopBadge
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.shop.pageheader.presentation.uimodel.widget.ShopHeaderWidgetUiModel

data class NewShopPageP1HeaderData(
        val isOfficial: Boolean = false,
        val isGoldMerchant: Boolean = false,
        val shopHomeType: String = "",
        val shopName: String = "",
        val shopAvatar: String = "",
        val shopDomain: String = "",
        val isWhitelist: Boolean = false,
        val url: String = "",
        val listShopHeaderWidget: List<ShopHeaderWidgetUiModel> = listOf()
)