package com.tokopedia.user.session.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.tokopedia.user.session.UserSessionProto
import kotlinx.coroutines.flow.first

class UserSessionDataStoreImpl(val userSessionStore: DataStore<UserSessionProto>): UserSessionDataStore {

    private val USER_PREFERENCES_NAME = "user_session"
    private val DATA_STORE_FILE_NAME = "user_session.pb"
    private val SORT_ORDER_KEY = "sort_order"

    private val Context.userSessionStore: DataStore<UserSessionProto> by dataStore(
	fileName = DATA_STORE_FILE_NAME,
	serializer = UserSessionSerializer
    )

    suspend fun saveAccessToken(accessToken: String) {
	userSessionStore.updateData { preferences ->
	    val pref = preferences.toBuilder()
	    pref.accessToken = accessToken
	    pref.build()
	}
    }

    suspend fun saveRefreshToken(refreshToken: String) {
	userSessionStore.updateData { preferences ->
	    val pref = preferences.toBuilder()
	    pref.refreshToken = refreshToken
	    pref.build()
	}
    }

    suspend fun storeData(userData: UserData) {
	userSessionStore.updateData { preferences ->
	    val pref = preferences.toBuilder()
	    pref.accessToken = userData.accessToken
	    pref.name = userData.name
	    pref.phoneNumber = userData.phoneNumber
	    pref.refreshToken = userData.refreshToken
	    pref.email = userData.email
	    pref.build()
	}
    }

    suspend fun getUserSession(): UserData {
	val proto = userSessionStore.data.first()
	return UserData(
	    name = proto.name,
	    email = proto.email,
	    phoneNumber = proto.phoneNumber,
	    accessToken = proto.accessToken,
	    refreshToken = proto.refreshToken
	)
    }

    override fun getAccessToken(): String {
	TODO("Not yet implemented")
    }

    override fun getTokenType(): String {
	TODO("Not yet implemented")
    }

    override fun getRefreshToken(): String {
	TODO("Not yet implemented")
    }

    override fun getUserId(): String {
	TODO("Not yet implemented")
    }

    override fun isLoggedIn(): Boolean {
	TODO("Not yet implemented")
    }

    override fun getShopId(): String {
	TODO("Not yet implemented")
    }

    override fun getName(): String {
	TODO("Not yet implemented")
    }

    override fun getProfilePicture(): String {
	TODO("Not yet implemented")
    }

    override fun getTemporaryUserId(): String {
	TODO("Not yet implemented")
    }

    override fun getDeviceId(): String {
	TODO("Not yet implemented")
    }

    override fun getTempEmail(): String {
	TODO("Not yet implemented")
    }

    override fun getTempPhoneNumber(): String {
	TODO("Not yet implemented")
    }

    override fun isMsisdnVerified(): Boolean {
	TODO("Not yet implemented")
    }

    override fun hasShownSaldoWithdrawalWarning(): Boolean {
	TODO("Not yet implemented")
    }

    override fun getPhoneNumber(): String {
	TODO("Not yet implemented")
    }

    override fun getEmail(): String {
	TODO("Not yet implemented")
    }

    override fun getRefreshTokenIV(): String {
	TODO("Not yet implemented")
    }

    override fun isFirstTimeUser(): Boolean {
	TODO("Not yet implemented")
    }

    override fun isGoldMerchant(): Boolean {
	TODO("Not yet implemented")
    }

    override fun getShopName(): String {
	TODO("Not yet implemented")
    }

    override fun hasShop(): Boolean {
	TODO("Not yet implemented")
    }

    override fun hasPassword(): Boolean {
	TODO("Not yet implemented")
    }

    override fun getGCToken(): String {
	TODO("Not yet implemented")
    }

    override fun getShopAvatar(): String {
	TODO("Not yet implemented")
    }

    override fun isPowerMerchantIdle(): Boolean {
	TODO("Not yet implemented")
    }

    override fun getAutofillUserData(): String {
	TODO("Not yet implemented")
    }

    override fun getTwitterAccessToken(): String {
	TODO("Not yet implemented")
    }

    override fun getTwitterAccessTokenSecret(): String {
	TODO("Not yet implemented")
    }

    override fun getTwitterShouldPost(): Boolean {
	TODO("Not yet implemented")
    }

    override fun getLoginMethod(): String {
	TODO("Not yet implemented")
    }

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

    override fun clearToken() {
	TODO("Not yet implemented")
    }

    override fun logoutSession() {
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

    override fun isShopOfficialStore(): Boolean {
	TODO("Not yet implemented")
    }

    override suspend fun setDeviceId(deviceId: String) {
	TODO("Not yet implemented")
    }

    override suspend fun setFcmTimestamp() {
	TODO("Not yet implemented")
    }

    override fun getFcmTimestamp(): Long {
	TODO("Not yet implemented")
    }

    override fun getGTMLoginID(): String {
	TODO("Not yet implemented")
    }

    override fun getAndroidId(): String {
	TODO("Not yet implemented")
    }

    override fun getAdsId(): String {
	TODO("Not yet implemented")
    }

    override fun isAffiliate(): Boolean {
	TODO("Not yet implemented")
    }

    override fun hasShownSaldoIntroScreen(): Boolean {
	TODO("Not yet implemented")
    }

    override fun isShopOwner(): Boolean {
	TODO("Not yet implemented")
    }

    override suspend fun setIsShopOwner(isShopOwner: Boolean) {
	TODO("Not yet implemented")
    }

    override fun isShopAdmin(): Boolean {
	TODO("Not yet implemented")
    }

    override suspend fun setIsShopAdmin(isShopAdmin: Boolean) {
	TODO("Not yet implemented")
    }

    override fun isLocationAdmin(): Boolean {
	TODO("Not yet implemented")
    }

    override suspend fun setIsLocationAdmin(isLocationAdmin: Boolean) {
	TODO("Not yet implemented")
    }

    override fun isMultiLocationShop(): Boolean {
	TODO("Not yet implemented")
    }

    override suspend fun setIsMultiLocationShop(isMultiLocationShop: Boolean) {
	TODO("Not yet implemented")
    }
}