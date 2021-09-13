package com.tokopedia.inbox.view.activity.notifcenter.buyer

import com.tokopedia.inbox.view.activity.base.notifcenter.InboxNotifcenterTest
import com.tokopedia.inbox.view.activity.base.notifcenter.NotifcenterAction
import com.tokopedia.inbox.view.activity.base.notifcenter.NotifcenterAssertion
import com.tokopedia.notifcenter.presentation.adapter.viewholder.notification.v3.NotificationTopAdsBannerViewHolder
import com.tokopedia.test.application.matcher.hasViewHolderItemAtPosition
import com.tokopedia.test.application.matcher.hasViewHolderOf
import org.hamcrest.CoreMatchers.not
import org.junit.Test

class NotifcenterTDNTest : InboxNotifcenterTest() {

    @Test
    fun should_show_TDN_when_ad_exist() {
        // Given
        inboxNotifcenterDep.apply {
            topAdsRepository.response = topAdsRepository.defaultResponse
        }
        startInboxActivity()

        // When
        NotifcenterAction.scrollNotificationToPosition(6)

        // Then
        NotifcenterAssertion.assertRecyclerviewItem(
            hasViewHolderItemAtPosition(
                6, NotificationTopAdsBannerViewHolder::class.java
            )
        )
    }

    @Test
    fun should_hide_TDN_when_ad_does_not_exist() {
        // Given
        inboxNotifcenterDep.apply {
            topAdsRepository.response = topAdsRepository.noDataResponse
        }
        startInboxActivity()

        // When
        NotifcenterAction.scrollNotificationToPosition(6)

        // Then
        NotifcenterAssertion.assertRecyclerviewItem(
            not(hasViewHolderOf(NotificationTopAdsBannerViewHolder::class.java))
        )
    }

    @Test
    fun should_hide_TDN_when_user_has_notification_filter() {
        // Given
        inboxNotifcenterDep.apply {
            topAdsRepository.response = topAdsRepository.defaultResponse
        }
        startInboxActivity()

        // When
        NotifcenterAction.clickFilterAt(0)

        // Then
        NotifcenterAssertion.assertItemListSize(2)
        NotifcenterAssertion.assertRecyclerviewItem(
            not(hasViewHolderOf(NotificationTopAdsBannerViewHolder::class.java))
        )
    }

}