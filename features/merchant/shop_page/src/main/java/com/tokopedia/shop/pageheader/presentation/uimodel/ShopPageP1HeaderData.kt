package com.tokopedia.shop.pageheader.presentation.uimodel

import com.tokopedia.feedcomponent.data.pojo.whitelist.Whitelist
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopBadge
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo

data class ShopPageP1HeaderData(
        val isOfficial: Boolean = false,
        val isGoldMerchant: Boolean = false,
        val topContentUrl: String = "",
        val shopHomeType: String = "",
        val shopName: String = "",
        val shopAvatar: String = "",
        val shopDomain: String = "",
        val isWhitelist: Boolean = false,
        val url: String = ""
)