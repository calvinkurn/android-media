package com.tokopedia.inbox.view.activity.notifcenter.seller

import com.tokopedia.inbox.view.activity.base.notifcenter.NotifcenterAssertion
import com.tokopedia.inbox.view.activity.base.notifcenter.NotifcenterForceSellerRole
import com.tokopedia.notifcenter.ui.adapter.viewholder.notification.v3.NotificationTopAdsBannerViewHolder
import com.tokopedia.notifcenter.ui.adapter.viewholder.notification.v3.RecommendationViewHolder
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.test.application.matcher.hasViewHolderOf
import org.hamcrest.CoreMatchers.not
import org.junit.Test

@UiTest
class NotifcenterTDNandRecommendationTest : NotifcenterForceSellerRole() {

    @Test
    fun should_hide_TDN_when_user_role_is_seller() {
        // Given
        inboxNotifcenterDep.apply {
            topAdsRepository.response = topAdsRepository.defaultResponse
        }
        startInboxActivity()

        // Then
        NotifcenterAssertion.assertRecyclerviewItem(
            not(hasViewHolderOf(NotificationTopAdsBannerViewHolder::class.java))
        )
    }

    @Test
    fun should_hide_product_recom_when_user_role_is_seller() {
        // Given
        inboxNotifcenterDep.apply {
            topAdsRepository.response = topAdsRepository.defaultResponse
            getRecommendationUseCase.response = getRecommendationUseCase.defaultResponse
        }
        startInboxActivity()

        // Then
        NotifcenterAssertion.assertRecyclerviewItem(
            not(hasViewHolderOf(RecommendationViewHolder::class.java))
        )
    }

}
