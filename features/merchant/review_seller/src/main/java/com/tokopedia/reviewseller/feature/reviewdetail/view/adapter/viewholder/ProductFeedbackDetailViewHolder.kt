package com.tokopedia.reviewseller.feature.reviewdetail.view.adapter.viewholder

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.Group
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.reviewseller.R
import com.tokopedia.reviewseller.common.util.PaddingItemDecoratingReviewSeller
import com.tokopedia.reviewseller.feature.reviewdetail.util.ReviewUtil
import com.tokopedia.reviewseller.feature.reviewdetail.util.toRelativeDayAndWeek
import com.tokopedia.reviewseller.feature.reviewdetail.util.toReviewDescriptionFormatted
import com.tokopedia.reviewseller.feature.reviewdetail.view.adapter.ReviewDetailFeedbackImageAdapter
import com.tokopedia.reviewseller.feature.reviewdetail.view.model.FeedbackUiModel
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.item_product_feedback_detail.view.*

class ProductFeedbackDetailViewHolder(private val view: View) : AbstractViewHolder<FeedbackUiModel>(view) {

    companion object {
        @JvmStatic
        val LAYOUT = R.layout.item_product_feedback_detail

        private const val isAutoReply = "false"
    }

    private var ivRatingFeedback: AppCompatImageView? = null
    private var tvFeedbackUser: Typography? = null
    private var tvFeedbackDate: Typography? = null
    private var tvFeedbackReview: Typography? = null
    private var tvVariantFeedback: Typography? = null
    private var tvVariantFeedbackValue: Typography? = null
    private var rvItemAttachmentFeedback: RecyclerView? = null
    private var tvReplyUser: Typography? = null
    private var tvReplyDate: Typography? = null
    private var tvReplyComment: Typography? = null
    private var replyFeedbackState: View? = null

    init {
        ivRatingFeedback = view.findViewById(R.id.ivRatingFeedback)
        tvFeedbackUser = view.findViewById(R.id.tvFeedbackUser)
        tvFeedbackDate = view.findViewById(R.id.tvFeedbackDate)
        tvFeedbackReview = view.findViewById(R.id.tvFeedbackReview)
        tvVariantFeedback = view.findViewById(R.id.tvVariantFeedback)
        tvVariantFeedbackValue = view.findViewById(R.id.tvVariantFeedbackValue)
        rvItemAttachmentFeedback = view.findViewById(R.id.rvItemAttachmentFeedback)
        tvReplyUser = view.findViewById(R.id.tvReplyUser)
        tvReplyDate = view.findViewById(R.id.tvReplyDate)
        tvReplyComment = view.findViewById(R.id.tvReplyComment)
        replyFeedbackState = view.findViewById(R.id.replyFeedbackState)
    }

    private val reviewDetailFeedbackImageAdapter by lazy {
        ReviewDetailFeedbackImageAdapter()
    }

    override fun bind(element: FeedbackUiModel) {
        ivRatingFeedback?.setImageResource(ReviewUtil.getReviewStar(element.rating.orZero()))
        tvFeedbackUser?.text = element.reviewerName.orEmpty()
        tvFeedbackDate?.text = element.reviewTime.orEmpty() toRelativeDayAndWeek (DATE_REVIEW_FORMAT)

        setupReplySection(element.replyText ?: "", element.replyTime ?: "", element.autoReply ?: "")
        setupVariant(element.variantName ?: "")
        setupFeedbackReview(element.reviewText ?: "")
        setImageAttachment(element)
    }

        if (element.replyText?.isNotEmpty() == true) {
            if (element.autoReply == isAutoReply) {
                tvReplyUser?.text = getString(R.string.otomatis_reply)
            } else {
                tvReplyUser?.text = String.format(getString(R.string.user_reply_feedback), element.sellerUser.orEmpty())
            }
            tvReplyDate?.text = element.replyTime.orEmpty()
            tvReplyComment?.text = element.replyText.orEmpty()
            view.partialFeedbackReplyDetail?.show()
        } else {
            view.partialFeedbackReplyDetail?.hide()

        }
    }

    private fun setImageAttachment(element: FeedbackUiModel) {
        val linearLayoutManager = LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)

        rvItemAttachmentFeedback?.apply {
            if (element.attachments.isNotEmpty()) {
                layoutManager = linearLayoutManager
                if (itemDecorationCount == 0) {
                    addItemDecoration(PaddingItemDecoratingReviewSeller())
                }
                adapter = reviewDetailFeedbackImageAdapter
                reviewDetailFeedbackImageAdapter.submitList(element.attachments)
                show()
            } else {
                hide()
            }
        }
    }

    private fun hideReplySection() {
        reviewDetailGroup?.hide()
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
        reviewDetailGroup?.show()
    }
}