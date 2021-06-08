package com.tokopedia.review.feature.inboxreview.presentation.viewholder

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.review.R
import com.tokopedia.review.common.util.PaddingItemDecoratingReview
import com.tokopedia.review.common.util.getReviewStar
import com.tokopedia.review.common.util.toRelativeDate
import com.tokopedia.review.common.util.toReviewDescriptionFormatted
import com.tokopedia.review.feature.inboxreview.presentation.adapter.FeedbackInboxReviewListener
import com.tokopedia.review.feature.inboxreview.presentation.adapter.InboxReviewFeedbackImageAdapter
import com.tokopedia.review.feature.inboxreview.presentation.model.FeedbackInboxUiModel
import com.tokopedia.review.feature.reviewdetail.view.adapter.viewholder.ProductFeedbackDetailViewHolder
import kotlinx.android.synthetic.main.item_inbox_review.view.*
import kotlinx.android.synthetic.main.partial_feedback_variant_detail.view.*

class InboxReviewFeedbackViewHolder(view: View,
                                    private val feedbackInboxReviewListener: FeedbackInboxReviewListener): AbstractViewHolder<FeedbackInboxUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_inbox_review
        const val DATE_REVIEW_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'"
        const val FEEDBACK_MAX_CHAR = 150
    }

    private var reviewInboxFeedbackImageAdapter: InboxReviewFeedbackImageAdapter? = null
    private val impressHolder = ImpressHolder()

    override fun bind(element: FeedbackInboxUiModel) {
        reviewInboxFeedbackImageAdapter = InboxReviewFeedbackImageAdapter(feedbackInboxReviewListener)
        with(itemView) {
            if(adapterPosition == 0) {
                feedbackInboxReviewListener.onBackgroundMarginIsReplied(element.replyText.isBlank())
            }
            if(element.replyText.isNotBlank()) {
                containerInboxReview?.setBackgroundColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N0))
            } else {
                containerInboxReview?.setBackgroundColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G100))
            }
            tvProductTitle.text = element.productName
            ivRatingFeedback.setImageResource(getReviewStar(element.rating.orZero()))

            tvFeedbackUser?.text = MethodChecker.fromHtml(getString(R.string.label_name_reviewer_builder, element.username))
            tvFeedbackDate?.text = element.reviewTime toRelativeDate  (DATE_REVIEW_FORMAT)

            setupVariant(element.variantName)
            setActionReply(element)
            setFeedbackReply(element)
            setupFeedbackReview(element.reviewText, element.feedbackId.toString(), element.productID.toString())
            setImageAttachment(element)
            showKejarUlasanLabel(element.isKejarUlasan)
        }
    }

    private fun setupVariant(variantName: String) {
        with(itemView) {
            if (variantName.isEmpty()) {
                tvVariantFeedback?.hide()
                tvVariantFeedbackValue?.hide()
            } else {
                tvVariantFeedback?.show()
                tvVariantFeedbackValue?.show()
                tvVariantFeedbackValue?.text = variantName
            }
        }
    }

    private fun setupFeedbackReview(feedbackText: String, feedbackId: String, productID: String) {
        with(itemView) {
            replyFeedbackState?.background = ContextCompat.getDrawable(context, R.drawable.rectangle_8)
            if (feedbackText.isEmpty()) {
                tvFeedbackReview?.text = getString(R.string.review_not_found)
                tvFeedbackReview?.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_44))
            } else {
                tvFeedbackReview?.apply {
                    setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_96))
                    text = feedbackText.toReviewDescriptionFormatted(ProductFeedbackDetailViewHolder.FEEDBACK_MAX_CHAR, itemView.context)
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
        with(itemView) {
            if (element.replyText.isNotEmpty()) {
                if (!element.isAutoReply) {
                    tvReplyUser?.text = getString(R.string.user_reply)
                } else {
                    tvReplyUser?.text = getString(R.string.otomatis_reply)
                }
                tvReplyDate?.text = element.replyTime toRelativeDate (DATE_REVIEW_FORMAT)

                tvReplyComment?.text = element.replyText
                tvReplyComment?.let {
                    it.text = element.replyText.toReviewDescriptionFormatted(FEEDBACK_MAX_CHAR, itemView.context)
                    it.setOnClickListener { _ ->
                        it.maxLines = Integer.MAX_VALUE
                        it.text = MethodChecker.fromHtml(element.replyText)
                    }
                }
                showReplySection()
            } else {
                tvReplyComment?.text = ""
                hideReplySection()
            }
        }
    }

    private fun setImageAttachment(element: FeedbackInboxUiModel) {
        with(itemView) {
            val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            rvItemAttachmentFeedback?.apply {
                layoutManager = linearLayoutManager
                if (itemDecorationCount == 0) {
                    addItemDecoration(PaddingItemDecoratingReview())
                }
                adapter = reviewInboxFeedbackImageAdapter
            }
            if (element.attachments.isEmpty()) {
                rvItemAttachmentFeedback?.hide()
            } else {
                reviewInboxFeedbackImageAdapter?.setAttachmentUiData(element.attachments)
                reviewInboxFeedbackImageAdapter?.setFeedbackId(element.feedbackId.toString())
                reviewInboxFeedbackImageAdapter?.setTitleProduct(element.productName)
                reviewInboxFeedbackImageAdapter?.setProductId(element.productID.toString())
                reviewInboxFeedbackImageAdapter?.submitList(element.attachments)
                rvItemAttachmentFeedback?.show()
            }
        }
    }

    private fun setActionReply(element: FeedbackInboxUiModel) {
        with(itemView) {
            if(element.replyText.isNotBlank()) {
                tvActionReplyReview?.text = getString(R.string.edit_review_label)
            } else {
                tvActionReplyReview.text = getString(R.string.review_reply_label)
            }

            tvActionReplyReview.setOnClickListener {
                feedbackInboxReviewListener.onItemReplyOrEditClicked(element, element.replyText.isBlank(), adapterPosition)
            }
        }
    }

    private fun hideReplySection() {
        with(itemView) {
            replyFeedbackState?.hide()
            tvReplyUser?.hide()
            tvReplyDate?.hide()
            tvReplyComment?.hide()
        }
    }

    private fun showReplySection() {
        with(itemView) {
            replyFeedbackState?.show()
            tvReplyUser?.show()
            tvReplyDate?.show()
            tvReplyComment?.show()
        }
    }

    private fun showKejarUlasanLabel(isKejarUlasan: Boolean) {
        with(itemView) {
            kejarUlasanLabel?.showWithCondition(isKejarUlasan)
            if(isKejarUlasan) {
                itemView.kejarUlasanLabel.addOnImpressionListener(impressHolder) {
                    feedbackInboxReviewListener.showCoachMark(kejarUlasanLabel)
                }
            }
        }
    }

}