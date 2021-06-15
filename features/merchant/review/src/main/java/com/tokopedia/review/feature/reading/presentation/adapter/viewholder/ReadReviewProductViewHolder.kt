package com.tokopedia.review.feature.reading.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.review.R
import com.tokopedia.review.common.util.getReviewStar
import com.tokopedia.review.feature.reading.presentation.adapter.ReadReviewItemListener
import com.tokopedia.review.feature.reading.presentation.adapter.uimodel.ReadReviewUiModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class ReadReviewProductViewHolder(view: View, private val readReviewItemListener: ReadReviewItemListener) : AbstractViewHolder<ReadReviewUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_read_review
    }

    private var ratingStars: ImageUnify? = null
    private var timeStamp: Typography? = null
    private var reportOption: ImageUnify? = null
    private var reviewerName: Typography? = null

    override fun bind(element: ReadReviewUiModel) {
        bindViews()
        with(element.reviewData) {
            setRating(productRating)
            setCreateTime(reviewCreateTimestamp)
            setReviewerName(user.fullName)
            showReportOptionWithCondition(isReportable, feedbackID, element.shopId)
        }
    }

    private fun bindViews() {
        ratingStars = itemView.findViewById(R.id.read_review_item_rating)
        timeStamp = itemView.findViewById(R.id.read_review_item_timestamp)
        reportOption = itemView.findViewById(R.id.read_review_item_three_dots)
        reviewerName = itemView.findViewById(R.id.read_review_item_reviewer_name)
    }

    private fun setRating(rating: Int) {
        ratingStars?.let {
            it.loadImage(getReviewStar(rating))
        }
    }

    private fun setCreateTime(createTime: String) {
        timeStamp?.let {
            it.text = createTime
        }
    }

    private fun showReportOptionWithCondition(isReportable: Boolean, reviewId: String, shopId: String) {
        reportOption?.let {
            if(isReportable) {
                it.show()
                it.setOnClickListener {
                    readReviewItemListener.onThreeDotsClicked(reviewId, shopId)
                }
            } else {
                it.hide()
            }
        }
    }

    private fun setReviewerName(name: String) {
        reviewerName?.let {
            it.text = name
        }
    }

    private fun setReview() {

    }

    private fun setImageReview() {

    }

    private fun setLikeButton() {

    }

    private fun setReply() {

    }
}