package com.tokopedia.smartbills.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListCheckableAdapter
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.smartbills.data.RechargeBills

class SmartBillsAdapter(adapterFactory: SmartBillsAdapterFactory,
                        checkableListener: OnCheckableAdapterListener<RechargeBills>) :
        BaseListCheckableAdapter<RechargeBills, SmartBillsAdapterFactory>(adapterFactory, checkableListener) {

    fun renderEmptyState() {
        clearAllElements()
        addElement(EmptyModel())
    }

    fun toggleAllItems(value: Boolean, bills: List<RechargeBills>) {
        val allCheckedSet = HashSet<Int>()
        if (value) {
            // Check all items
            if (totalChecked < bills.size && bills.size > 0) {
                for (i in 0 until bills.size) {
                    allCheckedSet.add(i)
                    setCheckedPositionList(allCheckedSet)
                    notifyItemChanged(i)
                }
            }
        } else {
            if (bills.size > 0) {
                for (i in 0 until bills.size) {
                    setCheckedPositionList(allCheckedSet)
                    notifyItemChanged(i)
                }
            }
        }
    }
}