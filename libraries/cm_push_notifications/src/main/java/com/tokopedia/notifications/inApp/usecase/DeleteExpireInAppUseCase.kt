package com.tokopedia.notifications.inApp.usecase

import android.content.Context
import com.tokopedia.notifications.common.IrisAnalyticsEvents
import com.tokopedia.notifications.inApp.ruleEngine.repository.RepositoryManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DeleteExpireInAppUseCase(private val applicationContext: Context,
                               private val repositoryManager: RepositoryManager) {

    suspend fun deleteAllCompletedAndExpireInAPP() {
        return withContext(Dispatchers.IO) {
            synchronized(this) {
                deleteAllExpireInAPP()
            }
        }
    }

    private fun deleteAllExpireInAPP() {
        val weekInMilliSec = 7 * 24 * 60 * 60 * 1000L
        val expiredInAPPList =
                repositoryManager.inAppDataDao?.getAllExpiredInApp(System.currentTimeMillis(), weekInMilliSec)
        expiredInAPPList?.forEach {
            if (it.freq > 0 && it.lastShownTime == 0L) {
                IrisAnalyticsEvents.sendInAppEvent(applicationContext,
                        IrisAnalyticsEvents.INAPP_EXPIRED, it)
            }
            RepositoryManager.getInstance().inAppDataDao?.deleteRecord(it.id)
        }
    }

}