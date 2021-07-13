package com.tokopedia.review.feature.reading.presentation.adapter.viewholder

import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.review.R
import com.tokopedia.review.common.presentation.widget.ReviewBasicInfoWidget
import com.tokopedia.review.common.util.ReviewUtil
import com.tokopedia.review.feature.reading.analytics.ReadReviewTracking
import com.tokopedia.review.feature.reading.data.LikeDislike
import com.tokopedia.review.feature.reading.data.ProductReview
import com.tokopedia.review.feature.reading.data.ProductReviewAttachments
import com.tokopedia.review.feature.reading.data.ProductReviewResponse
import com.tokopedia.review.feature.reading.presentation.adapter.uimodel.ReadReviewUiModel
import com.tokopedia.review.feature.reading.presentation.listener.ReadReviewAttachedImagesListener
import com.tokopedia.review.feature.reading.presentation.listener.ReadReviewItemListener
import com.tokopedia.review.feature.reading.presentation.widget.ReadReviewAttachedImages
import com.tokopedia.review.feature.reading.presentation.widget.ReadReviewProductInfo
import com.tokopedia.review.feature.reading.presentation.widget.ReadReviewSellerResponse
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class ReadReviewViewHolder(view: View, private val readReviewItemListener: ReadReviewItemListener, private val attachedImagesClickListener: ReadReviewAttachedImagesListener) : AbstractViewHolder<ReadReviewUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_read_review
        private const val MAX_CHAR = 140
        private const val ALLOW_CLICK = true
        private const val MAX_LINES_REVIEW = 3
    }

    private var productInfo: ReadReviewProductInfo? = null
    private var basicInfo: ReviewBasicInfoWidget? = null
    private var reportOption: IconUnify? = null
    private var likeImage: ImageUnify? = null
    private var likeCount: Typography? = null
    private var reviewMessage: Typography? = null
    private var attachedImages: ReadReviewAttachedImages? = null
    private var showResponseText: Typography? = null
    private var showResponseChevron: IconUnify? = null
    private var sellerResponse: ReadReviewSellerResponse? = null

    override fun bind(element: ReadReviewUiModel) {
        bindViews()
        with(element.reviewData) {
            if (element.isShopViewHolder) {
                setProductInfo(element.productImage, element.productName, isReportable, feedbackID, element.shopId)
            }
            itemView.addOnImpressionListener(element.impressHolder) {
                readReviewItemListener.onItemImpressed(feedbackID, adapterPosition, message.length, imageAttachments.size)
            }
            setRating(productRating)
            setCreateTime(reviewCreateTimestamp)
            setReviewerName(user.fullName)
            showReportOptionWithCondition(isReportable && !element.isShopViewHolder, feedbackID, element.shopId)
            setReview(message, feedbackID, element.productId)
            showAttachedImages(imageAttachments, this, element.shopId)
            setLikeButton(feedbackID, element.shopId, likeDislike)
            setReply(element.shopName, reviewResponse, feedbackID, element.productId)
        }
    }

    private fun bindViews() {
        productInfo = itemView.findViewById(R.id.read_review_product_info)
        basicInfo = itemView.findViewById(R.id.read_review_basic_info)
        reportOption = itemView.findViewById(R.id.read_review_item_three_dots)
        reviewMessage = itemView.findViewById(R.id.read_review_item_review)
        attachedImages = itemView.findViewById(R.id.read_review_attached_images)
        likeImage = itemView.findViewById(R.id.read_review_like_button)
        likeCount = itemView.findViewById(R.id.read_review_like_count)
        showResponseText = itemView.findViewById(R.id.read_review_show_response)
        showResponseChevron = itemView.findViewById(R.id.read_review_show_response_chevron)
        sellerResponse = itemView.findViewById(R.id.read_review_seller_response)
    }

    private fun setProductInfo(productImageUrl: String, productName: String, isReportable: Boolean, reviewId: String, shopId: String) {
        productInfo?.apply {
            setProductInfo(productImageUrl, productName)
            setListener(isReportable, reviewId, shopId, readReviewItemListener)
            show()
        }
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

    private fun setReview(message: String, feedbackId: String, productId: String) {
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
                    ReadReviewTracking.trackOnSeeFullReviewClicked(feedbackId, productId)
                    maxLines = Integer.MAX_VALUE
                    text = message
                }
            }
            show()
        }
    }

    private fun setLikeButton(reviewId: String, shopId: String, likeDislike: LikeDislike) {
        if (likeDislike.isLiked()) {
            likeImage?.setImageDrawable(ContextCompat.getDrawable(itemView.context, R.drawable.ic_read_review_liked))
            likeCount?.setTextColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_G500))
        } else {
            likeImage?.setImageDrawable(ContextCompat.getDrawable(itemView.context, R.drawable.ic_read_review_like))
            likeCount?.setTextColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_96))
        }
        likeImage?.setOnClickListener {
            readReviewItemListener.onLikeButtonClicked(reviewId, shopId, likeDislike.likeStatus, adapterPosition)
        }
        likeCount?.apply {
            text = if (likeDislike.totalLike == 0) {
                getString(R.string.review_reading_like)
            } else {
                likeDislike.totalLike.toString()
            }
        }
    }

    private fun setReply(shopName: String, response: ProductReviewResponse, feedbackId: String, productId: String) {
        if (response.message.isNotBlank()) {
            showResponseChevron?.apply {
                setSellerResponseClickListener(shopName, response.createTime, response.message, feedbackId, productId)
                show()
            }
            showResponseText?.apply {
                setSellerResponseClickListener(shopName, response.createTime, response.message, feedbackId, productId)
                show()
            }
        } else {
            showResponseChevron?.hide()
            showResponseText?.hide()
        }
    }

    private fun View.setSellerResponseClickListener(shopName: String, timeStamp: String, response: String, feedbackId: String, productId: String) {
        setOnClickListener {
            if (showResponseText?.text == getString(R.string.review_reading_show_response)) {
                showResponseChevron?.setImage(IconUnify.CHEVRON_UP)
                showResponseText?.text = getString(R.string.review_reading_hide_response)
                sellerResponse?.apply {
                    setResponseData(shopName, timeStamp, response)
                    show()
                }
                ReadReviewTracking.trackOnSeeReplyClicked(feedbackId, productId)
            } else {
                showResponseChevron?.setImage(IconUnify.CHEVRON_DOWN)
                showResponseText?.text = getString(R.string.review_reading_show_response)
                sellerResponse?.hide()
            }
        }
    }

    private fun showAttachedImages(imageAttachments: List<ProductReviewAttachments>, productReview: ProductReview, shopId: String) {
        if (imageAttachments.isEmpty()) {
            attachedImages?.hide()
            return
        }
        attachedImages?.apply {
            setImages(imageAttachments, attachedImagesClickListener, productReview, shopId)
            show()
        }
    }
}