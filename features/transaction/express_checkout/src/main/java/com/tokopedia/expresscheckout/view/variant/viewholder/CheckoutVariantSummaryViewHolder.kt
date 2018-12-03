package com.tokopedia.expresscheckout.view.variant.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.expresscheckout.R
import com.tokopedia.expresscheckout.view.variant.CheckoutVariantActionListener
import com.tokopedia.expresscheckout.view.variant.viewmodel.CheckoutVariantSummaryViewModel
import kotlinx.android.synthetic.main.item_summary_detail_product_page.view.*

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

class CheckoutVariantSummaryViewHolder(val view: View, val listener: CheckoutVariantActionListener) : AbstractViewHolder<CheckoutVariantSummaryViewModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_summary_detail_product_page
    }

    override fun bind(element: CheckoutVariantSummaryViewModel?) {
        if (element != null) {
            itemView.tv_purchase_summary_item_price_value.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(element.itemPrice, false)
            itemView.tv_purchase_summary_shipping_price_value.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(element.shippingPrice, false)
            itemView.tv_purchase_summary_service_price_value.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(element.servicePrice, false)
            itemView.tv_purchase_summary_insurance_price_value.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(element.insurancePrice, false)
            itemView.img_bt_insurance_info.setOnClickListener { listener.onClickInsuranceInfo() }
        }
    }

}