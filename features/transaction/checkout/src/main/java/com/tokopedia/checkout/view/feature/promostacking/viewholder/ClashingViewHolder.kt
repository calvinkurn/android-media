package com.tokopedia.checkout.view.feature.promostacking.viewholder

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.tokopedia.checkout.R
import com.tokopedia.checkout.view.feature.promostacking.adapter.ClashingInnerAdapter
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

    fun bind(element: ClashingVoucherOptionUiModel?) {
        if (element != null) {
            if (adapterPosition == 0) {
                itemView.tv_title_voucher.text = "Voucher Baru Dipilih"
            } else {
                itemView.tv_title_voucher.text = "Voucher Sebelumnya"
            }

            var totalBenefit = 0
            for (voucherModel: ClashingVoucherOrderUiModel in element.voucherOrders) {
                totalBenefit += voucherModel.potentialBenefit
            }

            itemView.tv_voucher_amout_total.text = totalBenefit.toString()
            val adapter = ClashingInnerAdapter()
            adapter.data = element.voucherOrders
            itemView.rv_voucher_list.adapter = adapter
            itemView.rv_voucher_list.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.VERTICAL, false)
            adapter.notifyDataSetChanged()
        }
    }

}