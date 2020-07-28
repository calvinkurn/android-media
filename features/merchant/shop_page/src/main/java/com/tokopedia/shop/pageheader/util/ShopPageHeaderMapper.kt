package com.tokopedia.shop.pageheader.util

import com.tokopedia.feedcomponent.data.pojo.whitelist.Whitelist
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.shop.pageheader.data.model.ShopPageP1Data

object ShopPageHeaderMapper {

    fun mapToShopPageP1Data(
            shopInfoOsData: ShopInfo,
            shopInfoGoldData: ShopInfo,
            shopInfoTopContentData: ShopInfo,
            shopPageHomeTypeData: ShopInfo,
            shopInfoShopCoreShopAssetsData: ShopInfo,
            feedWhitelistData: Whitelist
    ) = ShopPageP1Data(
            shopInfoOsData.os.isOfficial == 1,
            shopInfoGoldData.gold.isGold == 1,
            shopInfoTopContentData.topContent.topUrl,
            shopPageHomeTypeData.shopHomeType,
            shopInfoShopCoreShopAssetsData.shopCore.name,
            shopInfoShopCoreShopAssetsData.shopAssets.avatar,
            shopInfoShopCoreShopAssetsData.shopCore.domain,
            feedWhitelistData.isWhitelist,
            feedWhitelistData.url
    )

}