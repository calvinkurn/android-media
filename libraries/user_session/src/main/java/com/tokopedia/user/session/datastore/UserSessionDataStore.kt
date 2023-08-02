package com.tokopedia.user.session.datastore

import android.content.Context
import kotlinx.coroutines.flow.Flow

interface UserSessionDataStore {
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

    suspend fun setTempUserId(userId: String)

    suspend fun setIsAffiliateStatus(isAffiliate: Boolean)

    suspend fun setToken(accessToken: String, tokenType: String)

    suspend fun setTokenType(tokenType: String)

    suspend fun setAccessToken(accessToken: String)

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

    suspend fun setGCToken(gcToken: String)

    suspend fun setShopAvatar(shopAvatar: String)

    suspend fun setIsPowerMerchantIdle(powerMerchantIdle: Boolean)

    suspend fun setAutofillUserData(autofillUserData: String)

    suspend fun setLoginMethod(loginMethod: String)

    suspend fun setIsShopOfficialStore(isShopOfficialStore: Boolean)

    suspend fun setIsShopOwner(isShopOwner: Boolean)

    suspend fun setIsShopAdmin(isShopAdmin: Boolean)

    suspend fun setIsLocationAdmin(isLocationAdmin: Boolean)

    suspend fun setIsMultiLocationShop(isMultiLocationShop: Boolean)

    suspend fun setAndroidId(androidId: String)

    suspend fun setTwitterAccessToken(token: String)

    suspend fun setTwitterAccessTokenSecret(token: String)

    suspend fun setTwitterShouldPost(shouldPost: Boolean)

    suspend fun clearToken()

    suspend fun logoutSession()

    suspend fun clearAllData()

    suspend fun clearDataStore()

    // Getter
    fun getUserSession(): Flow<UserData>

    fun getAccessToken(): Flow<String>

    fun getTokenType(): Flow<String>

    fun getRefreshToken(): Flow<String>

    fun getUserId(): Flow<String>

    fun isLoggedIn(): Flow<Boolean>

    fun getShopId(): Flow<String>

    fun getName(): Flow<String>

    fun getProfilePicture(): Flow<String>

    fun getTemporaryUserId(): Flow<String>

    fun isMsisdnVerified(): Flow<Boolean>

    fun getPhoneNumber(): Flow<String>

    fun getEmail(): Flow<String>

    fun isGoldMerchant(): Flow<Boolean>

    fun getShopName(): Flow<String>

    fun hasShop(): Flow<Boolean>

    fun hasPassword(): Flow<Boolean>

    fun getGCToken(): Flow<String>

    fun getShopAvatar(): Flow<String>

    fun isPowerMerchantIdle(): Flow<Boolean>

    fun getAutofillUserData(): Flow<String>

    // considering to take out these twitter data

    fun getTwitterAccessToken(): Flow<String>

    fun getTwitterAccessTokenSecret(): Flow<String>

    fun getTwitterShouldPost(): Flow<Boolean>

    fun getLoginMethod(): Flow<String>

    fun isShopOfficialStore(): Flow<Boolean>

    fun getGTMLoginID(): Flow<String>

    fun isAffiliate(): Flow<Boolean>

    fun isShopOwner(): Flow<Boolean>

    fun isShopAdmin(): Flow<Boolean>

    fun isLocationAdmin(): Flow<Boolean>

    fun isMultiLocationShop(): Flow<Boolean>

    fun getAndroidId(context: Context): Flow<String>
}
