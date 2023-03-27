package com.tokopedia.checkout.view.viewholder

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.checkout.R
import com.tokopedia.checkout.databinding.ViewItemShipmentCostDetailsBinding
import com.tokopedia.checkout.view.uimodel.ShipmentCostModel
import com.tokopedia.kotlin.extensions.view.setTextAndContentDescription
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.currency.CurrencyFormatUtil.convertPriceValueToIdrFormat

class ShipmentCostViewHolder(private val binding: ViewItemShipmentCostDetailsBinding, private val layoutInflater: LayoutInflater) : RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("StringFormatInvalid")
    fun bindViewHolder(shipmentCost: ShipmentCostModel) {
        binding.rlShipmentCost.visibility = View.VISIBLE
        binding.tvTotalItemLabel.text = getTotalItemLabel(binding.tvTotalItemLabel.context, shipmentCost.totalItem)
        binding.tvTotalItemPrice.setTextAndContentDescription(if (shipmentCost.totalItemPrice == 0.0) "-" else convertPriceValueToIdrFormat(shipmentCost.totalItemPrice.toLong(), false).removeDecimalSuffix(), R.string.content_desc_tv_total_item_price_summary)
        binding.tvShippingFeeLabel.text = binding.tvShippingFeeLabel.context.getString(com.tokopedia.purchase_platform.common.R.string.label_shipment_fee)
        binding.tvShippingFee.setTextAndContentDescription(getPriceFormat(binding.tvShippingFeeLabel, binding.tvShippingFee, shipmentCost.shippingFee), R.string.content_desc_tv_shipping_fee_summary)
        binding.tvInsuranceFee.setTextAndContentDescription(getPriceFormat(binding.tvInsuranceFeeLabel, binding.tvInsuranceFee, shipmentCost.insuranceFee), R.string.content_desc_tv_insurance_fee_summary)
        binding.tvOrderPrioritasPrice.text = getPriceFormat(binding.tvOrderPrioritasLabel, binding.tvOrderPrioritasPrice, shipmentCost.priorityFee)
        binding.tvPurchaseProtectionLabel.text = getTotalPurchaseProtectionItemLabel(binding.tvPurchaseProtectionLabel.context, shipmentCost.totalPurchaseProtectionItem)
        binding.tvPurchaseProtectionFee.text = getPriceFormat(binding.tvPurchaseProtectionLabel, binding.tvPurchaseProtectionFee, shipmentCost.purchaseProtectionFee)
        binding.tvPromo.text = String.format(
            binding.tvPromo.context.getString(R.string.promo_format),
            getPriceFormat(binding.tvPromoOrCouponLabel, binding.tvPromo, shipmentCost.promoPrice)
        )
        binding.tvSellerCostAdditionFee.text = getPriceFormat(binding.tvSellerCostAddition, binding.tvSellerCostAdditionFee, shipmentCost.additionalFee)
        binding.tvDonationPrice.text = getPriceFormat(binding.tvDonationLabel, binding.tvDonationPrice, shipmentCost.donation)
        if (shipmentCost.listCrossSell.isEmpty()) {
            binding.llCrossSell.removeAllViews()
            binding.llCrossSell.visibility = View.GONE
        } else {
            binding.llCrossSell.removeAllViews()
            shipmentCost.listCrossSell.forEach { crossSell ->
                val itemCrossSellView = layoutInflater.inflate(R.layout.item_summary_transaction_cross_sell, null, false) as RelativeLayout

                val crossSellItemLabel = itemCrossSellView.findViewById<Typography>(R.id.tv_cross_sell_label)
                crossSellItemLabel.text = crossSell.crossSellModel.orderSummary.title

                val crossSellItemPrice = itemCrossSellView.findViewById<Typography>(R.id.tv_cross_sell_price)
                crossSellItemPrice.text = getPriceFormat(crossSellItemLabel, crossSellItemPrice, crossSell.crossSellModel.price)
                binding.llCrossSell.addView(itemCrossSellView)
            }
            binding.llCrossSell.visibility = View.VISIBLE
        }
        binding.tvEmasPrice.text = getPriceFormat(binding.tvEmasLabel, binding.tvEmasPrice, shipmentCost.emasPrice)
        binding.tvTradeIn.text = String.format(
            binding.tvTradeIn.context.getString(R.string.promo_format),
            getPriceFormat(binding.tvTradeInLabel, binding.tvTradeIn, shipmentCost.tradeInPrice)
        )
        if (shipmentCost.bookingFee > 0) {
            binding.tvBookingFeeLabel.visibility = View.VISIBLE
            binding.tvBookingFeePrice.visibility = View.VISIBLE
            binding.tvBookingFeePrice.text = shipmentCost.bookingFee.toString() + ""
        } else {
            binding.tvBookingFeeLabel.visibility = View.GONE
            binding.tvBookingFeePrice.visibility = View.GONE
        }
        binding.tvBookingFeePrice.text = getPriceFormat(binding.tvBookingFeeLabel, binding.tvBookingFeePrice, shipmentCost.bookingFee.toDouble())
        renderDiscount(shipmentCost)
        renderAddOnCost(shipmentCost)
    }

    private fun renderDiscount(shipmentCost: ShipmentCostModel) {
        if (shipmentCost.isHasDiscountDetails) {
            renderShippingDiscount(shipmentCost)
            renderProductDiscount(shipmentCost)
            binding.tvDiscountLabel.visibility = View.GONE
            binding.tvDiscountPrice.visibility = View.GONE
        } else {
            renderGeneralDiscount(shipmentCost)
            binding.tvShippingDiscountLabel.visibility = View.GONE
            binding.tvShippingDiscountPrice.visibility = View.GONE
            binding.tvProductDiscountLabel.visibility = View.GONE
            binding.tvProductDiscountPrice.visibility = View.GONE
        }
    }

    private fun renderProductDiscount(shipmentCost: ShipmentCostModel) {
        if (shipmentCost.productDiscountAmount > 0) {
            binding.tvProductDiscountLabel.text = binding.tvProductDiscountLabel.context.getString(com.tokopedia.purchase_platform.common.R.string.label_product_discount)
            binding.tvProductDiscountPrice.text = getPriceFormat(binding.tvProductDiscountLabel, binding.tvProductDiscountPrice, (shipmentCost.productDiscountAmount * -1).toDouble())
        } else {
            binding.tvProductDiscountLabel.visibility = View.GONE
            binding.tvProductDiscountPrice.visibility = View.GONE
        }
    }

    private fun renderShippingDiscount(shipmentCost: ShipmentCostModel) {
        binding.tvShippingDiscountLabel.text = binding.tvShippingDiscountLabel.context.getString(com.tokopedia.purchase_platform.common.R.string.label_shipping_discount)
        if (shipmentCost.shippingDiscountAmount > 0) {
            if (shipmentCost.shippingDiscountAmount >= shipmentCost.shippingFee) {
                binding.tvShippingFee.setTextAndContentDescription(convertPriceValueToIdrFormat(0.0, false).removeDecimalSuffix(), R.string.content_desc_tv_shipping_fee_summary)
                binding.tvShippingDiscountPrice.visibility = View.GONE
                binding.tvShippingDiscountLabel.visibility = View.GONE
            } else {
                binding.tvShippingDiscountPrice.text = getPriceFormat(binding.tvShippingDiscountLabel, binding.tvShippingDiscountPrice, (shipmentCost.shippingDiscountAmount * -1).toDouble())
            }
        } else {
            binding.tvShippingDiscountLabel.visibility = View.GONE
            binding.tvShippingDiscountPrice.visibility = View.GONE
        }
    }

    private fun renderGeneralDiscount(shipmentCost: ShipmentCostModel) {
        binding.tvDiscountLabel.text = binding.tvDiscountLabel.context.getString(R.string.label_total_discount)
        binding.tvDiscountPrice.text = getPriceFormat(binding.tvDiscountLabel, binding.tvDiscountPrice, (shipmentCost.discountAmount * -1).toDouble())
    }

    private fun renderAddOnCost(shipmentCost: ShipmentCostModel) {
        if (shipmentCost.hasAddOn) {
            binding.tvSummaryAddOnLabel.text = binding.root.context.getString(R.string.label_add_on_cost)

            // exclusion : need to write if totalAddOnPrice is Rp 0
            binding.tvSummaryAddOnLabel.visibility = View.VISIBLE
            binding.tvSummaryAddOnPrice.visibility = View.VISIBLE
            binding.tvSummaryAddOnPrice.text = convertPriceValueToIdrFormat(shipmentCost.totalAddOnPrice.toLong(), false).removeDecimalSuffix()
        } else {
            binding.tvSummaryAddOnLabel.visibility = View.GONE
            binding.tvSummaryAddOnPrice.visibility = View.GONE
        }
    }

    private fun getTotalItemLabel(context: Context, totalItem: Int): String {
        return String.format(context.getString(R.string.label_item_count_summary_with_format), totalItem)
    }

    private fun getTotalPurchaseProtectionItemLabel(context: Context, totalItem: Int): String {
        return String.format(context.getString(R.string.label_item_count_purchase_protection), totalItem)
    }

    private fun getPriceFormat(textViewLabel: TextView, textViewPrice: TextView, price: Double): String {
        return if (price == 0.0) {
            textViewLabel.visibility = View.GONE
            textViewPrice.visibility = View.GONE
            "-"
        } else {
            textViewLabel.visibility = View.VISIBLE
            textViewPrice.visibility = View.VISIBLE
            convertPriceValueToIdrFormat(price.toLong(), false).removeDecimalSuffix()
        }
    }

    companion object {
        @JvmField
        val ITEM_VIEW_SHIPMENT_COST = R.layout.view_item_shipment_cost_details
    }
}
