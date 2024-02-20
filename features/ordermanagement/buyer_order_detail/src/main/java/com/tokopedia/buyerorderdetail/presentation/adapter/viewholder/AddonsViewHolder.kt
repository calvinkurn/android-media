package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.presentation.model.ProductListUiModel
import com.tokopedia.order_management_common.presentation.viewholder.BmgmAddOnSummaryViewHolder
import com.tokopedia.order_management_common.presentation.viewholder.BmgmAddOnViewHolder

class AddonsViewHolder(
    itemView: View,
    private val addOnListener: BmgmAddOnViewHolder.Listener
) : AbstractViewHolder<ProductListUiModel.OrderLevelAddOn>(itemView),
    BmgmAddOnSummaryViewHolder.Delegate.Mediator,
    BmgmAddOnSummaryViewHolder.Delegate by BmgmAddOnSummaryViewHolder.Delegate.Impl() {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_buyer_order_detail_addons_section
    }

    override fun bind(element: ProductListUiModel.OrderLevelAddOn?) {
        registerAddOnSummaryDelegate(this)
        bindAddonSummary(element?.addOnSummaryUiModel)
    }

    override fun bind(element: ProductListUiModel.OrderLevelAddOn?, payloads: MutableList<Any>) {
        payloads.firstOrNull()?.let {
            if (it is Pair<*, *>) {
                val (oldItem, newItem) = it
                if (oldItem is ProductListUiModel.OrderLevelAddOn && newItem is ProductListUiModel.OrderLevelAddOn) {
                    if (oldItem != newItem) bind(newItem)
                    return
                }
            }
        }
        super.bind(element, payloads)
    }

    override fun getAddOnSummaryLayout(): View? {
        return itemView.findViewById(R.id.layout_bom_detail_order_level_add_on)
    }

    override fun getRecycleViewSharedPool(): RecyclerView.RecycledViewPool? {
        return null
    }

    override fun getAddOnSummaryListener(): BmgmAddOnViewHolder.Listener {
        return addOnListener
    }
}
