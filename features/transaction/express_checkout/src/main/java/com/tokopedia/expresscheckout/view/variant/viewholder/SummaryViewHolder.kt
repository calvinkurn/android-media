package com.tokopedia.expresscheckout.view.variant.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.expresscheckout.R
import com.tokopedia.expresscheckout.view.variant.CheckoutVariantActionListener
import com.tokopedia.expresscheckout.view.variant.viewmodel.SummaryViewModel
import kotlinx.android.synthetic.main.item_summary_detail_product_page.view.*

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

class SummaryViewHolder(val view: View, val listener: CheckoutVariantActionListener) : AbstractViewHolder<SummaryViewModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_summary_detail_product_page
    }

    override fun bind(element: SummaryViewModel?) {
        if (element != null) {
            itemView.tv_purchase_summary_item_price_value.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(element.itemPrice, false)
            itemView.tv_purchase_summary_shipping_price_value.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(element.shippingPrice, false)
            if (element.isUseInsurance) {
                itemView.tv_purchase_summary_insurance_price_value.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(element.insurancePrice, false)
                itemView.img_bt_insurance_info.setOnClickListener { listener.onClickInsuranceInfo(element.insuranceInfo) }
                itemView.tv_purchase_summary_insurance_price_label.visibility = View.VISIBLE
                itemView.tv_purchase_summary_insurance_price_value.visibility = View.VISIBLE
                itemView.img_bt_insurance_info.visibility = View.VISIBLE
            } else {
                itemView.tv_purchase_summary_insurance_price_label.visibility = View.GONE
                itemView.tv_purchase_summary_insurance_price_value.visibility = View.GONE
                itemView.img_bt_insurance_info.visibility = View.GONE
            }
            if (element.servicePrice != 0) {
                itemView.tv_purchase_summary_service_price_value.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(element.servicePrice, false)
                itemView.tv_purchase_summary_service_price_label.visibility = View.VISIBLE
                itemView.tv_purchase_summary_service_price_value.visibility = View.VISIBLE
            } else {
                itemView.tv_purchase_summary_service_price_label.visibility = View.GONE
                itemView.tv_purchase_summary_service_price_value.visibility = View.GONE
            }
            listener.onSummaryChanged(element)
        }
    }

}