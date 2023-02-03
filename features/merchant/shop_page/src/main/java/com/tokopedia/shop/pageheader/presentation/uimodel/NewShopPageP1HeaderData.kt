package com.tokopedia.shop.pageheader.presentation.uimodel

import com.tokopedia.shop.common.data.model.ShopPageGetDynamicTabResponse
import com.tokopedia.shop.pageheader.presentation.uimodel.widget.ShopHeaderWidgetUiModel

data class NewShopPageP1HeaderData(
        val isOfficial: Boolean = false,
        val isGoldMerchant: Boolean = false,
        val pmTier: Int = 0,
        val shopHomeType: String = "",
        val shopName: String = "",
        val shopAvatar: String = "",
        val shopDomain: String = "",
        val isWhitelist: Boolean = false,
        val url: String = "",
        val listShopHeaderWidget: List<ShopHeaderWidgetUiModel> = listOf(),
        val listDynamicTabData: List<ShopPageGetDynamicTabResponse.ShopPageGetDynamicTab.TabData> = listOf()
)
