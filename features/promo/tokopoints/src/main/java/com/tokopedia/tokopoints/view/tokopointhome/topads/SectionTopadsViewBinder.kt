package com.tokopedia.tokopoints.view.tokopointhome.topads

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.view.model.section.SectionContent
import com.tokopedia.tokopoints.view.tokopointhome.SectionItemViewBinder

class SectionTopadsViewBinder()
    : SectionItemViewBinder<SectionContent, SectionTopadsVH>(
        SectionContent::class.java) {

    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return SectionTopadsVH(
                LayoutInflater.from(parent.context).inflate(getSectionItemType(), parent, false))
    }

    override fun bindViewHolder(model: SectionContent, viewHolder: SectionTopadsVH) {
        viewHolder.bind(model)
    }

    override fun getSectionItemType() = R.layout.tp_topads_reward_layout

}