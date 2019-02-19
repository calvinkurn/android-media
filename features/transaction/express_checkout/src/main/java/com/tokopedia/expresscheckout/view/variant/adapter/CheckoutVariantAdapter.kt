package com.tokopedia.expresscheckout.view.variant.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.expresscheckout.view.variant.viewmodel.*

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

class CheckoutVariantAdapter(adapterTypeFactory: CheckoutVariantAdapterTypeFactory) :
        BaseListAdapter<Visitable<*>, CheckoutVariantAdapterTypeFactory>(adapterTypeFactory) {

    fun addDataViewModel(visitableList: ArrayList<Visitable<*>>) {
        visitables.addAll(visitableList)
    }

}