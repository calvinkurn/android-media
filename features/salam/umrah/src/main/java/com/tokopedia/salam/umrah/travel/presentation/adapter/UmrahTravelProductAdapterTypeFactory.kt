package com.tokopedia.salam.umrah.travel.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.BaseEmptyViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.EmptyViewHolder
import com.tokopedia.salam.umrah.travel.presentation.adapter.viewholder.UmrahTravelAgentProductLoadingViewHolder
import com.tokopedia.salam.umrah.travel.presentation.adapter.viewholder.UmrahTravelAgentProductViewHolder


/**
 * @author by Firman on 03/02/2020
 */

class UmrahTravelProductAdapterTypeFactory (private val callback: BaseEmptyViewHolder.Callback, val listener: UmrahTravelAgentProductViewHolder.SetOnClickListener): BaseAdapterTypeFactory(){
    fun type(): Int = UmrahTravelAgentProductViewHolder.LAYOUT
    override fun type(viewModel: LoadingModel?): Int = UmrahTravelAgentProductLoadingViewHolder.LAYOUT


    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> =
            when(type){
                UmrahTravelAgentProductViewHolder.LAYOUT -> UmrahTravelAgentProductViewHolder(parent, listener)
                UmrahTravelAgentProductLoadingViewHolder.LAYOUT -> UmrahTravelAgentProductLoadingViewHolder(parent)
                EmptyViewHolder.LAYOUT -> EmptyViewHolder(parent, callback)
                else -> super.createViewHolder(parent, type)
            }
}