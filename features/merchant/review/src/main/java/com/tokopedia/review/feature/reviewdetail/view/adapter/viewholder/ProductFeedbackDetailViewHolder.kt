package com.tokopedia.review.feature.reviewdetail.view.adapter.viewholder

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.review.R
import com.tokopedia.review.common.util.PaddingItemDecoratingReview
import com.tokopedia.review.common.util.getReviewStar
import com.tokopedia.review.common.util.toRelativeDate
import com.tokopedia.review.common.util.toReviewDescriptionFormatted
import com.tokopedia.review.feature.reviewdetail.util.mapper.SellerReviewProductDetailMapper
import com.tokopedia.review.feature.reviewdetail.view.adapter.ProductFeedbackDetailListener
import com.tokopedia.review.feature.reviewdetail.view.adapter.ReviewDetailFeedbackImageAdapter
import com.tokopedia.review.feature.reviewdetail.view.model.FeedbackUiModel
import kotlinx.android.synthetic.main.item_product_feedback_detail.view.*
import kotlinx.android.synthetic.main.partial_feedback_variant_detail.view.*

class ProductFeedbackDetailViewHolder(private val view: View,
                                      private val productFeedbackDetailListener: ProductFeedbackDetailListener) : AbstractViewHolder<FeedbackUiModel>(view) {

    companion object {
        @JvmStatic
        val LAYOUT = R.layout.item_product_feedback_detail
        const val REPLY_MAX_CHAR = 100
        const val FEEDBACK_MAX_CHAR = 150
        private const val isAutoReply = false
        const val DATE_REVIEW_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'"
    }

    private var reviewDetailFeedbackImageAdapter: ReviewDetailFeedbackImageAdapter? = null

    override fun bind(element: FeedbackUiModel) {
        reviewDetailFeedbackImageAdapter = ReviewDetailFeedbackImageAdapter(productFeedbackDetailListener)
        with(itemView) {
            ivRatingFeedback?.setImageResource(getReviewStar(element.rating.orZero()))
            ivOptionReviewFeedback?.setOnClickListener {
                setBottomSheetFeedbackOption(element)
            }
            tvFeedbackUser?.text = MethodChecker.fromHtml(getString(R.string.label_name_reviewer_builder, element.reviewerName.orEmpty()))
            tvFeedbackDate?.text = element.reviewTime.orEmpty() toRelativeDate (DATE_REVIEW_FORMAT)
        }

        setFeedbackReply(element)
        setupVariant(element.variantName ?: "")
        setupFeedbackReview(element.reviewText ?: "", element.feedbackID.toString())
        setImageAttachment(element)
        showLabelKejarUlasan(element.isKejarUlasan)
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

    private fun setupFeedbackReview(feedbackText: String, feedbackId: String) {
        with(itemView) {
            replyFeedbackState?.background = ContextCompat.getDrawable(context, R.drawable.rectangle_8)
            if (feedbackText.isEmpty()) {
                tvFeedbackReview?.text = getString(R.string.review_not_found)
                tvFeedbackReview?.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_44))
            } else {
                tvFeedbackReview?.apply {
                    setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_96))
                    text = feedbackText.toReviewDescriptionFormatted(FEEDBACK_MAX_CHAR, itemView.context)
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
        with(itemView) {
            if (element.replyText?.isNotEmpty() == true) {
                if (element.autoReply == isAutoReply) {
                    tvReplyUser?.text = getString(R.string.user_reply)
                } else {
                    tvReplyUser?.text = getString(R.string.otomatis_reply)
                }
                tvReplyDate?.text = element.replyTime.orEmpty() toRelativeDate (DATE_REVIEW_FORMAT)

                tvReplyComment?.text = element.replyText.orEmpty()
                tvReplyComment?.let {
                    it.text = element.replyText.orEmpty().toReviewDescriptionFormatted(REPLY_MAX_CHAR, itemView.context)
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

    private fun setImageAttachment(element: FeedbackUiModel) {
        val linearLayoutManager = LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
        with(itemView) {
            rvItemAttachmentFeedback?.apply {
                layoutManager = linearLayoutManager
                if (itemDecorationCount == 0) {
                    addItemDecoration(PaddingItemDecoratingReview())
                }
                adapter = reviewDetailFeedbackImageAdapter
            }
            if (element.attachments.isEmpty()) {
                rvItemAttachmentFeedback?.hide()
            } else {
                reviewDetailFeedbackImageAdapter?.setAttachmentUiData(element.attachments)
                reviewDetailFeedbackImageAdapter?.setFeedbackId(element.feedbackID.toString())
                reviewDetailFeedbackImageAdapter?.submitList(element.attachments)
                rvItemAttachmentFeedback?.show()
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

    private fun showLabelKejarUlasan(isKejarUlasan: Boolean) {
        itemView.kejarUlasanLabel?.showWithCondition(isKejarUlasan)
    }
}