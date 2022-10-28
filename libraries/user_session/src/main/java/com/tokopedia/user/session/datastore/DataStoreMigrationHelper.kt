package com.tokopedia.user.session.datastore

import android.content.Context
import android.util.Log
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.user.session.datastore.workmanager.DataStoreMigrationWorker
import kotlinx.coroutines.flow.first

object DataStoreMigrationHelper {

    suspend fun migrateToDataStore(
        userSessionDataStore: UserSessionDataStore,
        userSessionInterface: UserSessionInterface
    ) {
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
                userSessionDataStore.setIsShopOwner(userSessionInterface.isShopOwner)
                userSessionDataStore.setIsShopAdmin(userSessionInterface.isShopAdmin)
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
                ServerLogger.log(
                    Priority.P2, DataStoreMigrationWorker.USER_SESSION_LOGGER_TAG,
                    mapOf(
                        "method" to "migrateToDataStore_exception",
                        "error" to Log.getStackTraceString(e)
                    )
                )
            }
        }
    }

    suspend fun checkDataSync(applicationContext: Context): List<String> {
        val result = mutableListOf<String>()
        val dataStore = UserSessionDataStoreClient.getInstance(applicationContext)
        val userSession = UserSession(applicationContext)

        if (dataStore.getName().first().trim() != userSession.name.trim()) {
            result.add("name")
        }
        if (dataStore.getAccessToken().first().trim() != userSession.accessToken.trim()) {
            result.add("accessToken")
        }
        if (dataStore.getRefreshToken().first().trim() != userSession.freshToken.trim()) {
            result.add("refreshToken")
        }
        if (dataStore.getEmail().first().trim() != userSession.email.trim()) {
            result.add("email")
        }
        if (dataStore.getUserId().first().trim() != userSession.userId.trim()) {
            result.add("userId")
        }
        if (dataStore.getShopId().first().trim() != userSession.shopId.trim()) {
            result.add("shopId")
        }
        if (dataStore.getShopName().first().trim() != userSession.shopName.trim()) {
            result.add("shopName")
        }
        if (dataStore.isGoldMerchant().first() != userSession.isGoldMerchant) {
            result.add("isGoldMerchant")
        }
        if (dataStore.isAffiliate().first() != userSession.isAffiliate) {
            result.add("isAffiliate")
        }
        if (dataStore.getTempPhoneNumber().first().trim() != userSession.tempPhoneNumber.trim()) {
            result.add("tempPhoneNumber")
        }
        if (dataStore.getTempEmail().first().trim() != userSession.tempEmail.trim()) {
            result.add("tempEmail")
        }
        if (dataStore.isFirstTimeUser().first() != userSession.isFirstTimeUser) {
            result.add("isFirstTimeUser")
        }
        if (dataStore.isMsisdnVerified().first() != userSession.isMsisdnVerified) {
            result.add("isMsisdnVerified")
        }
        if (dataStore.hasPassword().first() != userSession.hasPassword()) {
            result.add("hasPassword")
        }
        if (dataStore.getProfilePicture().first().trim() != userSession.profilePicture.trim()) {
            result.add("profilePicture")
        }
        if (dataStore.hasShownSaldoWithdrawalWarning()
                .first() != userSession.hasShownSaldoWithdrawalWarning()
        ) {
            result.add("saldoWithdrawalWaring")
        }
        if (dataStore.hasShownSaldoIntroScreen()
                .first() != userSession.hasShownSaldoIntroScreen()
        ) {
            result.add("hasShownSaldoIntroScreen")
        }
        if (dataStore.getGCToken().first().trim() != userSession.gcToken?.trim()) {
            result.add("gcToken")
        }
        if (dataStore.getShopAvatar().first().trim() != userSession.shopAvatar.trim()) {
            result.add("shopAvatar")
        }
        if (dataStore.isPowerMerchantIdle().first() != userSession.isPowerMerchantIdle) {
            result.add("isPowerMerchantIdle")
        }
        if (dataStore.getAutofillUserData().first().trim() != userSession.autofillUserData.trim()) {
            result.add("autofillUserData")
        }
        if (dataStore.getLoginMethod().first().trim() != userSession.loginMethod.trim()) {
            result.add("loginMethod")
        }
        if (dataStore.isShopOfficialStore().first() != userSession.isShopOfficialStore) {
            result.add("isShopOfficialStore")
        }
        if (dataStore.getDeviceId().first().trim() != userSession.deviceId.trim()) {
            result.add("deviceId")
        }
        if (dataStore.getFcmTimestamp().first() != userSession.fcmTimestamp) {
            result.add("fcmTimestamp")
        }
        if (dataStore.isShopOwner().first() != userSession.isShopOwner) {
            result.add("isShopOwner")
        }
        if (dataStore.isShopAdmin().first() != userSession.isShopAdmin) {
            result.add("isShopAdmin")
        }
        if (dataStore.isLocationAdmin().first() != userSession.isLocationAdmin) {
            result.add("isLocationAdmin")
        }
        if (dataStore.isMultiLocationShop().first() != userSession.isMultiLocationShop) {
            result.add("isMultiLocationShop")
        }
        if (dataStore.isLoggedIn().first() != userSession.isLoggedIn) {
            result.add("isLoggedIn")
        }
        if (dataStore.getPhoneNumber().first().trim() != userSession.phoneNumber.trim()) {
            result.add("phoneNumber")
        }
        if (dataStore.getTokenType().first().trim() != userSession.tokenType.trim()) {
            result.add("tokenType")
        }
        return result
    }
}