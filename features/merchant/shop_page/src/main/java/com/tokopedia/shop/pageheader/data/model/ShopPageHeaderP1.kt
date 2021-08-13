package com.tokopedia.shop.pageheader.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.feedcomponent.data.pojo.whitelist.Whitelist
import com.tokopedia.shop.common.graphql.data.isshopofficial.GetIsShopOfficialStore
import com.tokopedia.shop.common.graphql.data.isshoppowermerchant.GetIsShopPowerMerchant
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.shop.product.data.model.ShopProduct

data class ShopPageHeaderP1(
        val isShopOfficialStore: GetIsShopOfficialStore = GetIsShopOfficialStore(),
        val isShopPowerMerchant: GetIsShopPowerMerchant = GetIsShopPowerMerchant(),
        val shopInfoTopContentData: ShopInfo = ShopInfo(),
        val shopInfoHomeTypeData: ShopPageGetHomeType = ShopPageGetHomeType(),
        val shopInfoCoreAndAssetsData: ShopInfo = ShopInfo(),
        val feedWhitelist: Whitelist = Whitelist()
)