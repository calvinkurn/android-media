package com.tokopedia.topads.view.adapter.adgrouplist.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topads.create.R
import com.tokopedia.topads.create.databinding.AdGroupItemViewholderLayoutBinding
import com.tokopedia.topads.view.adapter.adgrouplist.model.AdGroupUiModel
import com.tokopedia.topads.view.sheet.AdGroupStatisticsBottomSheet
import com.tokopedia.topads.view.sheet.AdvertisingCostBottomSheet
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.view.binding.viewBinding

class AdGroupViewHolder(itemView: View,listener:AdGroupListener) : AbstractViewHolder<AdGroupUiModel>(itemView) {

    companion object{
        @LayoutRes
        val LAYOUT = R.layout.ad_group_item_viewholder_layout
    }

    private val binding:AdGroupItemViewholderLayoutBinding? by viewBinding()

    init {
        binding?.adStatsTopRow?.setOnClickListener {
            listener.onAdStatClicked(AdGroupStatisticsBottomSheet())
        }
        binding?.adStatsBottomRow?.setOnClickListener {
            listener.onAdStatClicked(AdvertisingCostBottomSheet())
        }
    }

    override fun bind(element: AdGroupUiModel?) {

    }
    
    interface AdGroupListener{
        fun onAdStatClicked(bottomSheet:BottomSheetUnify)
    }
}
