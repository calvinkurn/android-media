package com.tokopedia.checkout.view.viewholder

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.checkout.R
import com.tokopedia.checkout.databinding.ItemSummaryTransactionCrossSellBinding
import com.tokopedia.checkout.databinding.ViewItemShipmentCostDetailsBinding
import com.tokopedia.checkout.view.ShipmentAdapterActionListener
import com.tokopedia.checkout.view.adapter.ShipmentAddOnSummaryAdapter
import com.tokopedia.checkout.view.uimodel.ShipmentCostModel
import com.tokopedia.checkout.view.uimodel.ShipmentPaymentFeeModel
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.setTextAndContentDescription
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.utils.currency.CurrencyFormatUtil.convertPriceValueToIdrFormat

class ShipmentCostViewHolder(
    private val binding: ViewItemShipmentCostDetailsBinding,
    private val layoutInflater: LayoutInflater,
    private val shipmentAdapterActionListener: ShipmentAdapterActionListener
) : RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("StringFormatInvalid")
    fun bindViewHolder(shipmentCost: ShipmentCostModel) {
        binding.rlShipmentCost.visibility = View.VISIBLE
        binding.tvTotalItemLabel.text = getTotalItemLabel(binding.tvTotalItemLabel.context, shipmentCost.totalItem)
        binding.tvTotalItemPrice.setTextAndContentDescription(if (shipmentCost.totalItemPrice == 0.0) "-" else convertPriceValueToIdrFormat(shipmentCost.totalItemPrice.toLong(), false).removeDecimalSuffix(), R.string.content_desc_tv_total_item_price_summary)
        binding.tvShippingFeeLabel.text = binding.tvShippingFeeLabel.context.getString(com.tokopedia.purchase_platform.common.R.string.label_shipment_fee)
        binding.tvShippingFee.setTextAndContentDescription(getPriceFormat(binding.tvShippingFeeLabel, binding.tvShippingFee, shipmentCost.shippingFee), R.string.content_desc_tv_shipping_fee_summary)
        binding.tvInsuranceFee.setTextAndContentDescription(getPriceFormat(binding.tvInsuranceFeeLabel, binding.tvInsuranceFee, shipmentCost.insuranceFee), R.string.content_desc_tv_insurance_fee_summary)
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
                val itemCrossSellView = ItemSummaryTransactionCrossSellBinding.inflate(layoutInflater, null, false)

                val crossSellItemLabel = itemCrossSellView.tvCrossSellLabel
                crossSellItemLabel.text = crossSell.crossSellModel.orderSummary.title

                val crossSellItemPrice = itemCrossSellView.tvCrossSellPrice
                crossSellItemPrice.text = getPriceFormat(crossSellItemLabel, crossSellItemPrice, crossSell.crossSellModel.price)
                binding.llCrossSell.addView(itemCrossSellView.root)
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
        renderAddOnGiftingCost(shipmentCost)
        renderSummaryAddOn(shipmentCost, itemView.context)
        if (shipmentCost.totalItem > 0) {
            Log.i("qwertyuiop", "render platform fee")
            renderPlatformFee(shipmentCost.dynamicPlatformFee)
        } else {
            Log.i("qwertyuiop", "hide platform fee")
            hidePlatformFee()
        }
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
            binding.tvProductDiscountPrice.setTextColor(ContextCompat.getColor(binding.tvProductDiscountPrice.context, com.tokopedia.unifyprinciples.R.color.Unify_GN500))
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
                binding.tvShippingDiscountPrice.setTextColor(ContextCompat.getColor(binding.tvShippingDiscountPrice.context, com.tokopedia.unifyprinciples.R.color.Unify_GN500))
            }
        } else {
            binding.tvShippingDiscountLabel.visibility = View.GONE
            binding.tvShippingDiscountPrice.visibility = View.GONE
        }
    }

    private fun renderGeneralDiscount(shipmentCost: ShipmentCostModel) {
        binding.tvDiscountLabel.text = binding.tvDiscountLabel.context.getString(R.string.label_total_discount)
        binding.tvDiscountPrice.text = getPriceFormat(binding.tvDiscountLabel, binding.tvDiscountPrice, (shipmentCost.discountAmount * -1).toDouble())
        binding.tvDiscountPrice.setTextColor(ContextCompat.getColor(binding.tvDiscountPrice.context, com.tokopedia.unifyprinciples.R.color.Unify_GN500))
    }

    private fun renderAddOnGiftingCost(shipmentCost: ShipmentCostModel) {
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

    private fun renderSummaryAddOn(shipmentCost: ShipmentCostModel, context: Context) {
        if (shipmentCost.listAddOnSummary.isNotEmpty()) {
            val addOnSummaryAdapter = ShipmentAddOnSummaryAdapter(shipmentCost.listAddOnSummary)
            binding.rvSummaryAddon.apply {
                layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                setHasFixedSize(true)
                adapter = addOnSummaryAdapter
                visible()
            }
        } else {
            binding.rvSummaryAddon.gone()
        }
    }

    private fun hidePlatformFee() {
        binding.tickerPlatformFeeInfo.gone()
        binding.tvPlatformFeeLabel.gone()
        binding.icPlatformFeeInfo.gone()
        binding.tvPlatformFeeValue.gone()
        binding.tvPlatformSlashedFeeValue.gone()
        binding.loaderPlatformFeeLabel.gone()
        binding.loaderPlatformFeeValue.gone()
    }

    private fun renderPlatformFee(platformFeeModel: ShipmentPaymentFeeModel) {
        if (platformFeeModel.isLoading) {
            Log.i("qwertyuiop", "render platform fee loading")
            binding.tickerPlatformFeeInfo.gone()
            binding.tvPlatformFeeLabel.gone()
            binding.icPlatformFeeInfo.gone()
            binding.tvPlatformFeeValue.gone()
            binding.tvPlatformSlashedFeeValue.gone()
            binding.loaderPlatformFeeLabel.visible()
            binding.loaderPlatformFeeValue.visible()
        } else if (platformFeeModel.isShowTicker) {
            Log.i("qwertyuiop", "render platform fee ticker")
            binding.tvPlatformFeeLabel.gone()
            binding.icPlatformFeeInfo.gone()
            binding.tvPlatformFeeValue.gone()
            binding.tvPlatformSlashedFeeValue.gone()
            binding.loaderPlatformFeeLabel.gone()
            binding.loaderPlatformFeeValue.gone()
            binding.tickerPlatformFeeInfo.visible()
            binding.tickerPlatformFeeInfo.setHtmlDescription(platformFeeModel.ticker)
            binding.tickerPlatformFeeInfo.setDescriptionClickEvent(object : TickerCallback {
                override fun onDescriptionViewClick(linkUrl: CharSequence) {
                    shipmentAdapterActionListener.checkPlatformFee()
                }

                override fun onDismiss() { }
            })
        } else {
            Log.i("qwertyuiop", "render platform fee full")
            binding.tickerPlatformFeeInfo.gone()

            if (platformFeeModel.title.isEmpty()) {
                Log.i("qwertyuiop", "render platform fee empty")
                binding.loaderPlatformFeeLabel.gone()
                binding.loaderPlatformFeeValue.gone()
                binding.tvPlatformFeeLabel.gone()
                binding.icPlatformFeeInfo.gone()
                binding.tvPlatformFeeValue.gone()
                binding.tvPlatformSlashedFeeValue.gone()
            } else {
                Log.i("qwertyuiop", "render platform fee with ${platformFeeModel.title}")
                binding.loaderPlatformFeeLabel.gone()
                binding.loaderPlatformFeeValue.gone()
                binding.tvPlatformFeeLabel.visible()
                binding.tvPlatformFeeLabel.text = platformFeeModel.title
                binding.tvPlatformFeeValue.visible()

                if (platformFeeModel.isShowSlashed) {
                    binding.tvPlatformSlashedFeeValue.visible()
                    binding.tvPlatformSlashedFeeValue.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                    binding.tvPlatformSlashedFeeValue.text = convertPriceValueToIdrFormat(platformFeeModel.slashedFee.toLong(), false).removeDecimalSuffix()

                    binding.tvPlatformFeeValue.text = convertPriceValueToIdrFormat(platformFeeModel.fee.toLong(), false).removeDecimalSuffix()
                    binding.tvPlatformFeeValue.setTextColor(ContextCompat.getColor(binding.tvPlatformFeeValue.context, com.tokopedia.unifyprinciples.R.color.Unify_GN500))
                } else {
                    binding.tvPlatformSlashedFeeValue.gone()
                    binding.tvPlatformFeeValue.text = convertPriceValueToIdrFormat(platformFeeModel.fee.toLong(), false).removeDecimalSuffix()
                    binding.tvPlatformFeeValue.setTextColor(ContextCompat.getColor(binding.tvPlatformFeeValue.context, com.tokopedia.unifyprinciples.R.color.Unify_NN950))
                }

                if (platformFeeModel.isShowTooltip) {
                    binding.icPlatformFeeInfo.visible()
                    binding.icPlatformFeeInfo.setOnClickListener {
                        shipmentAdapterActionListener.showPlatformFeeTooltipInfoBottomSheet(platformFeeModel)
                    }
                } else {
                    binding.icPlatformFeeInfo.gone()
                }
            }
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
