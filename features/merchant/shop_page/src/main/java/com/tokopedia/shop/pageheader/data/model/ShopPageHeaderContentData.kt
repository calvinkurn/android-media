package com.tokopedia.shop.pageheader.data.model

import com.tokopedia.shop.common.graphql.data.shopinfo.Broadcaster
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopBadge
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.shop.common.graphql.data.shopoperationalhourstatus.ShopOperationalHourStatus

data class ShopPageHeaderContentData(
    var shopInfo: ShopInfo = ShopInfo(),
    var shopBadge: ShopBadge = ShopBadge(),
    var shopOperationalHourStatus: ShopOperationalHourStatus = ShopOperationalHourStatus(),
    var broadcasterConfig: Broadcaster.Config = Broadcaster.Config(),
    var favoriteData: ShopInfo.FavoriteData = ShopInfo.FavoriteData()
)
