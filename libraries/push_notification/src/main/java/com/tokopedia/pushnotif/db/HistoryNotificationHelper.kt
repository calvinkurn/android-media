package com.tokopedia.pushnotif.db

import android.content.Context
import com.tokopedia.pushnotif.db.model.HistoryNotificationDB
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

/**
 * @author okasurya on 2019-06-28.
 */
class HistoryNotificationHelper {
    companion object {
        fun getData(job: Job, context: Context, notificationType: Int, limit: Int): List<HistoryNotificationDB> {
            GlobalScope.launch(Dispatchers.Main + job) {
                val list = async(Dispatchers.IO) {
                    PushNotificationDB.getInstance(context).historyNotificationDao()
                            .getListHistoryNotification(notificationType, limit)
                }

                return list.await()
            }
        }

    }
}
