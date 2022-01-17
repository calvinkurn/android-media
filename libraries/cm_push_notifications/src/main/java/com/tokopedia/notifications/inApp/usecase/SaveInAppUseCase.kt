package com.tokopedia.notifications.inApp.usecase

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
        return when {
            isTestCampaign(cmInApp) -> {
                //Test campaign will always be saved
                true
            }
            isAlreadyInteractedWithCampaign(cmInApp.parentId) ->{
                //old campaign present with same parent id and user already interacted with
                //so dropping... ### There is no event to track this drop
                false
            }
            else -> {
                //Delete old campaign if new campaign is having same id (aka notification ID)
                deleteOldInAppIfAlreadyExists(cmInApp.id)
                //check for old campaign with same parent id and same screen name
                return if(isCampaignScreenAlreadyPresent(cmInApp)){
                    //campaign already present with same screen name and parent id so dropping...
                    sendCancelledIrisEvent(cmInApp)
                    false
                }else{
                    //update frequency of new campaign as old campaign with same parent id
                    updateFrequencyAsOldCampaign(cmInApp)
                    //All above checks passed and we can save notification
                    true
                }
            }
        }
    }

    private fun isTestCampaign(cmInApp: CMInApp): Boolean {
        return if (cmInApp.isTest) {
            cmInApp.isPersistentToggle = true
            true
        } else {
            false
        }
    }

    private fun isCampaignScreenAlreadyPresent(cmInApp: CMInApp) : Boolean{
        val newCampaignTargetScreens = cmInApp.screen
        val existingCampaignWithSameParentId : CMInApp = inAppDataDao
            .getDataForParentIdAndScreen(cmInApp.parentId, newCampaignTargetScreens) ?: return false
        val existingCampaignTargetScreens = existingCampaignWithSameParentId.screen
        if(existingCampaignTargetScreens.contains(",")
            && newCampaignTargetScreens.contains(",")){
            val newTargetScreenList = newCampaignTargetScreens.split(",")
            val existingScreenList = existingCampaignTargetScreens.split(",")
            for (screen in existingScreenList) {
                if (newTargetScreenList.contains(screen)) {
                    return true
                }
            }
            return false
        }else {
            return true
        }
    }

    private fun isAlreadyInteractedWithCampaign(parentId: String) : Boolean{
        val data: List<CMInApp>? =
            inAppDataDao.getDataFromParentIdForPerstOff(parentId)
        return data?.isNotEmpty() ?: false
    }

    private fun updateFrequencyAsOldCampaign(cmInApp: CMInApp): Boolean {
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

    private fun deleteOldInAppIfAlreadyExists(newCmInAppId: Long) {
        val oldCMInApp: CMInApp? = inAppDataDao.getInAppData(newCmInAppId)
        oldCMInApp?.let {
            //send event and delete old inApp campaign
            sendCancelledIrisEvent(it)
            inAppDataDao.deleteRecord(it.id)
        }
    }

    private fun sendCancelledIrisEvent(cmInApp: CMInApp) {
        IrisAnalyticsEvents.sendInAppEvent(
            applicationContext,
            IrisAnalyticsEvents.INAPP_CANCELLED,
            cmInApp
        )
    }

}