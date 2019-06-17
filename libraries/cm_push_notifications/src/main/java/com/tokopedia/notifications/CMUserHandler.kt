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
import com.tokopedia.notifications.common.CMNotificationUtils
import com.tokopedia.notifications.common.launchCatchError
import com.tokopedia.notifications.data.model.TokenResponse
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
            Log.e(TAG, "CMUserHandler: sendFcmTokenToServer ", it)
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
                    e.printStackTrace()
                    return ""
                } catch (e: GooglePlayServicesNotAvailableException) {
                    e.printStackTrace()
                    return ""
                } catch (e: GooglePlayServicesRepairableException) {
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
        get() = (mContext as CMRouter).userId

    private val userIdAsInt: Int
        get() {
            val userIdStr = userId
            var userIdInt = 0
            if (!TextUtils.isEmpty(userIdStr)) {
                try {
                    userIdInt = Integer.parseInt(userIdStr.trim { it <= ' ' })
                } catch (e: NumberFormatException) {
                }
            }
            return userIdInt
        }

    fun updateToken(token: String, remoteDelaySeconds: Long, userAction: Boolean) {
        try {
            var delay = getRandomDelay(remoteDelaySeconds)
            if (userAction)
                delay = 0L
            this.token = token
            handler.postDelayed(runnable, delay)
        } catch (e: Exception) {
        }

    }

    fun cancelRunnable() {
        try {
            handler.removeCallbacks(runnable)
            graphQlUseCase?.let {
                it.unsubscribe()
            }
        } catch (e: Exception) {
        }
    }

    private fun sendFcmTokenToServerGQL(token: String?) {
        try {
            Log.d("CMUserHandler", Thread.currentThread().name)
            if (tempFcmId.equals(token!!, ignoreCase = true)) {
                //ignore temporary fcm token
                return
            }
            val gAdId = googleAdId
            val appVersionName = CMNotificationUtils.getCurrentAppVersionName(mContext)

            if (CMNotificationUtils.tokenUpdateRequired(mContext, token) ||
                    CMNotificationUtils.mapTokenWithUserRequired(mContext, userId) ||
                    CMNotificationUtils.mapTokenWithGAdsIdRequired(mContext, gAdId) ||
                    CMNotificationUtils.mapTokenWithAppVersionRequired(mContext, appVersionName)) {
                val requestParams = HashMap<String, Any>()

                requestParams["macAddress"] = ""
                requestParams[USER_ID] = userIdAsInt
                requestParams[SOURCE] = SOURCE_ANDROID
                requestParams[FCM_TOKEN] = token
                requestParams[APP_ID] = CMNotificationUtils.getUniqueAppId(mContext)
                requestParams[SDK_VERSION] = CMNotificationUtils.sdkVersion.toString()
                requestParams[APP_VERSION] = appVersionName
                requestParams[USER_STATE] = CMNotificationUtils.getUserStatus(mContext, userId)
                requestParams[REQUEST_TIMESTAMP] = CMNotificationUtils.currentLocalTimeStamp.toString() + ""
                requestParams[APP_NAME] = CMNotificationUtils.getApplicationName(mContext)

                graphQlUseCase = GraphqlUseCase()

                val request = GraphqlRequest(GraphqlHelper.loadRawString(mContext.resources, R.raw.query_send_token_to_server),
                        TokenResponse::class.java, requestParams, "AddToken")
                graphQlUseCase.clearRequest()
                graphQlUseCase.addRequest(request)
                graphQlUseCase.execute(object : Subscriber<GraphqlResponse>() {
                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable) {
                        Log.e(TAG, "CMPushNotificationManager: sendFcmTokenToServer " + e.message)
                    }

                    override fun onNext(gqlResponse: GraphqlResponse) {
                        val tokenResponse = gqlResponse.getData<TokenResponse>(TokenResponse::class.java)
                        if (tokenResponse?.cmAddToken != null) {
                            CMNotificationUtils.saveToken(mContext, token)
                            CMNotificationUtils.saveUserId(mContext, userId)
                            CMNotificationUtils.saveGAdsIdId(mContext, gAdId)
                            CMNotificationUtils.saveAppVersion(mContext, appVersionName)
                        }
                    }
                })

            }
        } catch (e: Exception) {
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