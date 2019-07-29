package com.tokopedia.purchase_platform.express_checkout.view.variant.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.CompoundButton
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.purchase_platform.express_checkout.view.variant.CheckoutVariantActionListener
import com.tokopedia.purchase_platform.express_checkout.view.variant.viewmodel.InsuranceViewModel
import com.tokopedia.logisticdata.data.constant.InsuranceConstant
import com.tokopedia.purchase_platform.R
import kotlinx.android.synthetic.main.item_insurance_detail_product_page.view.*

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

class InsuranceViewHolder(val view: View, val listener: CheckoutVariantActionListener) : AbstractViewHolder<InsuranceViewModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_insurance_detail_product_page
    }

    override fun bind(element: InsuranceViewModel?) {
        if (element != null) {
            if (element.isVisible && element.insuranceLongInfo.isNotEmpty()) {
                if (element.insuranceType == InsuranceConstant.INSURANCE_TYPE_MUST) {
                    itemView.cb_insurance.visibility = View.GONE
                    itemView.cb_insurance_disabled.isChecked = true
                    itemView.cb_insurance_disabled.visibility = View.VISIBLE
                    itemView.cb_insurance_disabled.isClickable = false
                    element.isChecked = true
                    listener.onInsuranceCheckChanged(element)
                } else {
                    itemView.cb_insurance.setOnCheckedChangeListener { compoundButton: CompoundButton, isChecked: Boolean ->
                        run {
                            element.isChecked = isChecked
                            if (adapterPosition != RecyclerView.NO_POSITION) {
                                listener.onInsuranceCheckChanged(element)
                            }
                        }
                    }
                    itemView.cb_insurance_disabled.visibility = View.GONE
                    itemView.cb_insurance.visibility = View.VISIBLE
                    itemView.cb_insurance.isChecked = element.isChecked
                }
                itemView.img_bt_insurance_info.setOnClickListener { listener.onClickInsuranceInfo(element.insuranceLongInfo) }
                itemView.tv_insurance_long_info.text = element.insuranceLongInfo
                itemView.tv_insurance_price.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(element.insurancePrice, false)
                if (element.isChecked) {
                    itemView.tv_insurance_short_info.text = itemView.context.getString(R.string.label_insurance_info_short_yes)
                } else {
                    itemView.tv_insurance_short_info.text = itemView.context.getString(R.string.label_insurance_info_short_no)
                }
                itemView.card_insurance.visibility = View.VISIBLE
            } else {
                itemView.card_insurance.visibility = View.GONE
            }
        }
    }

}