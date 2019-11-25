package com.tokopedia.filter.newdynamicfilter.adapter

import android.view.View
import android.widget.ImageView

import com.tokopedia.filter.R
import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.newdynamicfilter.adapter.viewholder.DynamicFilterDetailViewHolder
import com.tokopedia.filter.newdynamicfilter.helper.RatingHelper
import com.tokopedia.filter.newdynamicfilter.view.DynamicFilterDetailView

class DynamicFilterDetailRatingAdapter(filterDetailView: DynamicFilterDetailView) : DynamicFilterDetailAdapter(filterDetailView) {

    override val layout: Int = R.layout.filter_detail_rating

    override fun getViewHolder(view: View): DynamicFilterDetailViewHolder {
        return RatingItemViewHolder(view, filterDetailView)
    }

    private class RatingItemViewHolder(itemView: View, filterDetailView: DynamicFilterDetailView) : DynamicFilterDetailViewHolder(itemView, filterDetailView) {

        private val ratingView: ImageView = itemView.findViewById(R.id.filter_rating_view) as ImageView

        override fun bind(option: Option) {
            super.bind(option)
            val ratingCount = getRatingCount(option)
            ratingView.setImageResource(RatingHelper.getRatingDrawable(ratingCount))
        }

        private fun getRatingCount(option: Option): Int {
            return try {
                Integer.parseInt(option.value)
            } catch (e: Exception) {
                0
            }

        }
    }
}
