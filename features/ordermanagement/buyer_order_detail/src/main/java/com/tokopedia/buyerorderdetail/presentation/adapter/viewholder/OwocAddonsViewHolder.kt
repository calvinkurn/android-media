package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.listener.OwocRecyclerviewPoolListener
import com.tokopedia.buyerorderdetail.presentation.model.OwocAddonsListUiModel
import com.tokopedia.order_management_common.presentation.uimodel.AddOnSummaryUiModel
import com.tokopedia.order_management_common.presentation.viewholder.BmgmAddOnSummaryViewHolder
import com.tokopedia.order_management_common.presentation.viewholder.BmgmAddOnViewHolder
import com.tokopedia.order_management_common.R as order_management_commonR

class OwocAddonsViewHolder(
    itemView: View,
    private val recyclerviewPoolListener: OwocRecyclerviewPoolListener
) : AbstractViewHolder<OwocAddonsListUiModel>(itemView),
    BmgmAddOnViewHolder.Listener,
    BmgmAddOnSummaryViewHolder.Delegate.Mediator,
    BmgmAddOnSummaryViewHolder.Delegate by BmgmAddOnSummaryViewHolder.Delegate.Impl() {

    companion object {
        @LayoutRes
        val LAYOUT = order_management_commonR.layout.item_buyer_order_detail_addon_order_level
    }

    override fun bind(element: OwocAddonsListUiModel?) {
        registerAddOnSummaryDelegate(this)
        bindAddonSummary(element?.addOnSummaryUiModel)
    }

    override fun bind(element: OwocAddonsListUiModel?, payloads: MutableList<Any>) {
        payloads.firstOrNull()?.let {
            if (it is Pair<*, *>) {
                val (oldItem, newItem) = it
                if (oldItem is OwocAddonsListUiModel && newItem is OwocAddonsListUiModel) {
                    if (oldItem != newItem) {
                        registerAddOnSummaryDelegate(this)
                        bindAddonSummary(element?.addOnSummaryUiModel)
                    }
                    return
                }
            }
        }
        super.bind(element, payloads)
    }

    override fun onCopyAddOnDescriptionClicked(label: String, description: CharSequence) {
    }

    override fun onAddOnsBmgmExpand(isExpand: Boolean, addOnsIdentifier: String) {
    }

    override fun onAddOnsInfoLinkClicked(infoLink: String, type: String) {
    }

    override fun onAddOnClicked(addOn: AddOnSummaryUiModel.AddonItemUiModel) {}

    override fun getAddOnSummaryLayout(): View? {
        return itemView.findViewById(order_management_commonR.id.itemAddonsOrderViewStub)
    }

    override fun getRecycleViewSharedPool(): RecyclerView.RecycledViewPool? {
        return recyclerviewPoolListener.parentPool
    }

    override fun getAddOnSummaryListener(): BmgmAddOnViewHolder.Listener {
        return this
    }
}
