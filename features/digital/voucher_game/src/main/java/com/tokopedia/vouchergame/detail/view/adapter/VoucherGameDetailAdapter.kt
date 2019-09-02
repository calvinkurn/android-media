package com.tokopedia.vouchergame.detail.view.adapter

import android.content.Context
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.network.utils.ErrorHandler

class VoucherGameDetailAdapter(val context: Context,
                               adapterFactory: VoucherGameDetailAdapterFactory,
                               interactionListener: OnAdapterInteractionListener<Visitable<*>>,
                               private val loaderListener: LoaderListener):
        BaseListAdapter<Visitable<*>, VoucherGameDetailAdapterFactory>(adapterFactory, interactionListener) {

    fun renderList(data: List<Visitable<*>>) {
        clearAllElements()
        addElement(data)
    }

    override fun showLoading() {
        clearAllElements()
        super.showLoading()
    }

    fun showEmpty() {
        clearAllElements()
        val emptyModel = EmptyModel()
        emptyModel.title = "Operator tidak ditemukan"
        emptyModel.description = "Coba ganti kata kunci pencarian"
        addElement(emptyModel)
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