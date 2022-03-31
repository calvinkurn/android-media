package com.tokopedia.user.session.datastore

import com.tokopedia.user.session.Constants.*
import kotlinx.coroutines.runBlocking


object UserSessionKeyMapper {
    fun mapUserSessionKeyString(key: String, userSessionDataStore: UserSessionDataStore?, value: String) {
	runBlocking {
	    when(key) {
		LOGIN_ID -> userSessionDataStore?.setUserId(value)
		SHOP_ID -> userSessionDataStore?.setShopId(value)
		SHOP_NAME -> userSessionDataStore?.setShopName(value)
		GC_TOKEN -> userSessionDataStore?.setGCToken(value)
		FULL_NAME -> userSessionDataStore?.setName(value)
		PROFILE_PICTURE -> userSessionDataStore?.setProfilePicture(value)
		GCM_ID -> userSessionDataStore?.setDeviceId(value)
		TEMP_PHONE_NUMBER -> userSessionDataStore?.setTempPhoneNumber(value)
		TEMP_EMAIL -> userSessionDataStore?.setTempLoginEmail(value)
		TEMP_NAME -> userSessionDataStore?.setTempLoginName(value)
		TEMP_USER_ID -> userSessionDataStore?.setTempUserId(value)
		TOKEN_TYPE -> userSessionDataStore?.setTokenType(value)
		ACCESS_TOKEN -> userSessionDataStore?.setAccessToken(value)
		REFRESH_TOKEN -> userSessionDataStore?.setRefreshToken(value)
		TWITTER_ACCESS_TOKEN -> userSessionDataStore?.setTwitterAccessToken(value)
		TWITTER_ACCESS_TOKEN_SECRET -> userSessionDataStore?.setTwitterSecret(value)
		PHONE_NUMBER -> userSessionDataStore?.setPhoneNumber(value)
		EMAIL -> userSessionDataStore?.setEmail(value)
		SHOP_AVATAR -> userSessionDataStore?.setShopAvatar(value)
		AUTOFILL_USER_DATA -> userSessionDataStore?.setAutofillUserData(value)
		LOGIN_METHOD -> userSessionDataStore?.setLoginMethod(value)
		GCM_ID_TIMESTAMP -> userSessionDataStore?.setFcmTimestamp(value)
		UUID_KEY -> userSessionDataStore?.setUUID(value)
		else -> {}
	    }
	}
    }

    fun mapUserSessionKeyBoolean(key: String, userSessionDataStore: UserSessionDataStore?, value: Boolean) {
	runBlocking {
	    when(key) {
		IS_SHOP_OFFICIAL_STORE -> userSessionDataStore?.setIsShopOfficialStore(value)
		IS_FIRST_TIME_USER ->  userSessionDataStore?.setFirstTimeUser(value)
		TWITTER_SHOULD_POST ->  userSessionDataStore?.setTwitterShouldPost(value)
		IS_LOGIN ->  userSessionDataStore?.setIsLogin(value)
		IS_FIRST_TIME_USER_NEW_ONBOARDING ->  userSessionDataStore?.setFirstTimeUserOnboarding(value)
		IS_MSISDN_VERIFIED ->  userSessionDataStore?.setIsMSISDNVerified(value)
		IS_AFFILIATE ->  userSessionDataStore?.setIsAffiliateStatus(value)
		HAS_PASSWORD ->  userSessionDataStore?.setHasPassword(value)
		HAS_SHOWN_SALDO_WARNING ->  userSessionDataStore?.setSaldoWithdrawalWaring(value)
		HAS_SHOWN_SALDO_INTRO_PAGE ->  userSessionDataStore?.setSaldoIntroPageStatus(value)
		IS_POWER_MERCHANT_IDLE ->  userSessionDataStore?.setIsPowerMerchantIdle(value)
		IS_SHOP_OWNER ->  userSessionDataStore?.setIsShopOwner(value)
		IS_SHOP_ADMIN ->  userSessionDataStore?.setIsShopAdmin(value)
		IS_LOCATION_ADMIN ->  userSessionDataStore?.setIsLocationAdmin(value)
		IS_MULTI_LOCATION_SHOP ->  userSessionDataStore?.setIsMultiLocationShop(value)
		else -> {}
	    }
	}
    }
}
