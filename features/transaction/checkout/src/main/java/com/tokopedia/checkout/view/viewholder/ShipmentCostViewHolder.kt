package com.tokopedia.checkout.view.viewholder

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import com.tokopedia.kotlin.extensions.view.setTextAndContentDescription
import com.tokopedia.purchase_platform.common.utils.Utils.removeDecimalSuffix
import com.tokopedia.utils.currency.CurrencyFormatUtil.convertPriceValueToIdrFormat
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.checkout.R
import com.tokopedia.checkout.view.uimodel.ShipmentCostModel

class ShipmentCostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val mRlShipmentCostLayout: RelativeLayout = itemView.findViewById(R.id.rl_shipment_cost)
    private val mTvTotalItemLabel: TextView = itemView.findViewById(R.id.tv_total_item_label)
    private val mTvTotalItemPrice: TextView = itemView.findViewById(R.id.tv_total_item_price)
    private val mTvShippingFeeLabel: TextView = itemView.findViewById(R.id.tv_shipping_fee_label)
    private val mTvShippingFee: TextView = itemView.findViewById(R.id.tv_shipping_fee)
    private val mTvInsuranceFee: TextView = itemView.findViewById(R.id.tv_insurance_fee)
    private val mTvPurchaseProtectionLabel: TextView = itemView.findViewById(R.id.tv_purchase_protection_label)
    private val mTvPurchaseProtectionFee: TextView = itemView.findViewById(R.id.tv_purchase_protection_fee)
    private val mTvPromoDiscount: TextView = itemView.findViewById(R.id.tv_promo)
    private val mTvSellerCostAdditionLabel: TextView = itemView.findViewById(R.id.tv_seller_cost_addition)
    private val mTvSellerCostAdditionFee: TextView = itemView.findViewById(R.id.tv_seller_cost_addition_fee)
    private val mTvInsuranceFeeLabel: TextView = itemView.findViewById(R.id.tv_insurance_fee_label)
    private val mTvPromoOrCouponLabel: TextView = itemView.findViewById(R.id.tv_promo_or_coupon_label)
    private val mTvDonationLabel: TextView = itemView.findViewById(R.id.tv_donation_label)
    private val mTvDonationPrice: TextView = itemView.findViewById(R.id.tv_donation_price)
    private val mTvEmasLabel: TextView = itemView.findViewById(R.id.tv_emas_label)
    private val mTvEmasPrice: TextView = itemView.findViewById(R.id.tv_emas_price)
    private val mTvTradeInLabel: TextView = itemView.findViewById(R.id.tv_trade_in_label)
    private val mTvTradeInPrice: TextView = itemView.findViewById(R.id.tv_trade_in)
    private val mTvOrderPrioritasLabel: TextView = itemView.findViewById(R.id.tv_order_prioritas_label)
    private val mTvOrderPrioritasFee: TextView = itemView.findViewById(R.id.tv_order_prioritas_price)
    private val mTvBookingFeeLabel: TextView = itemView.findViewById(R.id.tv_booking_fee_label)
    private val mTvBookingFee: TextView = itemView.findViewById(R.id.tv_booking_fee_price)
    private val mTvDiscountLabel: TextView = itemView.findViewById(R.id.tv_discount_label)
    private val mTvDiscountPrice: TextView = itemView.findViewById(R.id.tv_discount_price)
    private val mTvShippingDiscountLabel: TextView = itemView.findViewById(R.id.tv_shipping_discount_label)
    private val mTvShippingDiscountPrice: TextView = itemView.findViewById(R.id.tv_shipping_discount_price)
    private val mTvProductDiscountLabel: TextView = itemView.findViewById(R.id.tv_product_discount_label)
    private val mTvProductDiscountPrice: TextView = itemView.findViewById(R.id.tv_product_discount_price)

    @SuppressLint("StringFormatInvalid")
    fun bindViewHolder(shipmentCost: ShipmentCostModel) {
        mRlShipmentCostLayout.visibility = View.VISIBLE
        mTvTotalItemLabel.text = getTotalItemLabel(mTvTotalItemLabel.context, shipmentCost.totalItem)
        mTvTotalItemPrice.setTextAndContentDescription(if (shipmentCost.totalItemPrice == 0.0) "-" else removeDecimalSuffix(convertPriceValueToIdrFormat(shipmentCost.totalItemPrice.toLong(), false)), R.string.content_desc_tv_total_item_price_summary)
        mTvShippingFeeLabel.text = mTvShippingFeeLabel.context.getString(com.tokopedia.purchase_platform.common.R.string.label_shipment_fee)
        mTvShippingFee.setTextAndContentDescription(getPriceFormat(mTvShippingFeeLabel, mTvShippingFee, shipmentCost.shippingFee), R.string.content_desc_tv_shipping_fee_summary)
        mTvInsuranceFee.setTextAndContentDescription(getPriceFormat(mTvInsuranceFeeLabel, mTvInsuranceFee, shipmentCost.insuranceFee), R.string.content_desc_tv_insurance_fee_summary)
        mTvOrderPrioritasFee.text = getPriceFormat(mTvOrderPrioritasLabel, mTvOrderPrioritasFee, shipmentCost.priorityFee)
        mTvPurchaseProtectionLabel.text = getTotalPurchaseProtectionItemLabel(mTvPurchaseProtectionLabel.context, shipmentCost.totalPurchaseProtectionItem)
        mTvPurchaseProtectionFee.text = getPriceFormat(mTvPurchaseProtectionLabel, mTvPurchaseProtectionFee, shipmentCost.purchaseProtectionFee)
        mTvPromoDiscount.text = String.format(mTvPromoDiscount.context.getString(R.string.promo_format),
                getPriceFormat(mTvPromoOrCouponLabel, mTvPromoDiscount, shipmentCost.promoPrice))
        mTvSellerCostAdditionFee.text = getPriceFormat(mTvSellerCostAdditionLabel, mTvSellerCostAdditionFee, shipmentCost.additionalFee)
        mTvDonationPrice.text = getPriceFormat(mTvDonationLabel, mTvDonationPrice, shipmentCost.donation)
        mTvEmasPrice.text = getPriceFormat(mTvEmasLabel, mTvEmasPrice, shipmentCost.emasPrice)
        mTvTradeInPrice.text = String.format(mTvTradeInPrice.context.getString(R.string.promo_format),
                getPriceFormat(mTvTradeInLabel, mTvTradeInPrice, shipmentCost.tradeInPrice))
        if (shipmentCost.bookingFee > 0) {
            mTvBookingFeeLabel.visibility = View.VISIBLE
            mTvBookingFee.visibility = View.VISIBLE
            mTvBookingFee.text = shipmentCost.bookingFee.toString() + ""
        } else {
            mTvBookingFeeLabel.visibility = View.GONE
            mTvBookingFee.visibility = View.GONE
        }
        mTvBookingFee.text = getPriceFormat(mTvBookingFeeLabel, mTvBookingFee, shipmentCost.bookingFee.toDouble())
        renderDiscount(shipmentCost)
    }

    private fun renderDiscount(shipmentCost: ShipmentCostModel) {
        if (shipmentCost.isHasDiscountDetails) {
            renderShippingDiscount(shipmentCost)
            renderProductDiscount(shipmentCost)
            mTvDiscountLabel.visibility = View.GONE
            mTvDiscountPrice.visibility = View.GONE
        } else {
            renderGeneralDiscount(shipmentCost)
            mTvShippingDiscountLabel.visibility = View.GONE
            mTvShippingDiscountPrice.visibility = View.GONE
            mTvProductDiscountLabel.visibility = View.GONE
            mTvProductDiscountPrice.visibility = View.GONE
        }
    }

    private fun renderProductDiscount(shipmentCost: ShipmentCostModel) {
        if (shipmentCost.productDiscountAmount > 0) {
            mTvProductDiscountLabel.text = mTvProductDiscountLabel.context.getString(com.tokopedia.purchase_platform.common.R.string.label_product_discount)
            mTvProductDiscountPrice.text = getPriceFormat(mTvProductDiscountLabel, mTvProductDiscountPrice, (shipmentCost.productDiscountAmount * -1).toDouble())
        } else {
            mTvProductDiscountLabel.visibility = View.GONE
            mTvProductDiscountPrice.visibility = View.GONE
        }
    }

    private fun renderShippingDiscount(shipmentCost: ShipmentCostModel) {
        mTvShippingDiscountLabel.text = mTvShippingDiscountLabel.context.getString(com.tokopedia.purchase_platform.common.R.string.label_shipping_discount)
        if (shipmentCost.shippingDiscountAmount > 0) {
            if (shipmentCost.shippingDiscountAmount >= shipmentCost.shippingFee) {
                mTvShippingFee.setTextAndContentDescription(mTvShippingFee.context.getString(com.tokopedia.purchase_platform.common.R.string.label_free_shipping), R.string.content_desc_tv_shipping_fee_summary)
                mTvShippingDiscountPrice.visibility = View.GONE
                mTvShippingDiscountLabel.visibility = View.GONE
            } else {
                mTvShippingDiscountPrice.text = getPriceFormat(mTvShippingDiscountLabel, mTvShippingDiscountPrice, (shipmentCost.shippingDiscountAmount * -1).toDouble())
            }
        } else {
            mTvShippingDiscountLabel.visibility = View.GONE
            mTvShippingDiscountPrice.visibility = View.GONE
        }
    }

    private fun renderGeneralDiscount(shipmentCost: ShipmentCostModel) {
        mTvDiscountLabel.text = mTvDiscountLabel.context.getString(R.string.label_total_discount)
        mTvDiscountPrice.text = getPriceFormat(mTvDiscountLabel, mTvDiscountPrice, (shipmentCost.discountAmount * -1).toDouble())
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
            removeDecimalSuffix(convertPriceValueToIdrFormat(price.toLong(), false))
        }
    }

    companion object {
        @JvmField
        val ITEM_VIEW_SHIPMENT_COST = R.layout.view_item_shipment_cost_details
    }

}