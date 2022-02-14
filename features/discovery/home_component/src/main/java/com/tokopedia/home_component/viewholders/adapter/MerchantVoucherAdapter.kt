package com.tokopedia.home_component.viewholders.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.productcardgridcarousel.typeFactory.CommonCarouselProductCardTypeFactory

/**
 * Created by dhaba
 */
class MerchantVoucherAdapter(
    items: List<Visitable<*>>,
    typeFactory: CommonCarouselProductCardTypeFactory
) : BaseAdapter<CommonCarouselProductCardTypeFactory>(typeFactory, items) {

    val data: List<Visitable<*>>
        get() = visitables

    override fun onViewRecycled(holder: AbstractViewHolder<out Visitable<*>>) {
        super.onViewRecycled(holder)
        holder.onViewRecycled()
    }
}