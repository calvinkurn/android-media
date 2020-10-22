package com.tokopedia.smartbills.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListCheckableAdapter
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.smartbills.data.RechargeBills

class SmartBillsAdapter(adapterFactory: SmartBillsAdapterFactory,
                        checkableListener: OnCheckableAdapterListener<RechargeBills>):
        BaseListCheckableAdapter<RechargeBills, SmartBillsAdapterFactory>(adapterFactory, checkableListener) {

    fun renderEmptyState() {
        clearAllElements()
        addElement(EmptyModel())
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

}