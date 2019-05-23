package com.tokopedia.checkout.view.feature.emptycart2.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter

/**
 * Created by Irfan Khoirul on 2019-05-20.
 */

class EmptyCartAdapter(adapterTypeFactory: EmptyCartAdapterTypeFactory) :
        BaseListAdapter<Visitable<*>, EmptyCartAdapterTypeFactory>(adapterTypeFactory) {

    override fun addElement(position: Int, element: Visitable<*>?) {
        visitables.add(position, element)
    }

    override fun addElement(element: Visitable<*>?) {
        visitables.add(element)
    }

    fun removeElement(position: Int) {
        visitables.removeAt(position)
    }

    fun getIndexOf(element: Visitable<*>?): Int {
        return visitables.indexOf(element)
    }
}