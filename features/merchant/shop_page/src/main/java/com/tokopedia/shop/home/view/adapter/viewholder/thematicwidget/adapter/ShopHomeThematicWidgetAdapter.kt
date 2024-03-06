package com.tokopedia.shop.home.view.adapter.viewholder.thematicwidget.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.shop.home.view.adapter.viewholder.thematicwidget.typefactory.ShopHomeThematicWidgetTypeFactoryImpl

open class ShopHomeThematicWidgetAdapter(
    baseListAdapterTypeFactory: ShopHomeThematicWidgetTypeFactoryImpl,
    private val differ: ShopHomeThematicWidgetDiffUtil
): BaseListAdapter<Visitable<*>, ShopHomeThematicWidgetTypeFactoryImpl>(baseListAdapterTypeFactory) {

    fun submitList(items: List<Visitable<*>>) {
        val diffUtilCallback = differ.create(visitables, items)
        val result = DiffUtil.calculateDiff(diffUtilCallback)
        visitables.clear()
        visitables.addAll(items)
        result.dispatchUpdatesTo(this)
    }
}
