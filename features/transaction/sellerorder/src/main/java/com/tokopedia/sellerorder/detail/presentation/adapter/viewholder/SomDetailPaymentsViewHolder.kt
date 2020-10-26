package com.tokopedia.sellerorder.detail.presentation.adapter.viewholder

import android.annotation.SuppressLint
import android.view.View
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
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

            if (item.dataObject.totalPurchaseProtectionFee > 0) {
                itemView.tvPurchaseProtectionFeeLabel.apply {
                    text = context.getString(R.string.purchase_protection_fee_label, item.dataObject.totalPurchaseProtectionQuantity)
                    show()
                }
                itemView.tvPurchaseProtectionFeeValue.apply {
                    text = item.dataObject.totalPurchaseProtectionFeeText
                    show()
                }
            } else {
                itemView.tvPurchaseProtectionFeeLabel.gone()
                itemView.tvPurchaseProtectionFeeValue.gone()
            }

            if (item.dataObject.totalReadinessInsuranceFee > 0) {
                itemView.tvReadinessInsuranceFeeLabel.apply {
                    text = context.getString(R.string.readiness_insurance_fee_label, item.dataObject.totalReadinessInsuranceQuantity)
                    show()
                }
                itemView.tvReadinessInsuranceFeeValue.apply {
                    text = item.dataObject.totalReadinessInsuranceFeeText
                    show()
                }
            } else {
                itemView.tvReadinessInsuranceFeeLabel.gone()
                itemView.tvReadinessInsuranceFeeValue.gone()
            }

            if (item.dataObject.codFee > 0) {
                itemView.tvCodFeeValue.apply {
                    text = item.dataObject.codFeeText
                    show()
                }
            } else {
                itemView.tvCodFeeLabel.gone()
                itemView.tvCodFeeValue.gone()
            }

            itemView.total_price_value.text = item.dataObject.totalPriceText
        }
    }
}