package com.tokopedia.notifications.inApp.usecase

import android.app.Application
import com.tokopedia.notifications.common.IrisAnalyticsEvents
import com.tokopedia.notifications.inApp.ruleEngine.repository.RepositoryManager
import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata.CMInApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SaveNewInAppUseCase(
    private val application: Application,
    private val repositoryManager: RepositoryManager
) {

    suspend fun saveInApp(cmInApp: CMInApp): Boolean {
        return withContext(Dispatchers.IO) {
            synchronized(this) {
                saveInAppInDB(cmInApp)
            }
        }
    }

    private fun saveInAppInDB(newCMInApp: CMInApp): Boolean {
        val inAppDataDao = repositoryManager.inAppDataDao ?: return false

        //if inApp is from test campaign then no need to check for persistence
        if (newCMInApp.isTest) {
            newCMInApp.isPersistentToggle = true
            inAppDataDao.insert(newCMInApp)
            return true
        }
        val dataFromParentIDPerstOff: List<CMInApp>? =
            inAppDataDao.getDataFromParentIdForPerstOff(newCMInApp.parentId)

        if (dataFromParentIDPerstOff.isNullOrEmpty()) {

            checkIfInAppAlreadyExists(newCMInApp.id)

            val newScreenData = newCMInApp.screen
            val dataForParentIdAndScreen: CMInApp? =
                inAppDataDao.getDataForParentIdAndScreen(newCMInApp.parentId, newScreenData)
            dataForParentIdAndScreen?.let {
                val existingScreenData = dataForParentIdAndScreen.screen
                // if both new inApp and existing inApp have multiple screen names then check
                // if they have any common screen if yes then ignore the new popup
                if (newScreenData.contains(",") && existingScreenData.contains(",")) {
                    val newScreenDataList = newScreenData.split(",")
                    val existingScreenDataList = existingScreenData.split(",")
                    for (screen in existingScreenDataList) {
                        if (newScreenDataList.contains(screen)) {
                            sendIrisEvent(IrisAnalyticsEvents.INAPP_CANCELLED, newCMInApp)
                            return false
                        }
                    }
                } else {
                    sendIrisEvent(IrisAnalyticsEvents.INAPP_CANCELLED, newCMInApp)
                    return false
                }
            }
            val dataForParentId: CMInApp? = inAppDataDao.getDataForParentId(newCMInApp.parentId)
            dataForParentId?.let {
                if (dataForParentId.freq != 0) {
                    //to keep the frequency consistent with inApp popups having the same parentId
                    newCMInApp.freq = dataForParentId.freq
                    inAppDataDao.insert(newCMInApp)
                    return true
                }
            } ?: let {
                inAppDataDao.insert(newCMInApp)
                return true
            }
        }
        return false
    }

    private fun checkIfInAppAlreadyExists(newCMInAppId: Long) {
        val oldCMInApp: CMInApp? = repositoryManager.inAppDataDao?.getInAppData(newCMInAppId)
        oldCMInApp?.let {
            //send event and delete this inApp
            sendIrisEvent(IrisAnalyticsEvents.INAPP_CANCELLED, it)
            repositoryManager.inAppDataDao?.deleteRecord(it.id)
        }
    }

    private fun sendIrisEvent(event: String, cmInApp: CMInApp) {
        IrisAnalyticsEvents.sendInAppEvent(
            application.applicationContext,
            event,
            cmInApp
        )
    }

}