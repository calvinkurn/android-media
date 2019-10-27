package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import android.support.annotation.LayoutRes
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.R
import com.tokopedia.home.beranda.listener.HomeReviewListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.ReviewViewModel
import com.tokopedia.reputation.common.view.AnimatedReviewPicker
import kotlinx.android.synthetic.main.home_item_review.view.*

class ReviewViewHolder(itemView: View, private val listener: HomeReviewListener) : AbstractViewHolder<ReviewViewModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_item_review
    }

    override fun bind(element: ReviewViewModel?) {
        itemView.animated_review.setListener(object : AnimatedReviewPicker.AnimatedReviewPickerListener {
            override fun onStarsClick(position: Int) {
                listener.onReviewClick(adapterPosition, position)
            }
        })
    }

}