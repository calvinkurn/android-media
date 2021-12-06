package com.tokopedia.review.feature.inbox.pending.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.carousel.CarouselUnify
import com.tokopedia.review.feature.inbox.pending.presentation.adapter.uimodel.ReviewPendingCredibilityCarouselUiModel
import com.tokopedia.review.feature.inbox.pending.presentation.adapter.uimodel.ReviewPendingCredibilityUiModel
import com.tokopedia.review.feature.inbox.pending.presentation.util.ReviewPendingItemListener
import com.tokopedia.review.inbox.R
import com.tokopedia.review.inbox.databinding.ItemReviewPendingCredibilityCarouselBinding
import com.tokopedia.utils.view.binding.viewBinding

class ReviewPendingCredibilityCarouselViewHolder(
    itemView: View,
    private val reviewPendingItemListener: ReviewPendingItemListener
) : AbstractViewHolder<ReviewPendingCredibilityCarouselUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_review_pending_credibility_carousel
    }

    val binding by viewBinding<ItemReviewPendingCredibilityCarouselBinding>()

    override fun bind(element: ReviewPendingCredibilityCarouselUiModel) {
        setupCarousel(element)
    }

    private fun setupCarousel(element: ReviewPendingCredibilityCarouselUiModel) {
        binding?.root?.run {
            setupConfig(element.items.size)
            setupContents(element.items)
        }
    }

    private fun CarouselUnify.setupConfig(itemCount: Int) {
        freeMode = false
        centerMode = false
        slideToScroll = 1
        indicatorPosition = CarouselUnify.INDICATOR_HIDDEN
        infinite = false
        if (itemCount == 1) {
            autoplay = false
            slideToShow = 1f
        } else {
            autoplay = true
            slideToShow = 1.04f
        }
    }

    private fun CarouselUnify.setupContents(items: List<ReviewPendingCredibilityUiModel>) {
        addItems(ReviewPendingCredibilityViewHolder.LAYOUT, ArrayList(items), ::bindItem)
    }

    private fun bindItem(view: View, data: Any) {
        if (data is ReviewPendingCredibilityUiModel) {
            val viewHolder = ReviewPendingCredibilityViewHolder(view, reviewPendingItemListener)
            viewHolder.bind(data)
        }
    }
}