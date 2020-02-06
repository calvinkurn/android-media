package com.tokopedia.salam.umrah.travel.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.BaseEmptyViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.EmptyViewHolder
import com.tokopedia.salam.umrah.travel.presentation.adapter.viewholder.UmrahTravelAgentGalleryLoadingViewHolder
import com.tokopedia.salam.umrah.travel.presentation.adapter.viewholder.UmrahTravelAgentGalleryOneImageViewHolder
import com.tokopedia.salam.umrah.travel.presentation.adapter.viewholder.UmrahTravelAgentGalleryThreeImageViewHolder
import com.tokopedia.salam.umrah.travel.presentation.adapter.viewholder.UmrahTravelAgentGalleryVideoViewHolder

class UmrahTravelGalleryAdapterTypeFactory (private val callback: BaseEmptyViewHolder.Callback): BaseAdapterTypeFactory(){

    fun type(type:String, size:Int) : Int {
         return if((type.equals("DOCUMENTATION") || type.equals("TOOL")) && size ==1) UmrahTravelAgentGalleryOneImageViewHolder.LAYOUT
         else if ((type.equals("DOCUMENTATION") || type.equals("TOOL")) && size >1) UmrahTravelAgentGalleryThreeImageViewHolder.LAYOUT
         else UmrahTravelAgentGalleryVideoViewHolder.LAYOUT
    }

    override fun type(viewModel: LoadingModel?): Int  = UmrahTravelAgentGalleryLoadingViewHolder.LAYOUT

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> =
            when(type){
                UmrahTravelAgentGalleryOneImageViewHolder.LAYOUT -> UmrahTravelAgentGalleryOneImageViewHolder(parent)
                UmrahTravelAgentGalleryThreeImageViewHolder.LAYOUT -> UmrahTravelAgentGalleryThreeImageViewHolder(parent)
                UmrahTravelAgentGalleryVideoViewHolder.LAYOUT -> UmrahTravelAgentGalleryVideoViewHolder(parent)
                UmrahTravelAgentGalleryLoadingViewHolder.LAYOUT -> UmrahTravelAgentGalleryLoadingViewHolder(parent)
                EmptyViewHolder.LAYOUT -> EmptyViewHolder(parent, callback)
                else -> super.createViewHolder(parent, type)
            }
}