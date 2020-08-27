package com.tokopedia.tokopoints.view.tokopointhome

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.tokopoints.R
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel

class SectionTopadsViewBinder(val block: TopAdsImageViewModel)
    : SectionItemViewBinder<TopAdsImageViewModel, SectionTopadsVH>(
        TopAdsImageViewModel::class.java) {

    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return SectionTopadsVH(
                LayoutInflater.from(parent.context).inflate(getSectionItemType(), parent, false))
    }

    override fun bindViewHolder(model: TopAdsImageViewModel, viewHolder: SectionTopadsVH) {
        viewHolder.bind(model)
    }

    override fun getSectionItemType() = R.layout.tp_topads_reward_layout

}