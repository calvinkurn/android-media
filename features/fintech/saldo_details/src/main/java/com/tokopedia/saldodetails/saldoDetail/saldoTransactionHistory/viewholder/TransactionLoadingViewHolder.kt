package com.tokopedia.saldodetails.saldoDetail.saldoTransactionHistory.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.saldodetails.R

class TransactionLoadingViewHolder (itemView: View) :
    AbstractViewHolder<LoadingModel>(itemView) {



    override fun bind(element: LoadingModel) {}

    companion object {
        val LAYOUT = R.layout.saldo_item_load_transactions
    }
}
