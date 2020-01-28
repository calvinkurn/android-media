package com.tokopedia.purchase_platform.common.feature.promo_clashing.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.promocheckout.common.view.uimodel.ClashingVoucherOptionUiModel
import com.tokopedia.promocheckout.common.view.uimodel.ClashingVoucherOrderUiModel
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.common.feature.promo_clashing.adapter.ClashingAdapter
import com.tokopedia.purchase_platform.common.feature.promo_clashing.adapter.ClashingInnerAdapter
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

            var totalBenefit = 0
            for (voucherModel: ClashingVoucherOrderUiModel in element.voucherOrders) {
                totalBenefit += voucherModel.potentialBenefit
            }

            itemView.tv_voucher_amount_total.text = String.format(itemView.context.getString(R.string.checkout_module_promo_clashing_promo_potential), CurrencyFormatUtil.convertPriceValueToIdrFormat(totalBenefit, false))
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