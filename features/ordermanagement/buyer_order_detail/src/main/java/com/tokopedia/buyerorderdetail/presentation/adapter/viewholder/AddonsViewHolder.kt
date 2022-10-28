package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.databinding.ItemBuyerOrderDetailAddonsSectionBinding
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
}