package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.mixleft.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.pdpview.typeFactory.FlashSaleCardTypeFactory

/**
 * @author by yoasfs on 2020-03-05
 */
class MixLeftAdapter (items: List<Visitable<*>>,
                      typeFactory: FlashSaleCardTypeFactory)
    :BaseAdapter<FlashSaleCardTypeFactory>(typeFactory, items){

    val data: List<Visitable<*>>
        get() = visitables

    override fun onViewRecycled(holder: AbstractViewHolder<out Visitable<*>>) {
        super.onViewRecycled(holder)
        holder.onViewRecycled()
    }

}