package com.tokopedia.user.session.datastore

import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class DataStoreMigrationHelper {

    fun migrateToDataStore(userSessionDataStore: UserSessionDataStore, userSessionInterface: UserSessionInterface) {
	runBlocking {
	    if (userSessionInterface.isLoggedIn && userSessionDataStore.getUserId().first().isEmpty()) {
		try {
//        userSessionDataStore.setUUID(userSessionInterface)
		    userSessionDataStore.setIsLogin(userSessionInterface.isLoggedIn)
		    userSessionDataStore.setUserId(userSessionInterface.userId)
		    userSessionDataStore.setName(userSessionInterface.name)
		    userSessionDataStore.setEmail(userSessionInterface.email)
		    userSessionDataStore.setPhoneNumber(userSessionInterface.phoneNumber)
		    userSessionDataStore.setShopName(userSessionInterface.shopName)
		    userSessionDataStore.setShopId(userSessionInterface.shopId)
		    userSessionDataStore.setIsGoldMerchant(userSessionInterface.isGoldMerchant)
		    userSessionDataStore.setTempLoginName("")
		    userSessionDataStore.setTempUserId(userSessionInterface.temporaryUserId)
		    userSessionDataStore.setIsAffiliateStatus(userSessionInterface.isAffiliate)
		    userSessionDataStore.setTempPhoneNumber(userSessionInterface.tempPhoneNumber)
		    userSessionDataStore.setTempLoginEmail(userSessionInterface.tempEmail)
		    userSessionDataStore.setToken(
			userSessionInterface.accessToken,
			userSessionInterface.tokenType
		    )
		    userSessionDataStore.setTokenType(userSessionInterface.tokenType)
		    userSessionDataStore.setAccessToken(userSessionInterface.accessToken)
		    userSessionDataStore.setFirstTimeUserOnboarding(userSessionInterface.isFirstTimeUser)
//	userSessionDataStore.setFirstTimeUser(userSessionInterface.tempEmail)
		    userSessionDataStore.setToken(
			userSessionInterface.accessToken,
			userSessionInterface.tokenType,
			userSessionInterface.freshToken
		    )
		    userSessionDataStore.setLoginSession(
			userSessionInterface.isLoggedIn,
			userSessionInterface.userId,
			userSessionInterface.name,
			userSessionInterface.shopId,
			userSessionInterface.isMsisdnVerified,
			userSessionInterface.shopName,
			userSessionInterface.email,
			userSessionInterface.isGoldMerchant,
			userSessionInterface.phoneNumber
		    )
		    userSessionDataStore.setIsMSISDNVerified(userSessionInterface.isMsisdnVerified)
		    userSessionDataStore.setHasPassword(userSessionInterface.hasPassword())
		    userSessionDataStore.setProfilePicture(userSessionInterface.profilePicture)
		    userSessionDataStore.setSaldoWithdrawalWaring(userSessionInterface.hasShownSaldoWithdrawalWarning())
		    userSessionDataStore.setSaldoIntroPageStatus(userSessionInterface.hasShownSaldoIntroScreen())
		    userSessionDataStore.setGCToken(userSessionInterface.gcToken)
		    userSessionDataStore.setShopAvatar(userSessionInterface.shopAvatar)
		    userSessionDataStore.setIsPowerMerchantIdle(userSessionInterface.isPowerMerchantIdle)
		    userSessionDataStore.setTwitterAccessTokenAndSecret(
			userSessionInterface.twitterAccessToken ?: "",
			userSessionInterface.twitterAccessTokenSecret ?: ""
		    )
		    userSessionDataStore.setTwitterAccessToken(
			userSessionInterface.twitterAccessToken ?: ""
		    )
		    userSessionDataStore.setTwitterSecret(
			userSessionInterface.twitterAccessTokenSecret ?: ""
		    )
		    userSessionDataStore.setTwitterShouldPost(userSessionInterface.twitterShouldPost)
		    userSessionDataStore.setAutofillUserData(userSessionInterface.autofillUserData)
		    userSessionDataStore.setLoginMethod(userSessionInterface.loginMethod)
		    userSessionDataStore.setIsShopOfficialStore(userSessionInterface.isShopOfficialStore)
		    userSessionDataStore.setDeviceId(userSessionInterface.deviceId)
		    userSessionDataStore.setFcmTimestamp()
		} catch (e: Exception) {
		    e.printStackTrace()
		    // Log here
		}
	    }
	}
    }
}