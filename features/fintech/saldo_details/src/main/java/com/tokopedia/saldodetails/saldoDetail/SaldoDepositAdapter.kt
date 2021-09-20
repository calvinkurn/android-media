package com.tokopedia.saldodetails.saldoDetail

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.saldodetails.commom.listener.DataEndLessScrollListener
import com.tokopedia.saldodetails.saldoDetail.saldoTransactionHistory.adapter.SaldoDetailTransactionFactory
import com.tokopedia.saldodetails.saldoDetail.saldoTransactionHistory.domain.data.DepositHistoryList

class SaldoDepositAdapter(adapterTypeFactory: SaldoDetailTransactionFactory) :
        BaseListAdapter<DepositHistoryList, SaldoDetailTransactionFactory>(adapterTypeFactory),
        DataEndLessScrollListener.OnDataEndlessScrollListener {

    override fun endlessDataSize(): Int {
        return dataSize
    }
}
