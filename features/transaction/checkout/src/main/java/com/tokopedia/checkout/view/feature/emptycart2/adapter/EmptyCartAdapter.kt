package com.tokopedia.checkout.view.feature.emptycart2.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter

/**
 * Created by Irfan Khoirul on 2019-05-20.
 */

class EmptyCartAdapter(adapterTypeFactory: EmptyCartAdapterTypeFactory) :
        BaseListAdapter<Visitable<*>, EmptyCartAdapterTypeFactory>(adapterTypeFactory) {

    fun addUiModels(visitableList: ArrayList<Visitable<*>>) {
        visitables.addAll(visitableList)
    }

}