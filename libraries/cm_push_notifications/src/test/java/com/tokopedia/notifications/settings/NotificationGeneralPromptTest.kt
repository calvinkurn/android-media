package com.tokopedia.notifications.settings

import com.tokopedia.notifications.common.CMConstant.RemoteKeys.KEY_GENERAL_PROMPT_TIME_INTERVAL
import com.tokopedia.notifications.settings.NotificationGeneralPrompt.Companion.IGNORE_PROMPT_LIMIT
import com.tokopedia.notifications.settings.NotificationGeneralPrompt.Companion.DEFAULT_IGNORE_PROMPT_TIME_GAP_DAYS
import com.tokopedia.remoteconfig.RemoteConfig
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import java.util.concurrent.TimeUnit

class NotificationGeneralPromptTest {

    private val view = mockk<NotificationGeneralPromptView>(relaxed = true)
    private val repo = NotificationGeneralPromptMockRepository()
    private val remoteConfig = mockk<RemoteConfig>(relaxed = true)

    private fun notificationGeneralPrompt(
        shouldShowNotificationPermission: Boolean,
    ): NotificationGeneralPrompt = NotificationGeneralPrompt(
        shouldShowNotificationPermission = shouldShowNotificationPermission,
        view = view,
        repository = repo,
        remoteConfig = remoteConfig,
    )

    @Test
    fun `show general prompt on first install and should show notification prompt`() {
        val notificationGeneralPrompt = notificationGeneralPrompt(
            shouldShowNotificationPermission = true,
        )

        notificationGeneralPrompt.showNotification()

        verify {
            view.show()
        }
    }

    @Test
    fun `do not show general prompt if should not show`() {
        val notificationGeneralPrompt = notificationGeneralPrompt(
            shouldShowNotificationPermission = false,
        )

        notificationGeneralPrompt.showNotification()

        verify (exactly = 0) {
            view.show()
        }
    }

    @Test
    fun `show general prompt again several days after denied`() {
        val notificationGeneralPrompt = notificationGeneralPrompt(
            shouldShowNotificationPermission = true,
        )

        setupRemoteConfigTimeGap()

        notificationGeneralPrompt.showNotification()

        notificationGeneralPrompt.showNotification()

        saveDummyTimeStampGap()

        notificationGeneralPrompt.showNotification()

        verify (exactly = 2) { view.show() }
    }

    private fun setupRemoteConfigTimeGap() {
        every {
            remoteConfig.getLong(KEY_GENERAL_PROMPT_TIME_INTERVAL, any())
        } returns TimeUnit.DAYS.toMillis(DEFAULT_IGNORE_PROMPT_TIME_GAP_DAYS)
    }

    private fun saveDummyTimeStampGap() {
        val lastShownTimeStamp =
            System.currentTimeMillis() - TimeUnit.DAYS.toMillis(DEFAULT_IGNORE_PROMPT_TIME_GAP_DAYS)

        repo.saveLastShownTimeStamp(lastShownTimeStamp)
    }

    @Test
    fun `limit show general prompt`() {
        val notificationGeneralPrompt = notificationGeneralPrompt(
            shouldShowNotificationPermission = true,
        )

        notificationGeneralPrompt.showNotification()

        saveDummyTimeStampGap()

        notificationGeneralPrompt.showNotification()

        saveDummyTimeStampGap()

        notificationGeneralPrompt.showNotification()

        verify (exactly = IGNORE_PROMPT_LIMIT) { view.show() }
    }
}
