package com.tokopedia.review.feature.reviewreply.view.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.review.R
import com.tokopedia.review.common.util.getReviewStar
import com.tokopedia.review.common.util.toRelativeDate
import com.tokopedia.review.databinding.WidgetReplyFeedbackItemBinding
import com.tokopedia.review.feature.reviewdetail.view.model.FeedbackUiModel
import com.tokopedia.review.feature.reviewreply.view.adapter.ReviewReplyListener
import com.tokopedia.review.feature.reviewreply.view.model.ProductReplyUiModel
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.domain.model.Detail
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.domain.model.ProductrevGetReviewMedia
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.util.ReviewMediaGalleryRouter
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.adapter.typefactory.ReviewMediaThumbnailTypeFactory
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uimodel.ReviewMediaThumbnailUiModel
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uimodel.ReviewMediaThumbnailVisitable
import com.tokopedia.unifyprinciples.Typography

class FeedbackItemReply : FrameLayout, ReviewReplyListener {

    companion object {
        const val DATE_REVIEW_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'"
        private const val isAutoReply = false
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding = WidgetReplyFeedbackItemBinding.inflate(LayoutInflater.from(context), this, true)
    private var element: FeedbackUiModel? = null
    private var productReplyUiModel: ProductReplyUiModel? = null

    init {
        binding.reviewMediaThumbnails.setListener(ReviewMediaThumbnailListener())
    }

    fun setData(data: FeedbackUiModel, productReplyUiModel: ProductReplyUiModel) {
        this.element = data
        this.productReplyUiModel = productReplyUiModel
        binding.ivRatingFeedback.setImageResource(getReviewStar(data.rating.orZero()))
        binding.tvFeedbackUser.text = MethodChecker.fromHtml(String.format(context.getString(R.string.label_name_reviewer_builder), data.reviewerName.orEmpty()))
        binding.tvFeedbackDate.text = data.reviewTime.orEmpty() toRelativeDate (DATE_REVIEW_FORMAT)
        setupFeedbackReview(data.reviewText.orEmpty())
        setImageAttachment(data)
        setReplyView(data)
        showKejarUlasanLabel(data.isKejarUlasan)
        setBadRatingReason(data.badRatingReason)
        setBadRatingReasonDisclaimer(data.badRatingDisclaimer)
    }

    private fun setReplyView(data: FeedbackUiModel) {
        binding.tvReplyComment.text = data.replyText
        binding.tvReplyDate.text = data.replyTime.orEmpty() toRelativeDate (DATE_REVIEW_FORMAT)
        if (data.autoReply == isAutoReply) {
            binding.tvReplyUser.text = context.getString(R.string.user_reply)
        } else {
            binding.tvReplyUser.text = context.getString(R.string.otomatis_reply)
        }
    }

    private fun setupFeedbackReview(feedbackText: String) {
        binding.replyFeedbackState.background = ContextCompat.getDrawable(context, R.drawable.rectangle_8)
        if (feedbackText.isEmpty()) {
            binding.tvFeedbackReview.text = context.getString(R.string.review_not_found)
            binding.tvFeedbackReview.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN950_44))
        } else {
            binding.tvFeedbackReview.apply {
                setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN950_96))
                text = feedbackText
            }
        }
    }

    private fun setImageAttachment(element: FeedbackUiModel) {
        binding.reviewMediaThumbnails.apply {
            setData(element.reviewMediaThumbnail)
        }
        if (element.reviewMediaThumbnail.mediaThumbnails.isEmpty()) {
            binding.reviewMediaThumbnails.hide()
        } else {
            binding.reviewMediaThumbnails.show()
        }
    }

    private fun showKejarUlasanLabel(isKejarUlasan: Boolean) {
        binding.kejarUlasanLabel.showWithCondition(isKejarUlasan)
    }

    private fun setBadRatingReason(reason: String) {
        binding.badRatingReasonReview.showBadRatingReason(reason)
    }

    private fun setBadRatingReasonDisclaimer(disclaimer: String) {
        binding.badRatingReasonDisclaimer.setDisclaimer(disclaimer)
    }

    private fun mapFeedbackUiModelToReviewMediaData(
        reviewMediaThumbnailUiModel: ReviewMediaThumbnailUiModel
    ): ProductrevGetReviewMedia {
        return ProductrevGetReviewMedia(
            reviewMedia = reviewMediaThumbnailUiModel.generateReviewMedia(),
            detail = Detail(
                reviewDetail = listOf(),
                reviewGalleryImages = reviewMediaThumbnailUiModel.generateReviewGalleryImage(),
                reviewGalleryVideos = reviewMediaThumbnailUiModel.generateReviewGalleryVideo(),
                mediaCount = reviewMediaThumbnailUiModel.generateMediaCount()
            )
        )
    }

    override fun onImageItemClicked(
        reviewMediaThumbnailUiModel: ReviewMediaThumbnailUiModel,
        title: String,
        feedbackId: String,
        productID: String,
        position: Int
    ) {
        context.run {
            ReviewMediaGalleryRouter.routeToReviewMediaGallery(
                context = context,
                pageSource = ReviewMediaGalleryRouter.PageSource.REVIEW,
                productID = productID,
                shopID = "",
                isProductReview = true,
                isFromGallery = false,
                mediaPosition = position.inc(),
                showSeeMore = false,
                preloadedDetailedReviewMediaResult = mapFeedbackUiModelToReviewMediaData(
                    reviewMediaThumbnailUiModel
                )
            ).run { startActivity(this) }
        }
    }

    fun showGroupReply() {
        binding.groupReply.show()
    }

    fun hideGroupReply() {
        binding.groupReply.hide()
    }

    fun getEditReplyTypography(): Typography {
        return binding.tvReplyEdit
    }

    fun setReplyUserText(reply: String) {
        binding.tvReplyUser.text = reply
    }

    fun setReplyDate(date: String) {
        binding.tvReplyDate.text = date
    }

    fun setReplyComment(comment: String) {
        binding.tvReplyComment.text = comment
    }

    private inner class ReviewMediaThumbnailListener : ReviewMediaThumbnailTypeFactory.Listener {
        override fun onMediaItemClicked(item: ReviewMediaThumbnailVisitable, position: Int) {
            val element = element
            val productReplyUiModel = productReplyUiModel
            if (element != null && productReplyUiModel != null) {
                onImageItemClicked(
                    element.reviewMediaThumbnail,
                    productReplyUiModel.productName.orEmpty(),
                    element.feedbackID,
                    productReplyUiModel.productID,
                    position
                )
            }
        }
    }
}
