package com.tokopedia.notifications.database.pushRuleEngine

import android.content.Context
import com.tokopedia.notifications.database.RoomNotificationDB
import com.tokopedia.notifications.model.BaseNotificationModel
import kotlin.LazyThreadSafetyMode.NONE as NONE

class PushRepository private constructor(val context: Context) {

    private val roomDB: RoomNotificationDB by lazy(NONE) {
        RoomNotificationDB.getDatabase(context)
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

    suspend fun getNotification(): List<BaseNotificationModel> {
        return pushDataStore.getNotification()
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