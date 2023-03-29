package com.tokopedia.home_account.stub.domain

import android.content.Context
import com.tokopedia.user.session.datastore.UserData
import com.tokopedia.user.session.datastore.UserSessionDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeUserSessionDataStore: UserSessionDataStore {
    override suspend fun setUUID(uuid: String) {}

    override suspend fun setIsLogin(isLogin: Boolean) {}

    override suspend fun setUserId(userId: String) {}

    override suspend fun setName(fullName: String) {}

    override suspend fun setEmail(email: String) {
        
    }

    override suspend fun setPhoneNumber(phoneNumber: String) {
        
    }

    override suspend fun setShopId(shopId: String) {
        
    }

    override suspend fun setShopName(shopName: String) {
        
    }

    override suspend fun setIsGoldMerchant(isGoldMerchant: Boolean) {
        
    }

    override suspend fun setTempLoginName(fullName: String) {
        
    }

    override suspend fun setTempUserId(userId: String) {
        
    }

    override suspend fun setIsAffiliateStatus(isAffiliate: Boolean) {
        
    }

    override suspend fun setTempPhoneNumber(userPhone: String) {
        
    }

    override suspend fun setTempLoginEmail(email: String) {
        
    }

    override suspend fun setToken(accessToken: String, tokenType: String) {
        
    }

    override suspend fun setToken(accessToken: String, tokenType: String, refreshToken: String) {
        
    }

    override suspend fun setTokenType(tokenType: String) {
        
    }

    override suspend fun setAccessToken(accessToken: String) {
        
    }

    override suspend fun clearToken() {
        
    }

    override suspend fun logoutSession() {
        
    }

    override suspend fun clearDataStore() {
        
    }

    override suspend fun setFirstTimeUserOnboarding(isFirstTime: Boolean) {
        
    }

    override suspend fun setFirstTimeUser(isFirstTime: Boolean) {
        
    }

    override suspend fun setRefreshToken(refreshToken: String) {
        
    }

    override suspend fun setLoginSession(
        isLogin: Boolean,
        userId: String,
        fullName: String,
        shopId: String,
        isMsisdnVerified: Boolean,
        shopName: String,
        email: String,
        shopIsGold: Boolean,
        phoneNumber: String
    ) {
        
    }

    override suspend fun setIsMSISDNVerified(isMsisdnVerified: Boolean) {
        
    }

    override suspend fun setHasPassword(hasPassword: Boolean) {
        
    }

    override suspend fun setProfilePicture(profilePicture: String) {
        
    }

    override suspend fun setSaldoWithdrawalWaring(value: Boolean) {
        
    }

    override suspend fun setSaldoIntroPageStatus(value: Boolean) {
        
    }

    override suspend fun setGCToken(gcToken: String) {
        
    }

    override suspend fun setShopAvatar(shopAvatar: String) {
        
    }

    override suspend fun setIsPowerMerchantIdle(powerMerchantIdle: Boolean) {
        
    }

    override suspend fun setTwitterAccessTokenAndSecret(
        accessToken: String,
        accessTokenSecret: String
    ) {
        
    }

    override suspend fun setTwitterAccessToken(accessToken: String) {
        
    }

    override suspend fun setTwitterSecret(secret: String) {
        
    }

    override suspend fun setTwitterShouldPost(shouldPost: Boolean) {
        
    }

    override suspend fun setAutofillUserData(autofillUserData: String) {
        
    }

    override suspend fun setLoginMethod(loginMethod: String) {
        
    }

    override suspend fun setIsShopOfficialStore(isShopOfficialStore: Boolean) {
        
    }

    override suspend fun setDeviceId(deviceId: String) {
        
    }

    override suspend fun setFcmTimestamp(timestamp: String) {
        
    }

    override fun getUserSession(): Flow<UserData> {
        return flow{}
    }

    override fun getAccessToken(): Flow<String> { return flow{} }

    override fun getTokenType(): Flow<String> {
        return flow{}
    }

    override fun getRefreshToken(): Flow<String> {
        return flow{}
    }

    override fun getUserId(): Flow<String> {
        return flow{}
    }

    override fun isLoggedIn(): Flow<Boolean> {
        return flow { emit(true) }
    }

    override fun getShopId(): Flow<String> {
        return flow{}
    }

    override fun getName(): Flow<String> {
        return flow{}
    }

    override fun getProfilePicture(): Flow<String> {
        return flow{}
    }

    override fun getTemporaryUserId(): Flow<String> {
        return flow{}
    }

    override fun getDeviceId(): Flow<String> {
        return flow{}
    }

    override fun getTempEmail(): Flow<String> {
        return flow{}
    }

    override fun getTempPhoneNumber(): Flow<String> {
        return flow{}
    }

    override fun isMsisdnVerified(): Flow<Boolean> {
        return flow{}
    }

    override fun getPhoneNumber(): Flow<String> {
        return flow { emit("08123123123") }
    }

    override fun getEmail(): Flow<String> {
        return flow { emit("yoris.prayogo@tokopedia.com") }
    }

    override fun isFirstTimeUser(): Flow<Boolean> {
        return flow{}
    }

    override fun isGoldMerchant(): Flow<Boolean> {
        return flow{}
    }

    override fun getShopName(): Flow<String> {
        return flow{}
    }

    override fun hasShop(): Flow<Boolean> {
        return flow{}
    }

    override fun hasPassword(): Flow<Boolean> {
        return flow{}
    }

    override fun getGCToken(): Flow<String> {
        return flow{}
    }

    override fun getShopAvatar(): Flow<String> {
        return flow{}
    }

    override fun isPowerMerchantIdle(): Flow<Boolean> {
        return flow{}
    }

    override fun getAutofillUserData(): Flow<String> {
        return flow{}
    }

    override fun getTwitterAccessToken(): Flow<String> {
        return flow{}
    }

    override fun getTwitterAccessTokenSecret(): Flow<String> {
        return flow{}
    }

    override fun getTwitterShouldPost(): Flow<Boolean> {
        return flow{}
    }

    override fun getLoginMethod(): Flow<String> {
        return flow{}
    }

    override fun isShopOfficialStore(): Flow<Boolean> {
        return flow{}
    }

    override fun hasShownSaldoWithdrawalWarning(): Flow<Boolean> {
        return flow{}
    }

    override fun getFcmTimestamp(): Flow<Long> {
        return flow{}
    }

    override fun getGTMLoginID(): Flow<String> {
        return flow{}
    }

    override fun isAffiliate(): Flow<Boolean> {
        return flow{}
    }

    override fun hasShownSaldoIntroScreen(): Flow<Boolean> {
        return flow{}
    }

    override fun isShopOwner(): Flow<Boolean> {
        return flow{}
    }

    override suspend fun setIsShopOwner(isShopOwner: Boolean) {

    }

    override fun isShopAdmin(): Flow<Boolean> {
        return flow{}
    }

    override suspend fun setIsShopAdmin(isShopAdmin: Boolean) {
        
    }

    override fun isLocationAdmin(): Flow<Boolean> {
        return flow{}
    }

    override suspend fun setIsLocationAdmin(isLocationAdmin: Boolean) {
        
    }

    override fun isMultiLocationShop(): Flow<Boolean> {
        return flow{}
    }

    override suspend fun setIsMultiLocationShop(isMultiLocationShop: Boolean) {
        
    }

    override suspend fun getAndroidId(context: Context): Flow<String> {
        return flow{}
    }

    override suspend fun setAndroidId(androidId: String) {
        
    }
}