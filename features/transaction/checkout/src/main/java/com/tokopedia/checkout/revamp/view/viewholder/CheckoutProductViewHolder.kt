package com.tokopedia.checkout.revamp.view.viewholder

import android.annotation.SuppressLint
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.checkout.R
import com.tokopedia.checkout.databinding.ItemCheckoutProductBinding
import com.tokopedia.checkout.revamp.view.adapter.CheckoutAdapterListener
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutProductModel
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.utils.currency.CurrencyFormatUtil

class CheckoutProductViewHolder(
    private val binding: ItemCheckoutProductBinding,
    private val actionListener: CheckoutAdapterListener
) : RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("SetTextI18n")
    fun bind(product: CheckoutProductModel) {
        binding.ivProductImage.setImageUrl(product.imageUrl)
        binding.tvProductName.text = product.name
        if (product.variant.isNotBlank()) {
            binding.textVariant.text = product.variant
            binding.textVariant.isVisible = true
        } else {
            binding.textVariant.isVisible = false
        }
        val priceInRp =
            CurrencyFormatUtil.convertPriceValueToIdrFormat(product.price, false)
                .removeDecimalSuffix()
        val qty = product.quantity
        binding.tvProductPrice.text = "$qty x $priceInRp"

        if (product.noteToSeller.isNotEmpty()) {
            binding.tvOptionalNoteToSeller.text = product.noteToSeller
            binding.tvOptionalNoteToSeller.isVisible = true
        } else {
            binding.tvOptionalNoteToSeller.isVisible = false
        }
    }

    companion object {
        val VIEW_TYPE = R.layout.item_checkout_product
    }
}
