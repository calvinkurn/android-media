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

    fun showEmpty() {
//        clearAllElements()
//        val emptyModel = EmptyModel()
//        emptyModel.title = resources.getString(R.string.vg_empty_state_title)
//        emptyModel.description = resources.getString(R.string.vg_empty_state_desc)
//        addElement(emptyModel)
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