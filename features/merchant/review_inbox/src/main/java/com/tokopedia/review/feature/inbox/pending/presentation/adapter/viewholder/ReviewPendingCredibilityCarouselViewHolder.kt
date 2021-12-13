package com.tokopedia.review.feature.inbox.pending.presentation.adapter.viewholder

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.carousel.CarouselUnify
import com.tokopedia.kotlin.extensions.view.getScreenWidth
import com.tokopedia.review.feature.inbox.pending.presentation.adapter.uimodel.ReviewPendingCredibilityCarouselUiModel
import com.tokopedia.review.feature.inbox.pending.presentation.adapter.uimodel.ReviewPendingCredibilityUiModel
import com.tokopedia.review.feature.inbox.pending.presentation.util.ReviewPendingItemListener
import com.tokopedia.review.inbox.R
import com.tokopedia.review.inbox.databinding.ItemReviewPendingCredibilityCarouselBinding
import com.tokopedia.utils.view.binding.viewBinding
import kotlin.math.ceil

class ReviewPendingCredibilityCarouselViewHolder(
    itemView: View,
    private val reviewPendingItemListener: ReviewPendingItemListener
) : AbstractViewHolder<ReviewPendingCredibilityCarouselUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_review_pending_credibility_carousel

        private const val SLIDE_TO_SCROLL = 1
        private const val SLIDE_TO_SHOW_SINGLE = 1f
        private const val SLIDE_TO_SHOW_MULTIPLE = 1.04f
        private const val ITEM_VIEW_WIDTH_SINGLE = 0.9375
        private const val ITEM_VIEW_WIDTH_MULTIPLE = 0.9
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
        infinite = false
        centerMode = false
        autoplay = true
        slideToScroll = SLIDE_TO_SCROLL
        indicatorPosition = CarouselUnify.INDICATOR_HIDDEN
        slideToShow = if (itemCount == 1) {
            SLIDE_TO_SHOW_SINGLE
        } else {
            SLIDE_TO_SHOW_MULTIPLE
        }
    }

    private fun CarouselUnify.setupContents(items: List<ReviewPendingCredibilityUiModel>) {
        removeItemViews()
        setupItemViews(context, items)
    }

    private fun CarouselUnify.removeItemViews() {
        stage.removeAllViews()
    }

    private fun CarouselUnify.setupItemViews(
        context: Context,
        items: List<ReviewPendingCredibilityUiModel>
    ) {
        val itemWidth = calculateItemViewWidth(items.size == 1)
        items.forEach {
            createItemView(context, itemWidth).apply {
                bindItem(this, it)
            }.run {
                addItem(this)
            }
        }
    }

    private fun createItemView(context: Context, itemWidth: Int): View {
        return View.inflate(context, ReviewPendingCredibilityViewHolder.LAYOUT, null).apply {
            layoutParams = LinearLayout.LayoutParams(
                itemWidth,
                LinearLayout.LayoutParams.WRAP_CONTENT,
            )
        }
    }

    //TODO: Remove this manual calculation after unify fix CarouselUnify implementation on RV
    private fun calculateItemViewWidth(isSingle: Boolean): Int {
        return if (isSingle) {
            ceil(getScreenWidth() * ITEM_VIEW_WIDTH_SINGLE).toInt()
        } else {
            ceil(getScreenWidth() * ITEM_VIEW_WIDTH_MULTIPLE).toInt()
        }
    }

    private fun bindItem(view: View, data: Any) {
        if (data is ReviewPendingCredibilityUiModel) {
            val viewHolder = ReviewPendingCredibilityViewHolder(view, reviewPendingItemListener)
            viewHolder.bind(data)
        }
    }
}