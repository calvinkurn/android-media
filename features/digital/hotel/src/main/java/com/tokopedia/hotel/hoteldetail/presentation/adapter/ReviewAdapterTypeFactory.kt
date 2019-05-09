package com.tokopedia.hotel.hoteldetail.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.EmptyViewHolder
import com.tokopedia.hotel.R
import com.tokopedia.hotel.hoteldetail.presentation.adapter.viewholder.HotelReviewShimmeringViewHolder
import com.tokopedia.hotel.hoteldetail.presentation.adapter.viewholder.HotelReviewViewHolder
import com.tokopedia.hotel.hoteldetail.presentation.model.viewmodel.HotelReview
import com.tokopedia.hotel.search.data.model.Property

/**
 * @author by jessica on 29/04/19
 */

class ReviewAdapterTypeFactory: BaseAdapterTypeFactory() {

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        when (type) {
            HotelReviewViewHolder.LAYOUT -> return HotelReviewViewHolder(parent)
            HotelReviewShimmeringViewHolder.LAYOUT -> return HotelReviewShimmeringViewHolder(parent)
            emptyLayout -> return EmptyViewHolder(parent)
            else -> return super.createViewHolder(parent, type)
        }
    }

    fun type(data: HotelReview): Int = HotelReviewViewHolder.LAYOUT

    override fun type(viewModel: LoadingModel) = HotelReviewShimmeringViewHolder.LAYOUT


    override fun type(viewModel: EmptyModel) = emptyLayout

    companion object {
        val emptyLayout: Int = R.layout.item_hotel_room_empty_list
    }
}