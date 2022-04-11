package com.tokopedia.user.session.datastore

import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.runBlocking

class DataStoreMigrationHelper {

    fun migrateToDataStore(
	userSessionDataStore: UserSessionDataStore,
	userSessionInterface: UserSessionInterface
    ) {
	runBlocking {
	    if (userSessionInterface.isLoggedIn) {
		try {
		    userSessionDataStore.setIsLogin(userSessionInterface.isLoggedIn)
		    userSessionDataStore.setUserId(userSessionInterface.userId.trim())
		    userSessionDataStore.setShopId(userSessionInterface.shopId.ifEmpty { "0" })
		    userSessionDataStore.setName(userSessionInterface.name.trim())
		    userSessionDataStore.setEmail(userSessionInterface.email.trim())
		    userSessionDataStore.setPhoneNumber(userSessionInterface.phoneNumber.trim())
		    userSessionDataStore.setShopName(userSessionInterface.shopName.trim())
		    userSessionDataStore.setIsGoldMerchant(userSessionInterface.isGoldMerchant)
		    userSessionDataStore.setTempLoginName("")
		    userSessionDataStore.setTempUserId(userSessionInterface.temporaryUserId.trim())
		    userSessionDataStore.setIsAffiliateStatus(userSessionInterface.isAffiliate)
		    userSessionDataStore.setTempPhoneNumber(userSessionInterface.tempPhoneNumber.trim())
		    userSessionDataStore.setTempLoginEmail(userSessionInterface.tempEmail.trim())
		    userSessionDataStore.setAccessToken(userSessionInterface.accessToken.trim())
		    userSessionDataStore.setRefreshToken(userSessionInterface.freshToken.trim())
		    userSessionDataStore.setTokenType(userSessionInterface.tokenType.trim())
		    userSessionDataStore.setAccessToken(userSessionInterface.accessToken.trim())
		    userSessionDataStore.setFirstTimeUserOnboarding(userSessionInterface.isFirstTimeUser)
		    userSessionDataStore.setFirstTimeUser(userSessionInterface.isFirstTimeUser)
		    userSessionDataStore.setIsMSISDNVerified(userSessionInterface.isMsisdnVerified)
		    userSessionDataStore.setHasPassword(userSessionInterface.hasPassword())
		    userSessionDataStore.setProfilePicture(userSessionInterface.profilePicture.trim())
		    userSessionDataStore.setSaldoWithdrawalWaring(userSessionInterface.hasShownSaldoWithdrawalWarning())
		    userSessionDataStore.setSaldoIntroPageStatus(userSessionInterface.hasShownSaldoIntroScreen())
		    userSessionDataStore.setGCToken(userSessionInterface.gcToken.trim())
		    userSessionDataStore.setShopAvatar(userSessionInterface.shopAvatar.trim())
		    userSessionDataStore.setIsPowerMerchantIdle(userSessionInterface.isPowerMerchantIdle)
		    userSessionDataStore.setTwitterAccessToken(
			userSessionInterface.twitterAccessToken ?: ""
		    )
		    userSessionDataStore.setTwitterSecret(
			userSessionInterface.twitterAccessTokenSecret ?: ""
		    )
		    userSessionDataStore.setAutofillUserData(userSessionInterface.autofillUserData.trim())
		    userSessionDataStore.setLoginMethod(userSessionInterface.loginMethod.trim())
		    userSessionDataStore.setIsShopOfficialStore(userSessionInterface.isShopOfficialStore)
		    userSessionDataStore.setDeviceId(userSessionInterface.deviceId.trim())
		    userSessionDataStore.setFcmTimestamp(userSessionInterface.fcmTimestamp.toString())
		    userSessionDataStore.setAndroidId(userSessionInterface.androidId.trim())
		} catch (e: Exception) {
		    e.printStackTrace()
		}
	    }
	}
    }
}