package com.tokopedia.shop.pageheader.data.model

import com.tokopedia.feedcomponent.data.pojo.whitelist.Whitelist
import com.tokopedia.shop.common.data.model.ShopPageGetDynamicTabResponse
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo

data class NewShopPageHeaderP1(
    val shopPageGetDynamicTabResponse: ShopPageGetDynamicTabResponse = ShopPageGetDynamicTabResponse(),
    val shopInfoCoreAndAssetsData: ShopInfo = ShopInfo(),
    val feedWhitelist: Whitelist = Whitelist()
)
