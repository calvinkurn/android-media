package com.tokopedia.notifications.settings

import android.content.Context
import com.tokopedia.kotlin.extensions.int
import com.tokopedia.kotlin.extensions.long

internal class NotificationGeneralPromptSharedPreferences(
    context: Context
): NotificationGeneralPromptRepository {

    private val sharedPreferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE)

    private var _lastShownTimeStamp by sharedPreferences.long(0L, LAST_SHOWN_TIME_STAMP_KEY)
    private var _shownCount by sharedPreferences.int(0, SHOWN_COUNT)

    override fun saveLastShownTimeStamp(currentTimeStamp: Long) {
        _lastShownTimeStamp = currentTimeStamp
    }
    override fun getLastShownTimeStamp(): Long = _lastShownTimeStamp

    override fun incrementShownCount() {
        _shownCount++
    }

    override fun getShownCount(): Int = _shownCount

    companion object {
        private const val NAME = "notif_general_prompt_pref"
        private const val LAST_SHOWN_TIME_STAMP_KEY = "last_shown_timestamp"
        private const val SHOWN_COUNT = "shown_count"
    }
}
