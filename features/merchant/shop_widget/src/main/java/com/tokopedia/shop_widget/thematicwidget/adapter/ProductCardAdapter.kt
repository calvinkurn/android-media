package com.tokopedia.shop_widget.thematicwidget.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.shop_widget.thematicwidget.typefactory.ProductCardTypeFactoryImpl

open class ProductCardAdapter(
    baseListAdapterTypeFactory: ProductCardTypeFactoryImpl,
    private val differ: ProductCardDiffer
): BaseListAdapter<Visitable<*>, ProductCardTypeFactoryImpl>(baseListAdapterTypeFactory) {

    fun submitList(items: List<Visitable<*>>) {
        val diffUtilCallback = differ.create(visitables, items)
        val result = DiffUtil.calculateDiff(diffUtilCallback)
        visitables.clear()
        visitables.addAll(items)
        result.dispatchUpdatesTo(this)
    }
}