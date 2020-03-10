package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.pdpview.typeFactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.pdpview.dataModel.EmptyDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.pdpview.dataModel.FlashSaleDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.pdpview.dataModel.SeeMorePdpDataModel

interface FlashSaleCardTypeFactory: AdapterTypeFactory {
    fun type(dataModel: EmptyDataModel) : Int
    fun type(dataModel: FlashSaleDataModel) : Int
    fun type(dataModel: SeeMorePdpDataModel) : Int

    override fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*>
}
