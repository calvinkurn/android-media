package com.tokopedia.shop.pageheader.data.model

import com.tokopedia.feedcomponent.data.pojo.whitelist.Whitelist
import com.tokopedia.shop.common.graphql.data.shopinfo.BroadcasterConfig
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopBadge
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo

data class ShopInfoShopBadgeFeedWhitelist(
        var shopInfo: ShopInfo? = null,
        var shopBadge: ShopBadge? = null,
        var broadcasterConfigAllowed: Boolean? = false,
        var feedWhitelist: Whitelist? = null
)