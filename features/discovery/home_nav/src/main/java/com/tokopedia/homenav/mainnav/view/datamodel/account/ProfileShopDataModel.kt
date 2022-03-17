package com.tokopedia.homenav.mainnav.view.datamodel.account

/**
 * Created by dhaba
 */
data class ProfileShopDataModel (
    var profileSellerDataModel: ProfileSellerDataModel = ProfileSellerDataModel()
    ) {
    fun setUserShopName(
        shopName: String = "",
        shopId: String = "",
        shopOrderCount: Int,
        isError: Boolean = false,
        isLoading: Boolean = false
    ) {
        this.profileSellerDataModel.hasShop = shopId != AccountHeaderDataModel.DEFAULT_SHOP_ID_NOT_OPEN
        this.profileSellerDataModel.isGetShopError = isError
        this.profileSellerDataModel.isGetShopLoading = isLoading
        if (profileSellerDataModel.hasShop) {
            this.profileSellerDataModel.shopName = shopName
            this.profileSellerDataModel.shopId = shopId
            this.profileSellerDataModel.shopOrderCount = shopOrderCount
        } else {
            this.profileSellerDataModel.shopName =
                AccountHeaderDataModel.DEFAULT_SHOP_ID_NOT_OPEN_TEXT
            this.profileSellerDataModel.shopId = AccountHeaderDataModel.DEFAULT_SHOP_ID_NOT_OPEN
            this.profileSellerDataModel.shopOrderCount = 0
        }

    }
}