package com.tokopedia.promotionstarget.presentation

import android.app.Application
import com.tokopedia.notifications.inApp.CMInAppManager
import com.tokopedia.notifications.inApp.CmEventListenerManager
import com.tokopedia.promotionstarget.cmGratification.broadcast.BroadcastHandler
import com.tokopedia.promotionstarget.cmGratification.broadcast.BroadcastScreenNamesProvider
import com.tokopedia.promotionstarget.cmGratification.broadcast.PendingData
import com.tokopedia.promotionstarget.cmGratification.dialog.GratificationDialogHandler
import com.tokopedia.promotionstarget.cmGratification.lifecycle.ActivityProviderImpl
import com.tokopedia.promotionstarget.cmGratification.lifecycle.CmActivityLifecycleCallbacks
import com.tokopedia.promotionstarget.cmGratification.lifecycle.GratifFragmentLifeCycleCallback
import com.tokopedia.promotionstarget.cmGratification.pushIntent.GratifCmPushHandler
import com.tokopedia.promotionstarget.domain.presenter.GratificationPresenter
import kotlinx.coroutines.Job
import java.util.concurrent.ConcurrentHashMap

object GratifCmInitializer {

    fun start(appContext: Application) {
        val activityProvider = ActivityProviderImpl()
        appContext.registerActivityLifecycleCallbacks(activityProvider)

        val mapOfGratifJobs = ConcurrentHashMap<Int, Job>()
        val mapOfPendingInApp = ConcurrentHashMap<Int, PendingData>()

        val gratificationPresenter = GratificationPresenter(appContext)
        gratificationPresenter.dialogVisibilityContract = CMInAppManager.getInstance()
        val broadcastScreenNamesProvider = BroadcastScreenNamesProvider()


        val dialogHandler = GratificationDialogHandler(gratificationPresenter, mapOfGratifJobs, mapOfPendingInApp, broadcastScreenNamesProvider.screenNames(), activityProvider)
        val pushHandler = GratifCmPushHandler(dialogHandler)
        val broadCastHandler = BroadcastHandler(dialogHandler)

        val cmActivityLifecycleCallbacks = CmActivityLifecycleCallbacks(appContext, broadCastHandler, broadcastScreenNamesProvider, mapOfGratifJobs)
        val fragmentLifecycleCallback = GratifFragmentLifeCycleCallback(cmActivityLifecycleCallbacks)

        appContext.registerActivityLifecycleCallbacks(cmActivityLifecycleCallbacks)

        CmEventListenerManager.register(pushHandler, fragmentLifecycleCallback, arrayListOf("gratification"), dialogHandler)
    }
}