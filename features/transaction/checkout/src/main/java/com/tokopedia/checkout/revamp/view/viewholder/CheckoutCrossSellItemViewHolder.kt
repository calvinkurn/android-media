package com.tokopedia.checkout.revamp.view.viewholder

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.checkout.databinding.ItemCheckoutCrossSellItemBinding
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutCrossSellItem
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutCrossSellModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutDonationModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutEgoldModel
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.utils.currency.CurrencyFormatUtil

class CheckoutCrossSellItemViewHolder(private val binding: ItemCheckoutCrossSellItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(checkoutCrossSellItem: CheckoutCrossSellItem, parent: ViewGroup?) {
        val parentWidth = parent?.width ?: 0
        val defaultWidth = 340.dpToPx(binding.root.context.resources.displayMetrics)
        val expectedWidth = parentWidth - 40.dpToPx(binding.root.context.resources.displayMetrics)
        binding.root.maxWidth = if (expectedWidth > 0) {
            minOf(
                defaultWidth,
                expectedWidth
            )
        } else {
            defaultWidth
        }
        renderCrossSellItem(checkoutCrossSellItem)
    }

    private fun renderCrossSellItem(crossSellItem: CheckoutCrossSellItem) {
        when (crossSellItem) {
            is CheckoutCrossSellModel -> renderCrossSell(
                crossSellItem,
                binding
            )

            is CheckoutDonationModel -> renderDonation(
                crossSellItem,
                binding
            )

            is CheckoutEgoldModel -> renderEgold(crossSellItem, binding)
        }
    }

    private fun renderCrossSell(
        crossSellModel: CheckoutCrossSellModel,
        itemBinding: ItemCheckoutCrossSellItemBinding
    ) {
        itemBinding.tvCheckoutCrossSellItem.text = crossSellModel.crossSellModel.info.title
    }

    private fun renderDonation(
        donationModel: CheckoutDonationModel,
        itemBinding: ItemCheckoutCrossSellItemBinding
    ) {
        itemBinding.tvCheckoutCrossSellItem.text = donationModel.donation.title
    }

    @SuppressLint("SetTextI18n")
    private fun renderEgold(
        egoldModel: CheckoutEgoldModel,
        itemBinding: ItemCheckoutCrossSellItemBinding
    ) {
        val text = "${egoldModel.egoldAttributeModel.titleText} (${
            CurrencyFormatUtil.convertPriceValueToIdrFormat(
                egoldModel.egoldAttributeModel.buyEgoldValue,
                false
            ).removeDecimalSuffix()
        })"
        if (egoldModel.egoldAttributeModel.isEnabled) {
            itemBinding.cbCheckoutCrossSellItem.isEnabled = true
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                itemBinding.root.foreground = ContextCompat.getDrawable(
                    binding.root.context,
                    com.tokopedia.purchase_platform.common.R.drawable.fg_enabled_item
                )
            }
        } else {
            itemBinding.cbCheckoutCrossSellItem.isEnabled = false
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                itemBinding.root.foreground = ContextCompat.getDrawable(
                    binding.root.context,
                    com.tokopedia.purchase_platform.common.R.drawable.fg_disabled_item
                )
            }
        }
        itemBinding.tvCheckoutCrossSellItem.text = text
    }
}
