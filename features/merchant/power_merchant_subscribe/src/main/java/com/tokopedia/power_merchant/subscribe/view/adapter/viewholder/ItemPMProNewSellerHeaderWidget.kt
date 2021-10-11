package com.tokopedia.power_merchant.subscribe.view.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.media.loader.loadImage
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.databinding.ItemPmProNewSellerHeaderBinding
import com.tokopedia.power_merchant.subscribe.view.adapter.PMProNewSellerRequirementAdapter
import com.tokopedia.power_merchant.subscribe.view.model.WidgetPMProNewSellerHeaderUiModel
import com.tokopedia.utils.view.binding.viewBinding

class ItemPMProNewSellerHeaderWidget(view: View) :
    AbstractViewHolder<WidgetPMProNewSellerHeaderUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_pm_pro_new_seller_header
    }

    private val binding: ItemPmProNewSellerHeaderBinding? by viewBinding()

    override fun bind(element: WidgetPMProNewSellerHeaderUiModel) {
        binding?.run {
            ivPmProNewSellerHeader.loadImage(element.imageUrl)
        }
        setRequirementPMProNewSeller(element)
    }

    private fun setRequirementPMProNewSeller(element: WidgetPMProNewSellerHeaderUiModel) {
        binding?.run {
            val pmProNewSellerRequirementAdapter =
                PMProNewSellerRequirementAdapter(element.itemRequiredPMProNewSeller)
            rvRequirePmProNewSellerHeader.layoutManager = object: LinearLayoutManager(root.context) {
                override fun canScrollVertically(): Boolean = false
            }
            rvRequirePmProNewSellerHeader.adapter = pmProNewSellerRequirementAdapter
        }
    }
}