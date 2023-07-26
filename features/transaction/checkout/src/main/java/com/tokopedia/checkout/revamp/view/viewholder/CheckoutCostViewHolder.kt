package com.tokopedia.checkout.revamp.view.viewholder

import android.content.Context
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.checkout.R
import com.tokopedia.checkout.databinding.ItemCheckoutCostBinding
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutCostModel
import com.tokopedia.kotlin.extensions.view.setTextAndContentDescription
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.utils.currency.CurrencyFormatUtil

class CheckoutCostViewHolder(
    private val binding: ItemCheckoutCostBinding,
    private val layoutInflater: LayoutInflater
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(cost: CheckoutCostModel) {
        binding.tvCheckoutCostItemPriceTitle.text = getTotalItemLabel(binding.tvCheckoutCostItemPriceTitle.context, cost.totalItem)
        binding.tvCheckoutCostItemPriceValue.setTextAndContentDescription(
            if (cost.totalItemPrice == 0.0) {
                "-"
            } else {
                CurrencyFormatUtil.convertPriceValueToIdrFormat(
                    cost.totalItemPrice,
                    false
                ).removeDecimalSuffix()
            },
            R.string.content_desc_tv_total_item_price_summary
        )
        binding.tvCheckoutCostShippingValue.setTextAndContentDescription(
            if (cost.shippingFee == 0.0) {
                "-"
            } else {
                CurrencyFormatUtil.convertPriceValueToIdrFormat(
                    cost.shippingFee,
                    false
                ).removeDecimalSuffix()
            },
            R.string.content_desc_tv_shipping_fee_summary
        )
    }

    private fun getTotalItemLabel(context: Context, totalItem: Int): String {
        return String.format(context.getString(R.string.label_item_count_summary_with_format), totalItem)
    }

    companion object {
        val VIEW_TYPE = R.layout.item_checkout_cost
    }
}
