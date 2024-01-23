package com.tokopedia.sellerorder.detail.presentation.adapter.viewholder

import android.view.View
import android.view.ViewStub
import androidx.recyclerview.widget.RecyclerView.RecycledViewPool
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.order_management_common.databinding.PartialBmgmAddOnSummaryBinding
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
    BmgmAddOnViewHolder.Listener {
    companion object {
        val LAYOUT = order_management_commonR.layout.item_buyer_order_detail_addon_order_level
    }

    private var addOnSummaryViewHolder: BmgmAddOnSummaryViewHolder? = null

    private var partialAddonSummaryBinding: PartialBmgmAddOnSummaryBinding? = null

    override fun bind(element: SomDetailAddOnOrderLevelUiModel?) {
        if (element == null) return
        setupAddonSection(element.addOnSummaryUiModel)
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
                        recyclerViewSharedPool
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
        actionListener.onCopyAddOnDescription(label, description)
    }

    override fun onAddOnsBmgmExpand(isExpand: Boolean, addOnsIdentifier: String) {
        actionListener.onAddOnsBmgmExpand(isExpand, addOnsIdentifier)
    }

    override fun onAddOnsInfoLinkClicked(infoLink: String, type: String) {
        actionListener.onAddOnsInfoLinkClicked(infoLink, type)
    }

    override fun onAddOnClicked(addOn: AddOnSummaryUiModel.AddonItemUiModel) {}
}
