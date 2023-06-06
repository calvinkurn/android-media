package com.tokopedia.test.application.datastore

import android.content.Context
import com.tokopedia.user.session.datastore.UserData
import com.tokopedia.user.session.datastore.UserSessionDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TestUserSessionDataStore : UserSessionDataStore {
    override suspend fun setUUID(uuid: String) {
        // no-op
    }

    override suspend fun setIsLogin(isLogin: Boolean) {
        // no-op
    }

    override suspend fun setUserId(userId: String) {
        // no-op
    }

    override suspend fun setName(fullName: String) {
        // no-op
    }

    override suspend fun setEmail(email: String) {
        // no-op
    }

    override suspend fun setPhoneNumber(phoneNumber: String) {
        // no-op
    }

    override suspend fun setShopId(shopId: String) {
        // no-op
    }

    override suspend fun clearAllData() {
        // no-op
    }

    override suspend fun setShopName(shopName: String) {
        // no-op
    }

    override suspend fun setIsGoldMerchant(isGoldMerchant: Boolean) {
        // no-op
    }

    override suspend fun setTempUserId(userId: String) {
        // no-op
    }

    override suspend fun setIsAffiliateStatus(isAffiliate: Boolean) {
        // no-op
    }

    override suspend fun setToken(accessToken: String, tokenType: String) {
        // no-op
    }

    override suspend fun setToken(accessToken: String, tokenType: String, refreshToken: String) {
        // no-op
    }

    override suspend fun setTokenType(tokenType: String) {
        // no-op
    }

    override suspend fun setAccessToken(accessToken: String) {
        // no-op
    }

    override suspend fun clearToken() {
        // no-op
    }

    override suspend fun logoutSession() {
        // no-op
    }

    override suspend fun clearDataStore() {
        // no-op
    }

    override suspend fun setRefreshToken(refreshToken: String) {
        // no-op
    }

    override suspend fun setLoginSession(isLogin: Boolean, userId: String, fullName: String, shopId: String, isMsisdnVerified: Boolean, shopName: String, email: String, shopIsGold: Boolean, phoneNumber: String) {
        // no-op
    }

    override suspend fun setIsMSISDNVerified(isMsisdnVerified: Boolean) {
        // no-op
    }

    override suspend fun setHasPassword(hasPassword: Boolean) {
        // no-op
    }

    override suspend fun setProfilePicture(profilePicture: String) {
        // no-op
    }

    override suspend fun setGCToken(gcToken: String) {
        // no-op
    }

    override suspend fun setShopAvatar(shopAvatar: String) {
        // no-op
    }

    override suspend fun setIsPowerMerchantIdle(powerMerchantIdle: Boolean) {
        // no-op
    }

    override suspend fun setAutofillUserData(autofillUserData: String) {
        // no-op
    }

    override suspend fun setLoginMethod(loginMethod: String) {
        // no-op
    }

    override suspend fun setIsShopOfficialStore(isShopOfficialStore: Boolean) {
        // no-op
    }

    override fun getUserSession(): Flow<UserData> {
        return flow { }
    }

    override fun getAccessToken(): Flow<String> {
        return flow { }
    }

    override fun getTokenType(): Flow<String> {
        return flow { }
    }

    override fun getRefreshToken(): Flow<String> {
        return flow { }
    }

    override fun getUserId(): Flow<String> {
        return flow { }
    }

    override fun isLoggedIn(): Flow<Boolean> {
        return flow { }
    }

    override fun getShopId(): Flow<String> {
        return flow { }
    }

    override fun getName(): Flow<String> {
        return flow { }
    }

    override fun getProfilePicture(): Flow<String> {
        return flow { }
    }

    override fun getTemporaryUserId(): Flow<String> {
        return flow { }
    }

    override fun isMsisdnVerified(): Flow<Boolean> {
        return flow { }
    }

    override fun getPhoneNumber(): Flow<String> {
        return flow { }
    }

    override fun getEmail(): Flow<String> {
        return flow { }
    }

    override fun isGoldMerchant(): Flow<Boolean> {
        return flow { }
    }

    override fun getShopName(): Flow<String> {
        return flow { }
    }

    override fun hasShop(): Flow<Boolean> {
        return flow { }
    }

    override fun hasPassword(): Flow<Boolean> {
        return flow { }
    }

    override fun getGCToken(): Flow<String> {
        return flow { }
    }

    override fun getShopAvatar(): Flow<String> {
        return flow { }
    }

    override fun isPowerMerchantIdle(): Flow<Boolean> {
        return flow { }
    }

    override fun getAutofillUserData(): Flow<String> {
        return flow { }
    }

    override fun getTwitterAccessToken(): Flow<String> {
        return flow { }
    }

    override fun getTwitterAccessTokenSecret(): Flow<String> {
        return flow { }
    }

    override fun getTwitterShouldPost(): Flow<Boolean> {
        return flow { }
    }

    override fun getLoginMethod(): Flow<String> {
        return flow { }
    }

    override fun isShopOfficialStore(): Flow<Boolean> {
        return flow { }
    }

    override fun getGTMLoginID(): Flow<String> {
        return flow { }
    }

    override fun isAffiliate(): Flow<Boolean> {
        return flow { }
    }

    override fun isShopOwner(): Flow<Boolean> {
        return flow { }
    }

    override suspend fun setIsShopOwner(isShopOwner: Boolean) {
        // no-op
    }

    override fun isShopAdmin(): Flow<Boolean> {
        return flow { }
    }

    override suspend fun setIsShopAdmin(isShopAdmin: Boolean) {
        // no-op
    }

    override fun isLocationAdmin(): Flow<Boolean> {
        return flow { }
    }

    override suspend fun setIsLocationAdmin(isLocationAdmin: Boolean) {
        // no-op
    }

    override fun isMultiLocationShop(): Flow<Boolean> {
        return flow { }
    }

    override suspend fun setIsMultiLocationShop(isMultiLocationShop: Boolean) {
        // no-op
    }

    override fun getAndroidId(context: Context): Flow<String> {
        return flow { }
    }

    override suspend fun setAndroidId(androidId: String) {
        // no-op
    }

    override suspend fun setTwitterAccessToken(token: String) {
    }

    override suspend fun setTwitterAccessTokenSecret(token: String) {
    }

    override suspend fun setTwitterShouldPost(shouldPost: Boolean) {
    }
}
