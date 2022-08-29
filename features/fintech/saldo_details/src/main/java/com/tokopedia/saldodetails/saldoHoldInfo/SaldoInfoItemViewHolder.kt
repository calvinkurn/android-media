package com.tokopedia.saldodetails.saldoHoldInfo

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.saldodetails.saldoHoldInfo.response.SaldoHoldInfoItem
import kotlinx.android.synthetic.main.hold_balance_info_item.view.*

class SaldoInfoItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind1(list: SaldoHoldInfoItem) {
        if (list.invoice?.length == 0) {
            itemView.tv_invoice.visibility = View.GONE
        } else {
            itemView.tv_invoice.text = list.invoice
        }
        itemView.tv_valueFundhold.text = list.amountFmt
        itemView.tv_valueDateWithheld.text = list.holdDate

        itemView.tv_reasonTitle.text = list.reasonTitle
        if (list.reason?.length == 0) {
            itemView.tv_valueReason.visibility = View.GONE
        } else {
            itemView.tv_valueReason.text = list.reason
        }
    }
}
