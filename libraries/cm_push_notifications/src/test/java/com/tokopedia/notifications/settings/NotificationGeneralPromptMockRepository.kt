package com.tokopedia.notifications.settings

class NotificationGeneralPromptMockRepository: NotificationGeneralPromptRepository {

    private var lastShownTimeStamp: Long = 0
    private var shownCount: Int = 0

    override fun saveLastShownTimeStamp(currentTimeStamp: Long) {
        lastShownTimeStamp = currentTimeStamp
    }

    override fun getLastShownTimeStamp(): Long = lastShownTimeStamp

    override fun incrementShownCount() { shownCount++ }

    override fun getShownCount(): Int = shownCount
}
