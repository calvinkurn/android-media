package com.tokopedia.tokopoints.view.tokopointhome

import android.os.Parcelable
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.tokopoints.view.model.section.SectionContent
import com.tokopedia.tokopoints.view.tokopointhome.carousel.SectionHorizontalCarouselVH
import com.tokopedia.tokopoints.view.tokopointhome.catalog.SectionHorizontalCatalogVH
import com.tokopedia.tokopoints.view.tokopointhome.coupon.SectionHorizontalViewHolder
import com.tokopedia.tokopoints.view.util.CommonConstant

class SectionAdapter(private val viewBinders: Map<String, SectionItemBinder>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val sectionList: ArrayList<Any> = ArrayList()
    private val scrollStates = hashMapOf<String, Parcelable?>()

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

        if (holder is SectionHorizontalViewHolder) {
            val key = (sectionList[holder.adapterPosition] as SectionContent).sectionTitle
            val state = scrollStates[key]
            if (state != null) {
                holder.layoutManager.onRestoreInstanceState(state)
            } else {
                holder.layoutManager.scrollToPosition(0)
            }
        }

        if (holder is SectionHorizontalCarouselVH) {
            val key = (sectionList[holder.adapterPosition] as SectionContent).sectionTitle
            val state = scrollStates[key]
            if (state != null) {
                holder.layoutManager.onRestoreInstanceState(state)
            } else {
                holder.layoutManager.scrollToPosition(0)
            }
        }

        if (holder is SectionHorizontalCatalogVH) {
            val key = (sectionList[holder.adapterPosition] as SectionContent).sectionTitle
            val state = scrollStates[key]
            if (state != null) {
                holder.layoutManager.onRestoreInstanceState(state)
            } else {
                holder.layoutManager.scrollToPosition(0)
            }
        }
        return getViewBinder(getItemViewType(position)).bindViewHolder(sectionList[position], holder)
    }

    override fun getItemCount(): Int {
        return sectionList.size
    }

    fun addItem(data: ArrayList<Any>) {
        sectionList.addAll(data)
        notifyDataSetChanged()
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        if (holder is SectionHorizontalViewHolder) {
            val key = (sectionList[holder.adapterPosition] as SectionContent).sectionTitle
            scrollStates[key] = holder.layoutManager.onSaveInstanceState()
        }

        if (holder is SectionHorizontalCatalogVH) {
            val key = (sectionList[holder.adapterPosition] as SectionContent).sectionTitle
            scrollStates[key] = holder.layoutManager.onSaveInstanceState()
        }

        if (holder is SectionHorizontalCarouselVH) {
            val key = (sectionList[holder.adapterPosition] as SectionContent).sectionTitle
            scrollStates[key] = holder.layoutManager.onSaveInstanceState()
        }
        super.onViewRecycled(holder)
    }

}