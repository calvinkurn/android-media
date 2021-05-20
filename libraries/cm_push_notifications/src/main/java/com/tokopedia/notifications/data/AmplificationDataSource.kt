package com.tokopedia.notifications.data

import android.app.Application
import android.util.Log
import com.google.gson.GsonBuilder
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.notifications.PushController
import com.tokopedia.notifications.R
import com.tokopedia.notifications.common.CMConstant
import com.tokopedia.notifications.common.IrisAnalyticsEvents.INAPP_DELIVERED
import com.tokopedia.notifications.common.IrisAnalyticsEvents.sendAmplificationInAppEvent
import com.tokopedia.notifications.data.model.Amplification
import com.tokopedia.notifications.data.model.AmplificationNotifier
import com.tokopedia.notifications.domain.AmplificationUseCase
import com.tokopedia.notifications.inApp.ruleEngine.repository.RepositoryManager
import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata.AmplificationCMInApp
import com.tokopedia.notifications.inApp.viewEngine.CmInAppBundleConvertor
import com.tokopedia.notifications.utils.NextFetchCacheManager
import com.tokopedia.user.session.UserSession
import java.util.concurrent.TimeUnit
import com.tokopedia.abstraction.common.utils.GraphqlHelper.loadRawString as loadRaw

object AmplificationDataSource {

    private val useCase by lazy {
        GraphqlUseCase<AmplificationNotifier>(
                GraphqlInteractor.getInstance().graphqlRepository
        )
    }

    @JvmStatic fun invoke(application: Application) {
        val cacheManager = NextFetchCacheManager(application)
        val currentTime = System.currentTimeMillis()
        val userSession = UserSession(application)

        /*
        * preventing amplification data request
        * if user haven't login yet
        * */
        if (!userSession.isLoggedIn) return

        /*
        * preventing multiple fetching of amplification data
        * check based-on `next_fetch` from payload
        * */
        if (currentTime <= cacheManager.getNextFetch()) {
            return
        }

        val query = loadRaw(application.resources, R.raw.query_notification_amplification)
        val amplificationUseCase = AmplificationUseCase(useCase, query)
        RepositoryManager.initRepository(application)

        amplificationUseCase.execute {

            val webHook = it.webhookAttributionNotifier
            pushData(application, webHook)
            inAppData(application, webHook)

            // save `next_fetch` time data
            val nextFetchTime = webHook.nextFetch
            cacheManager.saveNextFetch(nextFetch(nextFetchTime))
        }
    }

    private fun pushData(application: Application, amplification: Amplification) {
        if (amplification.pushData.isNotEmpty()) {
            amplification.pushData.forEach {
                PushController(application).handleNotificationAmplification(it)
            }
        }
    }

    private fun inAppData(application: Application, amplification: Amplification) {
        if (amplification.inAppData.isNotEmpty()) {
            amplification.inAppData.forEach {
                try {
                    val gson = GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()
                    val amplificationCMInApp: AmplificationCMInApp = gson.fromJson(it, AmplificationCMInApp::class.java)

                    val cmInApp = CmInAppBundleConvertor.getCmInApp(amplificationCMInApp)
                    // flag if this data comes from amplification fetch API
                    amplificationCMInApp.isAmplification = true

                    // storage to local storage
                    RepositoryManager
                            .getInstance()
                            .storageProvider
                            .putDataToStore(cmInApp)
                            .subscribe()

                    // send amplification tracker
                    sendAmplificationInAppEvent(application, INAPP_DELIVERED, cmInApp)
                } catch (e: Exception) {
                    ServerLogger.log(Priority.P2, "CM_VALIDATION",
                            mapOf("type" to "exception",
                                    "err" to Log.getStackTraceString(e)
                                            .take(CMConstant.TimberTags.MAX_LIMIT),
                                    "data" to ""))
                }
            }
        }
    }

    private fun nextFetch(time: Long): Long {
        val currentTime = System.currentTimeMillis()
        val secondToMilis = TimeUnit.SECONDS.toMillis(time)
        return currentTime + secondToMilis
    }

}