package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.databinding.PartialItemBuyerOrderDetailAddonsBinding
import com.tokopedia.buyerorderdetail.presentation.model.AddonsListUiModel

class AddonsViewHolder(itemView: View) : AbstractViewHolder<AddonsListUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_buyer_order_detail_addons_section
    }

    private val partialItemBuyerOrderDetailAddonsBinding =
        PartialItemBuyerOrderDetailAddonsBinding.bind(this.itemView.findViewById(R.id.addonLayout))

    private var partialProductAddonViewHolder: PartialProductAddonViewHolder? = null

    override fun bind(element: AddonsListUiModel?) {
        if (element == null) return
        partialProductAddonViewHolder =
            PartialProductAddonViewHolder(partialItemBuyerOrderDetailAddonsBinding)
        partialProductAddonViewHolder?.bindViews(element)
    }

    override fun bind(element: AddonsListUiModel?, payloads: MutableList<Any>) {
        payloads.firstOrNull()?.let {
            if (it is Pair<*, *>) {
                val (oldItem, newItem) = it
                if (oldItem is AddonsListUiModel && newItem is AddonsListUiModel) {
                    if (oldItem != newItem) {
                        newItem?.let { addonListUiModel ->
                            partialProductAddonViewHolder?.bindViews(addonListUiModel)
                        }
                    }
                    return
                }
            }
        }
        super.bind(element, payloads)
    }
}
