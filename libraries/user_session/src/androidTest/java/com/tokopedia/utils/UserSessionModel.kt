package com.tokopedia.utils

import androidx.test.core.app.ApplicationProvider
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.datastore.UserSessionDataStore
import kotlinx.coroutines.flow.first

data class UserSessionModel(
    val isLogin: Boolean = true,
    val userId: String = "fakeId",
    val tempUserId: String = "tempFakeId",
    val name: String = "Foo Name",
    val accessToken: String = "fooToken",
    val tokenType: String = "bearer",
    val refreshToken: String = "barToken",
    val shopId: String = "1234",
    val shopName: String = "Kelontong",
    val email: String = "kelontong@gmail.com",
    val isMsisdnVerified: Boolean = true,
    val isGoldMerchant: Boolean = true,
    val phone: String = "0812345678",
    val isShopOwner: Boolean = false,
    val profilePicture: String = "pic.jpeg",
    val hasShop: Boolean = false,
    val hasPassword: Boolean = false,
    val gcToken: String = "gcToken",
    val shopAvatar: String = "avatar",
    val isPowerMerchantIdle: Boolean = false,
    val autofillUserData: String = "autofilldata",
    val twitterAccessToken: String = "twtToken",
    val twitterSecretToken: String = "twtSecret",
    val twitterShouldPost: Boolean = false,
    val loginMethod: String = "default",
    val isShopOS: Boolean = false,
    val gtmLoginId: String = "gtm_loginId",
    val isAffiliate: Boolean = false,
    val isShopAdmin: Boolean = false,
    val isLocationAdmin: Boolean = false,
    val isMultiLocationShop: Boolean = false,
    val androidId: String = "idAndroid"
)

internal fun UserSession.setModel(model: UserSessionModel) {
    setLoginSession(
        model.isLogin, model.userId, model.name, model.shopId, model.isMsisdnVerified,
        model.shopName, model.email, model.isGoldMerchant, model.phone
    )
    setToken(model.accessToken, model.tokenType, model.refreshToken)
    deviceId = "d3v1c3"
    androidId
    setIsShopOwner(model.isShopOwner)
    profilePicture = model.profilePicture
    setTempUserId(model.tempUserId)
    // setHasShop
    setHasPassword(model.hasPassword)
    gcToken = model.gcToken
    shopAvatar = model.shopAvatar
    setIsPowerMerchantIdle(model.isPowerMerchantIdle)
    autofillUserData = model.autofillUserData
    setTwitterAccessTokenAndSecret(model.twitterAccessToken, model.twitterSecretToken)
    twitterShouldPost = model.twitterShouldPost
    loginMethod = model.loginMethod
    setIsShopOfficialStore(model.isShopOS)
    // setgtmloginid
    setIsAffiliateStatus(model.isAffiliate)
    setIsShopAdmin(model.isShopAdmin)
    setIsLocationAdmin(model.isLocationAdmin)
    setIsMultiLocationShop(model.isMultiLocationShop)
}

internal suspend fun UserSessionDataStore.getUserModel(): UserSessionModel {
    return UserSessionModel(
        isLoggedIn().first(),
        getUserId().first(),
        getTemporaryUserId().first(),
        getName().first(),
        getAccessToken().first(),
        getTokenType().first(),
        getRefreshToken().first(),
        getShopId().first(),
        getShopName().first(),
        getEmail().first(),
        isMsisdnVerified().first(),
        isGoldMerchant().first(),
        getPhoneNumber().first(),
        isShopOwner().first(),
        getProfilePicture().first(),
        hasShop().first(),
        hasPassword().first(),
        getGCToken().first(),
        getShopAvatar().first(),
        isPowerMerchantIdle().first(),
        getAutofillUserData().first(),
        getTwitterAccessToken().first(),
        getTwitterAccessTokenSecret().first(),
        getTwitterShouldPost().first(),
        getLoginMethod().first(),
        isShopOfficialStore().first(),
        getGTMLoginID().first(),
        isAffiliate().first(),
        isShopAdmin().first(),
        isLocationAdmin().first(),
        isMultiLocationShop().first(),
        getAndroidId(ApplicationProvider.getApplicationContext()).first()
    )
}

internal fun UserSession.getUserModel(): UserSessionModel {
    return UserSessionModel(
        isLoggedIn,
        userId,
        temporaryUserId,
        name,
        accessToken,
        tokenType,
        freshToken,
        shopId,
        shopName,
        email,
        isMsisdnVerified,
        isGoldMerchant,
        phoneNumber,
        isShopOwner,
        profilePicture,
        hasShop(),
        hasPassword(),
        gcToken.orEmpty(),
        shopAvatar,
        isPowerMerchantIdle,
        autofillUserData,
        twitterAccessToken.orEmpty(),
        twitterAccessTokenSecret.orEmpty(),
        twitterShouldPost,
        loginMethod,
        isShopOfficialStore,
        gtmLoginID,
        isAffiliate,
        isShopAdmin,
        isLocationAdmin,
        isMultiLocationShop,
        androidId
    )
}
