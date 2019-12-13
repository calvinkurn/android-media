package com.tokopedia.saldodetails.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.saldodetails.response.model.saldoholdinfo.response.BuyerDataItem
import com.tokopedia.saldodetails.response.model.saldoholdinfo.response.SellerDataItem
import kotlinx.android.synthetic.main.hold_balance_info_item.view.*

class SaldoInfoItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind1(list: SellerDataItem) {
        itemView.tv_invoice.text = list.invoice
        itemView.tv_valueFundhold.text = list.amountFmt
        itemView.tv_valueDateWithheld.text = list.holdDate
        itemView.tv_valueLiquidOnDate.text = list.releaseDate

        itemView.tv_reasonTitle.text = list.reasonTitle
        itemView.tv_valueReason.text = list.reason
    }

    fun bind2(list: BuyerDataItem) {
        itemView.tv_invoice.text = list.invoice
        itemView.tv_valueFundhold.text = list.amountFmt
        itemView.tv_valueDateWithheld.text = list.holdDate
        itemView.tv_valueLiquidOnDate.text = list.releaseDate

        itemView.tv_reasonTitle.text = list.reasonTitle
        itemView.tv_valueReason.text = list.reason
    }
}