package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.view.View
import android.view.ViewStub
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorderdetail.common.utils.BuyerOrderDetailNavigator
import com.tokopedia.buyerorderdetail.presentation.adapter.listener.OwocRecyclerviewPoolListener
import com.tokopedia.buyerorderdetail.presentation.model.OwocAddonsListUiModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.order_management_common.databinding.PartialBmgmAddOnSummaryBinding
import com.tokopedia.order_management_common.presentation.uimodel.AddOnSummaryUiModel
import com.tokopedia.order_management_common.presentation.viewholder.BmgmAddOnSummaryViewHolder
import com.tokopedia.order_management_common.presentation.viewholder.BmgmAddOnViewHolder
import com.tokopedia.order_management_common.R as order_management_commonR

class OwocAddonsViewHolder(
    itemView: View,
    private val navigator: BuyerOrderDetailNavigator?,
    private val recyclerviewPoolListener: OwocRecyclerviewPoolListener
) : AbstractViewHolder<OwocAddonsListUiModel>(itemView),
    BmgmAddOnViewHolder.Listener {

    companion object {
        @LayoutRes
        val LAYOUT = order_management_commonR.layout.item_buyer_order_detail_addon_order_level
    }

    private var addOnSummaryViewHolder: BmgmAddOnSummaryViewHolder? = null

    private var partialAddonSummaryBinding: PartialBmgmAddOnSummaryBinding? = null

    override fun bind(element: OwocAddonsListUiModel?) {
        if (element == null) return
        setupAddonSection(element.addOnSummaryUiModel)
    }

    override fun bind(element: OwocAddonsListUiModel?, payloads: MutableList<Any>) {
        payloads.firstOrNull()?.let {
            if (it is Pair<*, *>) {
                val (oldItem, newItem) = it
                if (oldItem is OwocAddonsListUiModel && newItem is OwocAddonsListUiModel) {
                    if (oldItem != newItem) {
                        newItem.let { owocAddonListUiModel ->
                            addOnSummaryViewHolder?.bind(owocAddonListUiModel.addOnSummaryUiModel)
                        }
                    }
                    return
                }
            }
        }
        super.bind(element, payloads)
    }

    private fun setupAddonSection(addOnSummaryUiModel: AddOnSummaryUiModel?) {
        val addonsViewStub: View =
            itemView.findViewById(order_management_commonR.id.itemAddonsOrderViewStub)
        if (addOnSummaryUiModel?.addonItemList?.isNotEmpty() == true) {
            if (addonsViewStub is ViewStub) addonsViewStub.inflate() else addonsViewStub.show()
            setupAddonsBinding()
            addOnSummaryViewHolder =
                partialAddonSummaryBinding?.let {
                    BmgmAddOnSummaryViewHolder(
                        this,
                        it,
                        recyclerviewPoolListener.parentPool
                    )
                }
            addOnSummaryViewHolder?.bind(addOnSummaryUiModel)
        } else {
            addonsViewStub.hide()
        }
    }

    private fun setupAddonsBinding() {
        if (partialAddonSummaryBinding == null) {
            partialAddonSummaryBinding =
                PartialBmgmAddOnSummaryBinding
                    .bind(this.itemView.findViewById(order_management_commonR.id.itemAddonsOrderViewStub))
        }
    }

    override fun onCopyAddOnDescriptionClicked(label: String, description: CharSequence) {
    }

    override fun onAddOnsBmgmExpand(isExpand: Boolean, addOnsIdentifier: String) {
    }

    override fun onAddOnsInfoLinkClicked(infoLink: String, type: String) {
    }

    override fun onAddOnClicked(addOn: AddOnSummaryUiModel.AddonItemUiModel) {}
}
