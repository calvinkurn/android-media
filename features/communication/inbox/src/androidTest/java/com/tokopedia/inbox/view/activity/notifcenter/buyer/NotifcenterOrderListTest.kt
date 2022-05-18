package com.tokopedia.inbox.view.activity.notifcenter.buyer

import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.filters.FlakyTest
import com.tokopedia.inbox.view.activity.base.notifcenter.InboxNotifcenterTest
import com.tokopedia.inbox.view.activity.base.notifcenter.NotifcenterAction
import com.tokopedia.inbox.view.activity.base.notifcenter.NotifcenterAssertion
import com.tokopedia.inboxcommon.RoleType
import com.tokopedia.notifcenter.presentation.adapter.viewholder.notification.v3.NotificationOrderListViewHolder
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.test.application.matcher.hasViewHolderItemAtPosition
import com.tokopedia.test.application.matcher.hasViewHolderOf
import org.hamcrest.CoreMatchers.not
import org.junit.Test

@UiTest
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
    fun should_hide_order_list_when_app_is_seller() {
        // Given
        startInboxActivity(true)

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
        NotifcenterAssertion.assertNotifOrderCardTextAtPosition(
            0, "Cache Transaksi"
        )
        NotifcenterAssertion.assertNotifOrderCardTextAtPosition(
            1, "Cache All"
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
        NotifcenterAssertion.assertNotifOrderCardTextAtPosition(
            0, "Transaksi berlangsung"
        )
        NotifcenterAssertion.assertNotifOrderCardTextAtPosition(
            1, "Lihat semua"
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
    }

    @Test
    fun should_placed_first_when_rendered_first_instead_of_notification() {
        // Given
        val delay = 150L
        inboxNotifcenterDep.apply {
            notifOrderListUseCase.response = notifOrderListUseCase.defaultResponse
            notifcenterDetailUseCase.setResponseWithDelay(
                delay, notifcenterDetailUseCase.defaultResponse
            )
        }
        startInboxActivity()

        // When
        // Need to make sure that order list are rendered first, wait for it.
        waitForIt(delay * 2)

        // Then
        NotifcenterAssertion.assertRecyclerviewItem(
            hasViewHolderItemAtPosition(
                0, NotificationOrderListViewHolder::class.java
            )
        )
    }

    @Test
    @FlakyTest
    fun should_retain_last_position_when_user_scrolled_down_and_back_to_it() {
        // Given
        inboxNotifcenterDep.apply {
            notifOrderListUseCase.response = notifOrderListUseCase.fifteenOrderWidgetResponse
            notifcenterDetailUseCase.response = notifcenterDetailUseCase.fifteenNotifications
        }
        startInboxActivity()

        // When
        NotifcenterAction.smoothScrollOrderWidgetTo(14)
        NotifcenterAction.smoothScrollNotificationTo(14)
        NotifcenterAction.smoothScrollNotificationTo(0)

        // Then
        NotifcenterAssertion.assertOrderWidgetCardAt(
            14, isDisplayed()
        )
    }
}