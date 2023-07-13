package com.tokopedia.checkout.revamp.view.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.checkout.R
import com.tokopedia.checkout.databinding.ItemCheckoutOrderBinding
import com.tokopedia.checkout.revamp.view.adapter.CheckoutAdapterListener
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutOrderModel

class CheckoutOrderViewHolder(private val binding: ItemCheckoutOrderBinding, private val listener: CheckoutAdapterListener) : RecyclerView.ViewHolder(binding.root) {

    fun bind(order: CheckoutOrderModel) {
        renderAddOnOrderLevel(order)
    }

    private fun renderAddOnOrderLevel(order: CheckoutOrderModel) {
            val addOnsDataModel = order.addOnsOrderLevelModel
            val addOnsButton = addOnsDataModel.addOnsButtonModel
            val statusAddOn = addOnsDataModel.status
            if (statusAddOn == 0) {
                binding.buttonGiftingAddonOrderLevel.visibility = View.GONE
            } else {
                if (statusAddOn == 1) {
                    binding.buttonGiftingAddonOrderLevel.state = com.tokopedia.purchase_platform.common.feature.gifting.view.ButtonGiftingAddOnView.State.ACTIVE
                } else if (statusAddOn == 2) {
                    binding.buttonGiftingAddonOrderLevel.state = com.tokopedia.purchase_platform.common.feature.gifting.view.ButtonGiftingAddOnView.State.INACTIVE
                }
                binding.buttonGiftingAddonOrderLevel.visibility = View.VISIBLE
                binding.buttonGiftingAddonOrderLevel.title = addOnsButton.title
                binding.buttonGiftingAddonOrderLevel.desc = addOnsButton.description
                binding.buttonGiftingAddonOrderLevel.urlLeftIcon = addOnsButton.leftIconUrl
                binding.buttonGiftingAddonOrderLevel.urlRightIcon = addOnsButton.rightIconUrl
                binding.buttonGiftingAddonOrderLevel.setOnClickListener {
                    listener.openAddOnGiftingOrderLevelBottomSheet(
                        order
                    )
                }
                listener.addOnGiftingOrderLevelImpression(order.products)
            }
    }

    companion object {
        val VIEW_TYPE = R.layout.item_checkout_order
    }
}
