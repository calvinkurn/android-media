package com.tokopedia.inbox.view.activity.notifcenter

import com.tokopedia.inbox.view.activity.base.notifcenter.InboxNotifcenterTest
import com.tokopedia.inbox.view.activity.notifcenter.robot.NotificationBannerResult.assertNotificationBannerCountDown
import com.tokopedia.inbox.view.activity.notifcenter.robot.NotificationBannerResult.assertNotificationBannerExpiredDate
import com.tokopedia.inbox.view.activity.notifcenter.robot.NotificationBannerResult.assertNotificationBannerTitle
import com.tokopedia.inbox.view.activity.notifcenter.robot.NotificationBannerResult.assertNotificationTypeBanner
import com.tokopedia.inbox.view.activity.notifcenter.robot.NotificationGeneralRobot.scrollToProductPosition
import com.tokopedia.test.application.annotations.UiTest
import org.junit.Test

@UiTest
class NotifcenterNotificationBannerTest: InboxNotifcenterTest() {

    @Test
    fun should_not_show_expired_date_if_notif_banner_show_expire_false() {
        // Given
        inboxNotifcenterDep.apply {
            notifcenterDetailUseCase.response = notifcenterDetailUseCase.bannerOnly
        }
        startInboxActivity()

        //When
        scrollToProductPosition(2)

        // Then
        assertNotificationTypeBanner(2)
        assertNotificationBannerTitle(2, "Banner Pertama")
        assertNotificationBannerExpiredDate(2, false)
    }

    @Test
    fun should_show_expired_date_if_notif_banner_show_expire_true() {
        // Given
        inboxNotifcenterDep.apply {
            notifcenterDetailUseCase.response = notifcenterDetailUseCase.bannerOnly
        }
        startInboxActivity()

        //When
        scrollToProductPosition(3)

        // Then
        assertNotificationTypeBanner(3)
        assertNotificationBannerTitle(3, "Banner Kedua")
        assertNotificationBannerExpiredDate(3, true)
    }

    @Test
    fun should_not_show_countdown_if_notif_banner_show_expire_false() {
        // Given
        inboxNotifcenterDep.apply {
            notifcenterDetailUseCase.response = notifcenterDetailUseCase.bannerWithinTwentyFourHours
        }
        startInboxActivity()

        //When
        scrollToProductPosition(2)

        // Then
        assertNotificationTypeBanner(2)
        assertNotificationBannerTitle(2, "Banner Pertama")
        assertNotificationBannerCountDown(2, false)
    }

    @Test
    fun should_show_countdown_if_notif_banner_show_expire_true() {
        // Given
        inboxNotifcenterDep.apply {
            notifcenterDetailUseCase.response = notifcenterDetailUseCase.bannerWithinTwentyFourHours
        }
        startInboxActivity()

        //When
        scrollToProductPosition(3)

        // Then
        assertNotificationTypeBanner(3)
        assertNotificationBannerTitle(3, "Banner Kedua")
        assertNotificationBannerCountDown(3, true)
    }
}