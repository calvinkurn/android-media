package com.tokopedia.inbox.view.activity.notifcenter.buyer

import com.tokopedia.inbox.view.activity.base.notifcenter.InboxNotifcenterTest
import com.tokopedia.inbox.view.activity.base.notifcenter.NotifcenterAssertion
import com.tokopedia.notifcenter.presentation.adapter.viewholder.notification.v3.NotificationOrderListViewHolder
import com.tokopedia.test.application.matcher.hasViewHolderItemAtPosition
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

    // TODO: should hide order list when user has notification filter
    // TODO: should show cached version order list when cache data is exist
    // TODO: should update currently visible cached order list with counter when finished loading remote data
    // TODO: should placed first when notification rendered first
    // TODO: should placed first when rendered first instead of notification
    // TODO: should render notification empty state when notification remote data is empty

}