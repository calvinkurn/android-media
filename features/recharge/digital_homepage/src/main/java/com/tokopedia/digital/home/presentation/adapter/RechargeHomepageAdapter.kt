package com.tokopedia.digital.home.presentation.adapter

import android.content.Context
import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.digital.home.R
import com.tokopedia.network.utils.ErrorHandler

class RechargeHomepageAdapter(val context: Context,
                              adapterFactory: RechargeHomepageAdapterTypeFactory,
                              private val loaderListener: LoaderListener):
        BaseListAdapter<Visitable<*>, RechargeHomepageAdapterTypeFactory>(adapterFactory) {

    fun renderList(data: List<Visitable<*>>) {
        if (data.isEmpty()) {
            visitables.clear()
            val emptyModel = EmptyModel()
            emptyModel.content = context.getString(R.string.digital_home_empty_list_label)
            visitables.add(emptyModel)
            notifyDataSetChanged()
        } else {
            val result = DiffUtil.calculateDiff(RechargeHomepageDiffUtil(this.visitables, data))
            visitables.clear()
            visitables.addAll(data)
            result.dispatchUpdatesTo(this)
        }
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