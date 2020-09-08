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
        when {
            position == 0 -> {
                return viewBinders.getValue(CommonConstant.SectionLayoutType.TOPHEADER).getSectionItemType()
            }
            (sectionList[position] as SectionContent).layoutType == CommonConstant.SectionLayoutType.BANNER -> {
                when ((sectionList[position] as SectionContent).layoutBannerAttr.bannerType) {
                    CommonConstant.BannerType.BANNER_3_1,
                    CommonConstant.BannerType.BANNER_2_1,
                    CommonConstant.BannerType.BANNER_1_1,
                    CommonConstant.BannerType.COLUMN_3_1_BY_1,
                    CommonConstant.BannerType.COLUMN_2_1_BY_1,
                    CommonConstant.BannerType.COLUMN_2_3_BY_4,
                    CommonConstant.BannerType.CAROUSEL_1_1,
                    CommonConstant.BannerType.CAROUSEL_2_1,
                    CommonConstant.BannerType.CAROUSEL_3_1 ->
                        return viewBinders.getValue((sectionList[position] as SectionContent).layoutBannerAttr.bannerType).getSectionItemType()
                }
            }
            else -> {
                return viewBinders.getValue((sectionList[position] as SectionContent).layoutType).getSectionItemType()
            }
        }
        return 0
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