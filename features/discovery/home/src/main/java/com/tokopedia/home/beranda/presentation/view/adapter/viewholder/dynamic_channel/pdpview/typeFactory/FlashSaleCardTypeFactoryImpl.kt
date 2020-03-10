package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.pdpview.typeFactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.pdpview.dataModel.EmptyDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.pdpview.dataModel.FlashSaleDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.pdpview.dataModel.SeeMorePdpDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.pdpview.listener.FlashSaleCardListener
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.pdpview.viewHolder.EmptyCardViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.pdpview.viewHolder.FlashSaleViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.pdpview.viewHolder.SeeMorePdpViewHolder

/**
 * @author by yoasfs on 2020-03-07
 */

class FlashSaleCardViewTypeFactoryImpl(private val listener: FlashSaleCardListener) :
        BaseAdapterTypeFactory(),FlashSaleCardTypeFactory {

    override fun type(dataModel: EmptyDataModel): Int {
        return EmptyCardViewHolder.LAYOUT
    }

    override fun type(dataModel: FlashSaleDataModel): Int {
        return FlashSaleViewHolder.LAYOUT
    }

    override fun type(dataModel: SeeMorePdpDataModel): Int {
        return SeeMorePdpViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        val viewHolder: AbstractViewHolder<*>
        if (type == FlashSaleViewHolder.LAYOUT) {
            viewHolder = FlashSaleViewHolder(parent, listener)
        } else if (type == SeeMorePdpViewHolder.LAYOUT) {
            viewHolder = SeeMorePdpViewHolder(parent, listener)
        } else if (type == EmptyCardViewHolder.LAYOUT) {
            viewHolder = EmptyCardViewHolder(parent, listener)
        } else {
            viewHolder = super.createViewHolder(parent, type)
        }
        return viewHolder
    }

}