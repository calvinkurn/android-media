package com.tokopedia.pms.bankaccount.view.viewholder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.pms.R
import com.tokopedia.pms.bankaccount.data.model.BankListModel

/**
 * Created by zulfikarrahman on 7/5/18.
 */
class BankListViewHolder(view: View, val clickAction: (BankListModel) -> Unit) :
    RecyclerView.ViewHolder(view) {
    private val bankName: TextView = view.findViewById(R.id.bank_name)
    fun bind(element: BankListModel) {
        bankName.text = element.bankName
        itemView.setOnClickListener { clickAction(element) }
    }

    companion object {
        var Layout = R.layout.item_bank_list
    }

}