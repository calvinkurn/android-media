package com.tokopedia.vouchergame.detail.view.adapter

import android.content.Context
import android.content.res.Resources
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.vouchergame.R

class VoucherGameDetailAdapter(val context: Context,
                               val resources: Resources,
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
        emptyModel.title = resources.getString(R.string.vg_empty_state_title)
        emptyModel.description = resources.getString(R.string.vg_empty_state_desc)
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