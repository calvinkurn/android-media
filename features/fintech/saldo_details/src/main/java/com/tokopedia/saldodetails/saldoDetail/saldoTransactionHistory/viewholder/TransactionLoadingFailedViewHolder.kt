package com.tokopedia.saldodetails.saldoDetail.saldoTransactionHistory.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.saldodetails.R
import com.tokopedia.saldodetails.saldoDetail.saldoTransactionHistory.domain.data.TransactionErrorModel

class TransactionLoadingFailedViewHolder(itemView: View, val retryLoading: () -> Unit) :
    AbstractViewHolder<TransactionErrorModel>(itemView) {

    private val globalError: GlobalError = itemView.findViewById(R.id.globalErrorTransactionList)


    override fun bind(element: TransactionErrorModel) {
        if(element.throwable is MessageErrorException){
            // @Todo Custom error
            globalError.setType(GlobalError.SERVER_ERROR)
        }else {
            globalError.setType(GlobalError.NO_CONNECTION)
        }
        globalError.setActionClickListener {
            retryLoading.invoke()
        }
    }

    companion object {
        val LAYOUT = R.layout.saldo_transaction_loading_failed
    }
}
