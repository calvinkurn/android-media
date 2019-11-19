package com.tokopedia.notifications.inApp.viewEngine

import android.content.Context
import android.util.Log
import com.tokopedia.notifications.common.IrisAnalyticsEvents
import com.tokopedia.notifications.common.launchCatchError
import com.tokopedia.notifications.inApp.ruleEngine.repository.RepositoryManager
import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata.CMInApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

class CMInAppController: CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO

    fun downloadImagesAndUpdateDB(context: Context, cmInApp: CMInApp) {
        launchCatchError(
                block = {
                    val updatedCMInApp = ImageDownloadManager.downloadImages(context, cmInApp)
                    if (updatedCMInApp.type == CmInAppConstant.TYPE_DROP) {
                        IrisAnalyticsEvents.sendPushEvent(context, IrisAnalyticsEvents.INAPP_CANCELLED, updatedCMInApp)
                        return@launchCatchError
                    }
                    updatedCMInApp.currentTime = System.currentTimeMillis()
                    putDataToStore(updatedCMInApp)
                }, onError = {
            Log.d("CMInAppController", it.message)
        })

    }

    private fun putDataToStore(inAppData: CMInApp) {
        RepositoryManager.getInstance().storageProvider.putDataToStore(inAppData)
    }

}