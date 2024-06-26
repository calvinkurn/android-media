package com.tokopedia.promotionstarget.presentation

import android.app.Application
import com.tokopedia.notifications.inApp.CMInAppManager
import com.tokopedia.notifications.inApp.external.CmEventListenerManager
import com.tokopedia.promotionstarget.cm.broadcast.PendingData
import com.tokopedia.promotionstarget.cm.dialog.GratificationDialogHandler
import com.tokopedia.promotionstarget.cm.lifecycle.ActivityProviderImpl
import com.tokopedia.promotionstarget.cm.lifecycle.CmActivityLifecycleCallbacks
import com.tokopedia.promotionstarget.cm.lifecycle.GratifFragmentLifeCycleCallback
import com.tokopedia.promotionstarget.cm.pushIntent.GratifCmPushHandler
import com.tokopedia.promotionstarget.domain.presenter.GratificationPresenter
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import kotlinx.coroutines.Job
import timber.log.Timber
import java.util.concurrent.ConcurrentHashMap

object GratifCmInitializer {

    fun start(appContext: Application) {
        try {

            if(CMInAppManager.getInstance() == null || CMInAppManager.getInstance().externalInAppCallback == null) return

            val activityProvider = ActivityProviderImpl()
            appContext.registerActivityLifecycleCallbacks(activityProvider)

            val mapOfGratifJobs = ConcurrentHashMap<Int, Job>()
            val mapOfPendingInApp = ConcurrentHashMap<Int, PendingData>()

            val gratificationPresenter = GratificationPresenter(appContext)
            val externalCallback = CMInAppManager.getInstance().externalInAppCallback
            gratificationPresenter.iExternalInAppCallback = externalCallback

            val firebaseRemoteConfig: FirebaseRemoteConfigImpl? = try {
                FirebaseRemoteConfigImpl(appContext)
            } catch (ex: Exception) {
                Timber.e(ex)
                null
            }
            val dialogHandler = GratificationDialogHandler(gratificationPresenter,
                    mapOfGratifJobs,
                    mapOfPendingInApp,
                    arrayListOf(),
                    activityProvider,
                    firebaseRemoteConfig, externalCallback)
            val pushHandler = GratifCmPushHandler(dialogHandler)

            val cmActivityLifecycleCallbacks = CmActivityLifecycleCallbacks(appContext, null, null, mapOfGratifJobs)
            val fragmentLifecycleCallback = GratifFragmentLifeCycleCallback(cmActivityLifecycleCallbacks)

            appContext.registerActivityLifecycleCallbacks(cmActivityLifecycleCallbacks)

            CmEventListenerManager.register(pushHandler, fragmentLifecycleCallback, arrayListOf("gratification"), dialogHandler)
        }catch (t:Throwable){
            Timber.e(t)
        }
    }
}