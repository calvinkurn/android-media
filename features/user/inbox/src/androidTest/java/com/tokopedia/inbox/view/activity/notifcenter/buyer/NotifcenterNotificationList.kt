package com.tokopedia.inbox.view.activity.notifcenter.buyer

import com.tokopedia.inbox.view.activity.base.notifcenter.InboxNotifcenterTest
import com.tokopedia.inbox.view.activity.base.notifcenter.NotifcenterAssertion
import com.tokopedia.notifcenter.presentation.adapter.viewholder.notification.v3.LoadMoreViewHolder
import com.tokopedia.notifcenter.presentation.adapter.viewholder.notification.v3.SectionTitleViewHolder
import com.tokopedia.test.application.matcher.hasViewHolderItemAtPosition
import org.junit.Test

class NotifcenterNotificationList : InboxNotifcenterTest() {

    @Test
    fun should_show_new_list_title() {
        // Given
        inboxNotifcenterDep.apply {
            notifcenterDetailUseCase.response = notifcenterDetailUseCase.newListOnly
        }
        startInboxActivity()

        // Then
        NotifcenterAssertion.assertRecyclerviewItem(
            hasViewHolderItemAtPosition(
                1, SectionTitleViewHolder::class.java
            )
        )
        NotifcenterAssertion.assertSectionTitleTextAt(1, "Terbaru")
    }

    @Test
    fun should_show_new_list_section_load_more_button_when_new_list_section_has_next_true() {
        // Given
        inboxNotifcenterDep.apply {
            notifcenterDetailUseCase.response = notifcenterDetailUseCase.newListOnlyHasNextTrue
        }
        startInboxActivity()

        // Then
        NotifcenterAssertion.assertRecyclerviewItem(
            hasViewHolderItemAtPosition(
                3, LoadMoreViewHolder::class.java
            )
        )
        NotifcenterAssertion.assertLoadMoreTitle(3, "Lihat Lebih Banyak")
    }

    // TODO: should hide new list section load more button when new list section has next false
    // TODO: should show new list section next page when load more button success clicked
    // TODO: load more button on new list should not clickable when loading new list
    // TODO: should render notifications only when load more new list

    // TODO: should show earlier title
    // TODO: should show earlier section load more button when earlier section has next true
    // TODO: should hide earlier section load more button when earlier section has next false
    // TODO: should show earlier section next page when load more button success clicked
    // TODO: load more button on earlier should not clickable when loading earlier notifications
    // TODO: should render notifications only when load more earlier

    // TODO: should show new list title and earlier title when both response is not empty
    // TODO: should show empty state with just text when notifications is empty
    // TODO: should show empty state with illustration when notifications is empty with filter
    // TODO: assert big divider location

}