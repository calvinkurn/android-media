package com.tokopedia.shop.pageheader.data.model

import com.tokopedia.feedcomponent.data.pojo.whitelist.Whitelist
import com.tokopedia.shop.common.data.model.ShopPageGetDynamicTabResponse
import com.tokopedia.shop.common.graphql.data.isshopofficial.GetIsShopOfficialStore
import com.tokopedia.shop.common.graphql.data.isshoppowermerchant.GetIsShopPowerMerchant
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo

data class NewShopPageHeaderP1(
    val isShopOfficialStore: GetIsShopOfficialStore = GetIsShopOfficialStore(),
    val isShopPowerMerchant: GetIsShopPowerMerchant = GetIsShopPowerMerchant(),
    val shopInfoTopContentData: ShopInfo = ShopInfo(),
    val shopPageGetDynamicTabResponse: ShopPageGetDynamicTabResponse = ShopPageGetDynamicTabResponse(),
    val shopInfoCoreAndAssetsData: ShopInfo = ShopInfo(),
    val feedWhitelist: Whitelist = Whitelist()
)
