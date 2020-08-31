package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.pdpview.typeFactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.pdpview.dataModel.EmptyDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.pdpview.dataModel.FlashSaleDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.pdpview.dataModel.SeeMorePdpDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.pdpview.viewHolder.EmptyCardViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.pdpview.viewHolder.FlashSaleViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.pdpview.viewHolder.SeeMorePdpViewHolder

/**
 * @author by yoasfs on 2020-03-07
 */

class FlashSaleCardViewTypeFactoryImpl(private val channels: DynamicHomeChannel.Channels) :
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
        return when (type) {
            FlashSaleViewHolder.LAYOUT -> {
                FlashSaleViewHolder(parent, channels)
            }
            SeeMorePdpViewHolder.LAYOUT -> {
                SeeMorePdpViewHolder(parent, channels)
            }
            EmptyCardViewHolder.LAYOUT -> {
                EmptyCardViewHolder(parent)
            }
            else -> {
                super.createViewHolder(parent, type)
            }
        }
    }

}