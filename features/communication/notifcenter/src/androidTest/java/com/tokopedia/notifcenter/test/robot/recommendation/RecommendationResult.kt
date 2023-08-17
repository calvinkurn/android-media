package com.tokopedia.notifcenter.test.robot.recommendation

import com.tokopedia.notifcenter.test.robot.general.GeneralResult.assertRecyclerviewItem
import com.tokopedia.notifcenter.view.adapter.viewholder.notification.v3.RecommendationTitleViewHolder
import com.tokopedia.notifcenter.view.adapter.viewholder.notification.v3.RecommendationViewHolder
import com.tokopedia.test.application.matcher.hasViewHolderItemAtPosition
import com.tokopedia.test.application.matcher.hasViewHolderOf
import org.hamcrest.CoreMatchers.not

object RecommendationResult {
    fun assertRecommendationTitleAt(position: Int) {
        assertRecyclerviewItem(
            hasViewHolderItemAtPosition(position, RecommendationTitleViewHolder::class.java)
        )
    }

    fun assertRecommendationAt(position: Int) {
        assertRecyclerviewItem(
            hasViewHolderItemAtPosition(position, RecommendationViewHolder::class.java)
        )
    }

    fun assertNotRecommendationTitle() {
        assertRecyclerviewItem(
            not(hasViewHolderOf(RecommendationTitleViewHolder::class.java))
        )
    }

    fun assertNotRecommendation() {
        assertRecyclerviewItem(
            not(hasViewHolderOf(RecommendationViewHolder::class.java))
        )
    }
}
