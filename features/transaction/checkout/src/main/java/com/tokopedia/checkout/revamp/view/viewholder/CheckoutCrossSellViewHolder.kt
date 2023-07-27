package com.tokopedia.checkout.revamp.view.viewholder

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.checkout.R
import com.tokopedia.checkout.databinding.ItemCheckoutCrossSellBinding
import com.tokopedia.checkout.databinding.ItemCheckoutCrossSellItemBinding
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutCrossSellGroupModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutCrossSellItem
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutCrossSellModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutDonationModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutEgoldModel

class CheckoutCrossSellViewHolder(private val binding: ItemCheckoutCrossSellBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(checkoutCrossSellGroupModel: CheckoutCrossSellGroupModel) {
        if (checkoutCrossSellGroupModel.crossSellList.isEmpty()) {
            binding.rvCheckoutCrossSell.isVisible = false
            binding.itemCheckoutCrossSellItem.root.isVisible = false
        } else if (checkoutCrossSellGroupModel.crossSellList.size == 1) {
            val crossSellItem = checkoutCrossSellGroupModel.crossSellList[0]
            renderCrossSellSingleItem(crossSellItem)
        } else {
            binding.rvCheckoutCrossSell.isVisible = true
            binding.itemCheckoutCrossSellItem.root.isVisible = false
        }
    }

    private fun renderCrossSellSingleItem(crossSellItem: CheckoutCrossSellItem) {
        renderCrossSellItem(crossSellItem)
        binding.rvCheckoutCrossSell.isVisible = false
        binding.itemCheckoutCrossSellItem.dividerCheckoutCrossSellItem.isVisible = false
        binding.itemCheckoutCrossSellItem.root.isVisible = true
    }

    private fun renderCrossSellItem(crossSellItem: CheckoutCrossSellItem) {
        when(crossSellItem) {
            is CheckoutCrossSellModel -> renderCrossSell(crossSellItem, binding.itemCheckoutCrossSellItem)
            is CheckoutDonationModel -> renderDonation(crossSellItem, binding.itemCheckoutCrossSellItem)
            is CheckoutEgoldModel -> renderEgold(crossSellItem, binding.itemCheckoutCrossSellItem)
        }
    }

    private fun renderCrossSell(crossSellModel: CheckoutCrossSellModel, itemBinding: ItemCheckoutCrossSellItemBinding) {
        itemBinding.tvCheckoutCrossSellItem.text = crossSellModel.crossSellModel.info.title
    }

    private fun renderDonation(donationModel: CheckoutDonationModel, itemBinding: ItemCheckoutCrossSellItemBinding) {
        itemBinding.tvCheckoutCrossSellItem.text = donationModel.donation.title
    }

    private fun renderEgold(egoldModel: CheckoutEgoldModel, itemBinding: ItemCheckoutCrossSellItemBinding) {
        itemBinding.tvCheckoutCrossSellItem.text = egoldModel.egoldAttributeModel.titleText
    }

    companion object {
        val VIEW_TYPE = R.layout.item_checkout_cross_sell
    }
}
