package com.tokopedia.atc_variant.view.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

class CheckoutVariantAdapter(adapterTypeFactory: CheckoutVariantAdapterTypeFactory) :
        BaseListAdapter<Visitable<*>, CheckoutVariantAdapterTypeFactory>(adapterTypeFactory) {

    fun addDataViewModel(visitableList: ArrayList<Visitable<*>>) {
        visitables.addAll(visitableList)
    }

    fun addSingleDataViewModel(visitableList: Visitable<*>) {
        visitables.add(visitableList)
    }

}