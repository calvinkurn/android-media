package com.tokopedia.tokopoints.view.tokopointhome

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.tokopoints.view.model.rewardtopsection.TokopediaRewardTopSection
import com.tokopedia.tokopoints.view.model.section.SectionContent
import com.tokopedia.tokopoints.view.util.CommonConstant
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel

class SectionAdapter(private val viewBinders: Map<String, SectionItemBinder>,
                     val sectionList: ArrayList<Any>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val viewTypeToBinders = viewBinders.mapKeys { it.value.getSectionItemType() }

    private fun getViewBinder(viewType: Int): SectionItemBinder = viewTypeToBinders.getValue(viewType)

    override fun getItemViewType(position: Int): Int {
        return when {
            position == 0 -> {
                viewBinders.getValue(CommonConstant.SectionLayoutType.TOPHEADER).getSectionItemType()
            }
            (sectionList[position] as SectionContent).layoutType == CommonConstant.SectionLayoutType.BANNER -> {
                viewBinders.getValue((sectionList[position] as SectionContent).layoutBannerAttr.bannerType).getSectionItemType()
            }
            else -> {
                viewBinders.getValue((sectionList[position] as SectionContent).layoutType).getSectionItemType()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return getViewBinder(viewType).createViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        return getViewBinder(getItemViewType(position)).bindViewHolder(sectionList.get(position), holder)
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        getViewBinder(holder.itemViewType).onViewRecycled(holder)
        super.onViewRecycled(holder)
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        getViewBinder(holder.itemViewType).onViewDetachedFromWindow(holder)
        super.onViewDetachedFromWindow(holder)
    }

    override fun getItemCount(): Int {
        return sectionList.size
    }
}