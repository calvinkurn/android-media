package com.tokopedia.tokofood.home.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable

class TokoFoodHomeAdapter(
    typeFactory: TokoFoodHomeAdapterTypeFactory,
    differ: TokoFoodHomeListDiffer
): BaseTokoFoodHomeListAdapter<Visitable<*>, TokoFoodHomeAdapterTypeFactory>(typeFactory, differ) {

    fun <T> getItem(itemClass: Class<T>): Visitable<*>? {
        return data.find { it.javaClass == itemClass}
    }

    fun findPosition(visitable: Visitable<*>): Int {
        return data.indexOf(visitable)
    }
}