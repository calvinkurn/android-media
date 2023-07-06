package com.tokopedia.affiliate.ui.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.adapter.AffiliateAdapter
import com.tokopedia.affiliate.adapter.AffiliateAdapterFactory
import com.tokopedia.affiliate.interfaces.AffiliatePerformanceChipClick
import com.tokopedia.affiliate.model.response.ItemTypesItem
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliatePerformanceChipModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliatePerformanceChipRVModel
import com.tokopedia.affiliate_toko.R

class AffiliatePerformanceChipRVVH(
    itemView: View,
    private val affiliatePerformanceChipClick: AffiliatePerformanceChipClick? = null
) : AbstractViewHolder<AffiliatePerformanceChipRVModel>(itemView), AffiliatePerformanceChipClick {

    private var chipAdapter: AffiliateAdapter =
        AffiliateAdapter(AffiliateAdapterFactory(affiliatePerformanceChipClick = this))

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.affiliate_chip_recycler_view
    }

    override fun bind(element: AffiliatePerformanceChipRVModel?) {
        val performanceChipRv = itemView.findViewById<RecyclerView>(R.id.rv_performance_chip)
        val rvLayoutManager =
            LinearLayoutManager(itemView.context, RecyclerView.HORIZONTAL, false)
        performanceChipRv?.apply {
            layoutManager = rvLayoutManager
            adapter = chipAdapter
        }
        chipAdapter.setVisitables(
            element?.itemTypes?.map { AffiliatePerformanceChipModel(it) }
        )
    }

    override fun onChipClick(type: ItemTypesItem?) {
        val selectedIndex =
            chipAdapter.list.map { (it as AffiliatePerformanceChipModel).chipType }
                .indexOf(type)
        var previouslySelectedIndex = -1
        chipAdapter.list.forEachIndexed { index, visitable ->
            if ((visitable as AffiliatePerformanceChipModel).chipType.isSelected) {
                previouslySelectedIndex = index
            } else {
                previouslySelectedIndex - 1
            }
        }
        if (selectedIndex != previouslySelectedIndex && previouslySelectedIndex >= 0) {
            affiliatePerformanceChipClick?.onChipClick(type)
            (chipAdapter.list[selectedIndex] as AffiliatePerformanceChipModel).chipType.isSelected =
                true
            (chipAdapter.list[previouslySelectedIndex] as AffiliatePerformanceChipModel).chipType.isSelected =
                false
            chipAdapter.notifyItemChanged(selectedIndex)
            chipAdapter.notifyItemChanged(previouslySelectedIndex)
        }
    }
}
