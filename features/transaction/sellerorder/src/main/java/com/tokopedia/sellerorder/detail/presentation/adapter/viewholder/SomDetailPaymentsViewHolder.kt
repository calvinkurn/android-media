package com.tokopedia.sellerorder.detail.presentation.adapter.viewholder

import android.annotation.SuppressLint
import android.view.View
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.detail.data.model.SomDetailData
import com.tokopedia.sellerorder.detail.data.model.SomDetailPayments
import com.tokopedia.sellerorder.detail.presentation.adapter.SomDetailAdapter
import kotlinx.android.synthetic.main.detail_payments_item.view.*

/**
 * Created by fwidjaja on 2019-10-07.
 */
class SomDetailPaymentsViewHolder(itemView: View) : SomDetailAdapter.BaseViewHolder<SomDetailData>(itemView) {
    @SuppressLint("SetTextI18n")
    override fun bind(item: SomDetailData, position: Int) {
        if (item.dataObject is SomDetailPayments) {
            itemView.product_price_label.text = "${itemView.context.getString(R.string.product_price_label)} (${item.dataObject.totalProducts} Barang)"
            itemView.product_price_value.text = item.dataObject.productsPriceText
            itemView.shipping_price_label.text = "${itemView.context.getString(R.string.shipping_price_label)} (${item.dataObject.totalWeight})"
            itemView.shipping_price_value.text = item.dataObject.shippingPriceText

            if (item.dataObject.insurancePriceValue > 0) {
                itemView.insurance_price_label.visibility = View.VISIBLE
                itemView.insurance_price_value.visibility = View.VISIBLE
                itemView.insurance_price_value.text = item.dataObject.insurancePriceText
            } else {
                itemView.insurance_price_label.visibility = View.GONE
                itemView.insurance_price_value.visibility = View.GONE
            }

            if (item.dataObject.additionalPriceValue > 0) {
                itemView.additional_price_label.visibility = View.VISIBLE
                itemView.additional_price_value.visibility = View.VISIBLE
                itemView.additional_price_value.text = item.dataObject.additionalPriceText
            } else {
                itemView.additional_price_label.visibility = View.GONE
                itemView.additional_price_value.visibility = View.GONE
            }

            itemView.total_price_value.text = item.dataObject.totalPriceText
        }
    }
}