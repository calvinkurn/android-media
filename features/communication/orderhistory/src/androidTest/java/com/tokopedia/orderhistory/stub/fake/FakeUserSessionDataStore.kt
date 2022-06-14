package com.tokopedia.orderhistory.stub.fake

import android.content.Context
import com.tokopedia.user.session.datastore.UserData
import com.tokopedia.user.session.datastore.UserSessionDataStore
import kotlinx.coroutines.flow.Flow

/**
 * Fake class for UserSessionDataStore
 * Used in FakeAppModule for FakeBaseAppComponent
 */
class FakeUserSessionDataStore: UserSessionDataStore {

    override suspend fun setUUID(uuid: String) {
        TODO("Not yet implemented")
    }

    override suspend fun setIsLogin(isLogin: Boolean) {
        TODO("Not yet implemented")
    }

    override suspend fun setUserId(userId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun setName(fullName: String) {
        TODO("Not yet implemented")
    }

    override suspend fun setEmail(email: String) {
        TODO("Not yet implemented")
    }

    override suspend fun setPhoneNumber(phoneNumber: String) {
        TODO("Not yet implemented")
    }

    override suspend fun setShopId(shopId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun setShopName(shopName: String) {
        TODO("Not yet implemented")
    }

    override suspend fun setIsGoldMerchant(isGoldMerchant: Boolean) {
        TODO("Not yet implemented")
    }

    override suspend fun setTempLoginName(fullName: String) {
        TODO("Not yet implemented")
    }

    override suspend fun setTempUserId(userId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun setIsAffiliateStatus(isAffiliate: Boolean) {
        TODO("Not yet implemented")
    }

    override suspend fun setTempPhoneNumber(userPhone: String) {
        TODO("Not yet implemented")
    }

    override suspend fun setTempLoginEmail(email: String) {
        TODO("Not yet implemented")
    }

    override suspend fun setToken(accessToken: String, tokenType: String) {
        TODO("Not yet implemented")
    }

    override suspend fun setToken(accessToken: String, tokenType: String, refreshToken: String) {
        TODO("Not yet implemented")
    }

    override suspend fun setTokenType(tokenType: String) {
        TODO("Not yet implemented")
    }

    override suspend fun setAccessToken(accessToken: String) {
        TODO("Not yet implemented")
    }

    override suspend fun clearToken() {
        TODO("Not yet implemented")
    }

    override suspend fun logoutSession() {
        TODO("Not yet implemented")
    }

    override suspend fun clearDataStore() {
        TODO("Not yet implemented")
    }

    override suspend fun setFirstTimeUserOnboarding(isFirstTime: Boolean) {
        TODO("Not yet implemented")
    }

    override suspend fun setFirstTimeUser(isFirstTime: Boolean) {
        TODO("Not yet implemented")
    }

    override suspend fun setRefreshToken(refreshToken: String) {
        TODO("Not yet implemented")
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
        TODO("Not yet implemented")
    }

    override suspend fun setIsMSISDNVerified(isMsisdnVerified: Boolean) {
        TODO("Not yet implemented")
    }

    override suspend fun setHasPassword(hasPassword: Boolean) {
        TODO("Not yet implemented")
    }

    override suspend fun setProfilePicture(profilePicture: String) {
        TODO("Not yet implemented")
    }

    override suspend fun setSaldoWithdrawalWaring(value: Boolean) {
        TODO("Not yet implemented")
    }

    override suspend fun setSaldoIntroPageStatus(value: Boolean) {
        TODO("Not yet implemented")
    }

    override suspend fun setGCToken(gcToken: String) {
        TODO("Not yet implemented")
    }

    override suspend fun setShopAvatar(shopAvatar: String) {
        TODO("Not yet implemented")
    }

    override suspend fun setIsPowerMerchantIdle(powerMerchantIdle: Boolean) {
        TODO("Not yet implemented")
    }

    override suspend fun setTwitterAccessTokenAndSecret(
        accessToken: String,
        accessTokenSecret: String
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun setTwitterAccessToken(accessToken: String) {
        TODO("Not yet implemented")
    }

    override suspend fun setTwitterSecret(secret: String) {
        TODO("Not yet implemented")
    }

    override suspend fun setTwitterShouldPost(shouldPost: Boolean) {
        TODO("Not yet implemented")
    }

    override suspend fun setAutofillUserData(autofillUserData: String) {
        TODO("Not yet implemented")
    }

    override suspend fun setLoginMethod(loginMethod: String) {
        TODO("Not yet implemented")
    }

    override suspend fun setIsShopOfficialStore(isShopOfficialStore: Boolean) {
        TODO("Not yet implemented")
    }

    override suspend fun setDeviceId(deviceId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun setFcmTimestamp(timestamp: String) {
        TODO("Not yet implemented")
    }

    override fun getUserSession(): Flow<UserData> {
        TODO("Not yet implemented")
    }

    override fun getAccessToken(): Flow<String> {
        TODO("Not yet implemented")
    }

    override fun getTokenType(): Flow<String> {
        TODO("Not yet implemented")
    }

    override fun getRefreshToken(): Flow<String> {
        TODO("Not yet implemented")
    }

    override fun getUserId(): Flow<String> {
        TODO("Not yet implemented")
    }

    override fun isLoggedIn(): Flow<Boolean> {
        TODO("Not yet implemented")
    }

    override fun getShopId(): Flow<String> {
        TODO("Not yet implemented")
    }

    override fun getName(): Flow<String> {
        TODO("Not yet implemented")
    }

    override fun getProfilePicture(): Flow<String> {
        TODO("Not yet implemented")
    }

    override fun getTemporaryUserId(): Flow<String> {
        TODO("Not yet implemented")
    }

    override fun getDeviceId(): Flow<String> {
        TODO("Not yet implemented")
    }

    override fun getTempEmail(): Flow<String> {
        TODO("Not yet implemented")
    }

    override fun getTempPhoneNumber(): Flow<String> {
        TODO("Not yet implemented")
    }

    override fun isMsisdnVerified(): Flow<Boolean> {
        TODO("Not yet implemented")
    }

    override fun getPhoneNumber(): Flow<String> {
        TODO("Not yet implemented")
    }

    override fun getEmail(): Flow<String> {
        TODO("Not yet implemented")
    }

    override fun isFirstTimeUser(): Flow<Boolean> {
        TODO("Not yet implemented")
    }

    override fun isGoldMerchant(): Flow<Boolean> {
        TODO("Not yet implemented")
    }

    override fun getShopName(): Flow<String> {
        TODO("Not yet implemented")
    }

    override fun hasShop(): Flow<Boolean> {
        TODO("Not yet implemented")
    }

    override fun hasPassword(): Flow<Boolean> {
        TODO("Not yet implemented")
    }

    override fun getGCToken(): Flow<String> {
        TODO("Not yet implemented")
    }

    override fun getShopAvatar(): Flow<String> {
        TODO("Not yet implemented")
    }

    override fun isPowerMerchantIdle(): Flow<Boolean> {
        TODO("Not yet implemented")
    }

    override fun getAutofillUserData(): Flow<String> {
        TODO("Not yet implemented")
    }

    override fun getTwitterAccessToken(): Flow<String> {
        TODO("Not yet implemented")
    }

    override fun getTwitterAccessTokenSecret(): Flow<String> {
        TODO("Not yet implemented")
    }

    override fun getTwitterShouldPost(): Flow<Boolean> {
        TODO("Not yet implemented")
    }

    override fun getLoginMethod(): Flow<String> {
        TODO("Not yet implemented")
    }

    override fun isShopOfficialStore(): Flow<Boolean> {
        TODO("Not yet implemented")
    }

    override fun hasShownSaldoWithdrawalWarning(): Flow<Boolean> {
        TODO("Not yet implemented")
    }

    override fun getFcmTimestamp(): Flow<Long> {
        TODO("Not yet implemented")
    }

    override fun getGTMLoginID(): Flow<String> {
        TODO("Not yet implemented")
    }

    override fun isAffiliate(): Flow<Boolean> {
        TODO("Not yet implemented")
    }

    override fun hasShownSaldoIntroScreen(): Flow<Boolean> {
        TODO("Not yet implemented")
    }

    override fun isShopOwner(): Flow<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun setIsShopOwner(isShopOwner: Boolean) {
        TODO("Not yet implemented")
    }

    override fun isShopAdmin(): Flow<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun setIsShopAdmin(isShopAdmin: Boolean) {
        TODO("Not yet implemented")
    }

    override fun isLocationAdmin(): Flow<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun setIsLocationAdmin(isLocationAdmin: Boolean) {
        TODO("Not yet implemented")
    }

    override fun isMultiLocationShop(): Flow<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun setIsMultiLocationShop(isMultiLocationShop: Boolean) {
        TODO("Not yet implemented")
    }

    override suspend fun getAndroidId(context: Context): Flow<String> {
        TODO("Not yet implemented")
    }

    override suspend fun setAndroidId(androidId: String) {
        TODO("Not yet implemented")
    }
}