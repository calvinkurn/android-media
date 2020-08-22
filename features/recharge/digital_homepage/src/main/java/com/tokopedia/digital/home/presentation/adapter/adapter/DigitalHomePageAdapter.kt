package com.tokopedia.digital.home.presentation.adapter.adapter

import android.content.Context
import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.digital.home.presentation.adapter.DigitalHomePageAdapterFactory
import com.tokopedia.network.utils.ErrorHandler

class DigitalHomePageAdapter(val context: Context,
                             adapterFactory: DigitalHomePageAdapterFactory,
                             private val loaderListener: LoaderListener):
        BaseListAdapter<Visitable<*>, DigitalHomePageAdapterFactory>(adapterFactory) {

    fun renderList(data: List<Visitable<*>>) {
        val result = DiffUtil.calculateDiff(DigitalHomePageDiffUtil(this.visitables, data))
        visitables.clear()
        visitables.addAll(data)
        result.dispatchUpdatesTo(this)
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