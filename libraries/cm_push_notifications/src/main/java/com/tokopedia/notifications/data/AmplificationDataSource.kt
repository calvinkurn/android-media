package com.tokopedia.notifications.data

import android.app.Application
import com.google.gson.Gson
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.notifications.PushController
import com.tokopedia.notifications.R
import com.tokopedia.notifications.common.IrisAnalyticsEvents.INAPP_DELIVERED
import com.tokopedia.notifications.data.model.Amplification
import com.tokopedia.notifications.data.model.AmplificationNotifier
import com.tokopedia.notifications.domain.AmplificationUseCase
import com.tokopedia.notifications.inApp.ruleEngine.repository.RepositoryManager
import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata.CMInApp
import com.tokopedia.user.session.UserSession
import com.tokopedia.abstraction.common.utils.GraphqlHelper.loadRawString as loadRaw
import com.tokopedia.notifications.common.IrisAnalyticsEvents.sendAmplificationInAppEvent as sendAmplificationInAppEvent

object AmplificationDataSource {

    private val useCase by lazy {
        GraphqlUseCase<AmplificationNotifier>(
                GraphqlInteractor.getInstance().graphqlRepository
        )
    }

    @JvmStatic fun invoke(application: Application) {
        val userSession = UserSession(application)
        if (!userSession.isLoggedIn) return

        val query = loadRaw(application.resources, R.raw.query_notification_amplification)
        val amplificationUseCase = AmplificationUseCase(useCase, query)
        RepositoryManager.initRepository(application)

        amplificationUseCase.execute {
            val webHook = it.webhookAttributionNotifier
            pushData(application, webHook)
            inAppData(application, webHook)
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
                    val cmInApp = Gson().fromJson(it, CMInApp::class.java)

                    // flag if this data comes from amplification fetch API
                    cmInApp.isAmplification = true

                    // delivered
                    RepositoryManager
                            .getInstance()
                            .storageProvider
                            .putDataToStore(cmInApp)
                            .subscribe()
                    sendAmplificationInAppEvent(application, INAPP_DELIVERED, cmInApp)
                } catch (e: Exception) {}
            }
        }
    }

}