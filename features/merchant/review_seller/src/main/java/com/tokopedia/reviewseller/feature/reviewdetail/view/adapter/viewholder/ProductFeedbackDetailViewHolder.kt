package com.tokopedia.reviewseller.feature.reviewdetail.view.adapter.viewholder

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.reviewseller.R
import com.tokopedia.reviewseller.common.util.PaddingItemDecoratingReviewSeller
import com.tokopedia.reviewseller.common.util.getReviewStar
import com.tokopedia.reviewseller.common.util.toRelativeDate
import com.tokopedia.reviewseller.common.util.toReviewDescriptionFormatted
import com.tokopedia.reviewseller.feature.reviewdetail.util.mapper.SellerReviewProductDetailMapper
import com.tokopedia.reviewseller.feature.reviewdetail.view.adapter.ProductFeedbackDetailListener
import com.tokopedia.reviewseller.feature.reviewdetail.view.adapter.ReviewDetailFeedbackImageAdapter
import com.tokopedia.reviewseller.feature.reviewdetail.view.model.FeedbackUiModel
import com.tokopedia.unifyprinciples.Typography


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

    private var ivRatingFeedback: AppCompatImageView? = null
    private var ivOptionReviewFeedback: AppCompatImageView? = null
    private var tvFeedbackUser: Typography? = null
    private var tvFeedbackDate: Typography? = null
    private var tvFeedbackReview: Typography? = null
    private var tvVariantFeedback: Typography? = null
    private var tvVariantFeedbackValue: Typography? = null
    private var rvItemAttachmentFeedback: RecyclerView? = null
    private var tvReplyUser: Typography? = null
    private var tvReplyDate: Typography? = null
    private var tvReplyComment: Typography? = null
    private var replyFeedbackState: CardView? = null

    init {
        ivRatingFeedback = view.findViewById(R.id.ivRatingFeedback)
        ivOptionReviewFeedback = view.findViewById(R.id.ivOptionReviewFeedback)
        tvFeedbackUser = view.findViewById(R.id.tvFeedbackUser)
        tvFeedbackDate = view.findViewById(R.id.tvFeedbackDate)
        tvFeedbackReview = view.findViewById(R.id.tvFeedbackReview)
        tvVariantFeedback = view.findViewById(R.id.tvVariantFeedback)
        tvVariantFeedbackValue = view.findViewById(R.id.tvVariantFeedbackValue)
        rvItemAttachmentFeedback = view.findViewById(R.id.rvItemAttachmentFeedback)
        tvReplyUser = view.findViewById(R.id.tvReplyUser)
        tvReplyDate = view.findViewById(R.id.tvReplyDate)
        tvReplyComment = view.findViewById(R.id.tvReplyComment)
    }

    private val reviewDetailFeedbackImageAdapter by lazy {
        ReviewDetailFeedbackImageAdapter(productFeedbackDetailListener)
    }

    override fun bind(element: FeedbackUiModel) {
        ivRatingFeedback?.setImageResource(getReviewStar(element.rating.orZero()))
        ivOptionReviewFeedback?.setOnClickListener {
            setBottomSheetFeedbackOption(element)
        }
        tvFeedbackUser?.text = MethodChecker.fromHtml(getString(R.string.label_name_reviewer_builder, element.reviewerName.orEmpty()))
        tvFeedbackDate?.text = element.reviewTime.orEmpty() toRelativeDate  (DATE_REVIEW_FORMAT)

        setFeedbackReply(element)
        setupVariant(element.variantName ?: "")
        setupFeedbackReview(element.reviewText ?: "", element.feedbackID.toString())
        setImageAttachment(element)
    }

    private fun setupVariant(variantName: String) {
        if (variantName.isEmpty()) {
            tvVariantFeedback?.hide()
            tvVariantFeedbackValue?.hide()
        } else {
            tvVariantFeedback?.show()
            tvVariantFeedbackValue?.show()
            tvVariantFeedbackValue?.text = variantName
        }
    }

    private fun setupFeedbackReview(feedbackText: String, feedbackId: String) {
        if (feedbackText.isEmpty()) {
            tvFeedbackReview?.text = getString(R.string.review_not_found)
            tvFeedbackReview?.setTextColor(ContextCompat.getColor(itemView.context, R.color.clr_review_not_found))
        } else {
            tvFeedbackReview?.apply {
                setTextColor(ContextCompat.getColor(itemView.context, R.color.clr_f531353b))
                text = feedbackText.toReviewDescriptionFormatted(FEEDBACK_MAX_CHAR)
                setOnClickListener {
                    productFeedbackDetailListener.onFeedbackMoreReplyClicked(feedbackId)
                    maxLines = Integer.MAX_VALUE
                    text = feedbackText
                }
            }
        }
    }

    private fun setFeedbackReply(element: FeedbackUiModel) {
        if (element.replyText?.isNotEmpty() == true) {

            if (element.autoReply == isAutoReply) {
                tvReplyUser?.text = getString(R.string.user_reply)
            } else {
                tvReplyUser?.text = getString(R.string.otomatis_reply)
            }
            tvReplyDate?.text = element.replyTime.orEmpty() toRelativeDate  (DATE_REVIEW_FORMAT)

            tvReplyComment?.text = element.replyText.orEmpty()
            tvReplyComment?.let {
                it.text = element.replyText.orEmpty().toReviewDescriptionFormatted(REPLY_MAX_CHAR)
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

    private fun setImageAttachment(element: FeedbackUiModel) {
        val linearLayoutManager = LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
        rvItemAttachmentFeedback?.apply {
            layoutManager = linearLayoutManager
            if (itemDecorationCount == 0) {
                addItemDecoration(PaddingItemDecoratingReviewSeller())
            }
            adapter = reviewDetailFeedbackImageAdapter
        }
        if (element.attachments.isEmpty()) {
            rvItemAttachmentFeedback?.hide()
        } else {
            reviewDetailFeedbackImageAdapter.setAttachmentUiData(element.attachments)
            reviewDetailFeedbackImageAdapter.setFeedbackId(element.feedbackID.toString())
            reviewDetailFeedbackImageAdapter.submitList(element.attachments)
            rvItemAttachmentFeedback?.show()
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
        replyFeedbackState?.hide()
        tvReplyUser?.hide()
        tvReplyDate?.hide()
        tvReplyComment?.hide()
    }

    private fun showReplySection() {
        replyFeedbackState?.show()
        tvReplyUser?.show()
        tvReplyDate?.show()
        tvReplyComment?.show()
    }
}