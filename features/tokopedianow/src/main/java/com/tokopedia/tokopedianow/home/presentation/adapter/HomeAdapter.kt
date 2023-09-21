package com.tokopedia.tokopedianow.home.presentation.adapter

import androidx.recyclerview.widget.AsyncDifferConfig
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapterDiffutil
import com.tokopedia.tokopedianow.common.model.TokoNowChooseAddressWidgetUiModel

class HomeAdapter(
    asyncDifferConfig: AsyncDifferConfig<Visitable<*>>,
    typeFactory: HomeAdapterTypeFactory,
) : BaseListAdapterDiffutil<HomeAdapterTypeFactory>(asyncDifferConfig, typeFactory) {
    fun removeHomeChooseAddressWidget() {
        val items = currentList.toMutableList()
        val widget = getItem(TokoNowChooseAddressWidgetUiModel::class.java)
        items.remove(widget)
        submitList(items)
    }

    inline fun <reified T: Visitable<*>> getItem(itemClass: Class<T>): T? {
        return currentList.find { it.javaClass == itemClass } as T?
    }

    fun findPosition(visitable: Visitable<*>): Int {
        return currentList.indexOf(visitable)
    }
}
