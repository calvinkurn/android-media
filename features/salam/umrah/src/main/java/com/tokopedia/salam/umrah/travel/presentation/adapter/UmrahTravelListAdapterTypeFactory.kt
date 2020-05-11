package com.tokopedia.salam.umrah.travel.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.BaseEmptyViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.EmptyViewHolder
import com.tokopedia.salam.umrah.common.data.TravelAgent
import com.tokopedia.salam.umrah.common.data.UmrahTravelAgentsEntity
import com.tokopedia.salam.umrah.travel.presentation.adapter.viewholder.UmrahTravelListLoadingViewHolder
import com.tokopedia.salam.umrah.travel.presentation.adapter.viewholder.UmrahTravelListViewHolder

/**
 * @author by Firman on 04/03/2020
 */

class UmrahTravelListAdapterTypeFactory (private val callback: BaseEmptyViewHolder.Callback): BaseAdapterTypeFactory(){
    fun type(travelAgent: TravelAgent): Int = UmrahTravelListViewHolder.LAYOUT
    override fun type(viewModel:LoadingModel):Int = UmrahTravelListLoadingViewHolder.LAYOUT

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type){
            UmrahTravelListViewHolder.LAYOUT -> UmrahTravelListViewHolder(parent)
            UmrahTravelListLoadingViewHolder.LAYOUT -> UmrahTravelListLoadingViewHolder(parent)
            EmptyViewHolder.LAYOUT -> EmptyViewHolder(parent, callback)
            else -> super.createViewHolder(parent, type)
        }
    }
}