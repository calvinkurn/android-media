package com.tokopedia.sellerorder.detail.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView.RecycledViewPool
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.order_management_common.presentation.uimodel.AddOnSummaryUiModel
import com.tokopedia.order_management_common.presentation.viewholder.BmgmAddOnSummaryViewHolder
import com.tokopedia.order_management_common.presentation.viewholder.BmgmAddOnViewHolder
import com.tokopedia.sellerorder.detail.presentation.adapter.factory.SomDetailAdapterFactoryImpl.ActionListener
import com.tokopedia.sellerorder.detail.presentation.model.SomDetailAddOnOrderLevelUiModel
import com.tokopedia.order_management_common.R as order_management_commonR

class SomDetailAddOnsOrderLevelViewHolder(
    private val actionListener: ActionListener,
    private val recyclerViewSharedPool: RecycledViewPool,
    itemView: View?
) : AbstractViewHolder<SomDetailAddOnOrderLevelUiModel>(itemView),
    BmgmAddOnViewHolder.Listener,
    BmgmAddOnSummaryViewHolder.Delegate.Mediator,
    BmgmAddOnSummaryViewHolder.Delegate by BmgmAddOnSummaryViewHolder.Delegate.Impl() {

    companion object {
        val LAYOUT = order_management_commonR.layout.item_buyer_order_detail_addon_order_level
    }

    init {
        registerAddOnSummaryDelegate(this)
    }

    override fun bind(element: SomDetailAddOnOrderLevelUiModel?) {
        bindAddonSummary(element?.addOnSummaryUiModel)
    }

    override fun onCopyAddOnDescriptionClicked(label: String, description: CharSequence) {
        actionListener.onCopyAddOnDescription(label, description)
    }

    override fun onAddOnsBmgmExpand(isExpand: Boolean, addOnsIdentifier: String) {
        actionListener.onAddOnsBmgmExpand(isExpand, addOnsIdentifier)
    }

    override fun onAddOnsInfoLinkClicked(infoLink: String, type: String) {
        actionListener.onAddOnsInfoLinkClicked(infoLink, type)
    }

    override fun onAddOnClicked(addOn: AddOnSummaryUiModel.AddonItemUiModel) {}

    override fun getAddOnSummaryLayout(): View? {
        return itemView.findViewById(order_management_commonR.id.itemAddonsOrderViewStub)
    }

    override fun getRecycleViewSharedPool(): RecycledViewPool? {
        return recyclerViewSharedPool
    }

    override fun getAddOnSummaryListener(): BmgmAddOnViewHolder.Listener {
        return this
    }
}
