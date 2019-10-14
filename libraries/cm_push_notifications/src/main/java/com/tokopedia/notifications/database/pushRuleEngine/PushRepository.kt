package com.tokopedia.notifications.database.pushRuleEngine

import android.content.Context
import android.util.Log
import com.tokopedia.notifications.common.launchCatchError
import com.tokopedia.notifications.database.RoomDB
import com.tokopedia.notifications.model.BaseNotificationModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

class PushRepository private constructor(val context: Context) : CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO

    var roomDB: RoomDB = RoomDB.getDatabase(context)
    var pushDataStore: IPushDataStore

    init {
        pushDataStore = PushDataStore(roomDB.baseNotificationDao())
    }

    companion object {
        @Volatile private var INSTANCE: PushRepository? = null
        fun getInstance(context: Context): PushRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildPushRepository(context).also { INSTANCE = it }
            }

        private fun buildPushRepository(context: Context) = PushRepository(context)
    }

    fun updateNotificationModel(baseNotificationModel: BaseNotificationModel) {
        launchCatchError(block = {
            pushDataStore.insertNotification(baseNotificationModel)
        }, onError = {
        })
    }

    fun insertNotificationModel(baseNotificationModel: BaseNotificationModel) {
        launchCatchError(block = {
            pushDataStore.insertNotification(baseNotificationModel)
        }, onError = {
        })
    }
}