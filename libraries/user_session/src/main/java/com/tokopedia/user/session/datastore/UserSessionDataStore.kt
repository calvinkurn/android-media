package com.tokopedia.user.session.datastore

import kotlinx.coroutines.flow.Flow

interface UserSessionDataStore {
    
    companion object {
	const val LOGIN_METHOD_EMAIL = "email"
	const val LOGIN_METHOD_GOOGLE = "google"
	const val LOGIN_METHOD_PHONE = "phone"
	const val LOGIN_METHOD_EMAIL_SMART_LOCK = "email_smartlock"
    }

    fun getAccessToken(): Flow<String>

    fun getTokenType(): Flow<String>

    fun getRefreshToken(): Flow<String>

    fun getUserId(): Flow<String>

    fun isLoggedIn(): Flow<Boolean>

    fun getShopId(): Flow<String>

    fun getName(): Flow<String>

    fun getProfilePicture(): Flow<String>

    fun getTemporaryUserId(): Flow<String>

    fun getDeviceId(): Flow<String>

    fun getTempEmail(): Flow<String>

    fun getTempPhoneNumber(): Flow<String>

    suspend fun isMsisdnVerified(): Flow<Boolean>

    suspend fun hasShownSaldoWithdrawalWarning(): Flow<Boolean>

    fun getPhoneNumber(): Flow<String>

    fun getEmail(): Flow<String>

    fun getRefreshTokenIV(): Flow<String>

    suspend fun isFirstTimeUser(): Flow<Boolean>

    suspend fun isGoldMerchant(): Flow<Boolean>

    fun getShopName(): Flow<String>

    suspend fun hasShop(): Flow<Boolean>

    suspend fun hasPassword(): Flow<Boolean>

    fun getGCToken(): Flow<String>

    fun getShopAvatar(): Flow<String>

    suspend fun isPowerMerchantIdle(): Flow<Boolean>

    fun getAutofillUserData(): Flow<String>

    fun getTwitterAccessToken(): Flow<String>

    fun getTwitterAccessTokenSecret(): Flow<String>

    fun getTwitterShouldPost(): Flow<Boolean>

    /**
     * @return method name from this class
     */
    fun getLoginMethod(): Flow<String>

    /**
     * SETTER METHOD
     */
    suspend fun setUUID(uuid: String)

    suspend fun setIsLogin(isLogin: Boolean)

    suspend fun setUserId(userId: String)

    suspend fun setName(fullName: String)

    suspend fun setEmail(email: String)

    suspend fun setPhoneNumber(phoneNumber: String)

    suspend fun setShopId(shopId: String)

    suspend fun setShopName(shopName: String)

    suspend fun setIsGoldMerchant(isGoldMerchant: Boolean)

    suspend fun setTempLoginName(fullName: String)

    suspend fun setTempUserId(userId: String)

    suspend fun setIsAffiliateStatus(isAffiliate: Boolean)

    suspend fun setTempPhoneNumber(userPhone: String)

    suspend fun setTempLoginEmail(email: String)

    suspend fun setToken(accessToken: String, tokenType: String)

    suspend fun setTokenType(tokenType: String)

    suspend fun setAccessToken(accessToken: String)

    suspend fun clearToken()

    suspend fun logoutSession()

    suspend fun setFirstTimeUserOnboarding(isFirstTime: Boolean)

    suspend fun setFirstTimeUser(isFirstTime: Boolean)

    suspend fun setToken(accessToken: String, tokenType: String, refreshToken: String)

    suspend fun setRefreshToken(refreshToken: String)

    suspend fun setLoginSession(
        isLogin: Boolean,
        userId: String,
        fullName: String,
        shopId: String,
        isMsisdnVerified: Boolean,
        shopName: String,
        email: String,
        shopIsGold: Boolean,
        phoneNumber: String
    )

    suspend fun setIsMSISDNVerified(isMsisdnVerified: Boolean)

    suspend fun setHasPassword(hasPassword: Boolean)

    suspend fun setProfilePicture(profilePicture: String)

    suspend fun setSaldoWithdrawalWaring(value: Boolean)

    suspend fun setSaldoIntroPageStatus(value: Boolean)

    suspend fun setGCToken(gcToken: String)

    suspend fun setShopAvatar(shopAvatar: String)

    suspend fun setIsPowerMerchantIdle(powerMerchantIdle: Boolean)

    suspend fun setTwitterAccessTokenAndSecret(accessToken: String, accessTokenSecret: String)

    suspend fun setTwitterAccessToken(accessToken: String)

    suspend fun setTwitterSecret(secret: String)

    suspend fun setTwitterShouldPost(shouldPost: Boolean)

    suspend fun setAutofillUserData(autofillUserData: String)

    suspend fun setLoginMethod(loginMethod: String)

    suspend fun setIsShopOfficialStore(isShopOfficialStore: Boolean)

    suspend fun isShopOfficialStore(): Flow<Boolean>

    suspend fun setDeviceId(deviceId: String)

    suspend fun setFcmTimestamp()

    fun getFcmTimestamp(): Flow<Long>

    fun getGTMLoginID(): Flow<String>

    fun getAndroidId(): Flow<String>

    fun getAdsId(): Flow<String>

    suspend fun isAffiliate(): Flow<Boolean>

    suspend fun hasShownSaldoIntroScreen(): Flow<Boolean>

    suspend fun isShopOwner(): Flow<Boolean>

    suspend fun setIsShopOwner(isShopOwner: Boolean)

    suspend fun isShopAdmin(): Flow<Boolean>

    suspend fun setIsShopAdmin(isShopAdmin: Boolean)

    suspend fun isLocationAdmin(): Flow<Boolean>

    suspend fun setIsLocationAdmin(isLocationAdmin: Boolean)

    suspend fun isMultiLocationShop(): Flow<Boolean>

    suspend fun setIsMultiLocationShop(isMultiLocationShop: Boolean)

}