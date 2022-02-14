package com.tokopedia.user.session.datastore

interface UserSessionDataStore {
    
    companion object {
	const val LOGIN_METHOD_EMAIL = "email"
	const val LOGIN_METHOD_GOOGLE = "google"
	const val LOGIN_METHOD_PHONE = "phone"
	const val LOGIN_METHOD_EMAIL_SMART_LOCK = "email_smartlock"
    }

    fun getAccessToken(): String

    fun getTokenType(): String

    fun getRefreshToken(): String

    fun getUserId(): String

    fun isLoggedIn(): Boolean

    fun getShopId(): String

    fun getName(): String

    fun getProfilePicture(): String

    fun getTemporaryUserId(): String

    fun getDeviceId(): String

    fun getTempEmail(): String

    fun getTempPhoneNumber(): String

    fun isMsisdnVerified(): Boolean

    fun hasShownSaldoWithdrawalWarning(): Boolean

    fun getPhoneNumber(): String

    fun getEmail(): String

    fun getRefreshTokenIV(): String

    fun isFirstTimeUser(): Boolean

    fun isGoldMerchant(): Boolean

    fun getShopName(): String

    fun hasShop(): Boolean

    fun hasPassword(): Boolean

    fun getGCToken(): String

    fun getShopAvatar(): String

    fun isPowerMerchantIdle(): Boolean

    fun getAutofillUserData(): String

    fun getTwitterAccessToken(): String

    fun getTwitterAccessTokenSecret(): String

    fun getTwitterShouldPost(): Boolean

    /**
     * @return method name from this class
     */
    fun getLoginMethod(): String

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

    fun clearToken()

    fun logoutSession()

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

    suspend fun setTwitterShouldPost(shouldPost: Boolean)

    suspend fun setAutofillUserData(autofillUserData: String)

    suspend fun setLoginMethod(loginMethod: String)

    suspend fun setIsShopOfficialStore(isShopOfficialStore: Boolean)

    fun isShopOfficialStore(): Boolean

    suspend fun setDeviceId(deviceId: String)

    suspend fun setFcmTimestamp()

    fun getFcmTimestamp(): Long

    fun getGTMLoginID(): String

    fun getAndroidId(): String

    fun getAdsId(): String

    fun isAffiliate(): Boolean

    fun hasShownSaldoIntroScreen(): Boolean

    fun isShopOwner(): Boolean

    suspend fun setIsShopOwner(isShopOwner: Boolean)

    fun isShopAdmin(): Boolean

    suspend fun setIsShopAdmin(isShopAdmin: Boolean)

    fun isLocationAdmin(): Boolean

    suspend fun setIsLocationAdmin(isLocationAdmin: Boolean)

    fun isMultiLocationShop(): Boolean

    suspend fun setIsMultiLocationShop(isMultiLocationShop: Boolean)

}