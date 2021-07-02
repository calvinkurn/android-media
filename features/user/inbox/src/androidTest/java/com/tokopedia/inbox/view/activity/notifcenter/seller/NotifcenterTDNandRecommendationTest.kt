package com.tokopedia.inbox.view.activity.notifcenter.seller

import com.tokopedia.inbox.view.activity.base.notifcenter.NotifcenterAssertion
import com.tokopedia.inbox.view.activity.base.notifcenter.NotifcenterForceSellerRole
import com.tokopedia.notifcenter.presentation.adapter.viewholder.notification.v3.NotificationTopAdsBannerViewHolder
import com.tokopedia.test.application.matcher.hasViewHolderOf
import org.hamcrest.CoreMatchers.not
import org.junit.Test

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

    //TODO: should hide product recom when user role is seller

}