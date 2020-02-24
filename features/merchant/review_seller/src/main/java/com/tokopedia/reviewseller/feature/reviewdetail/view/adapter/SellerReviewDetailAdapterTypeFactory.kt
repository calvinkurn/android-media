package com.tokopedia.reviewseller.feature.reviewdetail.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.reviewseller.feature.reviewdetail.view.adapter.viewholder.HeaderViewHolder
import com.tokopedia.reviewseller.feature.reviewdetail.view.adapter.viewholder.RatingBarViewHolder
import com.tokopedia.reviewseller.feature.reviewdetail.view.model.HeaderModel
import com.tokopedia.reviewseller.feature.reviewdetail.view.model.RatingBarModel

class SellerReviewDetailAdapterTypeFactory: BaseAdapterTypeFactory() {

    fun type(headerModel: HeaderModel?) = HeaderViewHolder.LAYOUT

    fun type(ratingBarModel: RatingBarModel?) = RatingBarViewHolder.LAYOUT

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            HeaderViewHolder.LAYOUT -> HeaderViewHolder(parent)
            RatingBarViewHolder.LAYOUT -> RatingBarViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }

}