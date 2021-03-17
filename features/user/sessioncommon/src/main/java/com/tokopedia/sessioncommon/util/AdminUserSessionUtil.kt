package com.tokopedia.sessioncommon.util

import com.tokopedia.sessioncommon.data.admin.AdminDataResponse
import com.tokopedia.sessioncommon.data.profile.ShopData
import com.tokopedia.user.session.UserSessionInterface

object AdminUserSessionUtil {

    fun UserSessionInterface.refreshUserSessionShopData(shopData: ShopData) {
        val levelGold = 1
        val levelOfficialStore = 2

        shopId = shopData.shopId
        shopName = shopData.shopName
        shopAvatar = shopData.shopAvatar
        setIsGoldMerchant(shopData.shopLevel == levelGold  ||  shopData.shopLevel == levelOfficialStore)
        setIsShopOfficialStore(shopData.shopLevel == levelOfficialStore)
    }

    fun UserSessionInterface.refreshUserSessionAdminData(adminDataResponse: AdminDataResponse) {
        adminDataResponse.data.detail.roleType.let {
            setIsShopOwner(it.isShopOwner)
            setIsLocationAdmin(it.isLocationAdmin)
            setIsShopAdmin(it.isShopAdmin)
            setIsMultiLocationShop(adminDataResponse.isMultiLocationShop)
            if (it.isLocationAdmin) {
                removeUnnecessaryShopData()
            }
        }
    }

    private fun UserSessionInterface.removeUnnecessaryShopData() {
        shopAvatar = ""
        shopId = "0"
        shopName = ""
        setIsGoldMerchant(false)
        setIsShopOfficialStore(false)
    }

}