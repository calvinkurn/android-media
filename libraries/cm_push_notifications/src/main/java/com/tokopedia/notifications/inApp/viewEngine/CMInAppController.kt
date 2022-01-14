package com.tokopedia.notifications.inApp.viewEngine

import android.app.Application
import android.content.Context
import android.util.Log
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.GsonBuilder
import com.tokopedia.logger.ServerLogger.log
import com.tokopedia.logger.utils.Priority
import com.tokopedia.notifications.common.CMConstant
import com.tokopedia.notifications.common.IrisAnalyticsEvents
import com.tokopedia.notifications.common.launchCatchError
import com.tokopedia.notifications.inApp.ruleEngine.repository.RepositoryManager
import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata.AmplificationCMInApp
import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata.CMInApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.*
import kotlin.coroutines.CoroutineContext

class CMInAppController(
    private val applicationContext: Context,
    private val listenerOnNewInApp: OnNewInAppDataStoreListener
) : CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO


    fun processAndSaveCMInAppRemotePayload(remoteMessage: RemoteMessage) {
        try {
            val cmInApp: CMInApp? = CmInAppBundleConvertor.getCmInApp(remoteMessage)
            cmInApp?.let {
                IrisAnalyticsEvents.trackCmINAppEvent(
                    applicationContext, cmInApp,
                    IrisAnalyticsEvents.INAPP_DELIVERED, null
                )
                downloadImagesAndUpdateDB(applicationContext, cmInApp)
            }
        } catch (e: Exception) {
            val data: Map<String, String> = remoteMessage.data
            val messageMap: MutableMap<String, String> = HashMap()
            messageMap["type"] = "exception"
            messageMap["err"] = Log.getStackTraceString(e).substring(
                0,
                Log.getStackTraceString(e).length.coerceAtMost(CMConstant.TimberTags.MAX_LIMIT)
            )
            messageMap["data"] = data.toString()
                .substring(0, data.toString().length.coerceAtMost(CMConstant.TimberTags.MAX_LIMIT))
            log(Priority.P2, "CM_VALIDATION", messageMap)
        }
    }

    fun processAndSaveCMInAppAmplificationData(dataString: String?) {
        try {
            val gson = GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()
            val amplificationCMInApp = gson.fromJson(dataString, AmplificationCMInApp::class.java)
            val cmInApp = CmInAppBundleConvertor.getCmInApp(amplificationCMInApp)
            cmInApp?.let {
                cmInApp.isAmplification = true
                IrisAnalyticsEvents.sendAmplificationInAppEvent(
                    applicationContext, IrisAnalyticsEvents.INAPP_DELIVERED, cmInApp)
                downloadImagesAndUpdateDB(applicationContext, cmInApp)
            }
        } catch (e: Exception) {
            val messageMap: MutableMap<String, String> = HashMap()
            messageMap["type"] = "exception"
            messageMap["err"] = Log.getStackTraceString(e).substring(
                0,
                Log.getStackTraceString(e).length.coerceAtMost(CMConstant.TimberTags.MAX_LIMIT)
            )
            messageMap["data"] = ""
            log(Priority.P2, "CM_VALIDATION", messageMap)
        }
    }


    private fun downloadImagesAndUpdateDB(context: Context, cmInApp: CMInApp) {
        launchCatchError(
            block = {
                val updatedCMInApp = ImageDownloadManager.downloadImages(context, cmInApp)
                if (updatedCMInApp.type == CmInAppConstant.TYPE_DROP) {
                    IrisAnalyticsEvents.sendInAppEvent(
                        context,
                        IrisAnalyticsEvents.INAPP_CANCELLED,
                        updatedCMInApp
                    )
                    return@launchCatchError
                }
                val isStored = putDataToStore(updatedCMInApp)
                launch(Dispatchers.Main) {
                    if (isStored)
                        listenerOnNewInApp.onNewInAppDataStored()
                }
            }, onError = {
                val messageMap: MutableMap<String, String> = HashMap()
                messageMap["type"] = "exception"
                messageMap["err"] =
                    Log.getStackTraceString(it).take(CMConstant.TimberTags.MAX_LIMIT)
                messageMap["data"] = cmInApp.toString().take(CMConstant.TimberTags.MAX_LIMIT)
                log(Priority.P2, "CM_VALIDATION", messageMap)
            })

    }

    private fun putDataToStore(inAppData: CMInApp): Boolean {
        return RepositoryManager.getInstance().storageProvider.putDataToStore(inAppData)
    }

    interface OnNewInAppDataStoreListener {
        fun onNewInAppDataStored()
    }

}