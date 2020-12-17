package com.tokopedia.tokopoints.view.tokopointhome.header

import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.view.model.rewardtopsection.TokopediaRewardTopSection
import com.tokopedia.tokopoints.view.tokopointhome.SectionItemViewBinder
import com.tokopedia.tokopoints.view.tokopointhome.TokoPointsHomeFragmentNew
import com.tokopedia.tokopoints.view.tokopointhome.TopSectionResponse
import com.tokopedia.unifycomponents.NotificationUnify

class TopSectionViewBinder(val block: TopSectionResponse?, val tokoPointsHomeFragmentNew: TokoPointsHomeFragmentNew,
                           val toolbarItemList: MutableList<NotificationUnify>)
    : SectionItemViewBinder<TopSectionResponse, TopSectionVH>(
        TopSectionResponse::class.java) {
    override fun createViewHolder(parent: ViewGroup): TopSectionVH {
        return TopSectionVH(
                LayoutInflater.from(parent.context).inflate(getSectionItemType(), parent, false), tokoPointsHomeFragmentNew, toolbarItemList)
    }

    override fun bindViewHolder(model: TopSectionResponse, viewHolder: TopSectionVH) {
        viewHolder.bind(model)
    }

    override fun getSectionItemType() = R.layout.tp_topsection_layout_new

}
