package com.tokopedia.review.feature.reviewdetail.view.adapter.viewholder

import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.review.R
import com.tokopedia.review.common.presentation.widget.ReviewBadRatingReasonWidget
import com.tokopedia.review.common.util.getReviewStar
import com.tokopedia.review.common.util.toRelativeDate
import com.tokopedia.review.common.util.toReviewDescriptionFormatted
import com.tokopedia.review.databinding.ItemProductFeedbackDetailBinding
import com.tokopedia.review.feature.reviewdetail.util.mapper.SellerReviewProductDetailMapper
import com.tokopedia.review.feature.reviewdetail.view.adapter.ProductFeedbackDetailListener
import com.tokopedia.review.feature.reviewdetail.view.model.FeedbackUiModel
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uimodel.ReviewMediaThumbnailVisitable
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.widget.ReviewMediaThumbnail

class ProductFeedbackDetailViewHolder(
    private val view: View,
    private val productFeedbackDetailListener: ProductFeedbackDetailListener
) : AbstractViewHolder<FeedbackUiModel>(view) {

    companion object {
        @JvmStatic
        val LAYOUT = com.tokopedia.review.R.layout.item_product_feedback_detail
        const val REPLY_MAX_CHAR = 100
        const val FEEDBACK_MAX_CHAR = 150
        private const val isAutoReply = false
        const val DATE_REVIEW_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'"
    }

    private var element: FeedbackUiModel? = null
    private val badRatingReason: ReviewBadRatingReasonWidget = view.findViewById(R.id.badRatingReasonReview)
    private val reviewMediaThumbnailListener = ReviewMediaThumbnailListener()

    private val binding = ItemProductFeedbackDetailBinding.bind(view)

    override fun bind(element: FeedbackUiModel) {
        this.element = element
        with(binding) {
            ivRatingFeedback.setImageResource(getReviewStar(element.rating.orZero()))
            ivOptionReviewFeedback.setOnClickListener {
                setBottomSheetFeedbackOption(element)
            }
            tvFeedbackUser.text = MethodChecker.fromHtml(getString(R.string.label_name_reviewer_builder, element.reviewerName.orEmpty()))
            tvFeedbackDate.text = element.reviewTime.orEmpty() toRelativeDate (DATE_REVIEW_FORMAT)
        }

        setFeedbackReply(element)
        setupVariant(element.variantName ?: "")
        setupFeedbackReview(element.reviewText ?: "", element.feedbackID)
        setImageAttachment(element)
        showLabelKejarUlasan(element.isKejarUlasan)
        setBadRatingReason(element.badRatingReason)
    }

    private fun setupVariant(variantName: String) {
        with(binding.partialFeedbackVariantReviewDetail) {
            if (variantName.isEmpty()) {
                tvVariantFeedback.hide()
                tvVariantFeedbackValue.hide()
            } else {
                tvVariantFeedback.show()
                tvVariantFeedbackValue.show()
                tvVariantFeedbackValue.text = variantName
            }
        }
    }

    private fun setupFeedbackReview(feedbackText: String, feedbackId: String) {
        with(binding) {
            replyFeedbackState.background = ContextCompat.getDrawable(root.context, R.drawable.rectangle_8)
            if (feedbackText.isEmpty()) {
                tvFeedbackReview.text = getString(R.string.review_not_found)
                tvFeedbackReview.setTextColor(ContextCompat.getColor(root.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_44))
            } else {
                tvFeedbackReview.apply {
                    setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_96))
                    text = feedbackText.toReviewDescriptionFormatted(FEEDBACK_MAX_CHAR, root.context)
                    setOnClickListener {
                        productFeedbackDetailListener.onFeedbackMoreReplyClicked(feedbackId)
                        maxLines = Integer.MAX_VALUE
                        text = feedbackText
                    }
                }
            }
        }
    }

    private fun setFeedbackReply(element: FeedbackUiModel) {
        with(binding) {
            if (!element.replyText.isNullOrEmpty()) {
                if (element.autoReply == isAutoReply) {
                    tvReplyUser.text = getString(R.string.user_reply)
                } else {
                    tvReplyUser.text = getString(R.string.otomatis_reply)
                }
                tvReplyDate.text = element.replyTime.orEmpty() toRelativeDate (DATE_REVIEW_FORMAT)

                tvReplyComment.text = element.replyText.orEmpty()
                tvReplyComment.let {
                    it.text = element.replyText.orEmpty().toReviewDescriptionFormatted(REPLY_MAX_CHAR, root.context)
                    it.setOnClickListener { _ ->
                        it.maxLines = Integer.MAX_VALUE
                        it.text = MethodChecker.fromHtml(element.replyText)
                    }
                }
                showReplySection()
            } else {
                tvReplyComment.text = ""
                hideReplySection()
            }
        }
    }

    private fun setImageAttachment(element: FeedbackUiModel) {
        with(binding) {
            reviewMediaThumbnails.apply {
                setData(element.reviewMediaThumbnail)
                setListener(reviewMediaThumbnailListener)
            }
            if (element.reviewMediaThumbnail.mediaThumbnails.isEmpty()) {
                reviewMediaThumbnails.hide()
            } else {
                reviewMediaThumbnails.show()
            }
        }
    }

    private fun setBottomSheetFeedbackOption(element: FeedbackUiModel) {
        val optionDetailListItemUnify = SellerReviewProductDetailMapper.mapToItemUnifyListFeedback(view.context,
                element.replyText?.isEmpty() ?: false)
        productFeedbackDetailListener.onOptionFeedbackClicked(
                view, getString(R.string.option_menu_label),
                element,
                optionDetailListItemUnify,
                element.replyText?.isEmpty() ?: false)
    }

    private fun hideReplySection() {
        with(binding) {
            replyFeedbackState.hide()
            tvReplyUser.hide()
            tvReplyDate.hide()
            tvReplyComment.hide()
        }
    }

    private fun showReplySection() {
        with(binding) {
            replyFeedbackState.show()
            tvReplyUser.show()
            tvReplyDate.show()
            tvReplyComment.show()
        }
    }

    private fun showLabelKejarUlasan(isKejarUlasan: Boolean) {
        binding.kejarUlasanLabel.showWithCondition(isKejarUlasan)
    }

    private fun setBadRatingReason(reason: String) {
        badRatingReason.showBadRatingReason(reason)
    }

    private inner class ReviewMediaThumbnailListener: ReviewMediaThumbnail.Listener {
        override fun onMediaItemClicked(mediaItem: ReviewMediaThumbnailVisitable, position: Int) {
            element?.let {
                productFeedbackDetailListener.onImageItemClicked(
                    it.attachments.mapNotNull { it.fullSizeURL },
                    it.attachments.mapNotNull { it.thumbnailURL },
                    it.feedbackID,
                    position
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