package com.tokopedia.reviewseller.feature.reviewdetail.view.adapter.viewholder

import android.view.View
import android.view.ViewTreeObserver
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
import com.tokopedia.reviewseller.feature.reviewdetail.util.ReviewUtil
import com.tokopedia.reviewseller.feature.reviewdetail.util.mapper.SellerReviewProductDetailMapper
import com.tokopedia.reviewseller.feature.reviewdetail.util.toRelativeDayAndWeek
import com.tokopedia.reviewseller.feature.reviewdetail.util.toReviewDescriptionFormatted
import com.tokopedia.reviewseller.feature.reviewdetail.view.adapter.ProductFeedbackDetailListener
import com.tokopedia.reviewseller.feature.reviewdetail.view.adapter.ReviewDetailFeedbackImageAdapter
import com.tokopedia.reviewseller.feature.reviewdetail.view.model.FeedbackUiModel
import com.tokopedia.unifyprinciples.Typography


class ProductFeedbackDetailViewHolder(private val view: View,
                                      private val productFeedbackDetailListener: ProductFeedbackDetailListener) : AbstractViewHolder<FeedbackUiModel>(view) {

    companion object {
        @JvmStatic
        val LAYOUT = R.layout.item_product_feedback_detail
        const val DATE_REVIEW_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'"

        private const val isAutoReply = "false"
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
        ivRatingFeedback?.setImageResource(ReviewUtil.getReviewStar(element.rating.orZero()))
        ivOptionReviewFeedback?.setOnClickListener {
            setBottomSheetFeedbackOption(element)
        }
        ivRatingFeedback?.setImageResource(ReviewUtil.getReviewStar(element.rating.orZero()))
        tvFeedbackUser?.text = MethodChecker.fromHtml(getString(R.string.label_name_reviewer_builder, element.reviewerName.orEmpty()))
        tvFeedbackDate?.text = element.reviewTime.orEmpty() toRelativeDayAndWeek (DATE_REVIEW_FORMAT)

        setFeedbackReply(element)
        setupVariant(element.variantName ?: "")
        setupFeedbackReview(element.reviewText ?: "")
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

    private fun setupFeedbackReview(feedbackText: String) {
        if (feedbackText.isEmpty()) {
            tvFeedbackReview?.text = getString(R.string.review_not_found)
            tvFeedbackReview?.setTextColor(ContextCompat.getColor(itemView.context, R.color.clr_review_not_found))
        } else {
            tvFeedbackReview?.apply {
                setTextColor(ContextCompat.getColor(itemView.context, R.color.clr_f531353b))
                text = feedbackText
                setOnClickListener {
                    maxLines = Integer.MAX_VALUE
                    text = feedbackText
                }
            }
        }

        tvFeedbackReview?.setReplyCommentSeeMore(feedbackText, 3)
    }

    private fun Typography.setReplyCommentSeeMore(replyText: String, maxLines: Int) = this.let {
        val vto = it.viewTreeObserver

        vto.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {

                if (it.lineCount > maxLines) {
                    it.text = replyText.toReviewDescriptionFormatted(view.context, it.layout.getLineEnd(maxLines - 1))
                } else {
                    it.text = replyText
                }

                it.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }

        })
    }

    private fun setFeedbackReply(element: FeedbackUiModel) {
        if (element.replyText?.isNotEmpty() == true) {

            if (element.autoReply == isAutoReply) {
                tvReplyUser?.text = getString(R.string.otomatis_reply)
            } else {
                tvReplyUser?.text = String.format(getString(R.string.user_reply_feedback), element.sellerUser.orEmpty())
            }
            tvReplyDate?.text = element.replyTime.orEmpty() toRelativeDayAndWeek (DATE_REVIEW_FORMAT)

            tvReplyComment?.text = element.replyText.orEmpty()
            tvReplyComment?.let {
                it.setReplyCommentSeeMore(element.replyText.orEmpty(), 2)
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
            reviewDetailFeedbackImageAdapter.submitList(element.attachments)
            rvItemAttachmentFeedback?.show()
        }
    }

    private fun setBottomSheetFeedbackOption(element: FeedbackUiModel) {
        val optionDetailListItemUnify = SellerReviewProductDetailMapper.mapToItemUnifyListFeedback(view.context,
                element.replyText?.isEmpty() ?: false)
        productFeedbackDetailListener.onOptionFeedbackClicked(view, getString(R.string.option_menu_label), optionDetailListItemUnify,
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