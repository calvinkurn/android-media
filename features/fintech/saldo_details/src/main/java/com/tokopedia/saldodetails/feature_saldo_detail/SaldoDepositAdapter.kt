package com.tokopedia.saldodetails.feature_saldo_detail

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.saldodetails.commom.listener.DataEndLessScrollListener
import com.tokopedia.saldodetails.feature_saldo_transaction_history.adapter.SaldoDetailTransactionFactory
import com.tokopedia.saldodetails.feature_saldo_transaction_history.domain.data.DepositHistoryList

class SaldoDepositAdapter(adapterTypeFactory: SaldoDetailTransactionFactory) :
        BaseListAdapter<DepositHistoryList, SaldoDetailTransactionFactory>(adapterTypeFactory),
        DataEndLessScrollListener.OnDataEndlessScrollListener {

    override fun endlessDataSize(): Int {
        return dataSize
    }
}
