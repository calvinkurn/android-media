package com.tokopedia.user.session.datastore

import com.tokopedia.user.session.Constants.*
import kotlinx.coroutines.runBlocking


object UserSessionKeyMapper {
    fun mapUserSessionKeyString(key: String, userSessionDataStore: UserSessionDataStore, value: String) {
        when(key) {
	    LOGIN_ID -> runBlocking { userSessionDataStore.setUserId(value) }
	    SHOP_ID -> runBlocking { userSessionDataStore.setUserId(value) }
	    SHOP_NAME -> runBlocking { userSessionDataStore.setShopName(value) }
	    GC_TOKEN -> runBlocking { userSessionDataStore.setGCToken(value) }
	    FULL_NAME -> runBlocking { userSessionDataStore.setName(value) }
	    PROFILE_PICTURE -> runBlocking { userSessionDataStore.setProfilePicture(value) }
	    GCM_ID -> runBlocking { userSessionDataStore.setDeviceId(value) }
	    TEMP_PHONE_NUMBER -> runBlocking { userSessionDataStore.setTempPhoneNumber(value) }
	    TEMP_EMAIL -> runBlocking { userSessionDataStore.setTempLoginEmail(value) }
	    TEMP_NAME -> runBlocking { userSessionDataStore.setTempLoginName(value) }
	    TEMP_USER_ID -> runBlocking { userSessionDataStore.setTempUserId(value) }
	    TOKEN_TYPE -> runBlocking { userSessionDataStore.setTokenType(value) }
	    ACCESS_TOKEN -> runBlocking { userSessionDataStore.setAccessToken(value) }
	    REFRESH_TOKEN -> runBlocking { userSessionDataStore.setRefreshToken(value) }
	    TWITTER_ACCESS_TOKEN -> runBlocking { userSessionDataStore.setTwitterAccessToken(value) }
	    TWITTER_ACCESS_TOKEN_SECRET -> runBlocking { userSessionDataStore.setTwitterSecret(value) }
	    PHONE_NUMBER -> runBlocking { userSessionDataStore.setPhoneNumber(value) }
	    EMAIL -> runBlocking { userSessionDataStore.setEmail(value) }
	    SHOP_AVATAR -> runBlocking { userSessionDataStore.setShopAvatar(value) }
	    AUTOFILL_USER_DATA -> runBlocking { userSessionDataStore.setAutofillUserData(value) }
	    LOGIN_METHOD -> runBlocking { userSessionDataStore.setLoginMethod(value) }
	    GCM_ID_TIMESTAMP -> runBlocking { userSessionDataStore.setFcmTimestamp() }
	    UUID_KEY -> runBlocking { userSessionDataStore.setUUID(value) }
	}
    }

    fun mapUserSessionKeyBoolean(key: String, userSessionDataStore: UserSessionDataStore, value: Boolean) {
	when(key) {
	    IS_SHOP_OFFICIAL_STORE -> runBlocking { userSessionDataStore.setIsShopOfficialStore(value) }
	    IS_FIRST_TIME_USER -> runBlocking { userSessionDataStore.setFirstTimeUser(value) }
	    TWITTER_SHOULD_POST -> runBlocking { userSessionDataStore.setTwitterShouldPost(value) }
	    IS_LOGIN -> runBlocking { userSessionDataStore.setIsLogin(value) }
	    IS_FIRST_TIME_USER_NEW_ONBOARDING -> runBlocking { userSessionDataStore.setFirstTimeUserOnboarding(value) }
	    IS_MSISDN_VERIFIED -> runBlocking { userSessionDataStore.setIsMSISDNVerified(value) }
	    IS_AFFILIATE -> runBlocking { userSessionDataStore.setIsAffiliateStatus(value) }
	    HAS_PASSWORD -> runBlocking { userSessionDataStore.setHasPassword(value) }
	    HAS_SHOWN_SALDO_WARNING -> runBlocking { userSessionDataStore.setSaldoWithdrawalWaring(value) }
	    HAS_SHOWN_SALDO_INTRO_PAGE -> runBlocking { userSessionDataStore.setSaldoIntroPageStatus(value) }
	    IS_POWER_MERCHANT_IDLE -> runBlocking { userSessionDataStore.setIsPowerMerchantIdle(value) }
	    IS_SHOP_OWNER -> runBlocking { userSessionDataStore.setIsShopOwner(value) }
	    IS_SHOP_ADMIN -> runBlocking { userSessionDataStore.setIsShopAdmin(value) }
	    IS_LOCATION_ADMIN -> runBlocking { userSessionDataStore.setIsLocationAdmin(value) }
	    IS_MULTI_LOCATION_SHOP -> runBlocking { userSessionDataStore.setIsMultiLocationShop(value) }

	}
    }
}
