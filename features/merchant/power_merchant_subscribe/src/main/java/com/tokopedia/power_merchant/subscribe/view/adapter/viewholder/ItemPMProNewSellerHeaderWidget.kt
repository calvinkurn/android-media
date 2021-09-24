package com.tokopedia.power_merchant.subscribe.view.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.media.loader.loadImage
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.view.adapter.PMProNewSellerRequirementAdapter
import com.tokopedia.power_merchant.subscribe.view.model.WidgetPMProNewSellerHeaderUiModel
import kotlinx.android.synthetic.main.item_pm_pro_new_seller_header.view.*

class ItemPMProNewSellerHeaderWidget(view: View) :
    AbstractViewHolder<WidgetPMProNewSellerHeaderUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_pm_pro_new_seller_header
    }

    override fun bind(element: WidgetPMProNewSellerHeaderUiModel) {
        with(itemView) {
            ivPmProNewSellerHeader.loadImage(element.imageUrl)
        }
        setRequirementPMProNewSeller(element)
    }

    private fun setRequirementPMProNewSeller(element: WidgetPMProNewSellerHeaderUiModel) {
        with(itemView) {
            val pmProNewSellerRequirementAdapter =
                PMProNewSellerRequirementAdapter(element.itemRequiredPMProNewSeller)
            rvRequirePmProNewSellerHeader.layoutManager = LinearLayoutManager(context)
            rvRequirePmProNewSellerHeader.adapter = pmProNewSellerRequirementAdapter
        }
    }
}