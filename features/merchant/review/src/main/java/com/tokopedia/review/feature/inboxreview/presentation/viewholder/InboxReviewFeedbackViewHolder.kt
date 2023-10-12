package com.tokopedia.review.feature.inboxreview.presentation.viewholder

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.review.R
import com.tokopedia.review.common.util.getReviewStar
import com.tokopedia.review.common.util.toRelativeDate
import com.tokopedia.review.common.util.toReviewDescriptionFormatted
import com.tokopedia.review.databinding.ItemInboxReviewBinding
import com.tokopedia.review.feature.inboxreview.presentation.adapter.FeedbackInboxReviewListener
import com.tokopedia.review.feature.inboxreview.presentation.model.FeedbackInboxUiModel
import com.tokopedia.review.feature.reviewdetail.view.adapter.viewholder.ProductFeedbackDetailViewHolder
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.adapter.typefactory.ReviewMediaThumbnailTypeFactory

class InboxReviewFeedbackViewHolder(
    view: View,
    reviewMediaThumbnailRecycledViewPool: RecyclerView.RecycledViewPool,
    reviewMediaThumbnailListener: ReviewMediaThumbnailTypeFactory.Listener,
    private val feedbackInboxReviewListener: FeedbackInboxReviewListener
) : AbstractViewHolder<FeedbackInboxUiModel>(view) {

    companion object {
        val LAYOUT = com.tokopedia.review.R.layout.item_inbox_review
        const val DATE_REVIEW_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'"
        const val FEEDBACK_MAX_CHAR = 150
    }

    private val impressHolder = ImpressHolder()
    private val binding = ItemInboxReviewBinding.bind(view)

    init {
        binding.reviewMediaThumbnails.setListener(reviewMediaThumbnailListener)
        binding.reviewMediaThumbnails.setRecycledViewPool(reviewMediaThumbnailRecycledViewPool)
    }

    override fun bind(element: FeedbackInboxUiModel) {
        with(binding) {
            if(adapterPosition == 0) {
                feedbackInboxReviewListener.onBackgroundMarginIsReplied(element.replyText.isBlank())
            }
            if (element.replyText.isNotBlank()) {
                root.setBackgroundColor(
                    ContextCompat.getColor(
                        root.context,
                        com.tokopedia.unifyprinciples.R.color.Unify_Background
                    )
                )
            } else {
                root.setBackgroundColor(
                    ContextCompat.getColor(
                        root.context,
                        com.tokopedia.unifyprinciples.R.color.Unify_GN50
                    )
                )
            }
            tvProductTitle.text = element.productName
            ivRatingFeedback.setImageResource(getReviewStar(element.rating.orZero()))

            tvFeedbackUser.text = MethodChecker.fromHtml(getString(R.string.label_name_reviewer_builder, element.username))
            tvFeedbackDate.text = element.reviewTime toRelativeDate  (DATE_REVIEW_FORMAT)

            setupVariant(element.variantName)
            setActionReply(element)
            setFeedbackReply(element)
            setupFeedbackReview(element.reviewText, element.feedbackId, element.productID)
            setImageAttachment(element)
            showKejarUlasanLabel(element.isKejarUlasan)
            setBadRatingReason(element.badRatingReason)
            setBadRatingDisclaimer(element.ratingDisclaimer)
        }
    }

    private fun setupVariant(variantName: String) {
        with(binding.partialFeedbackVariantInboxReview) {
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

    private fun setupFeedbackReview(feedbackText: String, feedbackId: String, productID: String) {
        with(binding) {
            replyFeedbackState.background = ContextCompat.getDrawable(root.context, R.drawable.rectangle_8)
            if (feedbackText.isEmpty()) {
                tvFeedbackReview.text = getString(R.string.review_not_found)
                tvFeedbackReview.setTextColor(ContextCompat.getColor(root.context, com.tokopedia.unifyprinciples.R.color.Unify_NN950_44))
            } else {
                tvFeedbackReview.apply {
                    setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN950_96))
                    text = feedbackText.toReviewDescriptionFormatted(ProductFeedbackDetailViewHolder.FEEDBACK_MAX_CHAR, root.context)
                    setOnClickListener {
                        feedbackInboxReviewListener.onInFullReviewClicked(
                                feedbackId,
                                productID
                        )
                        maxLines = Integer.MAX_VALUE
                        text = feedbackText
                    }
                }
            }
        }
    }

    private fun setFeedbackReply(element: FeedbackInboxUiModel) {
        with(binding) {
            if (element.replyText.isNotEmpty()) {
                if (!element.isAutoReply) {
                    tvReplyUser.text = getString(R.string.user_reply)
                } else {
                    tvReplyUser.text = getString(R.string.otomatis_reply)
                }
                tvReplyDate.text = element.replyTime toRelativeDate (DATE_REVIEW_FORMAT)

                tvReplyComment.text = element.replyText
                tvReplyComment.let {
                    it.text = element.replyText.toReviewDescriptionFormatted(FEEDBACK_MAX_CHAR, root.context)
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

    private fun setImageAttachment(element: FeedbackInboxUiModel) {
        with(binding) {
            reviewMediaThumbnails.apply {
                setData(element.reviewMediaThumbnail)
            }
            if (element.reviewMediaThumbnail.mediaThumbnails.isEmpty()) {
                reviewMediaThumbnails.hide()
            } else {
                reviewMediaThumbnails.show()
            }
        }
    }

    private fun setActionReply(element: FeedbackInboxUiModel) {
        with(binding) {
            if(element.replyText.isNotBlank()) {
                tvActionReplyReview.text = getString(R.string.edit_review_label)
            } else {
                tvActionReplyReview.text = getString(R.string.review_reply_label)
            }

            tvActionReplyReview.setOnClickListener {
                feedbackInboxReviewListener.onItemReplyOrEditClicked(element, element.replyText.isBlank(), adapterPosition)
            }
        }
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

    private fun showKejarUlasanLabel(isKejarUlasan: Boolean) {
        with(binding) {
            kejarUlasanLabel.showWithCondition(isKejarUlasan)
            if(isKejarUlasan) {
                binding.kejarUlasanLabel.addOnImpressionListener(impressHolder) {
                    feedbackInboxReviewListener.showCoachMark(kejarUlasanLabel)
                }
            }
        }
    }

    private fun setBadRatingReason(reason: String) {
        binding.badRatingReasonReview.showBadRatingReason(reason)
    }

    private fun setBadRatingDisclaimer(disclaimer: String) {
        binding.badRatingReasonDisclaimer.setDisclaimer(disclaimer)
    }
}