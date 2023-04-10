package com.tokopedia.checkout.view.viewholder

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.checkout.R
import com.tokopedia.checkout.view.ShipmentAdapterActionListener
import com.tokopedia.checkout.view.uimodel.ShipmentCostModel
import com.tokopedia.checkout.view.uimodel.ShipmentPlatformFeeModel
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.setTextAndContentDescription
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.currency.CurrencyFormatUtil.convertPriceValueToIdrFormat

class ShipmentCostViewHolder(itemView: View, private val layoutInflater: LayoutInflater,
                             private val shipmentAdapterActionListener: ShipmentAdapterActionListener) : RecyclerView.ViewHolder(itemView) {

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
    private val mLinearLayoutCrossSell: LinearLayout = itemView.findViewById(R.id.ll_cross_sell)
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
    private val mTvSummaryAddOnLabel: Typography = itemView.findViewById(R.id.tv_summary_add_on_label)
    private val mTvSummaryAddOnPrice: Typography = itemView.findViewById(R.id.tv_summary_add_on_price)
    private val mTickerPlatformFeeInfo: Ticker = itemView.findViewById(R.id.ticker_platform_fee_info)
    private val mTvPlatformFeeLabel: Typography = itemView.findViewById(R.id.tv_platform_fee_label)
    private val mIvPlatformFeeIconInfo: IconUnify = itemView.findViewById(R.id.ic_platform_fee_info)
    private val mTvPlatformFeeValue: Typography = itemView.findViewById(R.id.tv_platform_fee_value)
    private val mTvPlatformSlashedFeeValue: Typography = itemView.findViewById(R.id.tv_platform_slashed_fee_value)
    private val mLoaderPlatformFeeLabel: LoaderUnify = itemView.findViewById(R.id.loader_platform_fee_label)
    private val mLoaderPlatformFeeValue: LoaderUnify = itemView.findViewById(R.id.loader_platform_fee_value)

    @SuppressLint("StringFormatInvalid")
    fun bindViewHolder(shipmentCost: ShipmentCostModel) {
        mRlShipmentCostLayout.visibility = View.VISIBLE
        mTvTotalItemLabel.text = getTotalItemLabel(mTvTotalItemLabel.context, shipmentCost.totalItem)
        mTvTotalItemPrice.setTextAndContentDescription(if (shipmentCost.totalItemPrice == 0.0) "-" else convertPriceValueToIdrFormat(shipmentCost.totalItemPrice.toLong(), false).removeDecimalSuffix(), R.string.content_desc_tv_total_item_price_summary)
        mTvShippingFeeLabel.text = mTvShippingFeeLabel.context.getString(com.tokopedia.purchase_platform.common.R.string.label_shipment_fee)
        mTvShippingFee.setTextAndContentDescription(getPriceFormat(mTvShippingFeeLabel, mTvShippingFee, shipmentCost.shippingFee), R.string.content_desc_tv_shipping_fee_summary)
        mTvInsuranceFee.setTextAndContentDescription(getPriceFormat(mTvInsuranceFeeLabel, mTvInsuranceFee, shipmentCost.insuranceFee), R.string.content_desc_tv_insurance_fee_summary)
        mTvOrderPrioritasFee.text = getPriceFormat(mTvOrderPrioritasLabel, mTvOrderPrioritasFee, shipmentCost.priorityFee)
        mTvPurchaseProtectionLabel.text = getTotalPurchaseProtectionItemLabel(mTvPurchaseProtectionLabel.context, shipmentCost.totalPurchaseProtectionItem)
        mTvPurchaseProtectionFee.text = getPriceFormat(mTvPurchaseProtectionLabel, mTvPurchaseProtectionFee, shipmentCost.purchaseProtectionFee)
        mTvPromoDiscount.text = String.format(
            mTvPromoDiscount.context.getString(R.string.promo_format),
            getPriceFormat(mTvPromoOrCouponLabel, mTvPromoDiscount, shipmentCost.promoPrice)
        )
        mTvSellerCostAdditionFee.text = getPriceFormat(mTvSellerCostAdditionLabel, mTvSellerCostAdditionFee, shipmentCost.additionalFee)
        mTvDonationPrice.text = getPriceFormat(mTvDonationLabel, mTvDonationPrice, shipmentCost.donation)
        if (shipmentCost.listCrossSell.isEmpty()) {
            mLinearLayoutCrossSell.removeAllViews()
            mLinearLayoutCrossSell.visibility = View.GONE
        } else {
            mLinearLayoutCrossSell.removeAllViews()
            shipmentCost.listCrossSell.forEach { crossSell ->
                val itemCrossSellView = layoutInflater.inflate(R.layout.item_summary_transaction_cross_sell, null, false) as RelativeLayout

                val crossSellItemLabel = itemCrossSellView.findViewById<Typography>(R.id.tv_cross_sell_label)
                crossSellItemLabel.text = crossSell.crossSellModel.orderSummary.title

                val crossSellItemPrice = itemCrossSellView.findViewById<Typography>(R.id.tv_cross_sell_price)
                crossSellItemPrice.text = getPriceFormat(crossSellItemLabel, crossSellItemPrice, crossSell.crossSellModel.price)
                mLinearLayoutCrossSell.addView(itemCrossSellView)
            }
            mLinearLayoutCrossSell.visibility = View.VISIBLE
        }
        mTvEmasPrice.text = getPriceFormat(mTvEmasLabel, mTvEmasPrice, shipmentCost.emasPrice)
        mTvTradeInPrice.text = String.format(
            mTvTradeInPrice.context.getString(R.string.promo_format),
            getPriceFormat(mTvTradeInLabel, mTvTradeInPrice, shipmentCost.tradeInPrice)
        )
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
        renderAddOnCost(shipmentCost)
        renderPlatformFee(shipmentCost.dynamicPlatformFee)
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
                mTvShippingFee.setTextAndContentDescription(convertPriceValueToIdrFormat(0.0, false).removeDecimalSuffix(), R.string.content_desc_tv_shipping_fee_summary)
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

    private fun renderAddOnCost(shipmentCost: ShipmentCostModel) {
        if (shipmentCost.hasAddOn) {
            mTvSummaryAddOnLabel.text = mTvSummaryAddOnLabel.context.getString(R.string.label_add_on_cost)

            // exclusion : need to write if totalAddOnPrice is Rp 0
            mTvSummaryAddOnLabel.visibility = View.VISIBLE
            mTvSummaryAddOnPrice.visibility = View.VISIBLE
            mTvSummaryAddOnPrice.text = convertPriceValueToIdrFormat(shipmentCost.totalAddOnPrice.toLong(), false).removeDecimalSuffix()
        } else {
            mTvSummaryAddOnLabel.visibility = View.GONE
            mTvSummaryAddOnPrice.visibility = View.GONE
        }
    }

    private fun renderPlatformFee(platformFeeModel: ShipmentPlatformFeeModel) {
        if (platformFeeModel.isLoading) {
            mTickerPlatformFeeInfo.gone()
            mTvPlatformFeeLabel.gone()
            mIvPlatformFeeIconInfo.gone()
            mTvPlatformFeeValue.gone()
            mTvPlatformSlashedFeeValue.gone()
            mLoaderPlatformFeeLabel.visible()
            mLoaderPlatformFeeValue.visible()

        } else if (platformFeeModel.isShowTicker) {
            mTvPlatformFeeLabel.gone()
            mIvPlatformFeeIconInfo.gone()
            mTvPlatformFeeValue.gone()
            mTvPlatformSlashedFeeValue.gone()
            mLoaderPlatformFeeLabel.gone()
            mLoaderPlatformFeeValue.gone()
            mTickerPlatformFeeInfo.visible()
            mTickerPlatformFeeInfo.setHtmlDescription(platformFeeModel.ticker)
            mTickerPlatformFeeInfo.setDescriptionClickEvent(object: TickerCallback {
                override fun onDescriptionViewClick(linkUrl: CharSequence) {
                    shipmentAdapterActionListener.refetchPlatformFee();
                }

                override fun onDismiss() { }

            })

        } else {
            mTickerPlatformFeeInfo.gone()

            if (platformFeeModel.title.isEmpty()) {
                mLoaderPlatformFeeLabel.gone()
                mLoaderPlatformFeeValue.gone()
                mTvPlatformFeeLabel.gone()
                mIvPlatformFeeIconInfo.gone()
                mTvPlatformFeeValue.gone()
                mTvPlatformSlashedFeeValue.gone()
            } else {
                mLoaderPlatformFeeLabel.gone()
                mLoaderPlatformFeeValue.gone()
                mTvPlatformFeeLabel.visible()
                mTvPlatformFeeLabel.text = platformFeeModel.title
                mTvPlatformFeeValue.visible()

                if (platformFeeModel.isShowSlashed) {
                    mTvPlatformSlashedFeeValue.visible()
                    mTvPlatformSlashedFeeValue.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                    mTvPlatformSlashedFeeValue.text = convertPriceValueToIdrFormat(platformFeeModel.slashedFee.toLong(), false).removeDecimalSuffix()

                    mTvPlatformFeeValue.text = convertPriceValueToIdrFormat(platformFeeModel.fee.toLong(), false).removeDecimalSuffix()
                } else {
                    mTvPlatformSlashedFeeValue.gone()
                    mTvPlatformFeeValue.text = convertPriceValueToIdrFormat(platformFeeModel.fee.toLong(), false).removeDecimalSuffix()
                }

                if (platformFeeModel.isShowTooltip) {
                    mIvPlatformFeeIconInfo.visible()
                    mIvPlatformFeeIconInfo.setOnClickListener{
                        shipmentAdapterActionListener.showPlatformFeeTooltipInfoBottomSheet(platformFeeModel)
                    }
                } else {
                    mIvPlatformFeeIconInfo.gone()
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
