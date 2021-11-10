package com.tokopedia.notifications.inApp.usecase

import android.app.Application
import android.content.Context
import com.tokopedia.notifications.common.IrisAnalyticsEvents
import com.tokopedia.notifications.inApp.ruleEngine.repository.RepositoryManager
import com.tokopedia.notifications.inApp.ruleEngine.storage.dao.InAppDataDao
import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata.CMInApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SaveInAppUseCase(
    private val applicationContext: Context,
    private val repositoryManager: RepositoryManager
) {

    private lateinit var inAppDataDao: InAppDataDao

    suspend fun saveInApp(cmInApp: CMInApp): Boolean {
        return withContext(Dispatchers.IO) {
            synchronized(this) {
                inAppDataDao = repositoryManager.inAppDataDao ?: return@synchronized false
                saveInAppInDB(cmInApp)
            }
        }
    }

    private fun saveInAppInDB(cmInApp: CMInApp): Boolean {
        return if (canSaveInApp(cmInApp)) {
            inAppDataDao.insert(cmInApp)
            true
        } else {
            false
        }
    }

    private fun canSaveInApp(cmInApp: CMInApp): Boolean {
        return if (testForTestCampaign(cmInApp)) {
            true
        } else if (testInteractionForPerstOff(cmInApp)) {
            if (testScreenWithParentId(cmInApp)) {
                testSameParentId(cmInApp)
            } else {
                false
            }
        } else {
            false
        }
    }

    private fun testForTestCampaign(cmInApp: CMInApp): Boolean {
        return if (cmInApp.isTest) {
            cmInApp.isPersistentToggle = true
            true
        } else {
            false
        }
    }

    private fun testInteractionForPerstOff(cmInApp: CMInApp): Boolean {
        val dataFromParentIDPerstOff: List<CMInApp>? =
            inAppDataDao.getDataFromParentIdForPerstOff(cmInApp.parentId)
        return dataFromParentIDPerstOff.isNullOrEmpty()
    }

    private fun testScreenWithParentId(cmInApp: CMInApp): Boolean {
        deleteInAppIfAlreadyExists(cmInApp.id)

        val newScreenData = cmInApp.screen
        val dataForParentIdAndScreen: CMInApp? =
            inAppDataDao.getDataForParentIdAndScreen(cmInApp.parentId, newScreenData)
        dataForParentIdAndScreen?.let {
            val existingScreenData = dataForParentIdAndScreen.screen
            // if both new inApp and existing inApp have multiple screen names then check
            // if they have any common screen if yes then ignore the new popup
            if (newScreenData.contains(",") && existingScreenData.contains(",")) {
                val newScreenDataList = newScreenData.split(",")
                val existingScreenDataList = existingScreenData.split(",")
                for (screen in existingScreenDataList) {
                    if (newScreenDataList.contains(screen)) {
                        //send event
                        sendIrisEvent(IrisAnalyticsEvents.INAPP_CANCELLED, cmInApp)
                        return false
                    }
                }
                return true
            } else {
                //send event
                sendIrisEvent(IrisAnalyticsEvents.INAPP_CANCELLED, cmInApp)
                return false
            }
        } ?: return true
    }

    private fun testSameParentId(cmInApp: CMInApp): Boolean {
        val cmInAppWithSameParentId: CMInApp? = inAppDataDao.getDataForParentId(cmInApp.parentId)
        cmInAppWithSameParentId?.let {
            return if (cmInAppWithSameParentId.freq != 0) {
                //to keep the frequency consistent with inApp popups having the same parentId
                cmInApp.freq = cmInAppWithSameParentId.freq
                true
            } else {
                false
            }
        } ?: return true
    }

    private fun deleteInAppIfAlreadyExists(newCmInAppId: Long) {
        val oldCMInApp: CMInApp? = inAppDataDao.getInAppData(newCmInAppId)
        oldCMInApp?.let {
            //send event and delete this inApp
            sendIrisEvent(IrisAnalyticsEvents.INAPP_CANCELLED, it)
            inAppDataDao.deleteRecord(it.id)
        }
    }

    private fun sendIrisEvent(event: String, cmInApp: CMInApp) {
        IrisAnalyticsEvents.sendInAppEvent(
            applicationContext,
            event,
            cmInApp
        )
    }

}