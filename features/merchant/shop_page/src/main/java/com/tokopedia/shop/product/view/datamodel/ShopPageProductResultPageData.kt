package com.tokopedia.shop.product.view.datamodel

import com.tokopedia.shop.common.data.model.ShopPageGetDynamicTabResponse
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo

data class ShopPageProductResultPageData(
    val shopInfo: ShopInfo,
    val shopDynamicTab: ShopPageGetDynamicTabResponse.ShopPageGetDynamicTab
)
