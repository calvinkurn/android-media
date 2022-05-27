package com.tokopedia.tokopedianow.home.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.tokopedianow.home.presentation.adapter.differ.HomeLeftCarouselProductCardDiffer

open class HomeLeftCarouselProductCardAdapter(
    baseListAdapterTypeFactory: HomeLeftCarouselProductCardTypeFactoryImpl,
    private val differ: HomeLeftCarouselProductCardDiffer
): BaseListAdapter<Visitable<*>, HomeLeftCarouselProductCardTypeFactoryImpl>(baseListAdapterTypeFactory) {

    fun submitList(items: List<Visitable<*>>) {
        val diffUtilCallback = differ.create(visitables, items)
        val result = DiffUtil.calculateDiff(diffUtilCallback)
        visitables.clear()
        visitables.addAll(items)
        result.dispatchUpdatesTo(this)
    }
}