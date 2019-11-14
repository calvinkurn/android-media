package com.tokopedia.similarsearch

class MockApplication
//    : BaseMainApplication(), TkpdCoreRouter, NetworkRouter {
//    override fun getDeeplinkClass(): Class<*> {
//        return MockApplication::class.java
//    }
//
//    override fun getSellerHomeActivityReal(context: Context?): Intent {
//        return Intent()
//    }
//
//    override fun getInboxTalkCallingIntent(mContext: Context?): Intent {
//        return Intent()
//    }
//
//    override fun getAppNotificationReceiver(): IAppNotificationReceiver {
//        TODO("Not implemented")
//    }
//
//    override fun getInboxMessageActivityClass(): Class<*> {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    override fun getInboxResCenterActivityClassReal(): Class<*> {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    override fun getActivitySellingTransactionShippingStatusReal(mContext: Context?): Intent {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    override fun getSellingActivityClassReal(): Class<*> {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    override fun getActivitySellingTransactionListReal(mContext: Context?): Intent {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    override fun getHomeIntent(context: Context?): Intent {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    override fun getHomeClass(context: Context?): Class<*> {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    override fun setNotificationPass(mContext: Context?, mNotificationPass: NotificationPass?, data: Bundle?, notifTitle: String?): NotificationPass {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    override fun getInboxMessageIntent(mContext: Context?): Intent {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    override fun onAppsFlyerInit() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    override fun legacySessionHandler(): SessionHandler {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    override fun legacyGCMHandler(): GCMHandler {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    override fun refreshFCMTokenFromBackgroundToCM(token: String?, force: Boolean) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    override fun refreshFCMFromInstantIdService(token: String?) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    override fun refreshFCMTokenFromForegroundToCM() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    override fun onCreate() {
//        FirebaseApp.initializeApp(this)
//        GraphqlClient.init(this)
//        TrackApp.initTrackApp(this)
//        TrackApp.getInstance().registerImplementation(TrackApp.GTM, GTMAnalytics::class.java)
////        TrackApp.getInstance().registerImplementation(TrackApp.APPSFLYER, AppsflyerAnalytics::class.java)
////        TrackApp.getInstance().registerImplementation(TrackApp.MOENGAGE, MoengageAnalytics::class.java)
//        TrackApp.getInstance().initializeAllApis()
//
//        super.onCreate()
//    }
//}