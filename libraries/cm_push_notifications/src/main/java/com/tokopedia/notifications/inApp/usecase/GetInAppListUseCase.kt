package com.tokopedia.notifications.inApp.usecase

import android.text.TextUtils
import com.tokopedia.notifications.common.CMConstant
import com.tokopedia.notifications.inApp.ruleEngine.repository.RepositoryManager
import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata.CMInApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class GetInAppListUseCase(private val repositoryManager: RepositoryManager) {

    suspend fun getInAppListByScreenName(screenName: String, isActivity: Boolean): List<CMInApp>? {
        return withContext(Dispatchers.IO) {
            synchronized(this) {
                getActiveInAppData(screenName, isActivity)
            }
        }
    }

    /**
    *   We can show test inApp in Case 24Hr Master frequency exhausted
    */
    private fun getActiveInAppData(key: String, isActivity: Boolean): List<CMInApp>? {
        val inAppDataDao = repositoryManager.inAppDataDao ?: return null

        val list: List<CMInApp>? = if (isMasterFrequencyAvailable()) {
            inAppDataDao.getActiveDataForScreen(key, System.currentTimeMillis())
        } else
            inAppDataDao.getActiveDataForScreenTestCampaign(key, System.currentTimeMillis())

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

    companion object {
        private const val HOURS_24_IN_MILLIS: Long = 24 * 60 * 60 * 1000L
    }

}