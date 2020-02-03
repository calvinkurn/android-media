package com.tokopedia.sellerhomedrawer.di

class SellerDrawerInjector {
//
//    private val BEARER_TOKEN = "Bearer "
//
//    fun getDrawerHelper(activity: AppCompatActivity,
//                        sessionHandler: SessionHandler,
//                        drawerCache: LocalCacheHandler): DrawerHelper {
//
//        return (activity.application as TkpdCoreRouter).getDrawer(activity,
//                sessionHandler, drawerCache, GlobalCacheManager())
//    }
//
//    fun getDrawerDataManager(context: Context,
//                             drawerDataListener: DrawerDataListener,
//                             drawerCache: LocalCacheHandler): DrawerDataManager {
//
//        val jobExecutor = JobExecutor()
//        val uiThread = UIThread()
//
////        val networkRouter = context.applicationContext as NetworkRouter
//        val userSession = UserSession(context)
//
//        val userAttributesRepository = UserAttributesRepositoryImpl(
//                UserAttributesFactory(DrawerService(context, userSession, networkRouter), drawerCache)
//        )
//
//        val getUserAttributesUseCase = GetUserAttributesUseCase(jobExecutor,
//                UIThread(), userAttributesRepository)
//
//        val getSellerrAttributesUseCase = GetSellerUserAttributesUseCase(jobExecutor,
//                UIThread(), userAttributesRepository)
//
//        val tokoCashModelObservable = Observable.just(TokoCashData())
//        val tokoCashUseCase = TokoCashUseCase(
//                jobExecutor,
//                uiThread,
//                tokoCashModelObservable
//        )
//
//        return DrawerDataManagerImpl(
//                drawerDataListener,
//                tokoCashUseCase,
//                getUserAttributesUseCase,
//                getSellerrAttributesUseCase)
//    }
}