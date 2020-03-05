package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.pdpview.typeFactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.pdpview.dataModel.ProductPdpDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.pdpview.dataModel.SeeMorePdpDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.pdpview.listener.PdpViewListener
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.pdpview.viewHolder.ProductPdpViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.pdpview.viewHolder.SeeMorePdpViewHolder

interface PdpViewTypeFactory{
    fun type(dataModel: ProductPdpDataModel) : Int
    fun type(dataModel: SeeMorePdpDataModel) : Int
    fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*>
}

class PdpViewTypeFactoryImpl(private val listener: PdpViewListener) : PdpViewTypeFactory{
    override fun type(dataModel: ProductPdpDataModel): Int {
        return ProductPdpViewHolder.LAYOUT
    }

    override fun type(dataModel: SeeMorePdpDataModel): Int {
        return SeeMorePdpViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when(type){
            ProductPdpViewHolder.LAYOUT -> ProductPdpViewHolder(parent, listener)
            SeeMorePdpViewHolder.LAYOUT -> SeeMorePdpViewHolder(parent, listener)
            else -> ProductPdpViewHolder(parent, listener)
        }
    }

}