package com.tokopedia.notifications.inApp.viewEngine

import android.app.Application
import android.util.Log
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.notifications.common.CMConstant
import com.tokopedia.notifications.common.IrisAnalyticsEvents
import com.tokopedia.notifications.common.launchCatchError
import com.tokopedia.notifications.inApp.ruleEngine.repository.RepositoryManager
import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata.CMInApp
import com.tokopedia.notifications.inApp.usecase.InAppLocalDatabaseController
import com.tokopedia.notifications.inApp.usecase.NewInAppSavedListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import java.util.*
import kotlin.coroutines.CoroutineContext

class CMInAppProcessor(
    private val application: Application
) : CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO

    fun processAndSaveCMInApp(cmInApp: CMInApp, newInAppSavedListener: NewInAppSavedListener) {
        launchCatchError(
            block = {
                val processedCMInApp =
                    ImageDownloadManager.downloadImages(application.applicationContext, cmInApp)
                if (processedCMInApp.type == CmInAppConstant.TYPE_DROP) {
                    IrisAnalyticsEvents.sendInAppEvent(
                        application.applicationContext,
                        IrisAnalyticsEvents.INAPP_CANCELLED,
                        processedCMInApp
                    )
                    return@launchCatchError
                }
                saveInApp(processedCMInApp, newInAppSavedListener)
            }, onError = {
                val messageMap: MutableMap<String, String> = HashMap()
                messageMap["type"] = "exception"
                messageMap["err"] =
                    Log.getStackTraceString(it).take(CMConstant.TimberTags.MAX_LIMIT)
                messageMap["data"] = cmInApp.toString().take(CMConstant.TimberTags.MAX_LIMIT)
                ServerLogger.log(Priority.P2, "CM_VALIDATION", messageMap)
            })

    }

    private fun saveInApp(processedCMInApp: CMInApp, newInAppSavedListener: NewInAppSavedListener) {
        InAppLocalDatabaseController.getInstance(
            application,
            RepositoryManager.getInstance()
        ).saveNewInApp(processedCMInApp, newInAppSavedListener)
    }
}