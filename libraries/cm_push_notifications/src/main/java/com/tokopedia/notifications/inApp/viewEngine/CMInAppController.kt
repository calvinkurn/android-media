package com.tokopedia.notifications.inApp.viewEngine

import android.content.Context
import android.util.Log
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.notifications.common.CMConstant
import com.tokopedia.notifications.common.IrisAnalyticsEvents
import com.tokopedia.notifications.common.launchCatchError
import com.tokopedia.notifications.inApp.ruleEngine.repository.RepositoryManager
import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata.CMInApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import java.util.*
import kotlin.coroutines.CoroutineContext

class CMInAppController : CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO

    fun downloadImagesAndUpdateDB(context: Context, cmInApp: CMInApp) {
        launchCatchError(
                block = {
                    val updatedCMInApp = ImageDownloadManager.downloadImages(context, cmInApp)
                    if (updatedCMInApp.type == CmInAppConstant.TYPE_DROP) {
                        IrisAnalyticsEvents.sendInAppEvent(context, IrisAnalyticsEvents.INAPP_CANCELLED, updatedCMInApp)
                        return@launchCatchError
                    }
                    putDataToStore(updatedCMInApp)
                }, onError = {
            val messageMap: MutableMap<String, String> = HashMap()
            messageMap["type"] = "exception"
            messageMap["err"] = Log.getStackTraceString(it).take(CMConstant.TimberTags.MAX_LIMIT)
            messageMap["data"] = cmInApp.toString().take(CMConstant.TimberTags.MAX_LIMIT)
            ServerLogger.log(Priority.P2, "CM_VALIDATION", messageMap)
        })

    }

    private fun putDataToStore(inAppData: CMInApp) {
        RepositoryManager.getInstance().storageProvider.putDataToStore(inAppData).subscribe()
    }

}