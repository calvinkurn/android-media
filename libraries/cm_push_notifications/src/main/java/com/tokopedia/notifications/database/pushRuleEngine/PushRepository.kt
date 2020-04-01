package com.tokopedia.notifications.database.pushRuleEngine

import android.content.Context
import com.tokopedia.notifications.database.RoomDB
import com.tokopedia.notifications.model.BaseNotificationModel

class PushRepository private constructor(val context: Context) {

    private val roomDB: RoomDB by lazy {
        RoomDB.getDatabase(context)
    }

    val pushDataStore: IPushDataStore by lazy {
        PushDataStore(roomDB.baseNotificationDao())
    }

    suspend fun updateNotificationModel(baseNotificationModel: BaseNotificationModel) {
        pushDataStore.insertNotification(baseNotificationModel)
    }

    suspend fun insertNotificationModel(baseNotificationModel: BaseNotificationModel) {
        pushDataStore.insertNotification(baseNotificationModel)
    }

    companion object {
        @Volatile
        private var INSTANCE: PushRepository? = null

        fun getInstance(context: Context): PushRepository =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: buildPushRepository(context).also { INSTANCE = it }
                }

        private fun buildPushRepository(context: Context) = PushRepository(context)
    }
}