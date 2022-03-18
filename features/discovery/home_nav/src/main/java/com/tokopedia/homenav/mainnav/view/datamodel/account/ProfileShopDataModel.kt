package com.tokopedia.homenav.mainnav.view.datamodel.account

/**
 * Created by dhaba
 */
data class ProfileShopDataModel (
    var isAffiliate: Boolean = false,
    var hasShop: Boolean = false,
    var shopName: String = "",
    var shopId: String = "",
    var shopOrderCount: Int = 0,
    var shopNotifCount: String = "",
    var shopApplink: String = "",
    var adminRoleText: String? = null,
    var canGoToSellerAccount: Boolean = true,

    /**
     * Status
     */
    var isGetShopLoading: Boolean = false,
    var isGetShopError: Boolean = false
)