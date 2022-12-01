package com.tokopedia.review.feature.inbox.pending.presentation.adapter.viewholder

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.carousel.CarouselUnify
import com.tokopedia.kotlin.extensions.view.ZERO
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
        private const val ITEM_VIEW_WIDTH_MULTIPLE = 0.905
        private const val AUTO_SLIDE_DURATION = 5000L
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
        slideToScroll = SLIDE_TO_SCROLL
        indicatorPosition = CarouselUnify.INDICATOR_HIDDEN
        autoplayDuration = AUTO_SLIDE_DURATION
        slideToShow = if (itemCount == 1) {
            SLIDE_TO_SHOW_SINGLE
        } else {
            SLIDE_TO_SHOW_MULTIPLE
        }
        freeMode = false
        infinite = false
        centerMode = false
        autoplay = true
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
        items.mapIndexed { index, item ->
            createItemView(context, itemWidth).apply {
                bindItem(this, item, index)
                measureItem(this, itemWidth)
            }
        }.run {
            val maxHeight = maxOf { it.measuredHeight }
            onEach {
                val layoutParamsCopy = it.layoutParams
                layoutParamsCopy.height = maxHeight
                it.layoutParams = layoutParamsCopy
                addItem(it)
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

    private fun bindItem(view: View, data: Any, index: Int) {
        if (data is ReviewPendingCredibilityUiModel) {
            val viewHolder = ReviewPendingCredibilityViewHolder(view, reviewPendingItemListener)
            viewHolder.bind(data, index)
        }
    }

    private fun measureItem(view: View, itemWidth: Int) {
        view.measure(
            View.MeasureSpec.makeMeasureSpec(itemWidth, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(Int.ZERO, View.MeasureSpec.UNSPECIFIED)
        )
    }
}