package com.tokopedia.shop.pageheader.data.model

import com.tokopedia.shop.common.data.model.ShopPageGetDynamicTabResponse
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo

data class NewShopPageHeaderP1(
    val shopPageGetDynamicTabResponse: ShopPageGetDynamicTabResponse = ShopPageGetDynamicTabResponse(),
    val shopInfoCoreAndAssetsData: ShopInfo = ShopInfo()
)
