package com.tokopedia.saldodetails.saldoDetail.saldoTransactionHistory.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.saldodetails.transactionDetailPages.withdrawal.SaldoWithdrawalStatusDetailViewHolder
import com.tokopedia.saldodetails.transactionDetailPages.withdrawal.WithdrawalInfoHistory

class SaldoWithdrawalStatusAdapter(var historyList: ArrayList<WithdrawalInfoHistory>) : RecyclerView.Adapter<SaldoWithdrawalStatusDetailViewHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SaldoWithdrawalStatusDetailViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return SaldoWithdrawalStatusDetailViewHolder.getViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: SaldoWithdrawalStatusDetailViewHolder, position: Int) {
        holder.bindData(historyList[position], position == 0, position != historyList.size-1)
    }

    override fun getItemCount() = historyList.size
}