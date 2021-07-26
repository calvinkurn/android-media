package com.tokopedia.saldodetails.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.saldodetails.response.model.DepositHistoryList

class SaldoTransactionAdapter(adapterTypeFactory: SaldoDetailTransactionFactory) :
        BaseListAdapter<DepositHistoryList, SaldoDetailTransactionFactory>(adapterTypeFactory) {

    private fun showEmptyState(){
        addElement(EmptyModel())
        notifyItemChanged(0)
    }

    fun showLoadingInAdapter(){
        //todo remove network state if any
        removeErrorNetwork()
        showLoading()
    }

    fun addAllElements(element: List<Visitable<*>>) {
        hideLoading()
        addElement(element)
        //get items from diff
        if(element.isEmpty()){
            showEmptyState()
        }
    }

}