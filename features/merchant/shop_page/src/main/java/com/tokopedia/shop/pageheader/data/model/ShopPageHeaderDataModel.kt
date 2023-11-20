package com.tokopedia.shop.pageheader.data.model

import com.tokopedia.shop.common.data.model.ShopPageGetDynamicTabResponse
import com.tokopedia.shop.common.graphql.data.shopinfo.Broadcaster
import com.tokopedia.shop.common.view.model.ShopSharingInShowCaseUiModel

data class ShopPageHeaderDataModel(
    var shopId: String = "",
    var shopName: String = "",
    var isOfficial: Boolean = false,
    var isGoldMerchant: Boolean = false,
    var pmTier: Int = 0,
    var shopHomeType: String = "",
    var topContentUrl: String = "",
    var avatar: String = "",
    var domain: String = "",
    var location: String = "",
    var isFreeOngkir: Boolean = false,
    var statusTitle: String = "",
    var statusMessage: String = "",
    var shopStatus: Int = -1,
    var broadcaster: Broadcaster.Config = Broadcaster.Config(),
    var shopSnippetUrl: String = "",
    var shopBranchLinkDomain: String = "",
    var shopCoreUrl: String = "",
    var shopBadge: String = "",
    var description: String = "",
    var tagline: String = "",
    var listDynamicTabData: List<ShopPageGetDynamicTabResponse.ShopPageGetDynamicTab.TabData> = listOf(),
    var isEnableDirectPurchase: Boolean = false
) {
    companion object {
        fun ShopPageHeaderDataModel.mapperForShopShowCase() = ShopSharingInShowCaseUiModel(
            shopId,
            shopName,
            avatar,
            location,
            isOfficial,
            isGoldMerchant,
            shopStatus,
            shopCoreUrl,
            shopBadge,
            tagline,
            shopSnippetUrl
        )
    }
}
