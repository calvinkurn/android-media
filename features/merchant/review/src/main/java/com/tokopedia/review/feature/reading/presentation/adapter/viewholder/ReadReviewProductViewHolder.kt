package com.tokopedia.review.feature.reading.presentation.adapter.viewholder

import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.review.R
import com.tokopedia.review.common.presentation.widget.ReviewBasicInfoWidget
import com.tokopedia.review.common.util.ReviewUtil
import com.tokopedia.review.feature.reading.data.LikeDislike
import com.tokopedia.review.feature.reading.data.ProductReviewResponse
import com.tokopedia.review.feature.reading.presentation.adapter.ReadReviewItemListener
import com.tokopedia.review.feature.reading.presentation.adapter.uimodel.ReadReviewUiModel
import com.tokopedia.review.feature.reading.presentation.widget.ReadReviewSellerResponse
import com.tokopedia.review.feature.reading.utils.ReadReviewUtils
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class ReadReviewProductViewHolder(view: View, private val readReviewItemListener: ReadReviewItemListener) : AbstractViewHolder<ReadReviewUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_read_review
        private const val MAX_CHAR = 140
        private const val ALLOW_CLICK = true
        private const val MAX_LINES_REVIEW = 3
    }

    private var basicInfo: ReviewBasicInfoWidget? = null
    private var reportOption: IconUnify? = null
    private var likeImage: ImageUnify? = null
    private var likeCount: Typography? = null
    private var reviewMessage: Typography? = null
    private var showResponseText: Typography? = null
    private var showResponseChevron: IconUnify? = null
    private var sellerResponse: ReadReviewSellerResponse? = null

    override fun bind(element: ReadReviewUiModel) {
        bindViews()
        with(element.reviewData) {
            setRating(productRating)
            setCreateTime(reviewCreateTimestamp)
            setReviewerName(user.fullName)
            showReportOptionWithCondition(isReportable, feedbackID, element.shopId)
            setReview(message)
            setLikeButton(feedbackID, element.shopId, likeDislike)
            setReply(element.shopName, reviewResponse)
        }
    }

    private fun bindViews() {
        basicInfo = itemView.findViewById(R.id.read_review_basic_info)
        reportOption = itemView.findViewById(R.id.read_review_item_three_dots)
        reviewMessage = itemView.findViewById(R.id.read_review_item_review)
        likeImage = itemView.findViewById(R.id.read_review_like_button)
        likeCount = itemView.findViewById(R.id.read_review_like_count)
        showResponseText = itemView.findViewById(R.id.read_review_show_response)
        showResponseChevron = itemView.findViewById(R.id.read_review_show_response_chevron)
        sellerResponse = itemView.findViewById(R.id.read_review_seller_response)
    }

    private fun setRating(rating: Int) {
        basicInfo?.setRating(rating)
    }

    private fun setCreateTime(createTime: String) {
        basicInfo?.setCreateTime(createTime)
    }

    private fun showReportOptionWithCondition(isReportable: Boolean, reviewId: String, shopId: String) {
        reportOption?.apply {
            if (isReportable) {
                show()
                setOnClickListener {
                    readReviewItemListener.onThreeDotsClicked(reviewId, shopId)
                }
            } else {
                hide()
            }
        }
    }

    private fun setReviewerName(name: String) {
        basicInfo?.setReviewerName(name)
    }

    private fun setReview(message: String) {
        if (message.isEmpty()) {
            reviewMessage?.apply {
                text = getString(R.string.review_reading_empty_review)
                isEnabled = false
            }
            return
        }
        reviewMessage?.apply {
            isEnabled = true
            val formattingResult = ReviewUtil.reviewDescFormatter(itemView.context, message, MAX_CHAR, ALLOW_CLICK)
            maxLines = MAX_LINES_REVIEW
            text = formattingResult.first
            if (formattingResult.second) {
                setOnClickListener {
                    maxLines = Integer.MAX_VALUE
                    text = message
                }
            }
            show()
        }
    }

    private fun setImageReview() {

    }

    private fun setLikeButton(reviewId: String, shopId: String, likeDislike: LikeDislike) {
        if (likeDislike.likeStatus == ReadReviewUtils.LIKED) {
            likeImage?.setImageDrawable(ContextCompat.getDrawable(itemView.context, R.drawable.ic_read_review_liked))
        } else {
            likeImage?.setImageDrawable(ContextCompat.getDrawable(itemView.context, R.drawable.ic_read_review_like))
        }
        likeImage?.setOnClickListener {
            readReviewItemListener.onLikeButtonClicked(reviewId, shopId, likeDislike.likeStatus)
        }
        likeCount?.apply {
            if (likeDislike.totalLike == 0) {
                text = getString(R.string.review_reading_like)
                setTextColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_96))

            } else {
                text = likeDislike.totalLike.toString()
                setTextColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_G500))
            }
        }
    }

    private fun setReply(shopName: String, response: ProductReviewResponse) {
        if (response.message.isNotBlank()) {
            showResponseChevron?.apply {
                setSellerResponseClickListener(shopName, response.createTime, response.message)
                show()
            }
            showResponseText?.apply {
                setSellerResponseClickListener(shopName, response.createTime, response.message)
                show()
            }
        } else {
            showResponseChevron?.hide()
            showResponseText?.hide()
        }
    }

    private fun View.setSellerResponseClickListener(shopName: String, timeStamp: String, response: String) {
        setOnClickListener {
            if (showResponseText?.text == getString(R.string.review_reading_show_response)) {
                showResponseChevron?.setImage(IconUnify.CHEVRON_UP)
                showResponseText?.text = getString(R.string.review_reading_hide_response)
                sellerResponse?.apply {
                    setResponseData(shopName, timeStamp, response)
                    show()
                }
            } else {
                showResponseChevron?.setImage(IconUnify.CHEVRON_DOWN)
                showResponseText?.text = getString(R.string.review_reading_show_response)
                sellerResponse?.hide()
            }
        }
    }
}