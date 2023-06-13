package com.tokopedia.notifications.settings

interface NotificationGeneralPromptRepository {

    fun saveLastShownTimeStamp(currentTimeStamp: Long)
    fun getLastShownTimeStamp(): Long
    fun incrementShownCount()
    fun getShownCount(): Int
}
