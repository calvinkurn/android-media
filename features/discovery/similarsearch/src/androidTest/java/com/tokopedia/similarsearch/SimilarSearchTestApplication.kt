package com.tokopedia.similarsearch

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.core.TkpdCoreRouter
import com.tokopedia.core.analytics.container.GTMAnalytics
import com.tokopedia.core.deprecated.SessionHandler
import com.tokopedia.core.gcm.GCMHandler
import com.tokopedia.core.gcm.base.IAppNotificationReceiver
import com.tokopedia.core.gcm.model.NotificationPass
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.data.model.FingerprintModel
import com.tokopedia.track.TrackApp
import okhttp3.Response

class SimilarSearchTestApplication : BaseMainApplication(), NetworkRouter, TkpdCoreRouter {

    override fun onCreate() {
//        FirebaseApp.initializeApp(this)
        GlobalConfig.DEBUG = true

        GraphqlClient.init(this)
        TrackApp.initTrackApp(this)
        TrackApp.getInstance().registerImplementation(TrackApp.GTM, GTMAnalytics::class.java)
//        TrackApp.getInstance().registerImplementation(TrackApp.APPSFLYER, AppsflyerAnalytics::class.java)
//        TrackApp.getInstance().registerImplementation(TrackApp.MOENGAGE, MoengageAnalytics::class.java)
        TrackApp.getInstance().initializeAllApis()

        super.onCreate()
    }

    override fun sendForceLogoutAnalytics(p0: Response?, p1: Boolean, p2: Boolean) {
    }

    override fun onForceLogout(p0: Activity?) {
    }

    override fun showMaintenancePage() {
    }

    override fun showTimezoneErrorSnackbar() {
    }

    override fun getFingerprintModel(): FingerprintModel {
        return FingerprintModel().also {
            it.registrarionId = "test registration id"
            it.fingerprintHash = "test fingerprint hash"
            it.adsId = "test ads id"
        }
    }

    override fun showForceLogoutTokenDialog(p0: String?) {
    }

    override fun logInvalidGrant(p0: Response?) {
    }

    override fun showServerError(p0: Response?) {
    }

    override fun doRelogin(p0: String?) {
    }

    override fun legacyGCMHandler(): GCMHandler {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun refreshFCMTokenFromBackgroundToCM(token: String?, force: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getInboxMessageActivityClass(): Class<*> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun refreshFCMTokenFromForegroundToCM() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getSellerHomeActivityReal(context: Context?): Intent {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getDeeplinkClass(): Class<*> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getActivitySellingTransactionListReal(mContext: Context?): Intent {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun legacySessionHandler(): SessionHandler {
        return object : SessionHandler(applicationContext) {
            override fun getFreshToken(): String {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun getUserId(): String {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun getEmail(): String {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun isUserHasShop(): Boolean {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun getProfilePicture(): String {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun getLoginID(): String {
                return "test login id"
            }

            override fun getPhoneNumber(): String {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun getRefreshToken(): String {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun isHasPassword(): Boolean {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun getShopID(): String {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun isV4Login(): Boolean {
                return false
            }

            override fun isMsisdnVerified(): Boolean {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun getAccessToken(): String {
                return "test access token"
            }

            override fun getDeviceId(): String {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun getGTMLoginID(): String {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun getLoginName(): String {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        }
    }

    override fun getHomeIntent(context: Context?): Intent {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getInboxTalkCallingIntent(mContext: Context?): Intent {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getHomeClass(context: Context?): Class<*> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getActivitySellingTransactionShippingStatusReal(mContext: Context?): Intent {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getInboxResCenterActivityClassReal(): Class<*> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onAppsFlyerInit() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getSellingActivityClassReal(): Class<*> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAppNotificationReceiver(): IAppNotificationReceiver {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getInboxMessageIntent(mContext: Context?): Intent {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setNotificationPass(mContext: Context?, mNotificationPass: NotificationPass?, data: Bundle?, notifTitle: String?): NotificationPass {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun refreshFCMFromInstantIdService(token: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}