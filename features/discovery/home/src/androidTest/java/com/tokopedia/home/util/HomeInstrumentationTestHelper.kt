package com.tokopedia.home.util

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import androidx.test.rule.ActivityTestRule
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.HomeInitialShimmerViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.ShimmeringChannelViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.ShimmeringIconViewHolder
import com.tokopedia.kotlin.extensions.view.gone

object HomeInstrumentationTestHelper {
    fun ActivityTestRule<*>.deleteHomeDatabase() {
        this.activity.deleteDatabase("HomeCache.db")
    }

    fun Context.deleteHomeDatabase() {
        this.deleteDatabase("HomeCache.db")
    }

    fun RecyclerView.disableHomeAnimation() {
        for (i in 0..this.adapter?.itemCount!!) {
            val currViewholder = this.findViewHolderForAdapterPosition(i)
            when (currViewholder) {
                is ShimmeringIconViewHolder,
                is ShimmeringChannelViewHolder,
                is HomeInitialShimmerViewHolder -> {
                    currViewholder.itemView.gone()
                }
            }
        }
    }
}