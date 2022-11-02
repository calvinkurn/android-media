package com.tokopedia.home.topads

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import androidx.test.rule.ActivityTestRule
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationLoading
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.HomeInitialShimmerViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.ShimmeringChannelViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.ShimmeringIconViewHolder
import com.tokopedia.kotlin.extensions.view.gone

object HomeRecomTabInstrumentationTestHelper {
    fun RecyclerView.disableHomeAnimation() {
        for (i in 0..this.adapter?.itemCount!!) {
            val currViewholder = this.findViewHolderForAdapterPosition(i)
//            when (currViewholder) {
//                is HomeRecommendationLoading -> {
//                    currViewholder.itemView.gone()
//                }
//            }
        }
    }
}
