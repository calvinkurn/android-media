package com.tokopedia.user.session.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.tokopedia.user.session.UserSessionProto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class UserSessionDataStoreImpl (val context: Context):
    UserSessionDataStore {
    
    private val Context.userSessionStore: DataStore<UserSessionProto> by dataStore(
        fileName = DATA_STORE_FILE_NAME,
        serializer = UserSessionSerializer()
    )

    fun getUserSessionFlow(): Flow<UserSessionProto> {
        return context.userSessionStore.data
    }

    suspend fun getUserSessionSuspend(): UserData {
        val proto = context.userSessionStore.data.first()
        return UserData(
            name = proto.name,
            email = proto.email,
            phoneNumber = proto.phoneNumber,
            accessToken = proto.accessToken,
            refreshToken = proto.refreshToken
        )
    }

    private suspend fun userSessionSetter(func: UserSessionProto.Builder.() -> Unit) {
        context.userSessionStore.updateData { preferences ->
            preferences.toBuilder()
                .also { func(it) }
                .build()
        }
    }

    override fun getAccessToken(): Flow<String> {
        return getUserSessionFlow().map { it.accessToken }
    }

    override fun getTokenType(): Flow<String> {
        return getUserSessionFlow().map { it.tokenType }
    }

    override fun getRefreshToken(): Flow<String> {
        return getUserSessionFlow().map { it.refreshToken }
    }

    override fun getUserId(): Flow<String> {
        return getUserSessionFlow().map { it.userId }
    }

    override fun isLoggedIn(): Flow<Boolean> {
        return getUserSessionFlow().map { it.isLoggedIn }
    }

    override fun getShopId(): Flow<String> {
        return getUserSessionFlow().map { it.shopId }
    }

    override fun getName(): Flow<String> {
        return getUserSessionFlow().map { it.name }
    }

    override fun getProfilePicture(): Flow<String> {
        return getUserSessionFlow().map { it.profilePicture }
    }

    override fun getTemporaryUserId(): Flow<String> {
        return getUserSessionFlow().map { it.tempUserId }
    }

    override fun getDeviceId(): Flow<String> {
        return getUserSessionFlow().map { it.deviceId }
    }

    override fun getTempEmail(): Flow<String> {
        return getUserSessionFlow().map { it.tempLoginEmail }
    }

    override fun getTempPhoneNumber(): Flow<String> {
        return getUserSessionFlow().map { it.tempPhoneNumber }
    }

    override suspend fun isMsisdnVerified(): Flow<Boolean> {
        return getUserSessionFlow().map { it.isMSISDNVerified }
    }

    override suspend fun hasShownSaldoWithdrawalWarning(): Flow<Boolean> {
        return getUserSessionFlow().map { it.saldoWithdrawalWaring }
    }

    override fun getPhoneNumber(): Flow<String> {
        return getUserSessionFlow().map { it.phoneNumber }
    }

    override fun getEmail(): Flow<String> {
        return getUserSessionFlow().map { it.email }
    }

    override fun getRefreshTokenIV(): Flow<String> {
        TODO("Refresh Token IV")
    }

    override suspend fun isFirstTimeUser(): Flow<Boolean> {
        return getUserSessionFlow().map { it.firstTimeUser }
    }

    override suspend fun isGoldMerchant(): Flow<Boolean> {
        return getUserSessionFlow().map { it.isGoldMerchant }
    }

    override fun getShopName(): Flow<String> {
        return getUserSessionFlow().map { it.shopName }
    }

    override suspend fun hasShop(): Flow<Boolean> {
        return getUserSessionFlow().map {
            it.shopId.isNotEmpty() && it.shopId != "0"
        }
    }

    override suspend fun hasPassword(): Flow<Boolean> {
        return getUserSessionFlow().map { it.hasPassword }
    }

    override fun getGCToken(): Flow<String> {
        return getUserSessionFlow().map { it.gcToken }
    }

    override fun getShopAvatar(): Flow<String> {
        return getUserSessionFlow().map { it.shopAvatar }
    }

    override suspend fun isPowerMerchantIdle(): Flow<Boolean> {
        return getUserSessionFlow().map { it.isPowerMerchantIdle }
    }

    override fun getAutofillUserData(): Flow<String> {
        return getUserSessionFlow().map { it.autofillUserData }
    }

    override fun getTwitterAccessToken(): Flow<String> {
        TODO("Twitter access token")
    }

    override fun getTwitterAccessTokenSecret(): Flow<String> {
        return getUserSessionFlow().map { it.twitterAccessTokenSecret }
    }

    override fun getTwitterShouldPost(): Flow<Boolean> {
        return getUserSessionFlow().map { it.twitterShouldPost }
    }

    override fun getLoginMethod(): Flow<String> {
        return getUserSessionFlow().map { it.loginMethod }
    }

    override suspend fun setUUID(uuid: String) {
        userSessionSetter {
            setUuid(uuid)
        }
    }

    override suspend fun setIsLogin(isLogin: Boolean) {
        userSessionSetter {
            isLoggedIn = isLogin
        }
    }

    override suspend fun setUserId(userId: String) {
        userSessionSetter {
            setUserId(userId)
        }
    }

    override suspend fun setName(fullName: String) {
        userSessionSetter {
            name = fullName
        }
    }

    override suspend fun setEmail(email: String) {
        userSessionSetter {
            setEmail(email)
        }
    }

    override suspend fun setPhoneNumber(phoneNumber: String) {
        userSessionSetter {
            setPhoneNumber(phoneNumber)
        }
    }

    override suspend fun setShopId(shopId: String) {
        userSessionSetter {
            setShopId(shopId)
        }
    }

    override suspend fun setShopName(shopName: String) {
        userSessionSetter {
            setShopName(shopName)
        }
    }

    override suspend fun setIsGoldMerchant(isGoldMerchant: Boolean) {
        userSessionSetter {
            setIsGoldMerchant(isGoldMerchant)
        }
    }

    override suspend fun setTempLoginName(fullName: String) {
        userSessionSetter {
            tempLoginName = fullName
        }
    }

    override suspend fun setTempUserId(userId: String) {
        userSessionSetter {
            tempUserId = userId
        }
    }

    override suspend fun setIsAffiliateStatus(isAffiliate: Boolean) {
        userSessionSetter {
            isAffiliateStatus = isAffiliate
        }
    }

    override suspend fun setTempPhoneNumber(userPhone: String) {
        userSessionSetter {
            tempPhoneNumber = userPhone
        }
    }

    override suspend fun setTempLoginEmail(email: String) {
        userSessionSetter {
            tempLoginEmail = email
        }
    }

    override suspend fun setToken(accessToken: String, tokenType: String) {
        userSessionSetter {
            setAccessToken(accessToken)
            setTokenType(tokenType)
        }
    }

    override suspend fun setTokenType(tokenType: String) {
        userSessionSetter {
            setTokenType(tokenType)
        }
    }

    override suspend fun setAccessToken(accessToken: String) {
        userSessionSetter {
            setAccessToken(accessToken)
        }
    }

    override suspend fun setToken(accessToken: String, tokenType: String, refreshToken: String) {
        userSessionSetter {
            setAccessToken(accessToken)
            setTokenType(tokenType)
            setRefreshToken(refreshToken)
        }
    }

    override suspend fun clearToken() {
        userSessionSetter {
            clearAccessToken()
            clearTokenType()
            clearRefreshToken()
        }
    }

    override suspend fun logoutSession() {
        TODO("Not yet implemented")
    }

    override suspend fun setFirstTimeUserOnboarding(isFirstTime: Boolean) {
        userSessionSetter {
            firstTimeUserOnboarding = isFirstTime
        }
    }

    override suspend fun setFirstTimeUser(isFirstTime: Boolean) {
        userSessionSetter {
            firstTimeUser = isFirstTime
        }
    }

    override suspend fun setRefreshToken(refreshToken: String) {
        userSessionSetter {
            setRefreshToken(refreshToken)
        }
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
        userSessionSetter {
            isLoggedIn = isLogin
            setUserId(userId)
            name = fullName
            setShopId(shopId)
            isMSISDNVerified = isMsisdnVerified
            setShopName(shopName)
            setEmail(email)
            isGoldMerchant = shopIsGold
            setPhoneNumber(phoneNumber)
        }
    }

    override suspend fun setIsMSISDNVerified(isMsisdnVerified: Boolean) {
        userSessionSetter {
            isMSISDNVerified = isMsisdnVerified
        }
    }

    override suspend fun setHasPassword(hasPassword: Boolean) {
        userSessionSetter {
            setHasPassword(hasPassword)
        }
    }

    override suspend fun setProfilePicture(profilePicture: String) {
        userSessionSetter {
            setProfilePicture(profilePicture)
        }
    }

    override suspend fun setSaldoWithdrawalWaring(value: Boolean) {
        userSessionSetter {
            saldoWithdrawalWaring = value
        }
    }

    override suspend fun setSaldoIntroPageStatus(value: Boolean) {
        userSessionSetter {
            saldoIntroPageStatus = value
        }
    }

    override suspend fun setGCToken(gcToken: String) {
        userSessionSetter {
            setGcToken(gcToken)
        }
    }

    override suspend fun setShopAvatar(shopAvatar: String) {
        userSessionSetter {
            setShopAvatar(shopAvatar)
        }
    }

    override suspend fun setIsPowerMerchantIdle(powerMerchantIdle: Boolean) {
        userSessionSetter {
            isPowerMerchantIdle = powerMerchantIdle
        }
    }

    override suspend fun setTwitterAccessTokenAndSecret(
        accessToken: String,
        accessTokenSecret: String
    ) {
        setTwitterAccessToken(accessToken)
        setTwitterAccessToken(accessTokenSecret)
    }

    override suspend fun setTwitterAccessToken(accessToken: String) {
        userSessionSetter {
            twitterAccessToken = accessToken
        }
    }

    override suspend fun setTwitterSecret(secret: String) {
        userSessionSetter {
            twitterAccessTokenSecret = secret
        }
    }

    override suspend fun setTwitterShouldPost(shouldPost: Boolean) {
        userSessionSetter {
            twitterShouldPost = shouldPost
        }
    }

    override suspend fun setAutofillUserData(autofillUserData: String) {
        userSessionSetter {
            setAutofillUserData(autofillUserData)
        }
    }

    override suspend fun setLoginMethod(loginMethod: String) {
        userSessionSetter {
            setLoginMethod(loginMethod)
        }
    }

    override suspend fun setIsShopOfficialStore(isShopOfficialStore: Boolean) {
        userSessionSetter {
            setIsShopOfficialStore(isShopOfficialStore)
        }
    }

    override suspend fun isShopOfficialStore(): Flow<Boolean> {
         TODO()
    }

    override suspend fun setDeviceId(deviceId: String) {
        userSessionSetter {
            setDeviceId(deviceId)
        }
    }

    override suspend fun setFcmTimestamp() {
        userSessionSetter {
            fcmTimestamp = System.currentTimeMillis().toString()
        }
    }

    override fun getFcmTimestamp(): Flow<Long> {
        return getUserSessionFlow().map { it.fcmTimestamp.toLong() }
    }

    override fun getGTMLoginID(): Flow<String> {
        return getUserSessionFlow().map { it.userId }
    }

    override fun getAndroidId(): Flow<String> {
        TODO("android id")
    }

    override fun getAdsId(): Flow<String> {
        TODO("ads Id")
    }

    override suspend fun isAffiliate(): Flow<Boolean> {
        return getUserSessionFlow().map { it.isAffiliateStatus }
    }

    override suspend fun hasShownSaldoIntroScreen(): Flow<Boolean> {
        return getUserSessionFlow().map { it.saldoIntroPageStatus }
    }

    override suspend fun isShopOwner(): Flow<Boolean> {
        return getUserSessionFlow().map { it.isShopOwner }
    }

    override suspend fun setIsShopOwner(isShopOwner: Boolean) {
        userSessionSetter {
            TODO("shop owner")
//            setIsShopOwner(isShopOwner)
        }
    }

    override suspend fun isShopAdmin(): Flow<Boolean> {
        return getUserSessionFlow().map { it.isShopAdmin }
    }

    override suspend fun setIsShopAdmin(isShopAdmin: Boolean) {
        userSessionSetter {
            setIsShopAdmin(isShopAdmin)
        }
    }

    override suspend fun isLocationAdmin(): Flow<Boolean> {
        return getUserSessionFlow().map { it.isLocationAdmin }
    }

    override suspend fun setIsLocationAdmin(isLocationAdmin: Boolean) {
        userSessionSetter {
            setIsLocationAdmin(isLocationAdmin)
        }
    }

    override suspend fun isMultiLocationShop(): Flow<Boolean> {
        return getUserSessionFlow().map { it.isMultiLocationShop }
    }

    override suspend fun setIsMultiLocationShop(isMultiLocationShop: Boolean) {
        userSessionSetter {
            setIsMultiLocationShop(isMultiLocationShop)
        }
    }
    
    companion object {
        const val USER_PREFERENCES_NAME = "user_session"
        const val DATA_STORE_FILE_NAME = "user_session.pb"
        const val SORT_ORDER_KEY = "sort_order"
    }
}