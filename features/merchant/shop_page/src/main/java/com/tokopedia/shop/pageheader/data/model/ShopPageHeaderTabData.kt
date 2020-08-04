package com.tokopedia.shop.pageheader.data.model

import com.tokopedia.feedcomponent.data.pojo.whitelist.Whitelist
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopBadge
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo

data class ShopPageHeaderTabData(
        var shopInfo: ShopInfo = ShopInfo(),
        var feedWhitelist: Whitelist = Whitelist()
)