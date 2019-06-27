package com.tokopedia.topads.debit.autotopup.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topads.debit.autotopup.data.model.AutoTopUpItem
import com.tokopedia.topads.debit.autotopup.view.adapter.viewholder.TopAdsAutoTopUpPriceViewHolder

class TopAdsAutoTopUpPriceTypeFactory(private val listener: TopAdsAutoTopUpPriceViewHolder.ItemListener)
    : BaseAdapterTypeFactory(){

    fun type(item: AutoTopUpItem) = TopAdsAutoTopUpPriceViewHolder.LAYOUT

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type){
            TopAdsAutoTopUpPriceViewHolder.LAYOUT -> TopAdsAutoTopUpPriceViewHolder(parent, listener)
            else -> super.createViewHolder(parent, type)
        }

    }
}