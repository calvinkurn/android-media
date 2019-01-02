package com.tokopedia.expresscheckout.view.profile.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter

/**
 * Created by Irfan Khoirul on 01/01/19.
 */

class CheckoutProfileAdapter(adapterTypeFactory: CheckoutProfileAdapterTypeFactory) :
        BaseListAdapter<Visitable<*>, CheckoutProfileAdapterTypeFactory>(adapterTypeFactory) {

    fun addDataViewModel(visitableList: ArrayList<Visitable<*>>) {
        visitables.addAll(visitableList)
    }

}