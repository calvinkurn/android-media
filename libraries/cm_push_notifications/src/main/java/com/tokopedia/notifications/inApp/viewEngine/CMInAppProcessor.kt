package com.tokopedia.notifications.inApp.viewEngine

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.notifications.common.CMConstant
import com.tokopedia.notifications.common.IrisAnalyticsEvents
import com.tokopedia.notifications.common.launchCatchError
import com.tokopedia.notifications.inApp.ruleEngine.repository.RepositoryManager
import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata.CMInApp
import com.tokopedia.notifications.inApp.usecase.InAppLocalDatabaseController
import com.tokopedia.notifications.inApp.usecase.InAppSaveListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import java.util.*
import kotlin.coroutines.CoroutineContext

class CMInAppProcessor(
    private val applicationContext: Context,
    private val inAppSaveListener: InAppSaveListener
) : CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO

    fun processAndSaveCMInApp(cmInApp: CMInApp) {
        try {
            sendInAppReceivedEvent(cmInApp)
            downloadImagesAndUpdateDB(cmInApp)
        } catch (e: Exception) {
            val data = Gson().toJson(cmInApp)
            val messageMap: MutableMap<String, String> = HashMap()
            messageMap["type"] = "exception"
            messageMap["err"] = Log.getStackTraceString(e).substring(
                0,
                Log.getStackTraceString(e).length.coerceAtMost(CMConstant.TimberTags.MAX_LIMIT)
            )
            messageMap["data"] = data.toString()
                .substring(0, data.toString().length.coerceAtMost(CMConstant.TimberTags.MAX_LIMIT))
            ServerLogger.log(Priority.P2, "CM_VALIDATION", messageMap)
        }
    }

    private fun sendInAppReceivedEvent(cmInApp: CMInApp) {
        if (cmInApp.isAmplification)
            IrisAnalyticsEvents.sendAmplificationInAppEvent(
                applicationContext, IrisAnalyticsEvents.INAPP_DELIVERED, cmInApp)
        else
            IrisAnalyticsEvents.trackCmINAppEvent(
                applicationContext, cmInApp,
                IrisAnalyticsEvents.INAPP_DELIVERED, null)
    }

    private fun downloadImagesAndUpdateDB(cmInApp: CMInApp) {
        launchCatchError(
            block = {
                val processedCMInApp =
                    ImageDownloadManager.downloadImages(applicationContext, cmInApp)
                if (processedCMInApp.type == CmInAppConstant.TYPE_DROP) {
                    IrisAnalyticsEvents.sendInAppEvent(
                        applicationContext,
                        IrisAnalyticsEvents.INAPP_CANCELLED,
                        processedCMInApp
                    )
                    return@launchCatchError
                }
                saveInApp(processedCMInApp, inAppSaveListener)
            }, onError = {
                val messageMap: MutableMap<String, String> = HashMap()
                messageMap["type"] = "exception"
                messageMap["err"] =
                    Log.getStackTraceString(it).take(CMConstant.TimberTags.MAX_LIMIT)
                messageMap["data"] = cmInApp.toString().take(CMConstant.TimberTags.MAX_LIMIT)
                ServerLogger.log(Priority.P2, "CM_VALIDATION", messageMap)
            })
    }

    private fun saveInApp(processedCMInApp: CMInApp, inAppSaveListener: InAppSaveListener) {
        InAppLocalDatabaseController.getInstance(
            applicationContext,
            RepositoryManager.getInstance()
        ).saveInApp(processedCMInApp, inAppSaveListener)
    }
}