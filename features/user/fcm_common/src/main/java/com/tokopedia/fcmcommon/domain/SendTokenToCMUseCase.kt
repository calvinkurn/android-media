package com.tokopedia.fcmcommon.domain

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.abstraction.constant.TkpdCache
import com.tokopedia.fcmcommon.common.FcmCacheHandler
import com.tokopedia.fcmcommon.common.FcmConstant
import com.tokopedia.fcmcommon.data.TokenResponse
import com.tokopedia.fcmcommon.utils.FcmRemoteConfigUtils
import com.tokopedia.fcmcommon.utils.FcmTokenUtils
import com.tokopedia.fcmcommon.utils.launchCatchError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import rx.Subscriber
import java.io.IOException
import java.util.*
import kotlin.coroutines.CoroutineContext

class SendTokenToCMUseCase(
    private val mContext: Context,
    private val rawQuery: Int
) : CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO

    private var token: String? = null
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var graphQlUseCase: GraphqlUseCase
    private val fcmRemoteConfigUtils by lazy {
        FcmRemoteConfigUtils(mContext)
    }
    private val sellerAppCmAddTokenEnabled: Boolean
        get() = fcmRemoteConfigUtils.getBooleanRemoteConfig(
            FcmConstant.KEY_SELLERAPP_CM_ADD_TOKEN_ENABLED,
            false
        )
    private val remoteDelaySeconds: Long
        get() = fcmRemoteConfigUtils.getLongRemoteConfig("app_token_send_delay", 60)

    private var runnable: Runnable = Runnable {
        launchCatchError(block = {
            sendFcmTokenToServerGQL(token)
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

    private val googleAdId: String
        get() {
            val localCacheHandler = LocalCacheHandler(mContext, TkpdCache.ADVERTISINGID)
            val adsId = localCacheHandler.getString(TkpdCache.Key.KEY_ADVERTISINGID)

            if (adsId != null && !TextUtils.isEmpty(adsId.trim { it <= ' ' })) {
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
            val userSession: UserSessionInterface = UserSession(mContext)
            userSession.userId?.let {
                return it
            }
            return ""
        }

    fun updateToken(token: String, userAction: Boolean) {
        try {
            var delay = getRandomDelay(remoteDelaySeconds)
            if (userAction) {
                delay = 0L
            }
            this.token = token
            handler.postDelayed(runnable, delay)
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

    fun cancelRunnable() {
        try {
            handler.removeCallbacks(runnable)
            if (::graphQlUseCase.isInitialized) {
                graphQlUseCase.unsubscribe()
            }
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

    private fun sendFcmTokenToServerGQL(token: String?) {
        try {
            if (tempFcmId.equals(token!!, ignoreCase = true)) {
                // ignore temporary fcm token
                return
            }

            if (FcmTokenUtils.checkTokenValidity(token)) return

            val gAdId = googleAdId
            val appVersionName = FcmTokenUtils.getCurrentAppVersionName(mContext)
            val applicationName = FcmTokenUtils.getApplicationName(mContext)
            if (applicationName == FcmTokenUtils.SELLER_APP_NAME && !sellerAppCmAddTokenEnabled) {
                return
            }

            if (FcmTokenUtils.isTokenExpired(
                    FcmCacheHandler(mContext, CACHE_CM_NOTIFICATIONS),
                    token,
                    userId,
                    gAdId,
                    appVersionName
                )
            ) {
                val requestParams = getRequestParams(token, appVersionName, applicationName)
                graphQlUseCase = GraphqlUseCase()

                val request = GraphqlRequest(
                    GraphqlHelper.loadRawString(mContext.resources, rawQuery),
                    TokenResponse::class.java,
                    requestParams,
                    "AddToken"
                )
                graphQlUseCase.clearRequest()
                graphQlUseCase.addRequest(request)
                executeGQL(token, gAdId, appVersionName)
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

    private fun getRequestParams(
        token: String,
        appVersionName: String,
        applicationName: String
    ): HashMap<String, Any> {
        val requestParams = HashMap<String, Any>()

        requestParams[MAC_ADDRESS] = FcmTokenUtils.getWifiMacAddress(mContext)
        requestParams[SOURCE] = SOURCE_ANDROID
        requestParams[FCM_TOKEN] = token
        requestParams[APP_ID] = FcmTokenUtils.getUniqueAppId(mContext)
        requestParams[SDK_VERSION] = FcmTokenUtils.sdkVersion.toString()
        requestParams[APP_VERSION] = appVersionName
        val userIdAndStatus = FcmTokenUtils.getUserIdAndStatus(mContext, userId)
        requestParams[USER_STATE] = userIdAndStatus.first
        requestParams[USER_ID] = userIdAndStatus.second
        requestParams[REQUEST_TIMESTAMP] = FcmTokenUtils.currentLocalTimeStamp.toString() + ""
        requestParams[APP_NAME] = applicationName

        return requestParams
    }

    private fun executeGQL(token: String, gAdId: String, appVersionName: String) {
        graphQlUseCase.execute(object : Subscriber<GraphqlResponse>() {
            override fun onCompleted() {
            }

            override fun onError(e: Throwable) {
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

            override fun onNext(gqlResponse: GraphqlResponse) {
                val gqlError = gqlResponse.getError(TokenResponse::class.java)
                if (!gqlError.isNullOrEmpty()) {
                    val errorStr = gqlError[0].message
                    if (errorStr.isNullOrEmpty()) {
                        ServerLogger.log(
                            Priority.P2,
                            "CM_VALIDATION",
                            mapOf(
                                "type" to "validation",
                                "reason" to "cm_gql_error_thrown",
                                "data" to ""
                            )
                        )
                    } else {
                        ServerLogger.log(
                            Priority.P2,
                            "CM_VALIDATION",
                            mapOf(
                                "type" to "validation",
                                "reason" to "cm_gql_error_thrown",
                                "data" to errorStr.take(FcmConstant.MAX_LIMIT)
                            )
                        )
                    }
                } else {
                    val tokenResponse =
                        gqlResponse.getData<TokenResponse>(TokenResponse::class.java)
                    if (tokenResponse?.cmAddToken != null && tokenResponse.cmAddToken.error.isNullOrEmpty()) {
                        FcmTokenUtils.saveToken(mContext, token)
                        FcmTokenUtils.saveUserId(mContext, userId)
                        FcmTokenUtils.saveGAdsIdId(mContext, gAdId)
                        FcmTokenUtils.saveAppVersion(mContext, appVersionName)
                    } else {
                        if (tokenResponse == null) {
                            ServerLogger.log(
                                Priority.P2,
                                "CM_VALIDATION",
                                mapOf(
                                    "type" to "validation",
                                    "reason" to "no_response_cm_add_token",
                                    "data" to ""
                                )
                            )
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
                }
            }
        })
    }

    private fun getRandomDelay(randomDelaySeconds: Long): Long {
        val rand = Random()
        val millis = rand.nextInt(1000) + 1 // in millis delay
        val seconds = rand.nextInt(randomDelaySeconds.toInt()) + 1 // in seconds
        return (seconds * 1000 + millis).toLong()
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
