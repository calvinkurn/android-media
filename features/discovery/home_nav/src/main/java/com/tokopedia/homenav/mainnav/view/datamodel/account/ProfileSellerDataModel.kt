package com.tokopedia.homenav.mainnav.view.datamodel.account

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.homenav.mainnav.view.adapter.typefactory.SellerTypeFactory

data class ProfileSellerDataModel  (
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
) : Visitable<SellerTypeFactory> {
    override fun type(typeFactory: SellerTypeFactory): Int {
        return typeFactory.type(this)
    }
}