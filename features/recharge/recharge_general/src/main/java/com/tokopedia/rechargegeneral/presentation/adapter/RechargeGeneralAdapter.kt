package com.tokopedia.rechargegeneral.presentation.adapter

import android.content.Context
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.network.utils.ErrorHandler

class RechargeGeneralAdapter(val context: Context,
                             adapterFactory: RechargeGeneralAdapterFactory,
                             private val loaderListener: LoaderListener):
        BaseListAdapter<Visitable<RechargeGeneralAdapterFactory>, RechargeGeneralAdapterFactory>(adapterFactory) {

    fun renderList(data: List<Visitable<*>>) {
        clearAllElements()
        addElement(data)
    }

    override fun showLoading() {
        clearAllElements()
        super.showLoading()
    }

    fun showGetListError(e: Throwable) {
        showErrorNetwork(ErrorHandler.getErrorMessage(context, e)) {
            showLoading()
            loaderListener.loadData()
        }
    }

    interface LoaderListener {
        fun loadData()
    }

}