package com.tokopedia.review.feature.inbox.pending.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.media.loader.loadImage
import com.tokopedia.review.feature.inbox.pending.presentation.adapter.uimodel.ReviewPendingCredibilityUiModel
import com.tokopedia.review.feature.inbox.pending.presentation.util.ReviewPendingItemListener
import com.tokopedia.review.inbox.R
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class ReviewPendingCredibilityViewHolder(view: View, private val reviewPendingItemListener: ReviewPendingItemListener) : AbstractViewHolder<ReviewPendingCredibilityUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_review_pending_credibility
    }

    private var parentLayout: View? = null
    private var credibilityImage: ImageUnify? = null
    private var credibilityTitle: Typography? = null
    private var credibilitySubtitle: Typography? = null
    private var coachMark: CoachMark2? = null

    init {
        bindViews()
    }

    override fun bind(element: ReviewPendingCredibilityUiModel) {
        with(element) {
            credibilityImage?.loadImage(imageUrl)
            credibilityTitle?.text = title
            credibilitySubtitle?.text = subtitle
            itemView.setOnClickListener {
                reviewPendingItemListener.onReviewCredibilityWidgetClicked()
            }
            if (reviewPendingItemListener.shouldShowCoachMark()) {
                coachMark = CoachMark2(itemView.context)
                coachMark?.showCoachMark(getCoachMarkItem())
                reviewPendingItemListener.updateCoachMark()
            }
        }
    }

    private fun getCoachMarkItem(): ArrayList<CoachMark2Item> {
        parentLayout?.let {
            return arrayListOf(
                CoachMark2Item(
                    it,
                    getString(R.string.review_pending_credibility_coach_mark_title),
                    getString(R.string.review_pending_credibility_coach_mark_subtitle)
                )
            )
        }
        return arrayListOf()
    }

    private fun bindViews() {
        parentLayout = itemView.findViewById(R.id.review_credibility_card)
        credibilityImage = itemView.findViewById(R.id.review_pending_credibility_image)
        credibilityTitle = itemView.findViewById(R.id.review_pending_credibility_title)
        credibilitySubtitle = itemView.findViewById(R.id.review_pending_credibility_subtitle)
    }


}