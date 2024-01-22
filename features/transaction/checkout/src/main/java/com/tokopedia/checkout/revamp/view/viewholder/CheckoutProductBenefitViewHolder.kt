package com.tokopedia.checkout.revamp.view.viewholder

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.checkout.R
import com.tokopedia.checkout.databinding.ItemCheckoutProductBenefitBinding
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutProductBenefitModel
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.media.loader.loadImage
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.utils.currency.CurrencyFormatUtil

class CheckoutProductBenefitViewHolder(
    private val binding: ItemCheckoutProductBenefitBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(benefit: CheckoutProductBenefitModel) {
        binding.tvCheckoutBenefitHeader.text = benefit.headerText
        binding.tvCheckoutBenefitHeader.isVisible = benefit.shouldShowHeader

        val displayMetrics = binding.root.context.resources.displayMetrics
        val leftMargin = LEFT_MARGIN.dpToPx(displayMetrics)
        val topMargin = TOP_MARGIN.dpToPx(displayMetrics)
        if (benefit.shouldShowHeader) {
            binding.ivProductImageFrameBenefit.setMargin(
                leftMargin,
                topMargin,
                0,
                0
            )
        } else {
            binding.ivProductImageFrameBenefit.setMargin(
                leftMargin,
                topMargin * 2,
                0,
                0
            )
        }

        binding.ivProductImageBenefit.loadImage(benefit.imageUrl)
        binding.tvProductNameBenefit.text = benefit.productName

        val priceInRp =
            CurrencyFormatUtil.convertPriceValueToIdrFormat(benefit.finalPrice, false)
                .removeDecimalSuffix()
        val qty = benefit.quantity
        binding.tvProductPriceBenefit.text = "$qty x $priceInRp"
    }

    companion object {
        val VIEW_TYPE = R.layout.item_checkout_product_benefit

        private const val LEFT_MARGIN = 8
        private const val TOP_MARGIN = 12
    }
}
