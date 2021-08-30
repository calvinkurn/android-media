package com.tokopedia.saldodetails.saldoDetail.saldoTransactionHistory.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.saldodetails.R

class TransactionLoadMoreViewHolder (itemView: View) :
    AbstractViewHolder<LoadingMoreModel>(itemView) {



    override fun bind(element: LoadingMoreModel) {}

    companion object {
        val LAYOUT = R.layout.saldo_item_load_more_transaction
    }
}
