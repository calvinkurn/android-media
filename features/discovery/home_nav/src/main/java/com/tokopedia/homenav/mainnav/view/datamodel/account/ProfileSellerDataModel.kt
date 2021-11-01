package com.tokopedia.homenav.mainnav.view.datamodel.account

data class ProfileSellerDataModel(
    var hasShop: Boolean = false,
    var shopName: String = "",
    var shopId: String = "",
    var shopOrderCount: Int = 0,
    var shopNotifCount: String = "",
    var shopApplink: String = "",
    var adminRoleText: String? = null,
    var canGoToSellerAccount: Boolean = true,
    var shopAvatar: String = "",

    /**
     * Status
     */
    var isGetShopLoading: Boolean = false,
    var isGetShopError: Boolean = false
)
