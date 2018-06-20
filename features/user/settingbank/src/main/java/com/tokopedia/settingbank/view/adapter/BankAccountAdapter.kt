package com.tokopedia.settingbank.view.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.settingbank.view.viewmodel.BankAccountViewModel

/**
 * @author by nisie on 6/12/18.
 */

class BankAccountAdapter(adapterTypeFactory: BankAccountTypeFactoryImpl,
                         listBank: ArrayList<Visitable<*>>)
    : BaseAdapter<BankAccountTypeFactoryImpl>(adapterTypeFactory, listBank) {

    fun addList(list: ArrayList<BankAccountViewModel>) {
        val lastPosition = this.visitables.size
        this.visitables.addAll(list)
        notifyItemRangeInserted(lastPosition, list.size)
    }
}


