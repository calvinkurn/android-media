package com.tokopedia.inbox.view.activity.notifcenter.buyer

import com.tokopedia.inbox.view.activity.base.notifcenter.InboxNotifcenterTest
import com.tokopedia.inbox.view.activity.base.notifcenter.NotifcenterAction
import com.tokopedia.inbox.view.activity.base.notifcenter.NotifcenterAssertion
import com.tokopedia.notifcenter.presentation.adapter.viewholder.notification.v3.RecommendationTitleViewHolder
import com.tokopedia.notifcenter.presentation.adapter.viewholder.notification.v3.RecommendationViewHolder
import com.tokopedia.test.application.matcher.hasViewHolderItemAtPosition
import com.tokopedia.test.application.matcher.hasViewHolderOf
import org.hamcrest.CoreMatchers.not
import org.junit.Test

class NotifcenterRecommendationTest : InboxNotifcenterTest() {

    @Test
    fun should_show_product_recom_when_response_is_not_empty() {
        // Given
        inboxNotifcenterDep.apply {
            getRecommendationUseCase.response = getRecommendationUseCase.defaultResponse
        }
        startInboxActivity()

        // When
        NotifcenterAction.scrollNotificationToPosition(8)

        // Then
        NotifcenterAssertion.assertRecyclerviewItem(
            hasViewHolderItemAtPosition(7, RecommendationTitleViewHolder::class.java)
        )
        NotifcenterAssertion.assertRecyclerviewItem(
            hasViewHolderItemAtPosition(8, RecommendationViewHolder::class.java)
        )
    }

    @Test
    fun should_show_product_recom_when_TDN_does_not_has_ad() {
        // Given
        inboxNotifcenterDep.apply {
            topAdsRepository.response = topAdsRepository.noDataResponse
            getRecommendationUseCase.response = getRecommendationUseCase.defaultResponse
        }
        startInboxActivity()

        // When
        NotifcenterAction.scrollNotificationToPosition(8)

        // Then
        NotifcenterAssertion.assertRecyclerviewItem(
            hasViewHolderItemAtPosition(6, RecommendationTitleViewHolder::class.java)
        )
        NotifcenterAssertion.assertRecyclerviewItem(
            hasViewHolderItemAtPosition(7, RecommendationViewHolder::class.java)
        )
    }

    @Test
    fun should_show_product_recom_when_TDN_load_is_error() {
        // Given
        inboxNotifcenterDep.apply {
            topAdsRepository.isError = true
            getRecommendationUseCase.response = getRecommendationUseCase.defaultResponse
        }
        startInboxActivity()

        // When
        NotifcenterAction.scrollNotificationToPosition(8)

        // Then
        NotifcenterAssertion.assertRecyclerviewItem(
            hasViewHolderItemAtPosition(6, RecommendationTitleViewHolder::class.java)
        )
        NotifcenterAssertion.assertRecyclerviewItem(
            hasViewHolderItemAtPosition(7, RecommendationViewHolder::class.java)
        )
    }

    @Test
    fun should_hide_product_recom_when_user_has_notification_filter() {
        // Given
        inboxNotifcenterDep.apply {
            getRecommendationUseCase.response = getRecommendationUseCase.defaultResponse
        }
        startInboxActivity()

        // When
        NotifcenterAction.clickFilterAt(0)

        // Then
        NotifcenterAssertion.assertRecyclerviewItem(
            not(hasViewHolderOf(RecommendationTitleViewHolder::class.java))
        )
    }

}