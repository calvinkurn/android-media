package com.tokopedia.dilayanitokopedia.home.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.dilayanitokopedia.common.base.adapter.BaseDtListAdapter
import com.tokopedia.dilayanitokopedia.common.model.DtChooseAddressWidgetUiModel
import com.tokopedia.dilayanitokopedia.home.presentation.adapter.differ.HomeListDiffer
import timber.log.Timber


class DtHomeAdapter(
        typeFactory: DtHomeAdapterTypeFactory,
        differ: HomeListDiffer
) : BaseDtListAdapter<Visitable<*>, DtHomeAdapterTypeFactory>(typeFactory, differ) {

        init {
                Timber.d("logging--DtHomeAdapter called")

        }

    fun removeHomeChooseAddressWidget() {
        val items = data.toMutableList()
        val widget = getItem(DtChooseAddressWidgetUiModel::class.java)
        items.remove(widget)
        submitList(items)
    }

    inline fun <reified T: Visitable<*>> getItem(itemClass: Class<T>): T? {
        return data.find { it.javaClass == itemClass } as T?
    }

    fun findPosition(visitable: Visitable<*>): Int {
        return data.indexOf(visitable)
    }
}