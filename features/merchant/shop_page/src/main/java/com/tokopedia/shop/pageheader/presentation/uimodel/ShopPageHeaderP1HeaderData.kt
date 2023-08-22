package com.tokopedia.shop.pageheader.presentation.uimodel

import com.tokopedia.shop.common.data.model.ShopPageGetDynamicTabResponse
import com.tokopedia.shop.pageheader.presentation.uimodel.widget.ShopPageHeaderWidgetUiModel

data class ShopPageHeaderP1HeaderData(
    val isOfficial: Boolean = false,
    val isGoldMerchant: Boolean = false,
    val pmTier: Int = 0,
    val shopHomeType: String = "",
    val shopName: String = "",
    val shopAvatar: String = "",
    val shopDomain: String = "",
    val isWhitelist: Boolean = false,
    val feedUrl: String = "",
    val listShopPageHeaderWidget: List<ShopPageHeaderWidgetUiModel> = listOf(),
    val listDynamicTabData: List<ShopPageGetDynamicTabResponse.ShopPageGetDynamicTab.TabData> = listOf(),
    val shopHeaderLayoutData: ShopPageHeaderLayoutUiModel = ShopPageHeaderLayoutUiModel(),
)
