package com.tokopedia.fcmcommon

import android.content.Context
import android.text.TextUtils
import android.util.Log
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.abstraction.constant.TkpdCache
import com.tokopedia.fcmcommon.common.FcmCacheHandler
import com.tokopedia.fcmcommon.common.FcmConstant
import com.tokopedia.fcmcommon.data.TokenResponse
import com.tokopedia.fcmcommon.domain.SendTokenToCMUseCase
import com.tokopedia.fcmcommon.utils.FcmRemoteConfigUtils
import com.tokopedia.fcmcommon.utils.FcmTokenUtils
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import java.io.IOException
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class SendTokenToCMHandler @Inject constructor(
    private var sendTokenToCMUseCase: SendTokenToCMUseCase,
    @ApplicationContext private val mContext: Context,
    private val userSession: UserSessionInterface,
    fcmRemoteConfigUtils: FcmRemoteConfigUtils,
    private val fcmTokenUtils: FcmTokenUtils,
    private val fcmCacheHandler: FcmCacheHandler
) : CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO

    private var token: String? = null

    private val sellerAppCmAddTokenEnabled: Boolean =
        fcmRemoteConfigUtils.getBooleanRemoteConfig(
            FcmConstant.KEY_SELLERAPP_CM_ADD_TOKEN_ENABLED,
            false
        )

    private val googleAdId: String
        get() {
            val localCacheHandler = LocalCacheHandler(mContext, TkpdCache.ADVERTISINGID)
            val adsId = localCacheHandler.getString(TkpdCache.Key.KEY_ADVERTISINGID)
            val isAdsIdEmpty = TextUtils.isEmpty(adsId.trim { it <= ' ' })
            if (adsId != null && !isAdsIdEmpty) {
                return adsId
            } else {
                val adInfo: AdvertisingIdClient.Info?
                try {
                    adInfo = AdvertisingIdClient.getAdvertisingIdInfo(mContext)
                } catch (e: IOException) {
                    ServerLogger.log(
                        Priority.P2,
                        "CM_VALIDATION",
                        mapOf(
                            "type" to "exception",
                            "err" to Log.getStackTraceString(e).take(FcmConstant.MAX_LIMIT),
                            "data" to ""
                        )
                    )
                    e.printStackTrace()
                    return ""
                } catch (e: GooglePlayServicesNotAvailableException) {
                    ServerLogger.log(
                        Priority.P2,
                        "CM_VALIDATION",
                        mapOf(
                            "type" to "exception",
                            "err" to Log.getStackTraceString(e).take(FcmConstant.MAX_LIMIT),
                            "data" to ""
                        )
                    )
                    e.printStackTrace()
                    return ""
                } catch (e: GooglePlayServicesRepairableException) {
                    ServerLogger.log(
                        Priority.P2,
                        "CM_VALIDATION",
                        mapOf(
                            "type" to "exception",
                            "err" to Log.getStackTraceString(e).take(FcmConstant.MAX_LIMIT),
                            "data" to ""
                        )
                    )
                    e.printStackTrace()
                    return ""
                }

                if (adInfo != null) {
                    val adID = adInfo.id

                    if (!TextUtils.isEmpty(adID)) {
                        localCacheHandler.putString(TkpdCache.Key.KEY_ADVERTISINGID, adID)
                        localCacheHandler.applyEditor()
                    }
                    return adID
                }
            }
            return ""
        }

    private val userId: String
        get() {
            userSession.userId?.let {
                return it
            }
            return ""
        }

    fun updateToken(token: String) {
        try {
            this.token = token
            sendFcmTokenToServerGQL(token)
        } catch (e: Exception) {
            ServerLogger.log(
                Priority.P2,
                "CM_VALIDATION",
                mapOf(
                    "type" to "exception",
                    "err" to Log.getStackTraceString(e).take(FcmConstant.MAX_LIMIT),
                    "data" to ""
                )
            )
        }
    }

    private fun sendFcmTokenToServerGQL(token: String) {
        try {
            if (tempFcmId.equals(token, ignoreCase = true)) {
                // ignore temporary fcm token
                return
            }

            if (fcmTokenUtils.checkTokenValidity(token)) return

            val gAdId = googleAdId
            val appVersionName = fcmTokenUtils.getCurrentAppVersionName(mContext)
            val applicationName = fcmTokenUtils.getApplicationName(mContext)
            if (applicationName == fcmTokenUtils.SELLER_APP_NAME && !sellerAppCmAddTokenEnabled) {
                return
            }

            if (fcmTokenUtils.isTokenExpired(
                    fcmCacheHandler,
                    token,
                    userId,
                    gAdId,
                    appVersionName
                )
            ) {
                val requestParams = getRequestParams(token, appVersionName, applicationName)
                launchCatchError(block = {
                    val response = sendTokenToCMUseCase(requestParams)
                    onGqlSuccess(response, appVersionName, gAdId)
                }, onError = {
                        ServerLogger.log(
                            Priority.P2,
                            "CM_VALIDATION",
                            mapOf(
                                "type" to "exception",
                                "err" to Log.getStackTraceString(it)
                                    .take(FcmConstant.MAX_LIMIT),
                                "data" to ""
                            )
                        )
                    })
            }
        } catch (e: Exception) {
            ServerLogger.log(
                Priority.P2,
                "CM_VALIDATION",
                mapOf(
                    "type" to "exception",
                    "err" to Log.getStackTraceString(e)
                        .take(FcmConstant.MAX_LIMIT),
                    "data" to ""
                )
            )
            e.printStackTrace()
        }
    }

    private fun onGqlSuccess(tokenResponse: TokenResponse, appVersionName: String, gAdId: String) {
        if (tokenResponse.cmAddToken.error.isNullOrEmpty()) {
            token?.let { fcmTokenUtils.saveToken(it) }
            fcmTokenUtils.saveUserId(userId)
            fcmTokenUtils.saveGAdsIdId(gAdId)
            fcmTokenUtils.saveAppVersion(appVersionName)
        } else {
            ServerLogger.log(
                Priority.P2,
                "CM_VALIDATION",
                mapOf(
                    "type" to "validation",
                    "reason" to "no_response_cm_add_token",
                    "data" to tokenResponse.toString()
                        .take(FcmConstant.MAX_LIMIT)
                )
            )
        }
    }

    private fun getRequestParams(
        token: String,
        appVersionName: String,
        applicationName: String
    ): HashMap<String, Any> {
        val requestParams = HashMap<String, Any>()

        requestParams[MAC_ADDRESS] = fcmTokenUtils.getWifiMacAddress()
        requestParams[SOURCE] = SOURCE_ANDROID
        requestParams[FCM_TOKEN] = token
        requestParams[APP_ID] = fcmTokenUtils.getUniqueAppId()
        requestParams[SDK_VERSION] = fcmTokenUtils.sdkVersion.toString()
        requestParams[APP_VERSION] = appVersionName
        val userIdAndStatus = fcmTokenUtils.getUserIdAndStatus(userId)
        requestParams[USER_STATE] = userIdAndStatus.first
        requestParams[USER_ID] = userIdAndStatus.second
        requestParams[REQUEST_TIMESTAMP] = fcmTokenUtils.currentLocalTimeStamp.toString() + ""
        requestParams[APP_NAME] = applicationName

        return requestParams
    }

    companion object {

        private const val USER_ID = "userId"
        private const val SOURCE = "deviceOS"
        private const val FCM_TOKEN = "notificationToken"
        private const val APP_ID = "appId"
        private const val SDK_VERSION = "sdkVersion"
        private const val APP_VERSION = "appVersion"
        private const val REQUEST_TIMESTAMP = "requestTimestamp"
        private const val MAC_ADDRESS = "macAddress"

        private const val SOURCE_ANDROID = "android"
        private const val USER_STATE = "loggedStatus"
        private const val APP_NAME = "appName"
        private const val CACHE_CM_NOTIFICATIONS = "cache_fcmnotifications"

        private val tempFcmId: String
            get() = UUID.randomUUID().toString()
    }
}
