package com.tokopedia.smartbills.presentation.adapter

import android.content.Context
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListCheckableAdapter
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.smartbills.data.RechargeBills

class SmartBillsAdapter(private val context: Context,
                        adapterFactory: SmartBillsAdapterFactory,
                        private val loaderListener: LoaderListener,
                        private val checkableListener: OnCheckableAdapterListener<RechargeBills>):
        BaseListCheckableAdapter<RechargeBills, SmartBillsAdapterFactory>(adapterFactory, checkableListener) {

    fun renderList(data: List<RechargeBills>) {
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

    fun toggleAllItems(value: Boolean) {
        if (value) {
            // Check all items
            if (totalChecked < dataSize) {
                val allCheckedSet = HashSet<Int>()
                for (i in 0 until data.size) allCheckedSet.add(i)
                setCheckedPositionList(allCheckedSet)
                notifyDataSetChanged()
            }
        } else {
            resetCheckedItemSet()
        }
    }

    interface LoaderListener {
        fun loadData()
    }

}