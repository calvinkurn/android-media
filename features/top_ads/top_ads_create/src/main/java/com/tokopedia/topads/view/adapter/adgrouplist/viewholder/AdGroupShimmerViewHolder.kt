package com.tokopedia.topads.view.adapter.adgrouplist.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topads.view.adapter.adgrouplist.model.AdGroupShimmerUiModel
import com.tokopedia.topads.create.R

class AdGroupShimmerViewHolder(itemView: View) : AbstractViewHolder<AdGroupShimmerUiModel>(itemView) {

    companion object{
        @LayoutRes
        val LAYOUT = R.layout.ad_group_item_shimmer_viewholder_layout
    }

    override fun bind(element: AdGroupShimmerUiModel?) {

    }
}
