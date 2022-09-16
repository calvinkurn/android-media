package com.tokopedia.notifications.data

import android.app.Application
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.notifications.CMPushNotificationManager
import com.tokopedia.notifications.R
import com.tokopedia.notifications.data.model.Amplification
import com.tokopedia.notifications.data.model.AmplificationNotifier
import com.tokopedia.notifications.domain.AmplificationUseCase
import com.tokopedia.notifications.inApp.ruleEngine.repository.RepositoryManager
import com.tokopedia.notifications.utils.NextFetchCacheManager
import com.tokopedia.user.session.UserSession
import java.util.concurrent.TimeUnit
import com.tokopedia.abstraction.common.utils.GraphqlHelper.loadRawString as loadRaw

object AmplificationDataSource {

    var isRunning = false

    private val useCase by lazy {
        GraphqlUseCase<AmplificationNotifier>(
            GraphqlInteractor.getInstance().graphqlRepository
        )
    }


    @JvmStatic
    fun invoke(application: Application) {
        if(isRunning)
            return
        isRunning = true
        val cacheManager = NextFetchCacheManager(application)
        val currentTime = System.currentTimeMillis()
        val userSession = UserSession(application)

        /*
        * preventing amplification data request
        * if user haven't login yet
        * */
        if (!userSession.isLoggedIn) {
            isRunning = false
            return
        }

        /*
        * preventing multiple fetching of amplification data
        * check based-on `next_fetch` from payload
        * */
        if (currentTime <= cacheManager.getNextFetch()) {
            isRunning = false
            return
        }

        val query = loadRaw(application.resources, R.raw.query_notification_amplification)
        val amplificationUseCase = AmplificationUseCase(useCase, query)
        RepositoryManager.initRepository(application)

        amplificationUseCase.execute(
                {
                    val webHook = it.webhookAttributionNotifier
                    pushData(application, webHook)
                    inAppData(webHook)

                    // save `next_fetch` time data
                    val nextFetchTime = webHook.nextFetch
                    cacheManager.saveNextFetch(nextFetch(nextFetchTime))
                    isRunning = false
                },{
                    isRunning = false
        })
    }

    private fun pushData(application: Application, amplification: Amplification) {
        if (amplification.pushData.isNotEmpty()) {
            amplification.pushData.forEach {
                CMPushNotificationManager.instance.handleNotificationJsonPayload(it, true)
            }
        }
    }

    private fun inAppData(amplification: Amplification) {
        if (amplification.inAppData.isNotEmpty()) {
            amplification.inAppData.forEach {
                CMPushNotificationManager.instance.handleInAppJsonPayload(it, true)
            }
        }
    }

    private fun nextFetch(time: Long): Long {
        val currentTime = System.currentTimeMillis()
        val secondToMilis = TimeUnit.SECONDS.toMillis(time)
        return currentTime + secondToMilis
    }

}