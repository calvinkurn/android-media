package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.databinding.ItemBuyerOrderDetailAddonsSectionBinding
import com.tokopedia.buyerorderdetail.presentation.model.AddonsListUiModel

class AddonsViewHolder(itemView: View) : AbstractViewHolder<AddonsListUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_buyer_order_detail_addons_section
    }

    private val binding = ItemBuyerOrderDetailAddonsSectionBinding.bind(this.itemView)

    override fun bind(element: AddonsListUiModel?) {
        if (element == null) return
        with(binding) {
            setupViews(element)
        }
    }

    private fun ItemBuyerOrderDetailAddonsSectionBinding.setupViews(element: AddonsListUiModel) {
        itemBomDetailAddonsSection.run {
            tvBomDetailAddonsTitle.text = element.addonsTitle
            ivBomDetailAddonsIcon.setImageUrl(element.addonsLogoUrl)
            tvBomDetailAddonsTotalPriceValue.text = element.totalPriceText
        }
    }

    private fun ItemBuyerOrderDetailAddonsSectionBinding.setupRecyclerviewAddonList() {
        itemBomDetailAddonsSection
    }
}