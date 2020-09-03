package com.tokopedia.shop.pageheader.util

import com.tokopedia.feedcomponent.data.pojo.whitelist.Whitelist
import com.tokopedia.shop.common.graphql.data.isshopofficial.GetIsShopOfficialStore
import com.tokopedia.shop.common.graphql.data.isshoppowermerchant.GetIsShopPowerMerchant
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.shop.pageheader.ShopPageHeaderConstant.SHOP_PAGE_POWER_MERCHANT_ACTIVE
import com.tokopedia.shop.pageheader.data.model.ShopPageGetHomeType
import com.tokopedia.shop.pageheader.presentation.uimodel.ShopPageP1HeaderData

object ShopPageHeaderMapper {

    fun mapToShopPageP1HeaderData(
            shopInfoOsData: GetIsShopOfficialStore,
            shopInfoGoldData: GetIsShopPowerMerchant,
            shopInfoTopContentData: ShopInfo,
            shopPageHomeTypeData: ShopPageGetHomeType,
            shopInfoShopCoreShopAssetsData: ShopInfo,
            feedWhitelistData: Whitelist
    ) = ShopPageP1HeaderData(
            shopInfoOsData.data.isOfficial,
            shopInfoGoldData.data.powerMerchant.status == SHOP_PAGE_POWER_MERCHANT_ACTIVE,
            shopInfoTopContentData.topContent.topUrl,
            shopPageHomeTypeData.shopHomeType,
            shopInfoShopCoreShopAssetsData.shopCore.name,
            shopInfoShopCoreShopAssetsData.shopAssets.avatar,
            shopInfoShopCoreShopAssetsData.shopCore.domain,
            feedWhitelistData.isWhitelist,
            feedWhitelistData.url
    )

}