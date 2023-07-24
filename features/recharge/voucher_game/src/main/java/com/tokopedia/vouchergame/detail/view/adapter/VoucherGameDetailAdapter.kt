package com.tokopedia.vouchergame.detail.view.adapter

import android.content.Context
import android.content.res.Resources
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.vouchergame.R
import com.tokopedia.vouchergame.detail.view.adapter.viewholder.VoucherGameProductViewHolder

class VoucherGameDetailAdapter(val context: Context,
                               val resources: Resources,
                               adapterFactory: VoucherGameDetailAdapterFactory,
                               private val loaderListener: LoaderListener):
        BaseListAdapter<Visitable<*>, VoucherGameDetailAdapterFactory>(adapterFactory) {

    var hasMoreDetails = false
    var selectedPos = SELECTED_POSITION_INIT

    override fun onBindViewHolder(holder: AbstractViewHolder<out Visitable<*>>, position: Int) {
        if (holder is VoucherGameProductViewHolder) {
            holder.adapter = this
            holder.hasMoreDetails = hasMoreDetails
        }
        super.onBindViewHolder(holder, position)
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
        clearAllElements()
        val emptyModel = EmptyModel()
        emptyModel.title = resources.getString(R.string.vg_empty_state_title)
        emptyModel.description = resources.getString(R.string.vg_empty_state_desc)
        addElement(emptyModel)
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

    fun setSelectedProduct(position: Int) {
        // Check if a product has been selected
        if (selectedPos > -1) {
            val oldPosition = selectedPos
            notifyItemChanged(oldPosition)
        }
        selectedPos = position
        notifyItemChanged(position)
    }

    interface LoaderListener {
        fun loadData()
    }

    companion object {
        const val SELECTED_POSITION_INIT = -1
    }

}