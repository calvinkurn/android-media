package com.tokopedia.affiliate.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.affiliate.model.response.WithdrawalInfoHistory
import com.tokopedia.affiliate.ui.viewholder.AffiliateSaldoWithdrawalStatusDetailViewHolder

class AffiliateSaldoWithdrawalStatusAdapter(var historyList: ArrayList<WithdrawalInfoHistory>) : RecyclerView.Adapter<AffiliateSaldoWithdrawalStatusDetailViewHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AffiliateSaldoWithdrawalStatusDetailViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return AffiliateSaldoWithdrawalStatusDetailViewHolder.getViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holderAffiliate: AffiliateSaldoWithdrawalStatusDetailViewHolder, position: Int) {
        holderAffiliate.bindData(historyList[position], position == 0, position != historyList.size-1)
    }

    override fun getItemCount() = historyList.size
}