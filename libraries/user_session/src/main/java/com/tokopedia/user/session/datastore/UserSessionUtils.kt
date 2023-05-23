package com.tokopedia.user.session.datastore

import android.util.Log
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.user.session.Constants.ACCESS_TOKEN
import com.tokopedia.user.session.Constants.AUTOFILL_USER_DATA
import com.tokopedia.user.session.Constants.EMAIL
import com.tokopedia.user.session.Constants.FULL_NAME
import com.tokopedia.user.session.Constants.GC_TOKEN
import com.tokopedia.user.session.Constants.HAS_PASSWORD
import com.tokopedia.user.session.Constants.IS_AFFILIATE
import com.tokopedia.user.session.Constants.IS_GOLD_MERCHANT
import com.tokopedia.user.session.Constants.IS_LOCATION_ADMIN
import com.tokopedia.user.session.Constants.IS_LOGIN
import com.tokopedia.user.session.Constants.IS_MSISDN_VERIFIED
import com.tokopedia.user.session.Constants.IS_MULTI_LOCATION_SHOP
import com.tokopedia.user.session.Constants.IS_POWER_MERCHANT_IDLE
import com.tokopedia.user.session.Constants.IS_SHOP_ADMIN
import com.tokopedia.user.session.Constants.IS_SHOP_OFFICIAL_STORE
import com.tokopedia.user.session.Constants.IS_SHOP_OWNER
import com.tokopedia.user.session.Constants.LOGIN_ID
import com.tokopedia.user.session.Constants.LOGIN_METHOD
import com.tokopedia.user.session.Constants.PHONE_NUMBER
import com.tokopedia.user.session.Constants.PROFILE_PICTURE
import com.tokopedia.user.session.Constants.REFRESH_TOKEN
import com.tokopedia.user.session.Constants.SHOP_AVATAR
import com.tokopedia.user.session.Constants.SHOP_ID
import com.tokopedia.user.session.Constants.SHOP_NAME
import com.tokopedia.user.session.Constants.TEMP_USER_ID
import com.tokopedia.user.session.Constants.TOKEN_TYPE
import com.tokopedia.user.session.Constants.UUID_KEY
import com.tokopedia.user.session.datastore.workmanager.DataStoreMigrationWorker
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


object UserSessionUtils {
    fun mapUserSessionKeyString(
        key: String,
        userSessionDataStore: UserSessionDataStore?,
        value: String
    ) {
        runBlocking {
            when (key) {
                LOGIN_ID -> userSessionDataStore?.setUserId(value)
                SHOP_ID -> userSessionDataStore?.setShopId(value)
                SHOP_NAME -> userSessionDataStore?.setShopName(value)
                GC_TOKEN -> userSessionDataStore?.setGCToken(value)
                FULL_NAME -> userSessionDataStore?.setName(value)
                PROFILE_PICTURE -> userSessionDataStore?.setProfilePicture(value)
                TEMP_USER_ID -> userSessionDataStore?.setTempUserId(value)
                TOKEN_TYPE -> userSessionDataStore?.setTokenType(value)
                ACCESS_TOKEN -> userSessionDataStore?.setAccessToken(value)
                REFRESH_TOKEN -> userSessionDataStore?.setRefreshToken(value)
                PHONE_NUMBER -> userSessionDataStore?.setPhoneNumber(value)
                EMAIL -> userSessionDataStore?.setEmail(value)
                SHOP_AVATAR -> userSessionDataStore?.setShopAvatar(value)
                AUTOFILL_USER_DATA -> userSessionDataStore?.setAutofillUserData(value)
                LOGIN_METHOD -> userSessionDataStore?.setLoginMethod(value)
                UUID_KEY -> userSessionDataStore?.setUUID(value)
                else -> {}
            }
        }
    }

    fun mapUserSessionKeyBoolean(
        key: String,
        userSessionDataStore: UserSessionDataStore?,
        value: Boolean
    ) {
        runBlocking {
            when (key) {
                IS_SHOP_OFFICIAL_STORE -> userSessionDataStore?.setIsShopOfficialStore(value)
                IS_LOGIN -> userSessionDataStore?.setIsLogin(value)
                IS_MSISDN_VERIFIED -> userSessionDataStore?.setIsMSISDNVerified(value)
                IS_AFFILIATE -> userSessionDataStore?.setIsAffiliateStatus(value)
                HAS_PASSWORD -> userSessionDataStore?.setHasPassword(value)
                IS_POWER_MERCHANT_IDLE -> userSessionDataStore?.setIsPowerMerchantIdle(value)
                IS_SHOP_OWNER -> userSessionDataStore?.setIsShopOwner(value)
                IS_SHOP_ADMIN -> userSessionDataStore?.setIsShopAdmin(value)
                IS_GOLD_MERCHANT -> userSessionDataStore?.setIsGoldMerchant(value)
                IS_LOCATION_ADMIN -> userSessionDataStore?.setIsLocationAdmin(value)
                IS_MULTI_LOCATION_SHOP -> userSessionDataStore?.setIsMultiLocationShop(value)
                else -> {}
            }
        }
    }

    private const val MAX_STACKTRACE_LENGTH = 1000

    // we use helper to support java code, because we need global scope
    fun logoutSession(userSessionDataStore: UserSessionDataStore?) {
        GlobalScope.launch {
            try {
                userSessionDataStore?.logoutSession()
            } catch (e: Exception) {
                val data = mapOf(
                    "method" to "logout_datastore_session",
                    "error" to Log.getStackTraceString(e).take(MAX_STACKTRACE_LENGTH)
                )
                ServerLogger.log(
                    Priority.P2,
                    DataStoreMigrationWorker.USER_SESSION_LOGGER_TAG,
                    data
                )
            }
        }
    }
}
