package com.tokopedia.notifications.utils

import android.content.Context
import com.tokopedia.notifications.model.BaseNotificationModel
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import com.tokopedia.notifications.database.pushRuleEngine.PushRepository.Companion.getInstance as pushRepository

open class NotificationRemoveManager(
        private val context: Context
): CoroutineScope {

    private val jobs = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + jobs

    fun notificationIdToClear(invoke: (List<BaseNotificationModel>) -> Unit) {
        launch {
            val notifications = pushRepository(context)
                    .getNotification()
                    .intersect(excludeListByCampaignId) { a, b ->
                        a.campaignId == b
                    }
            withContext(Dispatchers.Main) { invoke(notifications) }
        }
    }

    fun cancel() {
        jobs.cancelChildren()
    }

    companion object {
        private val excludeListByCampaignId = listOf(0L)

        private fun <T, U> List<T>.intersect(
                uList: List<U>,
                filterPredicate : (T, U) -> Boolean
        ) = filter { m -> uList.any { filterPredicate(m, it)} }
    }

}