package com.tokopedia.pms.bankaccount.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.pms.bankaccount.view.viewholder.BankListViewHolder
import com.tokopedia.pms.bankaccount.data.model.BankListModel

/**
 * Created by zulfikarrahman on 7/5/18.
 */
class BankListAdapter(var bankList: List<BankListModel>, val actionItemListener: (BankListModel) -> Unit) :
    RecyclerView.Adapter<BankListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BankListViewHolder {
        return BankListViewHolder(
            LayoutInflater.from(parent.context).inflate(BankListViewHolder.Layout, parent, false),
            actionItemListener
        )
    }

    override fun onBindViewHolder(holder: BankListViewHolder, position: Int) {
        holder.bind(bankList[position])
    }

    override fun getItemCount() = bankList.size
}