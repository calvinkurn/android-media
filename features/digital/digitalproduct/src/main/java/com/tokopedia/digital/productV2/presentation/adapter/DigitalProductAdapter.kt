package com.tokopedia.digital.productV2.presentation.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.common.topupbills.widget.TopupBillsInputFieldWidget
import com.tokopedia.network.utils.ErrorHandler

class DigitalProductAdapter(val context: Context,
                            adapterFactory: DigitalProductAdapterFactory,
                            private val loaderListener: LoaderListener):
        BaseListAdapter<Visitable<DigitalProductAdapterFactory>, DigitalProductAdapterFactory>(adapterFactory) {

    override fun onCreateViewItem(parent: ViewGroup?, viewType: Int): View {
        if (viewType in 1..2) {
            return TopupBillsInputFieldWidget(context)
        }
        return super.onCreateViewItem(parent, viewType)
    }

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