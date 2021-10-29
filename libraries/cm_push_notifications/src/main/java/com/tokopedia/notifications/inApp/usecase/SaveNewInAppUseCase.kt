package com.tokopedia.notifications.inApp.usecase

import android.app.Application
import com.tokopedia.notifications.common.IrisAnalyticsEvents
import com.tokopedia.notifications.inApp.ruleEngine.repository.RepositoryManager
import com.tokopedia.notifications.inApp.ruleEngine.storage.dao.InAppDataDao
import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata.CMInApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SaveNewInAppUseCase(
    private val application: Application,
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
        return verifyInApp(SaveInAppVerificationTest.TEST_FOR_TEST_CAMPAIGN, cmInApp)
    }

    private fun verifyInApp(
        verificationTest: SaveInAppVerificationTest,
        cmInApp: CMInApp
    ): Boolean {
        return when (verificationTest) {
            SaveInAppVerificationTest.TEST_FOR_TEST_CAMPAIGN -> {
                if (testForTestCampaign(cmInApp)) {
                    true
                } else {
                    verifyInApp(SaveInAppVerificationTest.TEST_PERSISTENT_OFF, cmInApp)
                }
            }
            SaveInAppVerificationTest.TEST_PERSISTENT_OFF -> {
                if (testPersistentOff(cmInApp)) {
                    verifyInApp(SaveInAppVerificationTest.TEST_PARENT_ID_WITH_SCREEN, cmInApp)
                } else {
                    false
                }
            }
            SaveInAppVerificationTest.TEST_PARENT_ID_WITH_SCREEN -> {
                if (testParentIdWithScreen(cmInApp)) {
                    verifyInApp(SaveInAppVerificationTest.TEST_PARENT_ID, cmInApp)
                } else {
                    false
                }
            }
            SaveInAppVerificationTest.TEST_PARENT_ID -> {
                testParentId(cmInApp)
            }
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

    private fun testPersistentOff(cmInApp: CMInApp): Boolean {
        val dataFromParentIDPerstOff: List<CMInApp>? =
            inAppDataDao.getDataFromParentIdForPerstOff(cmInApp.parentId)
        return dataFromParentIDPerstOff.isNullOrEmpty()
    }

    private fun testParentIdWithScreen(cmInApp: CMInApp): Boolean {
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

    private fun testParentId(cmInApp: CMInApp): Boolean {
        val dataForParentId: CMInApp? = inAppDataDao.getDataForParentId(cmInApp.parentId)
        dataForParentId?.let {
            return if (dataForParentId.freq != 0) {
                //to keep the frequency consistent with inApp popups having the same parentId
                cmInApp.freq = dataForParentId.freq
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

    enum class SaveInAppVerificationTest {
        TEST_FOR_TEST_CAMPAIGN {
            override fun getStepNumber(): Int {
                return 1
            }

        },
        TEST_PERSISTENT_OFF {
            override fun getStepNumber(): Int {
                return 2
            }

        },
        TEST_PARENT_ID_WITH_SCREEN {
            override fun getStepNumber(): Int {
                return 3
            }

        },
        TEST_PARENT_ID {
            override fun getStepNumber(): Int {
                return 4
            }
        };

        abstract fun getStepNumber(): Int
    }

}