package com.tokopedia.hotel.hoteldetail.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.hotel.databinding.ItemHotelReviewBinding
import com.tokopedia.hotel.databinding.ItemHotelReviewShimmeringBinding
import com.tokopedia.hotel.hoteldetail.presentation.adapter.viewholder.HotelReviewShimmeringViewHolder
import com.tokopedia.hotel.hoteldetail.presentation.adapter.viewholder.HotelReviewViewHolder
import com.tokopedia.hotel.hoteldetail.presentation.model.viewmodel.HotelReview

/**
 * @author by jessica on 29/04/19
 */

class ReviewAdapterTypeFactory : BaseAdapterTypeFactory() {

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        when (type) {
            HotelReviewViewHolder.LAYOUT -> {
                val binding = ItemHotelReviewBinding.bind(parent)
                return HotelReviewViewHolder(binding)
            }
            HotelReviewShimmeringViewHolder.LAYOUT -> {
                val binding = ItemHotelReviewShimmeringBinding.bind(parent)
                return HotelReviewShimmeringViewHolder(binding)
            }
            else -> return super.createViewHolder(parent, type)
        }
    }

    fun type(data: HotelReview): Int = HotelReviewViewHolder.LAYOUT

    override fun type(viewModel: LoadingModel) = HotelReviewShimmeringViewHolder.LAYOUT
}