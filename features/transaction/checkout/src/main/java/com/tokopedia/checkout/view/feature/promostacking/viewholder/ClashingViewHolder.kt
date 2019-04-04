package com.tokopedia.checkout.view.feature.promostacking.viewholder

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.CompoundButton
import com.tokopedia.checkout.R
import com.tokopedia.checkout.view.feature.promostacking.adapter.ClashingAdapter
import com.tokopedia.checkout.view.feature.promostacking.adapter.ClashingInnerAdapter
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.promocheckout.common.view.uimodel.ClashingVoucherOptionUiModel
import com.tokopedia.promocheckout.common.view.uimodel.ClashingVoucherOrderUiModel
import kotlinx.android.synthetic.main.item_clashing_voucher.view.*

/**
 * Created by Irfan Khoirul on 22/03/19.
 */

class ClashingViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    companion object {
        val LAYOUT = R.layout.item_clashing_voucher
    }

    fun bind(element: ClashingVoucherOptionUiModel?, listener: ClashingAdapter.ActionListener) {
        if (element != null) {
            if (adapterPosition == 0) {
                itemView.tv_title_voucher.text = itemView.context.getString(R.string.checkout_module_title_promo_clashing_newly_selected)
            } else {
                itemView.tv_title_voucher.text = itemView.context.getString(R.string.checkout_module_title_promo_clashing_previously_selected)
            }

            var totalBenefit = 0
            for (voucherModel: ClashingVoucherOrderUiModel in element.voucherOrders) {
                totalBenefit += voucherModel.potentialBenefit
            }

            itemView.tv_voucher_amout_total.text = String.format(itemView.context.getString(R.string.checkout_module_promo_clashing_promo_potential), CurrencyFormatUtil.convertPriceValueToIdrFormat(totalBenefit, false))
            val adapter = ClashingInnerAdapter()
            adapter.data = element.voucherOrders
            itemView.rv_voucher_list.adapter = adapter
            itemView.rv_voucher_list.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.VERTICAL, false)
            adapter.notifyDataSetChanged()

            itemView.rb_option_clash.isChecked = element.isSelected
            itemView.rb_option_clash.setOnCheckedChangeListener { compoundButton, isSelected ->
                element.isSelected = isSelected
                if (isSelected) listener.onVoucherItemSelected(adapterPosition, element.isSelected)
            }

            itemView.setOnClickListener {
                itemView.rb_option_clash.isChecked = true
            }

        }
    }

}