package com.tokopedia.rechargegeneral.presentation.adapter

import android.content.Context
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel
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
        val (errMsg, errCode) = ErrorHandler.getErrorMessagePair(context, e, ErrorHandler.Builder().build())
        val errorNetworkModel = ErrorNetworkModel()

        errorNetworkModel.run {
            errorMessage = errMsg
            subErrorMessage = "${context.getString(com.tokopedia.utils.R.string.title_try_again)}. Kode Error: ($errCode)"
            onRetryListener = ErrorNetworkModel.OnRetryListener {
                showLoading()
                loaderListener.loadData()
            }
        }
        setErrorNetworkModel(errorNetworkModel)
        showErrorNetwork()
    }

    interface LoaderListener {
        fun loadData()
    }

}