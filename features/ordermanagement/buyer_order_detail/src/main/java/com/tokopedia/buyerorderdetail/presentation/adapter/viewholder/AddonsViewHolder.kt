package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.databinding.ItemBuyerOrderDetailAddonsListBinding
import com.tokopedia.buyerorderdetail.databinding.PartialItemBuyerOrderDetailAddonsBinding
import com.tokopedia.buyerorderdetail.presentation.model.AddonsUiModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show

class AddonsViewHolder(itemView: View) : AbstractViewHolder<AddonsUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_buyer_order_detail_addons_list
    }

    private val binding = ItemBuyerOrderDetailAddonsListBinding.bind(this.itemView)

    override fun bind(element: AddonsUiModel?) {
        if (element == null) return
        with(binding) {
            itemBomDetailAddonsSection.run {
                ivBomDetailAddonsIcon.setImageUrl(element.addonsLogoUrl)
                tvBomDetailAddonsTitle.text = element.addonsTitle
                tvBomDetailAddonsName.text = element.addOnsName
                ivBomDetailAddonsThumbnail.setImageUrl(element.addOnsThumbnailUrl)
                setupToPerson(element.toPerson)
                setupFromMessage(element.fromPerson)
            }
        }
    }

    private fun PartialItemBuyerOrderDetailAddonsBinding.setupToPerson(toPerson: String) {
        if (toPerson.isBlank()) {
            tvBomDetailAddonsToValue.hide()
            tvBomDetailAddonsToLabel.hide()
        } else {
            tvBomDetailAddonsToValue.show()
            tvBomDetailAddonsToLabel.show()
        }
    }

    private fun PartialItemBuyerOrderDetailAddonsBinding.setupFromMessage(fromPerson: String) {
        if (fromPerson.isBlank()) {
            tvBomDetailAddonsFromLabel.hide()
            tvBomDetailAddonsToValue.hide()
        } else {
            tvBomDetailAddonsFromLabel.show()
            tvBomDetailAddonsToValue.show()
        }
    }
}