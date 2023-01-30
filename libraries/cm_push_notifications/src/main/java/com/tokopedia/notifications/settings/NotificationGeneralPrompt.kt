package com.tokopedia.notifications.settings

import com.tokopedia.notifications.common.CMConstant.RemoteKeys.KEY_GENERAL_PROMPT_TIME_INTERVAL
import com.tokopedia.remoteconfig.RemoteConfig
import java.util.concurrent.TimeUnit

internal class NotificationGeneralPrompt(
    private val shouldShowNotificationPermission: Boolean,
    private val view: NotificationGeneralPromptView,
    private val repository: NotificationGeneralPromptRepository,
    private val remoteConfig: RemoteConfig,
) {

    fun showNotification() {
        if (!shouldShowPrompt()) return

        repository.saveLastShownTimeStamp(System.currentTimeMillis())
        repository.incrementShownCount()

        view.show()
    }

    private fun shouldShowPrompt() =
        shouldShowNotificationPermission &&
            hasPastSeveralDaysFromLastShown() &&
            isWithinPromptCountLimit()

    private fun hasPastSeveralDaysFromLastShown() =
        (System.currentTimeMillis() - repository.getLastShownTimeStamp()) >=
            getIgnorePromptTimeGapInMillis()

    private fun getIgnorePromptTimeGapInMillis() =
        remoteConfig.getLong(
            KEY_GENERAL_PROMPT_TIME_INTERVAL,
            TimeUnit.DAYS.toMillis(DEFAULT_IGNORE_PROMPT_TIME_GAP_DAYS),
        )

    private fun isWithinPromptCountLimit() =
        repository.getShownCount() < IGNORE_PROMPT_LIMIT

    companion object {
        const val DEFAULT_IGNORE_PROMPT_TIME_GAP_DAYS = 7L
        const val IGNORE_PROMPT_LIMIT = 2
    }
}
