package com.tokopedia.sellerhome.view.model

/**
 * Created By @ilhamsuaib on 2020-03-05
 */

data class ShopInfoUiModel(
        val dateShopCreated: String = "",
        val shopAvatar: String = "",
        val shopCover: String = "",
        val shopDomain: String = "",
        val shopId: String = "",
        val shopLocation: String = "",
        val shopName: String = "",
        val shopScore: Int = 0,
        val totalActiveProduct: Int = 0
)