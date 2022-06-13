package com.tokopedia.review.feature.inbox.pending.presentation.adapter.viewholder

import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.media.loader.loadImage
import com.tokopedia.review.feature.inbox.pending.presentation.adapter.uimodel.ReviewPendingCredibilityUiModel
import com.tokopedia.review.feature.inbox.pending.presentation.util.ReviewPendingItemListener
import com.tokopedia.review.inbox.R
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class ReviewPendingCredibilityViewHolder(
    private val itemView: View,
    private val reviewPendingItemListener: ReviewPendingItemListener
) {

    companion object {
        val LAYOUT = R.layout.item_review_pending_credibility

        private const val MINIMUM_WIDTH_PERCENT_TO_HIT_IMPRESS_TRACKER = 40
    }

    private var parentLayout: View? = null
    private var credibilityImage: ImageUnify? = null
    private var credibilityTitle: Typography? = null
    private var credibilitySubtitle: Typography? = null

    init {
        bindViews()
    }

    fun bind(element: ReviewPendingCredibilityUiModel, index: Int) {
        with(element) {
            credibilityImage?.loadImage(imageUrl)
            credibilityTitle?.text = title
            credibilitySubtitle?.text = subtitle
            itemView.setOnClickListener {
                reviewPendingItemListener.onReviewCredibilityWidgetClicked(
                    element.appLink,
                    element.title,
                    index
                )
            }
            itemView.viewTreeObserver.addOnGlobalLayoutListener {
                if (!element.impressHolder.isInvoke && itemView.shouldHitImpressTracker()) {
                    reviewPendingItemListener.onReviewCredibilityWidgetImpressed(
                        element.title,
                        index
                    )
                    element.impressHolder.invoke()
                }
            }
        }
    }

    private fun bindViews() {
        parentLayout = itemView.findViewById(R.id.review_credibility_card)
        credibilityImage = itemView.findViewById(R.id.review_pending_credibility_image)
        credibilityTitle = itemView.findViewById(R.id.review_pending_credibility_title)
        credibilitySubtitle = itemView.findViewById(R.id.review_pending_credibility_subtitle)
    }

    private fun View.shouldHitImpressTracker(): Boolean {
        return parent?.let {
            if (it !is ViewGroup) return false
            val rect = Rect()
            it.getHitRect(rect)
            getLocalVisibleRect(rect)
                    && height == rect.height()
                    && rect.width() * 100 / width >= MINIMUM_WIDTH_PERCENT_TO_HIT_IMPRESS_TRACKER
        }.orFalse()
    }
}