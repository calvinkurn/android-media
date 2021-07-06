package com.tokopedia.inbox.view.activity.notifcenter.buyer

import com.tokopedia.inbox.view.activity.base.notifcenter.InboxNotifcenterTest
import com.tokopedia.inbox.view.activity.base.notifcenter.NotifcenterAction
import com.tokopedia.inbox.view.activity.base.notifcenter.NotifcenterAssertion
import com.tokopedia.notifcenter.presentation.adapter.viewholder.notification.v3.LoadMoreViewHolder
import com.tokopedia.notifcenter.presentation.adapter.viewholder.notification.v3.NormalNotificationViewHolder
import com.tokopedia.notifcenter.presentation.adapter.viewholder.notification.v3.SectionTitleViewHolder
import com.tokopedia.test.application.matcher.hasViewHolderItemAtPosition
import com.tokopedia.test.application.matcher.hasViewHolderOf
import org.hamcrest.CoreMatchers.not
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

    @Test
    fun should_hide_new_list_section_load_more_button_when_new_list_section_has_next_false() {
        // Given
        inboxNotifcenterDep.apply {
            notifcenterDetailUseCase.response = notifcenterDetailUseCase.newListOnlyHasNextFalse
        }
        startInboxActivity()

        // Then
        NotifcenterAssertion.assertRecyclerviewItem(
            not(hasViewHolderOf(LoadMoreViewHolder::class.java))
        )
    }

    @Test
    fun should_show_new_list_section_next_page_when_load_more_button_success_clicked() {
        // Given
        inboxNotifcenterDep.apply {
            notifcenterDetailUseCase.response = notifcenterDetailUseCase.newListOnlyHasNextTrue
        }
        startInboxActivity()

        // When
        NotifcenterAction.clickLoadMoreAt(3)

        // Then
        NotifcenterAssertion.assertRecyclerviewItem(
            hasViewHolderItemAtPosition(
                2, NormalNotificationViewHolder::class.java
            )
        )
        NotifcenterAssertion.assertRecyclerviewItem(
            hasViewHolderItemAtPosition(
                3, NormalNotificationViewHolder::class.java
            )
        )
        NotifcenterAssertion.assertRecyclerviewItem(
            hasViewHolderItemAtPosition(
                4, LoadMoreViewHolder::class.java
            )
        )
        NotifcenterAssertion.assertLoadMoreTitle(4, "Lihat Lebih Banyak")
    }

    @Test
    fun should_hide_load_more_button_if_the_next_page_hasNext_is_false() {
        // Given
        inboxNotifcenterDep.apply {
            notifcenterDetailUseCase.response = notifcenterDetailUseCase.newListOnlyHasNextTrue
        }
        startInboxActivity()

        // When
        inboxNotifcenterDep.apply {
            notifcenterDetailUseCase.response = notifcenterDetailUseCase.newListOnlyHasNextFalse
        }
        NotifcenterAction.clickLoadMoreAt(3)

        // Then
        NotifcenterAssertion.assertRecyclerviewItem(
            not(hasViewHolderOf(LoadMoreViewHolder::class.java))
        )
    }

    @Test
    fun should_show_earlier_title_only() {
        // Given
        inboxNotifcenterDep.apply {
            notifcenterDetailUseCase.response = notifcenterDetailUseCase.earlierOnly
        }
        startInboxActivity()

        // Then
        NotifcenterAssertion.assertRecyclerviewItem(
            hasViewHolderItemAtPosition(
                1, SectionTitleViewHolder::class.java
            )
        )
        NotifcenterAssertion.assertSectionTitleTextAt(1, "Sebelumnya")
    }

    @Test
    fun should_show_earlier_section_load_more_button_when_earlier_section_has_next_true() {
        // Given
        inboxNotifcenterDep.apply {
            notifcenterDetailUseCase.response = notifcenterDetailUseCase.earlierOnlyHasNextTrue
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

    @Test
    fun should_hide_earlier_section_load_more_button_when_earlier_section_has_next_false() {
        // Given
        inboxNotifcenterDep.apply {
            notifcenterDetailUseCase.response = notifcenterDetailUseCase.earlierOnlyHasNextFalse
        }
        startInboxActivity()

        // Then
        NotifcenterAssertion.assertRecyclerviewItem(
            not(hasViewHolderOf(LoadMoreViewHolder::class.java))
        )
    }

    @Test
    fun should_show_earlier_section_next_page_when_load_more_button_success_clicked() {
        // Given
        inboxNotifcenterDep.apply {
            notifcenterDetailUseCase.response = notifcenterDetailUseCase.earlierOnlyHasNextTrue
        }
        startInboxActivity()

        // When
        NotifcenterAction.clickLoadMoreAt(3)

        // Then
        NotifcenterAssertion.assertRecyclerviewItem(
            hasViewHolderItemAtPosition(
                2, NormalNotificationViewHolder::class.java
            )
        )
        NotifcenterAssertion.assertRecyclerviewItem(
            hasViewHolderItemAtPosition(
                3, NormalNotificationViewHolder::class.java
            )
        )
        NotifcenterAssertion.assertRecyclerviewItem(
            hasViewHolderItemAtPosition(
                4, LoadMoreViewHolder::class.java
            )
        )
        NotifcenterAssertion.assertLoadMoreTitle(4, "Lihat Lebih Banyak")
    }

    // TODO: load more button on earlier should not clickable when loading earlier notifications

    // TODO: should show new list title and earlier title when both response is not empty
    // TODO: should show empty state with just text when notifications is empty
    // TODO: should show empty state with illustration when notifications is empty with filter
    // TODO: assert big divider location

    // TODO: load more button on new list should not clickable when loading new list - impossible with current unify, can't stop loading animation
}