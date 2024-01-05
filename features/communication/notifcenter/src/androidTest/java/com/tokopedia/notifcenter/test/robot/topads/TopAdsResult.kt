package com.tokopedia.notifcenter.test.robot.topads

import com.tokopedia.notifcenter.test.robot.general.GeneralResult.assertRecyclerviewItem
import com.tokopedia.notifcenter.view.adapter.viewholder.notification.v3.NotificationTopAdsBannerViewHolder
import com.tokopedia.test.application.matcher.hasViewHolderItemAtPosition
import com.tokopedia.test.application.matcher.hasViewHolderOf
import org.hamcrest.CoreMatchers.not

object TopAdsResult {

    fun assertTopAdsBannerAt(position: Int) {
        assertRecyclerviewItem(
            hasViewHolderItemAtPosition(
                position,
                NotificationTopAdsBannerViewHolder::class.java
            )
        )
    }

    fun assertNotTopAdsBanner() {
        assertRecyclerviewItem(
            not(hasViewHolderOf(NotificationTopAdsBannerViewHolder::class.java))
        )
    }
}
