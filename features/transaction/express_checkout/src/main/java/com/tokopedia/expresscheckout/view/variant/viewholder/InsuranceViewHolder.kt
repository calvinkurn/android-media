package com.tokopedia.expresscheckout.view.variant.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.CompoundButton
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.expresscheckout.R
import com.tokopedia.expresscheckout.view.variant.CheckoutVariantActionListener
import com.tokopedia.expresscheckout.view.variant.viewmodel.InsuranceViewModel
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
            itemView.cb_insurance.isChecked = element.isInsuranceChecked
            itemView.cb_insurance.setOnCheckedChangeListener { compoundButton: CompoundButton, isChecked: Boolean ->
                run {
                    itemView.cb_insurance.isChecked = isChecked
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        listener.onNeedToNotifySingleItem(adapterPosition)
                    }
                }
            }
            itemView.img_bt_insurance_info.setOnClickListener { listener.onClickInsuranceInfo(element.insuranceLongInfo) }
            itemView.tv_insurance_long_info.text = element.insuranceLongInfo
            itemView.tv_insurance_price.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(element.insurancePrice, false)
        }
    }

}