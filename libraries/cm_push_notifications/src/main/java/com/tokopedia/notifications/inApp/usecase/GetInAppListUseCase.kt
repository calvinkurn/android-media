package com.tokopedia.notifications.inApp.usecase

import android.app.Application
import android.text.TextUtils
import com.tokopedia.notifications.common.CMConstant
import com.tokopedia.notifications.common.IrisAnalyticsEvents
import com.tokopedia.notifications.common.IrisAnalyticsEvents.sendInAppEvent
import com.tokopedia.notifications.inApp.ruleEngine.repository.RepositoryManager
import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata.CMInApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class GetInAppListUseCase(private val application: Application,
                          private val repositoryManager: RepositoryManager) {

    private val HOURS_24_IN_MILLIS: Long = 24 * 60 * 60 * 1000L

    suspend fun getInAPPListByScreenName(screenName: String, isActivity: Boolean): List<CMInApp>? {
        return withContext(Dispatchers.IO) {
            synchronized(this) {
                getInAPPData(screenName, isActivity)
            }
        }
    }

    private fun getInAPPData(screenName: String, isActivity: Boolean): List<CMInApp>? {
        synchronized(this) {
            val inAppDataList = getDataFromStore(screenName, isActivity)
            if (inAppDataList != null) {
                deleteCompletedInAPPOnlyIfExpired(inAppDataList)
                deleteExpiredInAPP(inAppDataList)
                return inAppDataList.filter {
                    isInActiveTimeFrame(it)
                            && it.freq > 0
                            && !it.isShown
                }
            }
            return null
        }
    }

    private fun deleteCompletedInAPPOnlyIfExpired(inAppList : List<CMInApp>){
        inAppList.filter {
            !it.isShown()
        }.forEach {
            if(it.freq == 0){
                if(it.endTime == 0L || it.endTime < System.currentTimeMillis())
                    RepositoryManager.getInstance().inAppDataDao?.deleteRecord(it.id)
            }
        }
    }

    private fun deleteExpiredInAPP(inAppList : List<CMInApp>){
        inAppList.filter {
            !it.isShown()
        }.forEach {
            if(it.endTime != 0L && it.endTime < System.currentTimeMillis()){
                if(it.freq > 0 && it.lastShownTime == 0L){
                    sendInAppEvent(application.applicationContext,
                            IrisAnalyticsEvents.INAPP_EXPIRED, it)
                }
                RepositoryManager.getInstance().inAppDataDao?.deleteRecord(it.id)
            }
        }
    }

    /*
    We can show test inApp in Case 24Hr Master frequency exhausted
    */
    private fun getDataFromStore(key: String, isActivity: Boolean): List<CMInApp>? {
        val inAppDataDao = repositoryManager.inAppDataDao ?: return null

        val list: List<CMInApp>? = if (isMasterFrequencyAvailable()) {
            inAppDataDao.getDataForScreen(key)
        } else
            inAppDataDao.getDataForScreenTestCampaign(key)

        val finalList: MutableList<CMInApp> = ArrayList()
        list?.forEach { cmInApp ->
            val screenNames = cmInApp.getScreen()
            if (!TextUtils.isEmpty(screenNames)) {
                val screenNamesArray = screenNames.split(",").toTypedArray()
                for (screenName in screenNamesArray) {
                    if (key == screenName || (isActivity && screenName == "*")) {
                        finalList.add(cmInApp)
                        break
                    }
                }
            }
        }
        return finalList
    }

    private fun isMasterFrequencyAvailable(): Boolean {
        val cacheHandler = repositoryManager.cacheHandler
        val cmRemoteConfigUtils = repositoryManager.cmRemoteConfigUtils
        if (cacheHandler == null || cmRemoteConfigUtils == null)
            return true
        val currentTimeMillis = System.currentTimeMillis()
        val nextInappDisplayTime: Long = cacheHandler.getLongValue(CMConstant.NEXT_INAPP_DISPLAY_TIME)
        val inappDisplayCounter: Long = cacheHandler.getLongValue(CMConstant.INAPP_DISPLAY_COUNTER)
        val maxInappDisplayCount: Long = cmRemoteConfigUtils.getLongRemoteConfig(CMConstant.MAX_INAPP_DISPLAY_COUNT, 1)
        val isMaxReached = inappDisplayCounter == maxInappDisplayCount
        return if (nextInappDisplayTime == 0L) {
            cacheHandler.saveLongValue(CMConstant.NEXT_INAPP_DISPLAY_TIME, currentTimeMillis + HOURS_24_IN_MILLIS)
            true
        } else {
            if (nextInappDisplayTime > currentTimeMillis && isMaxReached) {
                false
            } else if (nextInappDisplayTime > currentTimeMillis && !isMaxReached) {
                true
            } else {
                cacheHandler.saveLongValue(CMConstant.NEXT_INAPP_DISPLAY_TIME, currentTimeMillis + HOURS_24_IN_MILLIS)
                cacheHandler.saveLongValue(CMConstant.INAPP_DISPLAY_COUNTER, 0)
                true
            }
        }
    }

    private fun isInActiveTimeFrame(inAppData: CMInApp): Boolean {
        return if (inAppData.startTime == 0L && inAppData.endTime == 0L) {
            true
        } else {
            //check how to get current time stamp
            return inAppData.startTime <= System.currentTimeMillis()
                    && (inAppData.endTime >= System.currentTimeMillis()
                    || inAppData.endTime == 0L)
        }
    }

}