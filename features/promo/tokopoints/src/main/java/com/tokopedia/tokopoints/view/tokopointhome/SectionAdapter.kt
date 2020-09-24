package com.tokopedia.tokopoints.view.tokopointhome

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.tokopoints.view.model.rewardtopsection.TokopediaRewardTopSection
import com.tokopedia.tokopoints.view.model.section.SectionContent
import com.tokopedia.tokopoints.view.util.CommonConstant
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel

class SectionAdapter(private val viewBinders: Map<String, SectionItemBinder>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val sectionList: ArrayList<Any> = ArrayList()
    private val viewTypeToBinders = viewBinders.mapKeys { it.value.getSectionItemType() }

    private fun getViewBinder(viewType: Int): SectionItemBinder = viewTypeToBinders.getValue(viewType)

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            viewBinders.getValue(CommonConstant.SectionLayoutType.TOPHEADER).getSectionItemType()
        } else {
            val item = sectionList[position] as SectionContent
            if (item.layoutType == CommonConstant.SectionLayoutType.BANNER) {
                viewBinders.getValue(item.layoutBannerAttr.bannerType).getSectionItemType()
            } else {
                viewBinders.getValue(item.layoutType).getSectionItemType()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return getViewBinder(viewType).createViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        return getViewBinder(getItemViewType(position)).bindViewHolder(sectionList.get(position), holder)
    }

    override fun getItemCount(): Int {
        return sectionList.size
    }

    fun addItem(data: ArrayList<Any>) {
        sectionList.addAll(data)
        notifyDataSetChanged()
    }
}