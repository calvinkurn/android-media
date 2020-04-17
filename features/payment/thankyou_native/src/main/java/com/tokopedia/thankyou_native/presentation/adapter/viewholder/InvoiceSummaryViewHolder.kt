package com.tokopedia.thankyou_native.presentation.adapter.viewholder

import android.view.View
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.presentation.adapter.model.InvoiceSummery
import kotlinx.android.synthetic.main.thank_widget_invoice_summary.view.*

class InvoiceSummaryViewHolder(val view: View) : AbstractViewHolder<InvoiceSummery>(view) {

    private val tvInvoiceSummaryTotalPrice: TextView = view.tvInvoiceSummaryTotalPrice
    private val tvInvoiceSummaryTotalPriceValue: TextView = view.tvInvoiceSummaryTotalPriceValue

    private val tvInvoiceSummaryTotalDiscountValue: TextView = view.tvInvoiceSummaryTotalDiscountValue
    private val tvInvoiceSummaryTotalDiscount: TextView = view.tvInvoiceSummaryTotalDiscount

    private val tvInvoiceSummaryTotalProductProtectionValue: TextView = view.tvInvoiceSummaryTotalProductProtectionValue
    private val tvInvoiceSummaryTotalProductProtection: TextView = view.tvInvoiceSummaryTotalProductProtection

    private val tvInvoiceSummaryTotalShippingCostValue: TextView = view.tvInvoiceSummaryTotalShippingCostValue
    private val tvInvoiceSummaryTotalShippingCost: TextView = view.tvInvoiceSummaryTotalShippingCost

    private val tvInvoiceSummaryTotalShippingCostDiscountValue: TextView = view.tvInvoiceSummaryTotalShippingCostDiscountValue
    private val tvInvoiceSummaryTotalShippingDiscount: TextView = view.tvInvoiceSummaryTotalShippingDiscount

    private val tvInvoiceSummaryTotalShippingInsuranceValue: TextView = view.tvInvoiceSummaryTotalShippingInsuranceValue
    private val tvInvoiceSummaryTotalShippingInsurance: TextView = view.tvInvoiceSummaryTotalShippingInsurance

    private val tvInvoiceSummaryTotalDonationValue: TextView = view.tvInvoiceSummaryTotalDonationValue
    private val tvInvoiceSummaryTotalDonation: TextView = view.tvInvoiceSummaryTotalDonation

    private val tvInvoiceSummaryTotalEGoldValue: TextView = view.tvInvoiceSummaryTotalEGoldValue
    private val tvInvoiceSummaryTotalEGold: TextView = view.tvInvoiceSummaryTotalEGold


    override fun bind(element: InvoiceSummery?) {
        element?.let {
            tvInvoiceSummaryTotalPrice.text = getString(R.string.thank_invoice_total_price, element.totalItemCount)

            tvInvoiceSummaryTotalPriceValue.text = getString(R.string.thankyou_rp_without_space, element.totalPriceStr)

            element.totalItemDiscountStr?.let {
                tvInvoiceSummaryTotalDiscountValue.text = getString(R.string.thankyou_discounted_rp, element.totalItemDiscountStr)
                tvInvoiceSummaryTotalDiscountValue.visible()
                tvInvoiceSummaryTotalDiscount.visible()
            } ?: run {
                tvInvoiceSummaryTotalDiscountValue.gone()
                tvInvoiceSummaryTotalDiscount.gone()
            }


            element.totalItemDiscountStr?.let {
                tvInvoiceSummaryTotalDiscountValue.text = getString(R.string.thankyou_discounted_rp, element.totalItemDiscountStr)
                tvInvoiceSummaryTotalDiscountValue.visible()
                tvInvoiceSummaryTotalDiscount.visible()
            } ?: run {
                tvInvoiceSummaryTotalDiscountValue.gone()
                tvInvoiceSummaryTotalDiscount.gone()
            }

            element.totalProductProtectionStr?.let {
                tvInvoiceSummaryTotalProductProtectionValue.text = getString(R.string.thankyou_rp_without_space, element.totalProductProtectionStr)
                tvInvoiceSummaryTotalProductProtectionValue.visible()
                tvInvoiceSummaryTotalProductProtection.visible()
            } ?: run {
                tvInvoiceSummaryTotalProductProtectionValue.gone()
                tvInvoiceSummaryTotalProductProtection.gone()
            }

            element.totalShippingChargeStr?.let {
                tvInvoiceSummaryTotalShippingCostValue.text = getString(R.string.thankyou_rp_without_space, element.totalShippingChargeStr)
                tvInvoiceSummaryTotalShippingCostValue.visible()
                tvInvoiceSummaryTotalShippingCost.visible()
            } ?: run {
                tvInvoiceSummaryTotalShippingCostValue.gone()
                tvInvoiceSummaryTotalShippingCost.gone()
            }
            element.totalShippingDiscountStr?.let {
                tvInvoiceSummaryTotalShippingCostDiscountValue.text = getString(R.string.thankyou_discounted_rp, element.totalShippingDiscountStr)
                tvInvoiceSummaryTotalShippingCostDiscountValue.visible()
                tvInvoiceSummaryTotalShippingDiscount.visible()
            } ?: run {
                tvInvoiceSummaryTotalShippingCostDiscountValue.gone()
                tvInvoiceSummaryTotalShippingDiscount.gone()
            }

            element.totalShippingInsuranceStr?.let {
                tvInvoiceSummaryTotalShippingInsuranceValue.text = getString(R.string.thankyou_rp_without_space, element.totalShippingInsuranceStr)
                tvInvoiceSummaryTotalShippingInsuranceValue.visible()
                tvInvoiceSummaryTotalShippingInsurance.visible()
            } ?: run {
                tvInvoiceSummaryTotalShippingInsuranceValue.gone()
                tvInvoiceSummaryTotalShippingInsurance.gone()
            }


            element.donationAmountStr?.let {
                tvInvoiceSummaryTotalDonationValue.text = getString(R.string.thankyou_rp_without_space, element.donationAmountStr)
                tvInvoiceSummaryTotalDonationValue.visible()
                tvInvoiceSummaryTotalDonation.visible()
            } ?: run {
                tvInvoiceSummaryTotalDonationValue.gone()
                tvInvoiceSummaryTotalDonation.gone()
            }
            element.eGoldPriceStr?.let {
                tvInvoiceSummaryTotalEGoldValue.text = getString(R.string.thankyou_rp_without_space, element.eGoldPriceStr)
                tvInvoiceSummaryTotalEGoldValue.visible()
                tvInvoiceSummaryTotalEGold.visible()
            } ?: run {
                tvInvoiceSummaryTotalEGoldValue.gone()
                tvInvoiceSummaryTotalEGold.gone()
            }
        }
    }

    companion object {
        val LAYOUT_ID = R.layout.thank_widget_invoice_summary
    }
}