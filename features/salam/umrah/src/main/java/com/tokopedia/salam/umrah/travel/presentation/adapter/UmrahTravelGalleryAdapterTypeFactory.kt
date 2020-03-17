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

class UmrahTravelGalleryAdapterTypeFactory (private val callback: BaseEmptyViewHolder.Callback,
                                            private val listenerThreeImage: UmrahTravelAgentGalleryThreeImageViewHolder.SetOnClickListener,
                                            private val listenerOneImage: UmrahTravelAgentGalleryOneImageViewHolder.SetOnClickListener,
                                            private val listenerYoutube: UmrahTravelAgentGalleryVideoViewHolder.OnYoutubeClick):
        BaseAdapterTypeFactory(){

    fun type(type:String, size:Int) : Int {
         return if((type.equals(TYPE_DOCUMENTATION) || type.equals(TYPE_TOOL)) && size ==1) UmrahTravelAgentGalleryOneImageViewHolder.LAYOUT
         else if ((type.equals(TYPE_DOCUMENTATION) || type.equals(TYPE_TOOL)) && size >1) UmrahTravelAgentGalleryThreeImageViewHolder.LAYOUT
         else if (type.equals(TYPE_VIDEO)) UmrahTravelAgentGalleryVideoViewHolder.LAYOUT
         else 0
    }

    override fun type(viewModel: LoadingModel?): Int  = UmrahTravelAgentGalleryLoadingViewHolder.LAYOUT

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> =
            when(type){
                UmrahTravelAgentGalleryOneImageViewHolder.LAYOUT -> UmrahTravelAgentGalleryOneImageViewHolder(parent, listenerOneImage)
                UmrahTravelAgentGalleryThreeImageViewHolder.LAYOUT -> UmrahTravelAgentGalleryThreeImageViewHolder(parent, listenerThreeImage)
                UmrahTravelAgentGalleryVideoViewHolder.LAYOUT -> UmrahTravelAgentGalleryVideoViewHolder(parent,listenerYoutube)
                UmrahTravelAgentGalleryLoadingViewHolder.LAYOUT -> UmrahTravelAgentGalleryLoadingViewHolder(parent)
                EmptyViewHolder.LAYOUT -> EmptyViewHolder(parent, callback)
                else -> super.createViewHolder(parent, type)
            }

    companion object{
        const val TYPE_DOCUMENTATION = "DOCUMENTATION"
        const val TYPE_TOOL = "TOOL"
        const val TYPE_VIDEO = "VIDEO"
    }

}