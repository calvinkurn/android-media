package com.tokopedia.settingbank.view.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter

/**
 * @author by nisie on 6/12/18.
 */

class BankAccountAdapter(adapterTypeFactory: BankAccountTypeFactoryImpl,
                         listBank: ArrayList<Visitable<Any>>)
    : BaseAdapter<BankAccountTypeFactoryImpl>(adapterTypeFactory, listBank) {

    private val list: ArrayList<Visitable<*>> = ArrayList()

}


