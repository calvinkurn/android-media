package com.tokopedia.checkout.revamp.view.viewholder

import android.annotation.SuppressLint
import android.widget.CompoundButton
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
import com.tokopedia.checkout.R
import com.tokopedia.checkout.revamp.view.adapter.CheckoutAdapterListener

class CheckoutCrossSellItemViewHolder(private val binding: ItemCheckoutCrossSellItemBinding, private val listener: CheckoutAdapterListener) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(checkoutCrossSellItem: CheckoutCrossSellItem, parentWidth: Int) {
        val expectedWidth = parentWidth - 50.dpToPx(binding.root.context.resources.displayMetrics)
        binding.root.layoutParams.width = expectedWidth
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
        itemBinding.ivCheckoutCrossSellItem.setImageResource(R.drawable.ic_donasi_lingkungan)
        itemBinding.tvCheckoutCrossSellItem.text = donationModel.donation.title
    }

    @SuppressLint("SetTextI18n")
    private fun renderEgold(
        egoldModel: CheckoutEgoldModel,
        itemBinding: ItemCheckoutCrossSellItemBinding
    ) {
        itemBinding.ivCheckoutCrossSellItem.setImageResource(R.drawable.ic_logam_mulia)
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
        itemBinding.cbCheckoutCrossSellItem.setOnCheckedChangeListener { _, _ -> }
        itemBinding.cbCheckoutCrossSellItem.isChecked = egoldModel.egoldAttributeModel.isChecked
        itemBinding.cbCheckoutCrossSellItem.skipAnimation()
        itemBinding.cbCheckoutCrossSellItem.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            if (egoldModel.egoldAttributeModel.isEnabled) {
                listener.onEgoldChecked(isChecked)
            }
        }
    }
}
