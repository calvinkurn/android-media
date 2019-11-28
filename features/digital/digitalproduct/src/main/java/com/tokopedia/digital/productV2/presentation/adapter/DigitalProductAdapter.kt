package com.tokopedia.digital.productV2.presentation.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.common.topupbills.widget.TopupBillsInputFieldWidget

class DigitalProductAdapter(val context: Context,
                            adapterFactory: DigitalProductAdapterFactory):
        BaseListAdapter<Visitable<DigitalProductAdapterFactory>, DigitalProductAdapterFactory>(adapterFactory) {

    override fun onCreateViewItem(parent: ViewGroup?, viewType: Int): View {
        return TopupBillsInputFieldWidget(context)
    }

    fun renderList(data: List<Visitable<*>>) {
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
//        showErrorNetwork(ErrorHandler.getErrorMessage(context, e)) {
//            showLoading()
//            loaderListener.loadData()
//        }
    }

    interface LoaderListener {
        fun loadData()
    }

}