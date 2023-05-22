package com.tokopedia.utils

import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.datastore.UserSessionDataStore
import kotlinx.coroutines.flow.first

data class SampleUserModel(
    val isLogin: Boolean = true,
    val userId: String = "fakeId",
    val name: String = "Foo Name",
    val token: String = "fooToken",
    val refreshToken: String = "barToken",
    val shopId: String = "1234",
    val shopName: String = "Kelontong",
    val email: String = "kelontong@gmail.com",
    val isMsisdnVerified: Boolean = true,
    val isGoldMerchant: Boolean = true,
    val phone: String = "0812345678",
    val isShopOwner: Boolean = false
)

internal fun UserSession.setSample(model: SampleUserModel) {
    setLoginSession(
        model.isLogin, model.userId, model.name, model.shopId, model.isMsisdnVerified,
        model.shopName, model.email, model.isGoldMerchant, model.phone
    )
    setToken(model.token, "Bearer", model.refreshToken)
    deviceId = "d3v1c3"
    androidId
    setIsShopOwner(model.isShopOwner)
}

internal suspend fun UserSessionDataStore.getSampleUser(): SampleUserModel {
    return SampleUserModel(
        isLoggedIn().first(),
        getUserId().first(),
        getName().first(),
        getAccessToken().first(),
        getRefreshToken().first(),
        getShopId().first(),
        getShopName().first(),
        getEmail().first(),
        isMsisdnVerified().first(),
        isGoldMerchant().first(),
        getPhoneNumber().first(),
        isShopOwner().first()
    )
}
