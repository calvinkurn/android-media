package com.tokopedia.home_component.productcardgridcarousel.viewHolder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselEmptyCardDataModel
import com.tokopedia.home_component.R

class CarouselEmptyCardViewHolder(view: View): AbstractViewHolder<CarouselEmptyCardDataModel>(view){

    override fun bind(element: CarouselEmptyCardDataModel?) {
        itemView.setOnClickListener(null)
        element?.applink?.let{ url ->
            if(url.isNotEmpty()) itemView.setOnClickListener {
                element.listener.onEmptyCardClicked(element.channel, url, element.parentPosition)
            }
        }
    }


    companion object{
        val LAYOUT = R.layout.home_banner_item_empty_carousel
    }
}