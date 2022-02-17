package com.tokopedia.user.session

import android.content.Context
import android.provider.Settings
import android.text.TextUtils
import java.security.MessageDigest
import javax.inject.Inject
import kotlin.experimental.and

/**
 * @author by milhamj on 04/04/18.
 * Please avoid using this class to get data. Use [UserSessionInterface] instead.
 */
open class UserSession @Inject constructor(context: Context?)
    : MigratedUserSession(context), UserSessionInterface {

    override fun getAccessToken(): String {
	return getAndTrimOldString(
	    Constants.LOGIN_SESSION,
	    Constants.ACCESS_TOKEN,
	    ""
	)!!.trim { it <= ' ' }
    }

    override fun getTokenType(): String {
	return getAndTrimOldString(
	    Constants.LOGIN_SESSION,
	    Constants.TOKEN_TYPE,
	    "Bearer"
	)!!.trim { it <= ' ' }
    }

    override fun getFreshToken(): String {
	return getAndTrimOldString(
	    Constants.LOGIN_SESSION,
	    Constants.REFRESH_TOKEN,
	    "Bearer"
	)!!.trim { it <= ' ' }
    }

    override fun getUserId(): String {
	return getAndTrimOldString(Constants.LOGIN_SESSION, Constants.LOGIN_ID, "")!!
    }

    override fun setUserId(userId: String) {
	setString(Constants.LOGIN_SESSION, Constants.LOGIN_ID, userId)
	setString(Constants.LOGIN_SESSION, Constants.GTM_LOGIN_ID, userId)
    }

    override fun getAdsId(): String {
	val adsId = getAndTrimOldString(Constants.ADVERTISINGID, Constants.KEY_ADVERTISINGID, "")
	return if (adsId != null && !"".equals(adsId.trim { it <= ' ' }, ignoreCase = true)) {
	    adsId
	} else {
	    ""
	}
    }

    override fun getAndroidId(): String {
	val androidId = getAndTrimOldString(Constants.ANDROID_ID, Constants.KEY_ANDROID_ID, "")
	return if (androidId != null && !"".equals(
		androidId.trim { it <= ' ' },
		ignoreCase = true
	    )
	) {
	    androidId
	} else {
	    val android_id = md5(
		Settings.Secure.getString(
		    context?.contentResolver,
		    Settings.Secure.ANDROID_ID
		)
	    )
	    if (!TextUtils.isEmpty(android_id)) {
		setString(Constants.ANDROID_ID, Constants.KEY_ANDROID_ID, android_id)
	    }
	    android_id
	}
    }

    fun md5(s: String): String {
	return try {
	    val digest = MessageDigest.getInstance("MD5")
	    digest.update(s.toByteArray())
	    val messageDigest = digest.digest()
	    val hexString = StringBuilder()
	    for (b in messageDigest) {
		hexString.append(String.format("%02x", b and(0xff).toByte()))
	    }
	    hexString.toString()
	} catch (e: Exception) {
	    e.printStackTrace()
	    ""
	}
    }

    override fun getGTMLoginID(): String {
	return getAndTrimOldString(Constants.LOGIN_SESSION, Constants.GTM_LOGIN_ID, "")!!
    }

    override fun isLoggedIn(): Boolean {
	val u_id = getAndTrimOldString(Constants.LOGIN_SESSION, Constants.LOGIN_ID, "")
	val isLogin = getAndTrimOldBoolean(Constants.LOGIN_SESSION, Constants.IS_LOGIN, false)
	return isLogin && u_id != null && !u_id.isEmpty()
    }

    override fun isShopOfficialStore(): Boolean {
	return getAndTrimOldBoolean(
	    Constants.LOGIN_SESSION,
	    Constants.IS_SHOP_OFFICIAL_STORE,
	    false
	)
    }

    override fun setIsShopOfficialStore(isShopOfficialStore: Boolean) {
	setBoolean(Constants.LOGIN_SESSION, Constants.IS_SHOP_OFFICIAL_STORE, isShopOfficialStore)
    }

    override fun getShopId(): String {
	var shopId =
	    getAndTrimOldString(Constants.LOGIN_SESSION, Constants.SHOP_ID, DEFAULT_EMPTY_SHOP_ID)
	if (DEFAULT_EMPTY_SHOP_ID_ON_PREF == shopId || TextUtils.isEmpty(shopId)) {
	    shopId = DEFAULT_EMPTY_SHOP_ID
	}
	return shopId!!
    }

    override fun setShopId(shopId: String) {
	setString(Constants.LOGIN_SESSION, Constants.SHOP_ID, shopId)
    }

    override fun getShopName(): String {
	return getAndTrimOldString(Constants.LOGIN_SESSION, Constants.SHOP_NAME, "")!!
    }

    override fun setShopName(shopName: String) {
	setString(Constants.LOGIN_SESSION, Constants.SHOP_NAME, shopName)
    }

    override fun hasShop(): Boolean {
	return !TextUtils.isEmpty(shopId) && DEFAULT_EMPTY_SHOP_ID != shopId
    }

    override fun getGCToken(): String? {
	return getAndTrimOldString(Constants.LOGIN_SESSION, Constants.GC_TOKEN, "")
    }

    override fun setGCToken(gcToken: String) {
	setString(Constants.LOGIN_SESSION, Constants.GC_TOKEN, gcToken)
    }

    override fun isGoldMerchant(): Boolean {
	return getAndTrimOldBoolean(Constants.LOGIN_SESSION, Constants.IS_GOLD_MERCHANT, false)
    }

    override fun getName(): String {
	return getAndTrimOldString(Constants.LOGIN_SESSION, Constants.FULL_NAME, "")!!
    }

    override fun setName(fullName: String) {
	setString(Constants.LOGIN_SESSION, Constants.FULL_NAME, fullName)
    }

    override fun getProfilePicture(): String {
	return getAndTrimOldString(Constants.LOGIN_SESSION, Constants.PROFILE_PICTURE, "")!!
    }

    override fun setProfilePicture(profilePicture: String) {
	setString(Constants.LOGIN_SESSION, Constants.PROFILE_PICTURE, profilePicture)
    }

    override fun getTemporaryUserId(): String {
	return getAndTrimOldString(Constants.LOGIN_SESSION, Constants.TEMP_USER_ID, "")!!
    }

    override fun setDeviceId(deviceId: String) {
	setString(Constants.GCM_STORAGE, Constants.GCM_ID, deviceId)
    }

    /**
     * Saved from FCMCacheManager
     *
     * @return gcm id / device id
     */
    override fun getDeviceId(): String {
	return getAndTrimOldString(Constants.GCM_STORAGE, Constants.GCM_ID, "")!!
    }

    override fun getTempEmail(): String {
	return getAndTrimOldString(Constants.LOGIN_SESSION, Constants.TEMP_EMAIL, "")!!
    }

    override fun getTempPhoneNumber(): String {
	return getAndTrimOldString(Constants.LOGIN_SESSION, Constants.TEMP_PHONE_NUMBER, "")!!
    }

    override fun setTempPhoneNumber(userPhone: String) {
	setString(Constants.LOGIN_SESSION, Constants.TEMP_PHONE_NUMBER, userPhone)
    }

    override fun isMsisdnVerified(): Boolean {
	return getAndTrimOldBoolean(Constants.LOGIN_SESSION, Constants.IS_MSISDN_VERIFIED, false)
    }

    override fun isAffiliate(): Boolean {
	return getAndTrimOldBoolean(Constants.LOGIN_SESSION, Constants.IS_AFFILIATE, false)
    }

    override fun hasShownSaldoWithdrawalWarning(): Boolean {
	return getAndTrimOldBoolean(
	    Constants.LOGIN_SESSION,
	    Constants.HAS_SHOWN_SALDO_WARNING,
	    false
	)
    }

    override fun hasShownSaldoIntroScreen(): Boolean {
	return getAndTrimOldBoolean(
	    Constants.LOGIN_SESSION,
	    Constants.HAS_SHOWN_SALDO_INTRO_PAGE,
	    false
	)
    }

    override fun isFirstTimeUser(): Boolean {
	return getAndTrimOldBoolean(
	    Constants.LOGIN_SESSION,
	    Constants.IS_FIRST_TIME_USER_NEW_ONBOARDING,
	    true
	)
    }

    override fun setFirstTimeUser(isFirstTime: Boolean) {
	setBoolean(Constants.LOGIN_SESSION, Constants.IS_FIRST_TIME_USER, isFirstTime)
    }

    override fun hasPassword(): Boolean {
	return getAndTrimOldBoolean(Constants.LOGIN_SESSION, Constants.HAS_PASSWORD, true)
    }

    override fun isPowerMerchantIdle(): Boolean {
	return getAndTrimOldBoolean(
	    Constants.LOGIN_SESSION,
	    Constants.IS_POWER_MERCHANT_IDLE,
	    false
	)
    }

    override fun getTwitterShouldPost(): Boolean {
	return getAndTrimOldBoolean(Constants.LOGIN_SESSION, Constants.TWITTER_SHOULD_POST, false)
    }

    override fun setTwitterShouldPost(shouldPost: Boolean) {
	setBoolean(Constants.LOGIN_SESSION, Constants.TWITTER_SHOULD_POST, shouldPost)
    }

    override fun setIsLogin(isLogin: Boolean) {
	setBoolean(Constants.LOGIN_SESSION, Constants.IS_LOGIN, isLogin)
    }

    override fun setIsGoldMerchant(isGoldMerchant: Boolean) {
	setBoolean(Constants.LOGIN_SESSION, Constants.IS_GOLD_MERCHANT, isGoldMerchant)
    }

    override fun setTempLoginName(fullName: String) {
	setString(Constants.LOGIN_SESSION, Constants.TEMP_NAME, fullName)
    }

    override fun setTempUserId(userId: String) {
	setString(Constants.LOGIN_SESSION, Constants.TEMP_USER_ID, userId)
    }

    override fun clearToken() {
	internalCleanKey(Constants.LOGIN_SESSION, Constants.TOKEN_TYPE)
	internalCleanKey(Constants.LOGIN_SESSION, Constants.ACCESS_TOKEN)
    }

    override fun setToken(accessToken: String?, tokenType: String?) {
	setString(Constants.LOGIN_SESSION, Constants.TOKEN_TYPE, tokenType)
	setString(Constants.LOGIN_SESSION, Constants.ACCESS_TOKEN, accessToken)
    }

    override fun setFirstTimeUserOnboarding(isFirstTime: Boolean) {
	setBoolean(
	    Constants.LOGIN_SESSION,
	    Constants.IS_FIRST_TIME_USER_NEW_ONBOARDING,
	    isFirstTime
	)
    }

    override fun setToken(accessToken: String, tokenType: String, refreshToken: String) {
	setString(Constants.LOGIN_SESSION, Constants.ACCESS_TOKEN, accessToken)
	setString(Constants.LOGIN_SESSION, Constants.TOKEN_TYPE, tokenType)
	setString(Constants.LOGIN_SESSION, Constants.REFRESH_TOKEN, refreshToken)
    }

    override fun setRefreshToken(refreshToken: String) {
	setString(Constants.LOGIN_SESSION, Constants.REFRESH_TOKEN, refreshToken)
    }

    override fun setIsMSISDNVerified(isMsisdnVerified: Boolean) {
	setBoolean(Constants.LOGIN_SESSION, Constants.IS_MSISDN_VERIFIED, isMsisdnVerified)
    }

    override fun setIsAffiliateStatus(isAffiliate: Boolean) {
	setBoolean(Constants.LOGIN_SESSION, Constants.IS_AFFILIATE, isAffiliate)
    }

    override fun setTempLoginEmail(email: String) {
	setString(Constants.LOGIN_SESSION, Constants.TEMP_EMAIL, email)
    }

    override fun setHasPassword(hasPassword: Boolean) {
	setBoolean(Constants.LOGIN_SESSION, Constants.HAS_PASSWORD, hasPassword)
    }

    override fun setSaldoWithdrawalWaring(value: Boolean) {
	setBoolean(Constants.LOGIN_SESSION, Constants.HAS_SHOWN_SALDO_WARNING, value)
    }

    override fun setSaldoIntroPageStatus(value: Boolean) {
	setBoolean(Constants.LOGIN_SESSION, Constants.HAS_SHOWN_SALDO_INTRO_PAGE, value)
    }

    override fun setIsPowerMerchantIdle(powerMerchantIdle: Boolean) {
	setBoolean(Constants.LOGIN_SESSION, Constants.IS_POWER_MERCHANT_IDLE, powerMerchantIdle)
    }

    override fun setTwitterAccessTokenAndSecret(accessToken: String, accessTokenSecret: String) {
	setString(Constants.LOGIN_SESSION, Constants.TWITTER_ACCESS_TOKEN, accessToken)
	setString(Constants.LOGIN_SESSION, Constants.TWITTER_ACCESS_TOKEN_SECRET, accessTokenSecret)
    }

    override fun getTwitterAccessToken(): String? {
	return getAndTrimOldString(Constants.LOGIN_SESSION, Constants.TWITTER_ACCESS_TOKEN, "")
    }

    override fun getPhoneNumber(): String {
	return getAndTrimOldString(Constants.LOGIN_SESSION, Constants.PHONE_NUMBER, "")!!
    }

    override fun setPhoneNumber(phoneNumber: String) {
	setString(Constants.LOGIN_SESSION, Constants.PHONE_NUMBER, phoneNumber)
    }

    override fun getEmail(): String {
	return getAndTrimOldString(Constants.LOGIN_SESSION, Constants.EMAIL, "")!!
    }

    override fun setEmail(email: String) {
	setString(Constants.LOGIN_SESSION, Constants.EMAIL, email)
    }

    override fun getShopAvatar(): String {
	return getAndTrimOldString(Constants.LOGIN_SESSION, Constants.SHOP_AVATAR, "")!!
    }

    override fun setShopAvatar(shopAvatar: String) {
	setString(Constants.LOGIN_SESSION, Constants.SHOP_AVATAR, shopAvatar)
    }

    override fun getAutofillUserData(): String {
	return getAndTrimOldString(Constants.LOGIN_SESSION, Constants.AUTOFILL_USER_DATA, "")!!
    }

    override fun setAutofillUserData(autofillUserData: String) {
	setString(Constants.LOGIN_SESSION, Constants.AUTOFILL_USER_DATA, autofillUserData)
    }

    override fun getLoginMethod(): String {
	return getAndTrimOldString(Constants.LOGIN_SESSION, Constants.LOGIN_METHOD, "")!!
    }

    override fun setLoginMethod(loginMethod: String) {
	setString(Constants.LOGIN_SESSION, Constants.LOGIN_METHOD, loginMethod)
    }

    override fun getTwitterAccessTokenSecret(): String? {
	return getAndTrimOldString(
	    Constants.LOGIN_SESSION,
	    Constants.TWITTER_ACCESS_TOKEN_SECRET,
	    ""
	)
    }

    override fun getRefreshTokenIV(): String {
	return getAndTrimOldString(Constants.LOGIN_SESSION, Constants.REFRESH_TOKEN_KEY, KEY_IV)!!
    }

    override fun getFcmTimestamp(): Long {
	return getLong(Constants.GCM_STORAGE, Constants.GCM_ID_TIMESTAMP, 0)
    }

    override fun setFcmTimestamp() {
	setLong(Constants.GCM_STORAGE, Constants.GCM_ID_TIMESTAMP, System.currentTimeMillis())
    }

    override fun isShopOwner(): Boolean {
	return getAndTrimOldBoolean(Constants.LOGIN_SESSION, Constants.IS_SHOP_OWNER, false)
    }

    override fun setIsShopOwner(isShopOwner: Boolean) {
	setBoolean(Constants.LOGIN_SESSION, Constants.IS_SHOP_OWNER, isShopOwner)
    }

    override fun isShopAdmin(): Boolean {
	return getAndTrimOldBoolean(Constants.LOGIN_SESSION, Constants.IS_SHOP_ADMIN, false)
    }

    override fun setIsShopAdmin(isShopAdmin: Boolean) {
	setBoolean(Constants.LOGIN_SESSION, Constants.IS_SHOP_ADMIN, isShopAdmin)
    }

    override fun isLocationAdmin(): Boolean {
	return getAndTrimOldBoolean(Constants.LOGIN_SESSION, Constants.IS_LOCATION_ADMIN, false)
    }

    override fun setIsLocationAdmin(isLocationAdmin: Boolean) {
	setBoolean(Constants.LOGIN_SESSION, Constants.IS_LOCATION_ADMIN, isLocationAdmin)
    }

    override fun isMultiLocationShop(): Boolean {
	return getAndTrimOldBoolean(
	    Constants.LOGIN_SESSION,
	    Constants.IS_MULTI_LOCATION_SHOP,
	    false
	)
    }

    override fun setIsMultiLocationShop(isMultiLocationShop: Boolean) {
	setBoolean(Constants.LOGIN_SESSION, Constants.IS_MULTI_LOCATION_SHOP, isMultiLocationShop)
    }

    override fun setLoginSession(
	isLogin: Boolean, userId: String, fullName: String,
	shopId: String, isMsisdnVerified: Boolean, shopName: String,
	email: String, isGoldMerchant: Boolean, phoneNumber: String
    ) {
	setIsLogin(isLogin)
	setUserId(userId)
	name = fullName
	setShopId(shopId)
	setShopName(shopName)
	setEmail(email)
	setIsMSISDNVerified(isMsisdnVerified)
	setBoolean(Constants.LOGIN_SESSION, Constants.HAS_SHOWN_SALDO_WARNING, false)
	setBoolean(Constants.LOGIN_SESSION, Constants.HAS_SHOWN_SALDO_INTRO_PAGE, false)
	setIsGoldMerchant(isGoldMerchant)
	setPhoneNumber(phoneNumber)
    }

    override fun logoutSession() {
	cleanKey(Constants.LOGIN_SESSION, Constants.LOGIN_ID)
	cleanKey(Constants.LOGIN_SESSION, Constants.FULL_NAME)
	cleanKey(Constants.LOGIN_SESSION, Constants.SHOP_ID)
	cleanKey(Constants.LOGIN_SESSION, Constants.SHOP_NAME)
	cleanKey(Constants.LOGIN_SESSION, Constants.IS_LOGIN)
	cleanKey(Constants.LOGIN_SESSION, Constants.IS_MSISDN_VERIFIED)
	cleanKey(Constants.LOGIN_SESSION, Constants.HAS_SHOWN_SALDO_WARNING)
	cleanKey(Constants.LOGIN_SESSION, Constants.IS_AFFILIATE)
	cleanKey(Constants.LOGIN_SESSION, Constants.PHONE_NUMBER)
	cleanKey(Constants.LOGIN_SESSION, Constants.REFRESH_TOKEN)
	cleanKey(Constants.LOGIN_SESSION, Constants.TOKEN_TYPE)
	cleanKey(Constants.LOGIN_SESSION, Constants.ACCESS_TOKEN)
	cleanKey(Constants.LOGIN_SESSION, Constants.PROFILE_PICTURE)
	setString(Constants.LOGIN_SESSION, Constants.GC_TOKEN, "")
	setString(Constants.LOGIN_SESSION, Constants.SHOP_AVATAR, "")
	setBoolean(Constants.LOGIN_SESSION, Constants.IS_POWER_MERCHANT_IDLE, false)
	cleanKey(Constants.LOGIN_SESSION, Constants.TWITTER_ACCESS_TOKEN)
	cleanKey(Constants.LOGIN_SESSION, Constants.TWITTER_ACCESS_TOKEN_SECRET)
	setString(Constants.LOGIN_SESSION, Constants.LOGIN_METHOD, "")
	setBoolean(Constants.LOGIN_SESSION, Constants.TWITTER_SHOULD_POST, false)
	cleanKey(Constants.LOGIN_SESSION, Constants.IS_SHOP_OFFICIAL_STORE)
    }

    /**
     * SETTER METHOD
     */
    override fun setUUID(uuid: String) {
	val sharedPrefs =
	    context?.getSharedPreferences(Constants.LOGIN_UUID_KEY, Context.MODE_PRIVATE)
	val editor = sharedPrefs?.edit()
	val prevUUID = sharedPrefs?.getString(Constants.UUID_KEY, "")
	val currUUID: String
	currUUID = if (prevUUID == "") {
	    uuid
	} else {
	    "$prevUUID*~*$uuid"
	}
	editor?.putString(Constants.UUID_KEY, currUUID)
	editor?.apply()
    }

    companion object {
	const val KEY_IV = "tokopedia1234567"
	private const val DEFAULT_EMPTY_SHOP_ID = "0"
	private const val DEFAULT_EMPTY_SHOP_ID_ON_PREF = "-1"
    }
}