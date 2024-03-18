package com.tokopedia.sellerorder.detail.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView.RecycledViewPool
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.order_management_common.presentation.viewholder.AddOnSummaryViewHolder
import com.tokopedia.order_management_common.presentation.viewholder.AddOnViewHolder
import com.tokopedia.sellerorder.detail.presentation.model.SomDetailAddOnOrderLevelUiModel
import com.tokopedia.order_management_common.R as order_management_commonR

class SomDetailAddOnsOrderLevelViewHolder(
    private val addOnListener: AddOnViewHolder.Listener,
    private val recyclerViewSharedPool: RecycledViewPool,
    itemView: View?
) : AbstractViewHolder<SomDetailAddOnOrderLevelUiModel>(itemView),
    AddOnSummaryViewHolder.Delegate.Mediator,
    AddOnSummaryViewHolder.Delegate by AddOnSummaryViewHolder.Delegate.Impl() {

    companion object {
        val LAYOUT = order_management_commonR.layout.item_buyer_order_detail_addon_order_level
    }

    init {
        registerAddOnSummaryDelegate(this)
    }

    override fun bind(element: SomDetailAddOnOrderLevelUiModel?) {
        bindAddonSummary(element?.addOnSummaryUiModel)
    }

    override fun getAddOnSummaryLayout(): View? {
        return itemView.findViewById(order_management_commonR.id.itemAddonsOrderViewStub)
    }

    override fun getRecycleViewSharedPool(): RecycledViewPool? {
        return recyclerViewSharedPool
    }

    override fun getAddOnSummaryListener(): AddOnViewHolder.Listener {
        return addOnListener
    }
}
