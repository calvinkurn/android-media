package com.tokopedia.affiliate.feature.explore.view.adapter.viewholder

import android.support.annotation.LayoutRes
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.R
import com.tokopedia.affiliate.feature.explore.view.adapter.RecommendationAdapter
import com.tokopedia.affiliate.feature.explore.view.viewmodel.PopularProfileViewModel

/**
 * @author by milhamj on 14/03/19.
 */
class RecommendationViewHolder(v: View) : AbstractViewHolder<PopularProfileViewModel>(v) {

    val adapter: RecommendationAdapter by lazy {
        RecommendationAdapter()
    }

    companion object {
        @JvmField
        @LayoutRes
        val LAYOUT = R.layout.item_af_popular_profile
    }

    override fun bind(element: PopularProfileViewModel?) {
        if (element == null) {
            return
        }
    }
}