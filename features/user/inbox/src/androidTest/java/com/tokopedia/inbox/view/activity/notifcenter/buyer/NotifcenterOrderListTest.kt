package com.tokopedia.inbox.view.activity.notifcenter.buyer

import com.tokopedia.inbox.view.activity.base.notifcenter.InboxNotifcenterTest
import com.tokopedia.inbox.view.activity.base.notifcenter.NotifcenterAction
import com.tokopedia.inbox.view.activity.base.notifcenter.NotifcenterAssertion
import com.tokopedia.inboxcommon.RoleType
import com.tokopedia.notifcenter.presentation.adapter.viewholder.notification.v3.NotificationOrderListViewHolder
import com.tokopedia.test.application.matcher.hasViewHolderItemAtPosition
import com.tokopedia.test.application.matcher.hasViewHolderOf
import org.hamcrest.CoreMatchers.not
import org.junit.Test

class NotifcenterOrderListTest : InboxNotifcenterTest() {

    @Test
    fun should_show_order_list_when_success_load_order_list_with_no_cache() {
        // Given
        inboxNotifcenterDep.apply {
            notifOrderListUseCase.response = notifOrderListUseCase.defaultResponse
        }
        startInboxActivity()

        // Then
        NotifcenterAssertion.assertRecyclerviewItem(
            hasViewHolderItemAtPosition(
                0, NotificationOrderListViewHolder::class.java
            )
        )
    }

    @Test
    fun should_hide_order_list_when_user_has_notification_filter() {
        // Given
        inboxNotifcenterDep.apply {
            notifOrderListUseCase.response = notifOrderListUseCase.defaultResponse
        }
        startInboxActivity()

        //
        NotifcenterAction.clickFilterAt(0)

        // Then
        NotifcenterAssertion.assertRecyclerviewItem(
            not(hasViewHolderOf(NotificationOrderListViewHolder::class.java))
        )
    }

    @Test
    fun should_show_cached_version_order_list_when_cache_data_is_exist() {
        // Given
        val delay = 10_000L
        inboxNotifcenterDep.apply {
            notifOrderListUseCase.setCache(
                notifOrderListUseCase.cacheResponse, RoleType.BUYER
            )
            notifOrderListUseCase.setResponseWithDelay(
                delay, notifOrderListUseCase.defaultResponse
            )
        }
        startInboxActivity()

        // Then
        NotifcenterAssertion.assertRecyclerviewItem(
            hasViewHolderItemAtPosition(
                0, NotificationOrderListViewHolder::class.java
            )
        )
        NotifcenterAssertion.assertNotifOrderFirstCardText(
            "Cache Transaksi"
        )
        NotifcenterAssertion.assertNotifOrderSecondCardText(
            "Cache All"
        )
    }

    @Test
    fun should_update_currently_visible_cached_order_list_with_counter_when_finished_loading_remote_data() {
        // Given
        inboxNotifcenterDep.apply {
            notifOrderListUseCase.setCache(
                notifOrderListUseCase.cacheResponse, RoleType.BUYER
            )
            notifOrderListUseCase.response = notifOrderListUseCase.defaultResponse
        }
        startInboxActivity()

        // Then
        NotifcenterAssertion.assertRecyclerviewItem(
            hasViewHolderItemAtPosition(
                0, NotificationOrderListViewHolder::class.java
            )
        )
        NotifcenterAssertion.assertNotifOrderFirstCardText(
            "Transaksi berlangsung"
        )
        NotifcenterAssertion.assertNotifOrderSecondCardText(
            "Lihat semua"
        )
    }

    @Test
    fun should_placed_first_when_notification_rendered_first() {
        // Given
        val delay = 150L
        inboxNotifcenterDep.apply {
            notifOrderListUseCase.setResponseWithDelay(
                delay, notifOrderListUseCase.defaultResponse
            )
        }
        startInboxActivity()

        // When
        // Need to make sure that notifications are rendered first, wait for it.
        waitForIt(delay * 2)

        // Then
        NotifcenterAssertion.assertRecyclerviewItem(
            hasViewHolderItemAtPosition(
                0, NotificationOrderListViewHolder::class.java
            )
        )
        NotifcenterAssertion.assertNotifOrderFirstCardText(
            "Transaksi berlangsung"
        )
        NotifcenterAssertion.assertNotifOrderSecondCardText(
            "Lihat semua"
        )
    }

    // TODO: should placed first when rendered first instead of notification
    // TODO: should render notification empty state when notification remote data is empty

}