package com.tokopedia.inbox.view.activity.notifcenter.buyer

import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import com.tokopedia.inbox.R
import com.tokopedia.inbox.view.activity.base.notifcenter.InboxNotifcenterTest
import com.tokopedia.inbox.view.activity.base.notifcenter.NotifcenterAction
import com.tokopedia.inbox.view.activity.base.notifcenter.NotifcenterAssertion
import com.tokopedia.notifcenter.presentation.adapter.viewholder.notification.v3.*
import com.tokopedia.test.application.matcher.hasViewHolderItemAtPosition
import com.tokopedia.test.application.matcher.hasViewHolderOf
import org.hamcrest.CoreMatchers.not
import org.junit.Test

class NotifcenterNotificationList : InboxNotifcenterTest() {

    private val TITLE_NEW_LIST = "Terbaru"
    private val TITLE_EARLIER = "Sebelumnya"
    private val TITLE_LOAD_MORE = "Lihat Lebih Banyak"

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
        NotifcenterAssertion.assertSectionTitleTextAt(1, TITLE_NEW_LIST)
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
        NotifcenterAssertion.assertLoadMoreTitle(3, TITLE_LOAD_MORE)
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
        NotifcenterAssertion.assertLoadMoreTitle(4, TITLE_LOAD_MORE)
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
        NotifcenterAssertion.assertSectionTitleTextAt(1, TITLE_EARLIER)
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
        NotifcenterAssertion.assertLoadMoreTitle(3, TITLE_LOAD_MORE)
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
        NotifcenterAssertion.assertLoadMoreTitle(4, TITLE_LOAD_MORE)
    }

    @Test
    fun should_hide_load_more_button_if_earlier_next_page_hasNext_is_false() {
        // Given
        inboxNotifcenterDep.apply {
            notifcenterDetailUseCase.response = notifcenterDetailUseCase.earlierOnlyHasNextTrue
        }
        startInboxActivity()

        // When
        inboxNotifcenterDep.apply {
            notifcenterDetailUseCase.response = notifcenterDetailUseCase.earlierOnlyHasNextFalse
        }
        NotifcenterAction.clickLoadMoreAt(3)

        // Then
        NotifcenterAssertion.assertRecyclerviewItem(
            not(hasViewHolderOf(LoadMoreViewHolder::class.java))
        )
    }

    @Test
    fun should_show_new_list_title_and_earlier_title_when_both_response_is_not_empty() {
        // Given
        inboxNotifcenterDep.apply {
            notifcenterDetailUseCase.response = notifcenterDetailUseCase.defaultResponse
        }
        startInboxActivity()

        // Then
        NotifcenterAssertion.assertRecyclerviewItem(
            hasViewHolderItemAtPosition(
                1, SectionTitleViewHolder::class.java
            )
        )
        NotifcenterAssertion.assertSectionTitleTextAt(1, TITLE_NEW_LIST)
        NotifcenterAssertion.assertRecyclerviewItem(
            hasViewHolderItemAtPosition(
                4, SectionTitleViewHolder::class.java
            )
        )
        NotifcenterAssertion.assertSectionTitleTextAt(4, TITLE_EARLIER)
    }

    @Test
    fun should_show_empty_state_with_just_text_when_notifications_is_empty() {
        // Given
        inboxNotifcenterDep.apply {
            notifcenterDetailUseCase.response = notifcenterDetailUseCase.emptyNotifications
        }
        startInboxActivity()

        // Then
        NotifcenterAssertion.assertRecyclerviewItem(
            hasViewHolderItemAtPosition(
                1, EmptyNotificationWithRecomViewHolder::class.java
            )
        )
        NotifcenterAssertion.assertEmptyNotifStateWithRecomText(
            1, R.string.title_notifcenter_empty_with_recom
        )
    }

    @Test
    fun should_show_empty_state_with_illustration_when_notifications_is_empty_with_filter() {
        // Given
        inboxNotifcenterDep.apply {
            notifcenterDetailUseCase.response = notifcenterDetailUseCase.defaultResponse
        }
        startInboxActivity()

        // When
        inboxNotifcenterDep.apply {
            notifcenterDetailUseCase.response = notifcenterDetailUseCase.emptyNotifications
        }
        NotifcenterAction.clickFilterAt(0)

        // Then
        val expectedEmptyStatePosition = 0
        NotifcenterAssertion.assertRecyclerviewItem(
            hasViewHolderItemAtPosition(
                expectedEmptyStatePosition, EmptyNotificationViewHolder::class.java
            )
        )
        NotifcenterAssertion.assertEmptyNotifStateIllustrationImageVisibility(
            expectedEmptyStatePosition, isDisplayed()
        )
        NotifcenterAssertion.assertEmptyNotifStateIllustrationTitle(
            expectedEmptyStatePosition, R.string.notification_empty_message
        )
        NotifcenterAssertion.assertEmptyNotifStateIllustrationDescription(
            expectedEmptyStatePosition, R.string.notification_empty_filter_message
        )
    }

    @Test
    fun should_have_big_divider_between_earlier_and_new_section() {
        // Given
        inboxNotifcenterDep.apply {
            notifcenterDetailUseCase.response = notifcenterDetailUseCase.defaultResponse
        }
        startInboxActivity()

        // Then
        NotifcenterAssertion.assertRecyclerviewItem(
            hasViewHolderItemAtPosition(
                3, BigDividerViewHolder::class.java
            )
        )
    }

    // TODO: load more button on new list should not clickable when loading new list - impossible with current unify, can't stop loading animation
    // TODO: load more button on earlier should not clickable when loading earlier notifications - impossible with current unify, can't stop loading animation
}