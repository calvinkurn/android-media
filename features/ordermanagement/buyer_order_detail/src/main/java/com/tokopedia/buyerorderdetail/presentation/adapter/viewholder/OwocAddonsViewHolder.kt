package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.common.utils.BuyerOrderDetailNavigator
import com.tokopedia.buyerorderdetail.databinding.PartialItemOwocAddonsBinding
import com.tokopedia.buyerorderdetail.presentation.model.OwocAddonsListUiModel

class OwocAddonsViewHolder(
    itemView: View,
    private val navigator: BuyerOrderDetailNavigator?
) : AbstractViewHolder<OwocAddonsListUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_owoc_addons_section
    }

    private val partialItemOwocAddonsBinding =
        PartialItemOwocAddonsBinding.bind(this.itemView.findViewById(R.id.owocAddonLayout))

    private var owocPartialProductAddonViewHolder: OwocPartialProductAddonViewHolder? = null

    override fun bind(element: OwocAddonsListUiModel?) {
        if (element == null) return
        owocPartialProductAddonViewHolder =
            OwocPartialProductAddonViewHolder(partialItemOwocAddonsBinding, navigator)
        owocPartialProductAddonViewHolder?.bindViews(element)
    }

    override fun bind(element: OwocAddonsListUiModel?, payloads: MutableList<Any>) {
        payloads.firstOrNull()?.let {
            if (it is Pair<*, *>) {
                val (oldItem, newItem) = it
                if (oldItem is OwocAddonsListUiModel && newItem is OwocAddonsListUiModel) {
                    if (oldItem != newItem) {
                        newItem?.let { owocAddonListUiModel ->
                            owocPartialProductAddonViewHolder?.bindViews(owocAddonListUiModel)
                        }
                    }
                    return
                }
            }
        }
        super.bind(element, payloads)
    }
}
