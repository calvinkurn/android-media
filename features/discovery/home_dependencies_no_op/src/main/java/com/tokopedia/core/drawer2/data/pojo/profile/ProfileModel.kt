package com.tokopedia.core.drawer2.data.pojo.profile

class ProfileModel {

    fun getProfileData(): ProfileData {
        return ProfileData()
    }
}

class ProfileData {

    fun getShopInfo(): ShopInfo {
        return ShopInfo()
    }
}

class ShopInfo {

    fun getShopDomain(): String {
        return ""
    }
}