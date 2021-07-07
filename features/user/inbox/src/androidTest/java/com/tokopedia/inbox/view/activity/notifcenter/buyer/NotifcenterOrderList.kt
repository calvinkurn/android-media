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

class NotifcenterOrderList : InboxNotifcenterTest() {

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
        waitForIt(5000)
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

    // TODO: should update currently visible cached order list with counter when finished loading remote data
    // TODO: should placed first when notification rendered first
    // TODO: should placed first when rendered first instead of notification
    // TODO: should render notification empty state when notification remote data is empty

}