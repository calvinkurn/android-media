package com.tokopedia.shop.pageheader.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.feedcomponent.data.pojo.whitelist.Whitelist
import com.tokopedia.shop.common.graphql.data.isshopofficial.GetIsShopOfficialStore
import com.tokopedia.shop.common.graphql.data.isshoppowermerchant.GetIsShopPowerMerchant
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.shop.product.data.model.ShopProduct

data class ShopPageHeaderP1(
        val isShopOfficialStore: GetIsShopOfficialStore,
        val isShopPowerMerchant: GetIsShopPowerMerchant,
        val shopInfoTopContentData: ShopInfo,
        val shopInfoHomeTypeData: ShopPageGetHomeType,
        val shopInfoCoreAndAssetsData: ShopInfo,
        val feedWhitelist: Whitelist,
        val productList: ShopProduct.GetShopProduct
)