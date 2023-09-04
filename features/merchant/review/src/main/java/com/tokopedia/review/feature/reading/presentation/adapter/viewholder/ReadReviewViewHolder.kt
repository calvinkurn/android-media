package com.tokopedia.review.feature.reading.presentation.adapter.viewholder

import android.view.View
import android.view.ViewTreeObserver
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.review.R
import com.tokopedia.review.common.presentation.widget.ReviewBadRatingReasonWidget
import com.tokopedia.review.feature.reading.analytics.ReadReviewTracking
import com.tokopedia.review.feature.reading.data.LikeDislike
import com.tokopedia.review.feature.reading.data.ProductReviewResponse
import com.tokopedia.review.feature.reading.presentation.adapter.uimodel.ReadReviewUiModel
import com.tokopedia.review.feature.reading.presentation.listener.ReadReviewItemListener
import com.tokopedia.review.feature.reading.presentation.widget.ReadReviewProductInfo
import com.tokopedia.review.feature.reading.presentation.widget.ReadReviewSellerResponse
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.domain.model.UserReviewStats
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.adapter.typefactory.ReviewMediaThumbnailTypeFactory
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uimodel.ReviewMediaThumbnailUiModel
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.widget.ReviewMediaThumbnail
import com.tokopedia.reviewcommon.feature.reviewer.presentation.listener.ReviewBasicInfoListener
import com.tokopedia.reviewcommon.feature.reviewer.presentation.listener.ReviewBasicInfoThreeDotsListener
import com.tokopedia.reviewcommon.feature.reviewer.presentation.widget.ProductReviewBasicInfoWidget
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifyprinciples.Typography

class ReadReviewViewHolder(
    view: View,
    reviewMediaThumbnailRecycledViewPool: RecyclerView.RecycledViewPool,
    reviewMediaThumbnailListener: ReviewMediaThumbnailTypeFactory.Listener,
    private val readReviewItemListener: ReadReviewItemListener,
    private val reviewBasicInfoListener: ReviewBasicInfoListener,
) : AbstractViewHolder<ReadReviewUiModel>(view), ReviewBasicInfoThreeDotsListener {

    companion object {
        val LAYOUT = com.tokopedia.review.R.layout.item_read_review
        private const val MAX_LINES_REVIEW_COLLAPSED = 3
        private const val MAX_LINES_REVIEW_EXPANDED = Int.MAX_VALUE
        private const val EMPTY_REVIEW_LIKE = 0
    }

    private var productInfo: ReadReviewProductInfo? = null
    private val basicInfo: ProductReviewBasicInfoWidget?
        get() = if (isProductReview) {
            itemView.findViewById(R.id.read_product_review_basic_info)
        } else {
            itemView.findViewById(R.id.read_shop_review_basic_info)
        }
    private var likeImage: IconUnify? = null
    private var likeCount: Typography? = null
    private var reviewMessage: Typography? = null
    private var tvReviewMessageSeeMore: Typography? = null
    private var attachedMedia: ReviewMediaThumbnail? = null
    private var showResponseText: Typography? = null
    private var showResponseChevron: IconUnify? = null
    private var sellerResponse: ReadReviewSellerResponse? = null
    private var isProductReview = false
    private var reviewId = ""
    private var shopId = ""
    private var badRatingReason: ReviewBadRatingReasonWidget? = null

    private val reviewMessageEllipsizeChecker = ReviewMessageEllipsizeChecker()

    init {
        bindViews()
        attachedMedia?.setRecycledViewPool(reviewMediaThumbnailRecycledViewPool)
        attachedMedia?.setListener(reviewMediaThumbnailListener)
    }

    override fun bind(element: ReadReviewUiModel) {
        isProductReview = !element.isShopViewHolder
        reviewId = element.reviewData.feedbackID
        shopId = element.shopId
        with(element.reviewData) {
            if (!isProductReview) {
                val productVariantName = if (variantName.isNotBlank()) {
                    getString(R.string.review_gallery_variant, variantName)
                } else {
                    variantName
                }
                setProductInfo(
                    element.productId,
                    element.productImage,
                    element.productName,
                    productVariantName,
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
            setReviewerLabel(user.label)
            setReviewerStats(userReviewStats)
            setProfilePicture(user.image)
            setVariantName(variantName)
            showReportOptionWithCondition(
                isReportable = isReportable && !element.isShopViewHolder
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
        itemView.findViewById<View>(R.id.read_product_review_basic_info)?.showWithCondition(isProductReview)
        itemView.findViewById<View>(R.id.read_shop_review_basic_info)?.showWithCondition(!isProductReview)
    }

    override fun onThreeDotsClicked() {
        readReviewItemListener.onThreeDotsClicked(reviewId, shopId)
    }

    private fun bindViews() {
        with(itemView) {
            productInfo = findViewById(R.id.read_review_product_info)
            reviewMessage = findViewById(R.id.read_review_item_review)
            tvReviewMessageSeeMore = findViewById(R.id.read_review_item_review_see_more)
            attachedMedia = findViewById(R.id.read_review_attached_media)
            likeImage = findViewById(R.id.read_review_like_button)
            likeCount = findViewById(R.id.read_review_like_count)
            showResponseText = findViewById(R.id.read_review_show_response)
            showResponseChevron = findViewById(R.id.read_review_show_response_chevron)
            sellerResponse = findViewById(R.id.read_review_seller_response)
            badRatingReason = findViewById(R.id.read_review_bad_rating_reason)
        }
    }

    private fun setupReviewMessageSeeMore(message: String, feedbackId: String, productId: String, expanded: Boolean) {
        setupReviewMessageSeeMoreText(expanded)
        setupReviewMessageSeeMoreListener(message, feedbackId, productId, expanded)
        reviewMessage?.viewTreeObserver?.addOnPreDrawListener(reviewMessageEllipsizeChecker)
    }

    private fun setupReviewMessageSeeMoreText(expanded: Boolean) {
        tvReviewMessageSeeMore?.text = HtmlLinkHelper(
            itemView.context,
            if (expanded) {
                getString(R.string.review_reading_collapse)
            } else {
                getString(R.string.review_expand)
            }
        ).spannedString
    }

    private fun setupReviewMessageSeeMoreListener(
        message: String,
        feedbackId: String,
        productId: String,
        expanded: Boolean
    ) {
        tvReviewMessageSeeMore?.apply {
            setOnClickListener {
                if (expanded) {
                    onCollapseReviewTextClicked(message, feedbackId, productId)
                } else {
                    onExpandReviewTextClicked(message, feedbackId, productId)
                }
            }
        }
    }

    private fun setProductInfo(
        productId: String,
        productImageUrl: String,
        productName: String,
        productVariantName: String,
        isReportable: Boolean,
        reviewId: String,
        shopId: String,
        shopName: String
    ) {
        productInfo?.apply {
            setProductInfo(productImageUrl, productName, productVariantName)
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

    private fun showReportOptionWithCondition(isReportable: Boolean) {
        if (isReportable) {
            basicInfo?.showThreeDots()
        } else {
            basicInfo?.hideThreeDots()
        }
    }

    private fun setReviewerName(name: String) {
        basicInfo?.setReviewerName(name)
    }

    private fun setReviewerLabel(label: String) {
        basicInfo?.setReviewerLabel(label)
    }

    private fun setVariantName(variantName: String) {
        basicInfo?.setVariantName(variantName)
    }

    private fun setReview(message: String, feedbackId: String, productId: String) {
        setupReviewMessageSeeMore(message, feedbackId, productId, false)
        if (message.isEmpty()) {
            setReviewText(
                message = getString(R.string.review_reading_empty_review),
                maxLines = MAX_LINES_REVIEW_COLLAPSED,
                enable = false
            )
            return
        }
        setReviewText(message = message, maxLines = MAX_LINES_REVIEW_COLLAPSED, enable = true)
    }

    private fun setReviewText(message: String, maxLines: Int, enable: Boolean) {
        reviewMessage?.apply {
            isEnabled = enable
            this.maxLines = maxLines
            text = HtmlLinkHelper(context, message).spannedString ?: ""
            show()
        }
    }

    private fun setLikeButton(reviewId: String, likeDislike: LikeDislike) {
        if (likeDislike.isLiked()) {
            setThumbLike(true)
            likeCount?.setTextColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_GN500))
        } else {
            setThumbLike(false)
            likeCount?.setTextColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_NN950_96))
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
            likeCount?.setTextColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_GN500))
        } else {
            setThumbLike(false)
            likeCount?.setTextColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_NN950_96))
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
                setImage(IconUnify.CHEVRON_DOWN)
                show()
            }
            showResponseText?.apply {
                setSellerResponseClickListener(shopName, response.createTime, response.message, feedbackId, productId)
                text = getString(R.string.review_reading_show_response)
                show()
            }
            sellerResponse?.hide()
        } else {
            showResponseChevron?.hide()
            showResponseText?.hide()
            sellerResponse?.hide()
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
        attachedMedia?.showWithCondition(mediaThumbnails.mediaThumbnails.isNotEmpty())
        attachedMedia?.setData(mediaThumbnails)
    }

    private fun setReviewerStats(userStats: List<UserReviewStats>) {
        basicInfo?.setStats(userStats)
    }

    private fun setProfilePicture(imageUrl: String) {
        basicInfo?.setReviewerImage(imageUrl)
    }

    private fun setBasicInfoDataAndListener(isAnonymous: Boolean, userId: String, feedbackId: String) {
        basicInfo?.setCredibilityData(isProductReview, isAnonymous, userId, feedbackId)
        basicInfo?.setListeners(reviewBasicInfoListener, this)
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
                val greyColor = ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_NN950_96)
                likeImage?.setImage(IconUnify.THUMB, greyColor)
            }
        }
    }

    private fun onCollapseReviewTextClicked(
        message: String,
        feedbackId: String,
        productId: String
    ) {
        setupReviewMessageSeeMore(message, feedbackId, productId, false)
        setReviewText(
            message = message,
            maxLines = MAX_LINES_REVIEW_COLLAPSED,
            enable = true
        )
    }

    private fun onExpandReviewTextClicked(
        message: String,
        feedbackId: String,
        productId: String
    ) {
        if (isProductReview) {
            ReadReviewTracking.trackOnSeeFullReviewClicked(feedbackId, productId)
        } else {
            ReadReviewTracking.trackOnShopReviewSeeFullReviewClicked(feedbackId, shopId)
        }
        setupReviewMessageSeeMore(message, feedbackId, productId, true)
        setReviewText(
            message = message,
            maxLines = MAX_LINES_REVIEW_EXPANDED,
            enable = true
        )
    }

    private inner class ReviewMessageEllipsizeChecker: ViewTreeObserver.OnPreDrawListener {
        override fun onPreDraw(): Boolean {
            reviewMessage?.layout?.run {
                val lines = lineCount
                val ellipsisCount = getEllipsisCount(lines.dec())
                val isEllipsized = ellipsisCount.isMoreThanZero()
                val isCollapsed = reviewMessage?.maxLines == MAX_LINES_REVIEW_COLLAPSED
                val isEllipsizedWhenCollapsed = isEllipsized && isCollapsed
                tvReviewMessageSeeMore?.showWithCondition(isEllipsizedWhenCollapsed || !isCollapsed)
            }
            reviewMessage?.viewTreeObserver?.removeOnPreDrawListener(this)
            return true
        }
    }
}
