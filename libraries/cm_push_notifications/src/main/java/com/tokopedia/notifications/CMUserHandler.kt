package com.tokopedia.notifications

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
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.notifications.common.CMConstant
import com.tokopedia.notifications.common.CMNotificationCacheHandler
import com.tokopedia.notifications.common.CMNotificationUtils
import com.tokopedia.notifications.common.launchCatchError
import com.tokopedia.notifications.data.model.TokenResponse
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import rx.Subscriber
import java.io.IOException
import java.util.*
import kotlin.coroutines.CoroutineContext

/**
 * @author lalit.singh
 */
class CMUserHandler(private val mContext: Context) : CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO

    private var token: String? = null
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var graphQlUseCase: GraphqlUseCase

    private var runnable: Runnable = Runnable {
        launchCatchError(block = {
            sendFcmTokenToServerGQL(token)
        }, onError = {
            ServerLogger.log(Priority.P2, "CM_VALIDATION", mapOf("type" to "exception", "err" to Log.getStackTraceString(it)
                    .take(CMConstant.TimberTags.MAX_LIMIT), "data" to ""))
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
                    ServerLogger.log(Priority.P2, "CM_VALIDATION",
                            mapOf("type" to "exception", "err" to Log.getStackTraceString(e).take(CMConstant.TimberTags.MAX_LIMIT),
                                    "data" to ""))
                    e.printStackTrace()
                    return ""
                } catch (e: GooglePlayServicesNotAvailableException) {
                    ServerLogger.log(Priority.P2, "CM_VALIDATION",
                            mapOf("type" to "exception", "err" to Log.getStackTraceString(e).take(CMConstant.TimberTags.MAX_LIMIT),
                                    "data" to ""))
                    e.printStackTrace()
                    return ""
                } catch (e: GooglePlayServicesRepairableException) {
                    ServerLogger.log(Priority.P2, "CM_VALIDATION",
                            mapOf("type" to "exception", "err" to Log.getStackTraceString(e).take(CMConstant.TimberTags.MAX_LIMIT),
                                    "data" to ""))
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

    fun updateToken(token: String, remoteDelaySeconds: Long, userAction: Boolean) {
        try {
            var delay = getRandomDelay(remoteDelaySeconds)
            if (userAction)
                delay = 0L
            this.token = token
            handler.postDelayed(runnable, delay)
        } catch (e: Exception) {
            ServerLogger.log(Priority.P2, "CM_VALIDATION",
                    mapOf("type" to "exception", "err" to Log.getStackTraceString(e).take(CMConstant.TimberTags.MAX_LIMIT),
                            "data" to ""))
        }

    }

    fun cancelRunnable() {
        try {
            handler.removeCallbacks(runnable)
            if (::graphQlUseCase.isInitialized)
                graphQlUseCase.unsubscribe()
        } catch (e: Exception) {
            ServerLogger.log(Priority.P2, "CM_VALIDATION",
                    mapOf("type" to "exception", "err" to Log.getStackTraceString(e).take(CMConstant.TimberTags.MAX_LIMIT),
                            "data" to ""))
        }
    }

    private fun sendFcmTokenToServerGQL(token: String?) {
        try {
            if (tempFcmId.equals(token!!, ignoreCase = true)) {
                //ignore temporary fcm token
                return
            }

            if (CMNotificationUtils.checkTokenValidity(token)) return

            val gAdId = googleAdId
            val appVersionName = CMNotificationUtils.getCurrentAppVersionName(mContext)
            val applicationName = CMNotificationUtils.getApplicationName(mContext)
            if (applicationName == CMNotificationUtils.SELLER_APP_NAME && !CMPushNotificationManager.instance.sellerAppCmAddTokenEnabled)
                return

            if (CMNotificationUtils.isTokenExpired(CMNotificationCacheHandler(mContext), token, userId, gAdId, appVersionName)) {
                val requestParams = HashMap<String, Any>()

                requestParams["macAddress"] = ""
                requestParams[SOURCE] = SOURCE_ANDROID
                requestParams[FCM_TOKEN] = token
                requestParams[APP_ID] = CMNotificationUtils.getUniqueAppId(mContext)
                requestParams[SDK_VERSION] = CMNotificationUtils.sdkVersion.toString()
                requestParams[APP_VERSION] = appVersionName
                val userIdAndStatus = CMNotificationUtils.getUserIdAndStatus(mContext, userId)
                requestParams[USER_STATE] = userIdAndStatus.first
                requestParams[USER_ID] = userIdAndStatus.second
                requestParams[REQUEST_TIMESTAMP] = CMNotificationUtils.currentLocalTimeStamp.toString() + ""
                requestParams[APP_NAME] = applicationName

                graphQlUseCase = GraphqlUseCase()

                val request = GraphqlRequest(GraphqlHelper.loadRawString(mContext.resources, R.raw.query_send_token_to_server),
                        TokenResponse::class.java, requestParams, "AddToken")
                graphQlUseCase.clearRequest()
                graphQlUseCase.addRequest(request)
                graphQlUseCase.execute(object : Subscriber<GraphqlResponse>() {
                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable) {
                        ServerLogger.log(Priority.P2, "CM_VALIDATION",
                                mapOf("type" to "exception", "err" to Log.getStackTraceString(e).take(CMConstant.TimberTags.MAX_LIMIT),
                                        "data" to ""))
                    }

                    override fun onNext(gqlResponse: GraphqlResponse) {
                        val gqlError = gqlResponse.getError(TokenResponse::class.java)
                        if (!gqlError.isNullOrEmpty()) {
                            val errorStr = gqlError[0].message
                            if (errorStr.isNullOrEmpty())
                                ServerLogger.log(Priority.P2, "CM_VALIDATION", mapOf("type" to "validation", "reason" to "cm_gql_error_thrown",
                                        "data" to ""))
                            else
                                ServerLogger.log(Priority.P2, "CM_VALIDATION", mapOf("type" to "validation", "reason" to "cm_gql_error_thrown",
                                        "data" to errorStr.take(CMConstant.TimberTags.MAX_LIMIT)))
                        } else {
                            val tokenResponse = gqlResponse.getData<TokenResponse>(TokenResponse::class.java)
                            if (tokenResponse?.cmAddToken != null && tokenResponse.cmAddToken.error.isNullOrEmpty()) {
                                CMNotificationUtils.saveToken(mContext, token)
                                CMNotificationUtils.saveUserId(mContext, userId)
                                CMNotificationUtils.saveGAdsIdId(mContext, gAdId)
                                CMNotificationUtils.saveAppVersion(mContext, appVersionName)
                            } else {
                                if (tokenResponse == null) {
                                    ServerLogger.log(Priority.P2, "CM_VALIDATION", mapOf("type" to "validation", "reason" to "no_response_cm_add_token",
                                            "data" to ""))
                                } else {
                                    ServerLogger.log(Priority.P2, "CM_VALIDATION", mapOf("type" to "validation", "reason" to "no_response_cm_add_token",
                                            "data" to tokenResponse.toString().take(CMConstant.TimberTags.MAX_LIMIT)))
                                }

                            }
                        }
                    }
                })

            }
        } catch (e: Exception) {
            ServerLogger.log(Priority.P2, "CM_VALIDATION", mapOf("type" to "exception", "err" to Log.getStackTraceString(e)
                    .take(CMConstant.TimberTags.MAX_LIMIT), "data" to ""))
            e.printStackTrace()
        }


    }

    private fun getRandomDelay(randomDelaySeconds: Long): Long {
        val rand = Random()
        val millis = rand.nextInt(1000) + 1 //in millis delay
        val seconds = rand.nextInt(randomDelaySeconds.toInt()) + 1 //in seconds
        return (seconds * 1000 + millis).toLong()
    }

    companion object {

        private val USER_ID = "userId"
        private val SOURCE = "deviceOS"
        private val FCM_TOKEN = "notificationToken"
        private val APP_ID = "appId"
        private val SDK_VERSION = "sdkVersion"
        private val APP_VERSION = "appVersion"
        private val REQUEST_TIMESTAMP = "requestTimestamp"

        private val SOURCE_ANDROID = "android"
        private val USER_STATE = "loggedStatus"
        private val APP_NAME = "appName"

        internal var TAG = CMUserHandler::class.java.simpleName

        private val tempFcmId: String
            get() = UUID.randomUUID().toString()
    }

}