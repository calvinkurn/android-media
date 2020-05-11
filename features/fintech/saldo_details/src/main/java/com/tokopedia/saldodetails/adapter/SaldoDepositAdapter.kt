package com.tokopedia.saldodetails.adapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.saldodetails.adapter.listener.DataEndLessScrollListener
import com.tokopedia.saldodetails.response.model.DepositHistoryList

class SaldoDepositAdapter(adapterTypeFactory: SaldoDetailTransactionFactory) :
        BaseListAdapter<DepositHistoryList, SaldoDetailTransactionFactory>(adapterTypeFactory),
        DataEndLessScrollListener.OnDataEndlessScrollListener {

    override fun endlessDataSize(): Int {
        return dataSize
    }
}
