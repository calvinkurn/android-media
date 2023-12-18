package com.tokopedia.tokopedianow.test.stub

import com.tokopedia.user.session.UserSessionInterface

open class UserSessionStub: UserSessionInterface {
    override fun getAccessToken(): String = ""

    override fun getTokenType(): String = ""

    override fun getFreshToken(): String = ""

    override fun getUserId(): String = ""
    
    override fun isLoggedIn(): Boolean = false

    override fun getShopId(): String = ""

    override fun getName(): String = ""

    override fun getProfilePicture() = ""

    override fun getTemporaryUserId() = ""

    override fun getDeviceId() = ""

    override fun getTempEmail() = ""

    override fun getTempPhoneNumber(): String = ""

    override fun isMsisdnVerified(): Boolean = false

    override fun hasShownSaldoWithdrawalWarning(): Boolean = false

    override fun getPhoneNumber(): String = ""

    override fun getEmail(): String = ""

    override fun getRefreshTokenIV(): String = ""

    override fun isFirstTimeUser(): Boolean = false

    override fun isGoldMerchant(): Boolean = false

    override fun getShopName(): String = ""

    override fun hasShop(): Boolean = false

    override fun hasPassword(): Boolean = false

    override fun getGCToken(): String = ""

    override fun getShopAvatar(): String = ""

    override fun getShopAvatarOriginal(): String = ""

    override fun isPowerMerchantIdle(): Boolean = false

    override fun getAutofillUserData(): String = ""

    override fun getTwitterAccessToken(): String? = null

    override fun getTwitterAccessTokenSecret(): String? = null

    override fun getTwitterShouldPost(): Boolean = false

    override fun getLoginMethod(): String = ""

    override fun setUUID(uuid: String?) {
    }

    override fun setIsLogin(isLogin: Boolean) {
    }

    override fun setUserId(userId: String?) {
    }

    override fun setName(fullName: String?) {
    }

    override fun setEmail(email: String?) {
    }

    override fun setPhoneNumber(phoneNumber: String?) {
    }

    override fun setShopId(shopId: String?) {
    }

    override fun setShopName(shopName: String?) {
    }

    override fun setIsGoldMerchant(isGoldMerchant: Boolean) {
    }

    override fun setTempLoginName(fullName: String?) {
    }

    override fun setTempUserId(userId: String?) {
    }

    override fun setIsAffiliateStatus(isAffiliate: Boolean) {
    }

    override fun setTempPhoneNumber(userPhone: String?) {
    }

    override fun setTempLoginEmail(email: String?) {
    }

    override fun setToken(accessToken: String?, tokenType: String?) {
    }

    override fun setToken(accessToken: String?, tokenType: String?, refreshToken: String?) {
    }

    override fun clearToken() {
    }

    override fun logoutSession() {
    }

    override fun setFirstTimeUserOnboarding(isFirstTime: Boolean) {
    }

    override fun setFirstTimeUser(isFirstTime: Boolean) {
    }

    override fun setRefreshToken(refreshToken: String?) {
    }

    override fun setLoginSession(
        isLogin: Boolean,
        userId: String?,
        fullName: String?,
        shopId: String?,
        isMsisdnVerified: Boolean,
        shopName: String?,
        email: String?,
        shopIsGold: Boolean,
        phoneNumber: String?
    ) {
    }

    override fun setIsMSISDNVerified(isMsisdnVerified: Boolean) {
        
    }

    override fun setHasPassword(hasPassword: Boolean) {
        
    }

    override fun setProfilePicture(profilePicture: String?) {
        
    }

    override fun setSaldoWithdrawalWaring(value: Boolean) {
        
    }

    override fun setSaldoIntroPageStatus(value: Boolean) {
        
    }

    override fun setGCToken(gcToken: String?) {
        
    }

    override fun setShopAvatar(shopAvatar: String?) {
        
    }

    override fun setShopAvatarOriginal(shopAvatarOriginal: String?) {
        
    }

    override fun setIsPowerMerchantIdle(powerMerchantIdle: Boolean) {
        
    }

    override fun setTwitterAccessTokenAndSecret(accessToken: String, accessTokenSecret: String) {
        
    }

    override fun setTwitterShouldPost(shouldPost: Boolean) {
        
    }

    override fun setAutofillUserData(autofillUserData: String?) {
        
    }

    override fun setLoginMethod(loginMethod: String) {
        
    }

    override fun setIsShopOfficialStore(isShopOfficialStore: Boolean) {
        
    }

    override fun isShopOfficialStore(): Boolean {
        return false
    }

    override fun setDeviceId(deviceId: String?) {
        
    }

    override fun setFcmTimestamp() {
        
    }

    override fun getFcmTimestamp(): Long {
        return 0L
    }

    override fun getGTMLoginID(): String {
        return ""
    }

    override fun getAndroidId(): String {
        return ""
    }

    override fun isAffiliate(): Boolean {
        return false
    }

    override fun hasShownSaldoIntroScreen(): Boolean {
        return false
    }

    override fun isShopOwner(): Boolean {
        return false
    }

    override fun setIsShopOwner(isShopOwner: Boolean) {
        
    }

    override fun isShopAdmin(): Boolean {
        return false
    }

    override fun setIsShopAdmin(isShopAdmin: Boolean) {
        
    }

    override fun isLocationAdmin(): Boolean {
        return false
    }

    override fun setIsLocationAdmin(isLocationAdmin: Boolean) {
        
    }

    override fun isMultiLocationShop(): Boolean {
        return false
    }

    override fun setIsMultiLocationShop(isMultiLocationShop: Boolean) {

    }
}
