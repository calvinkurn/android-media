package com.tokopedia.tokopoints.view.tokopointhome

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.view.model.rewardtopsection.TokopediaRewardTopSection
import com.tokopedia.unifycomponents.NotificationUnify

class TopSectionViewBinder(val block: TokopediaRewardTopSection?, val tokoPointsHomeFragmentNew: TokoPointsHomeFragmentNew,
                           val toolbarItemList: MutableList<NotificationUnify>,
                          val  adapter: SectionAdapter?)
    : SectionItemViewBinder<TokopediaRewardTopSection, TopSectionVH>(
        TokopediaRewardTopSection::class.java) {
    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return TopSectionVH(
                LayoutInflater.from(parent.context).inflate(getSectionItemType(), parent, false), tokoPointsHomeFragmentNew, toolbarItemList,adapter)
    }

    override fun bindViewHolder(model: TokopediaRewardTopSection, viewHolder: TopSectionVH) {
        viewHolder.bind(model)
    }

    override fun getSectionItemType() = R.layout.tp_topsection_layout_new

}
