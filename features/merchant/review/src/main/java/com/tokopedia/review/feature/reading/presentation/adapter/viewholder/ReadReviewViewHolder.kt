package com.tokopedia.review.feature.reading.presentation.adapter.viewholder

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.review.R
import com.tokopedia.review.common.presentation.listener.ReviewBasicInfoListener
import com.tokopedia.review.common.presentation.widget.ReviewBadRatingReasonWidget
import com.tokopedia.review.common.presentation.widget.ReviewBasicInfoWidget
import com.tokopedia.review.common.util.ReviewUtil
import com.tokopedia.review.feature.reading.analytics.ReadReviewTracking
import com.tokopedia.review.feature.reading.data.LikeDislike
import com.tokopedia.review.feature.reading.data.ProductReviewResponse
import com.tokopedia.review.feature.reading.presentation.adapter.uimodel.ReadReviewUiModel
import com.tokopedia.review.feature.reading.presentation.listener.ReadReviewAttachedImagesListener
import com.tokopedia.review.feature.reading.presentation.listener.ReadReviewItemListener
import com.tokopedia.review.feature.reading.presentation.widget.ReadReviewProductInfo
import com.tokopedia.review.feature.reading.presentation.widget.ReadReviewSellerResponse
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.domain.model.UserReviewStats
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uimodel.ReviewMediaThumbnailUiModel
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uimodel.ReviewMediaThumbnailVisitable
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.widget.ReviewMediaThumbnail
import com.tokopedia.unifyprinciples.Typography

class ReadReviewViewHolder(
    view: View,
    private val readReviewItemListener: ReadReviewItemListener,
    private val attachedImagesClickListener: ReadReviewAttachedImagesListener,
    private val reviewBasicInfoListener: ReviewBasicInfoListener,
    private val mediaRecyclerViewPool: RecyclerView.RecycledViewPool
) : AbstractViewHolder<ReadReviewUiModel>(view) {

    companion object {
        val LAYOUT = com.tokopedia.review.R.layout.item_read_review
        private const val MAX_CHAR = 140
        private const val ALLOW_CLICK = true
        private const val MAX_LINES_REVIEW = 3
        private const val EMPTY_REVIEW_LIKE = 0
    }

    private var productInfo: ReadReviewProductInfo? = null
    private var basicInfo: ReviewBasicInfoWidget? = null
    private var reportOption: IconUnify? = null
    private var likeImage: IconUnify? = null
    private var likeCount: Typography? = null
    private var variantLabel: Typography? = null
    private var reviewMessage: Typography? = null
    private var attachedMedia: ReviewMediaThumbnail? = null
    private var showResponseText: Typography? = null
    private var showResponseChevron: IconUnify? = null
    private var sellerResponse: ReadReviewSellerResponse? = null
    private var isProductReview = false
    private var shopId = ""
    private var badRatingReason: ReviewBadRatingReasonWidget? = null
    private var element: ReadReviewUiModel? = null

    init {
        bindViews()
        setupLayout()
        attachedMedia?.setListener(ReviewMediaThumbnailListener())
    }

    override fun bind(element: ReadReviewUiModel) {
        this.element = element
        isProductReview = !element.isShopViewHolder
        shopId = element.shopId
        with(element.reviewData) {
            if (!isProductReview) {
                setProductInfo(
                    element.productId,
                    element.productImage,
                    element.productName,
                    isReportable,
                    feedbackID,
                    element.shopId,
                    element.shopName
                )
            }
            itemView.addOnImpressionListener(element.impressHolder) {
                readReviewItemListener.onItemImpressed(
                    reviewId = feedbackID,
                    position = adapterPosition,
                    characterCount = message.length,
                    imageCount = imageAttachments.size
                )
            }
            setBasicInfoDataAndListener(isAnonymous, user.userID, feedbackID)
            setRating(productRating)
            setCreateTime(reviewCreateTimestamp)
            setReviewerName(user.fullName)
            setReviewerStats(userReviewStats)
            setProfilePicture(user.image)
            setVariantName(variantName)
            showReportOptionWithCondition(
                isReportable = isReportable && !element.isShopViewHolder,
                reviewId = feedbackID,
                shopId = element.shopId
            )
            setReview(message, feedbackID, element.productId)
            showAttachedImages(element.mediaThumbnails)
            if (isProductReview)
                setLikeButton(feedbackID, likeDislike)
            else
                setShopReviewLikeButton(feedbackID, element.shopId, element.productId, likeDislike)
            setReply(element.shopName, reviewResponse, feedbackID, element.productId)
            showBadRatingReason(badRatingReasonFmt)
        }
    }

    private fun bindViews() {
        with(itemView) {
            productInfo = findViewById(R.id.read_review_product_info)
            basicInfo = findViewById(R.id.read_review_basic_info)
            reportOption = findViewById(R.id.read_review_item_three_dots)
            variantLabel = findViewById(R.id.read_review_variant_name)
            reviewMessage = findViewById(R.id.read_review_item_review)
            attachedMedia = findViewById(R.id.read_review_attached_media)
            likeImage = findViewById(R.id.read_review_like_button)
            likeCount = findViewById(R.id.read_review_like_count)
            showResponseText = findViewById(R.id.read_review_show_response)
            showResponseChevron = findViewById(R.id.read_review_show_response_chevron)
            sellerResponse = findViewById(R.id.read_review_seller_response)
            badRatingReason = findViewById(R.id.read_review_bad_rating_reason)
        }
    }

    private fun setupLayout() {
        attachedMedia?.setRecyclerViewPool(mediaRecyclerViewPool)
    }

    private fun setProductInfo(
        productId: String,
        productImageUrl: String,
        productName: String,
        isReportable: Boolean,
        reviewId: String,
        shopId: String,
        shopName: String
    ) {
        productInfo?.apply {
            setProductInfo(productImageUrl, productName)
            setListener(
                isReportable,
                reviewId,
                shopName,
                productName,
                adapterPosition,
                shopId,
                productId,
                readReviewItemListener
            )
            show()
        }
    }

    private fun setRating(rating: Int) {
        basicInfo?.setRating(rating)
    }

    private fun setCreateTime(createTime: String) {
        basicInfo?.setCreateTime(createTime)
    }

    private fun showReportOptionWithCondition(
        isReportable: Boolean,
        reviewId: String,
        shopId: String
    ) {
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

    private fun setVariantName(variantName: String) {
        variantLabel?.shouldShowWithAction(variantName.isNotBlank()) {
            variantLabel?.text = getString(R.string.review_gallery_variant, variantName)
        }
    }

    private fun setReview(message: String, feedbackId: String, productId: String) {
        if (message.isEmpty()) {
            reviewMessage?.apply {
                text = getString(R.string.review_reading_empty_review)
                isEnabled = false
            }
            return
        }
        setExpandableReview(message, feedbackId, productId)
    }

    private fun setExpandableReview(message: String, feedbackId: String, productId: String) {
        reviewMessage?.apply {
            isEnabled = true
            val formattingResult = ReviewUtil.formatReviewExpand(itemView.context, message, MAX_CHAR, ALLOW_CLICK)
            maxLines = MAX_LINES_REVIEW
            text = formattingResult.first
            if (formattingResult.second) {
                setOnClickListener {
                    if(isProductReview) {
                        ReadReviewTracking.trackOnSeeFullReviewClicked(feedbackId, productId)
                    } else {
                        ReadReviewTracking.trackOnShopReviewSeeFullReviewClicked(feedbackId, shopId)
                    }
                    setCollapsableReview(message, feedbackId, productId)
                }
            } else {
                setOnClickListener {  }
            }
            show()
        }
    }

    private fun setCollapsableReview(message: String, feedbackId: String, productId: String) {
        reviewMessage?.apply {
            text = ReviewUtil.formatReviewCollapse(context, message)
            maxLines = Integer.MAX_VALUE
            setOnClickListener {
                setExpandableReview(message, feedbackId, productId)
            }
        }
    }

    private fun setLikeButton(reviewId: String, likeDislike: LikeDislike) {
        if (likeDislike.isLiked()) {
            setThumbLike(true)
            likeCount?.setTextColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_G500))
        } else {
            setThumbLike(false)
            likeCount?.setTextColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_96))
        }
        likeImage?.setOnClickListener {
            readReviewItemListener.onLikeButtonClicked(
                reviewId,
                likeDislike.likeStatus,
                adapterPosition
            )
        }
        likeCount?.apply {
            text = if (likeDislike.totalLike == 0) {
                getString(R.string.review_reading_like)
            } else {
                String.format(getString(R.string.review_reading_like_count), likeDislike.totalLike)
            }
            setOnClickListener {
                readReviewItemListener.onLikeButtonClicked(
                    reviewId,
                    likeDislike.likeStatus,
                    adapterPosition
                )
            }
        }
    }

    private fun setShopReviewLikeButton(reviewId: String, shopId: String, productId: String, likeDislike: LikeDislike) {
        if (likeDislike.isLiked()) {
            setThumbLike(true)
            likeCount?.setTextColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_G500))
        } else {
            setThumbLike(false)
            likeCount?.setTextColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_96))
        }
        likeImage?.setOnClickListener {
            readReviewItemListener.onShopReviewLikeButtonClicked(reviewId, shopId, productId, likeDislike.likeStatus, adapterPosition)
        }
        likeCount?.apply {
            text = if (likeDislike.totalLike == EMPTY_REVIEW_LIKE) {
                getString(R.string.review_reading_like)
            } else {
                String.format(getString(R.string.review_reading_like_count), likeDislike.totalLike)
            }
            setOnClickListener {
                readReviewItemListener.onShopReviewLikeButtonClicked(reviewId, shopId, productId, likeDislike.likeStatus, adapterPosition)
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
                if(isProductReview)
                    ReadReviewTracking.trackOnSeeReplyClicked(feedbackId, productId)
                else
                    ReadReviewTracking.trackOnShopReviewSeeReplyClicked(feedbackId, shopId)
            } else {
                showResponseChevron?.setImage(IconUnify.CHEVRON_DOWN)
                showResponseText?.text = getString(R.string.review_reading_show_response)
                sellerResponse?.hide()
            }
        }
    }

    private fun showAttachedImages(
        mediaThumbnails: ReviewMediaThumbnailUiModel
    ) {
        if (mediaThumbnails.mediaThumbnails.isEmpty()) {
            attachedMedia?.hide()
            return
        }
        attachedMedia?.apply {
            setData(mediaThumbnails)
            show()
        }
    }

    private fun setReviewerStats(userStats: List<UserReviewStats>) {
        basicInfo?.setStats(userStats)
    }

    private fun setProfilePicture(imageUrl: String) {
        basicInfo?.setReviewerImage(imageUrl)
    }

    private fun setBasicInfoDataAndListener(isAnonymous: Boolean, userId: String, feedbackId: String) {
        basicInfo?.setCredibilityData(isProductReview, isAnonymous, userId, feedbackId)
        basicInfo?.setListener(reviewBasicInfoListener)
    }

    private fun showBadRatingReason(reason: String) {
        badRatingReason?.showBadRatingReason(reason)
    }

    private fun setThumbLike(isLiked: Boolean) {
        if (isLiked) {
            itemView.context?.let {
                val greenColor = ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_GN500)
                likeImage?.setImage(IconUnify.THUMB_FILLED, greenColor)
            }
        } else {
            itemView.context?.let {
                val greyColor = ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_N700_96)
                likeImage?.setImage(IconUnify.THUMB, greyColor)
            }
        }
    }

    private inner class ReviewMediaThumbnailListener: ReviewMediaThumbnail.Listener {
        override fun onMediaItemClicked(mediaItem: ReviewMediaThumbnailVisitable, position: Int) {
            element?.let {
                attachedImagesClickListener.onAttachedImagesClicked(
                    productReview = it.reviewData,
                    positionClicked = position,
                    shopId = shopId,
                    reviewItemPosition = adapterPosition
                )
            }
        }

        override fun onRemoveMediaItemClicked(
            mediaItem: ReviewMediaThumbnailVisitable,
            position: Int
        ) {
            // noop
        }
    }
}