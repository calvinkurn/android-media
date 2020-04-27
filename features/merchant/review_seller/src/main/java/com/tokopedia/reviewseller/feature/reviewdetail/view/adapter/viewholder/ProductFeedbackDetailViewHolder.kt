package com.tokopedia.reviewseller.feature.reviewdetail.view.adapter.viewholder

import android.text.TextUtils
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.reviewseller.R
import com.tokopedia.reviewseller.common.util.PaddingItemDecoratingReviewSeller
import com.tokopedia.reviewseller.feature.reviewdetail.util.mapper.SellerReviewProductDetailMapper
import com.tokopedia.reviewseller.feature.reviewdetail.view.adapter.ReviewDetailFeedbackImageAdapter
import com.tokopedia.reviewseller.feature.reviewdetail.view.model.FeedbackUiModel
import com.tokopedia.unifycomponents.list.ListItemUnify
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.item_product_feedback_detail.view.*

class ProductFeedbackDetailViewHolder(private val view: View,
                                      private val productFeedbackDetailListener: ProductFeedbackDetailListener) : AbstractViewHolder<FeedbackUiModel>(view) {

    companion object {
        @JvmStatic
        val LAYOUT = R.layout.item_product_feedback_detail
        const val REPLY_TEXT_MAX_LINE = 3
        const val REPLY_TEXT_MAX_LENGTH = 150

        private const val isAutoReply = "false"
        private val PADDING_TEXT = 16.toPx()
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
    private var tvHideOrMore: Typography? = null

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
        replyFeedbackState = view.findViewById(R.id.replyFeedbackState)
        tvHideOrMore = view.findViewById(R.id.tgHideOrMore)
    }

    private val reviewDetailFeedbackImageAdapter by lazy {
        ReviewDetailFeedbackImageAdapter()
    }

    override fun bind(element: FeedbackUiModel) {

        ivRatingFeedback?.setImageResource(getReviewStar(element.rating.orZero()))
        ivOptionReviewFeedback?.setOnClickListener {
            setBottomSheetFeedbackOption(element)
        }
        tvFeedbackUser?.text = element.reviewerName.orEmpty()
        tvFeedbackDate?.text = element.reviewTime.orEmpty()

        setFeedbackVariant(element)
        setFeedbackReview(element)
        setImageAttachment(element)
        setFeedbackReply(element)
    }

    private fun setFeedbackVariant(element: FeedbackUiModel) {
        if (element.variantName?.isEmpty() == true) {
            view.partialFeedbackVariantReviewDetail?.hide()
        } else {
            view.partialFeedbackVariantReviewDetail?.show()
            tvVariantFeedbackValue?.text = element.variantName.orEmpty()
        }
    }

    private fun setFeedbackReview(element: FeedbackUiModel) {
        if (element.reviewText?.isEmpty() == true) {
            tvFeedbackReview?.text = getString(R.string.review_not_found)
            tvFeedbackReview?.setTextColor(ContextCompat.getColor(view.context, R.color.clr_review_not_found))
        } else {
            tvFeedbackReview?.setTextColor(ContextCompat.getColor(view.context, R.color.clr_f531353b))
            tvFeedbackReview?.text = element.reviewText.orEmpty()
        }
    }

    private fun setFeedbackReply(element: FeedbackUiModel) {
        if (element.replyText?.isNotEmpty() == true) {

            if (element.autoReply == isAutoReply) {
                tvReplyUser?.text = getString(R.string.otomatis_reply)
            } else {
                tvReplyUser?.text = String.format(getString(R.string.user_reply_feedback), element.sellerUser.orEmpty())
            }
            tvReplyDate?.text = element.replyTime.orEmpty()
            tvReplyComment?.text = element.replyText.orEmpty()

            tvReplyComment?.post {
                showMoreReplyComment()
            }

            tvHideOrMore?.setOnClickListener {
                hideMoreReplyComment()
            }

            view.partialFeedbackReplyDetail?.show()
        } else {
            view.partialFeedbackReplyDetail?.hide()
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

    private fun showMoreReplyComment() {
        if (tvReplyComment?.lineCount.orZero() >= REPLY_TEXT_MAX_LINE) {
            tvReplyComment?.maxLines = REPLY_TEXT_MAX_LINE
            tvReplyComment?.ellipsize = TextUtils.TruncateAt.END
            tvHideOrMore?.show()
        } else {
            tvHideOrMore?.hide()
        }
    }

    private fun hideMoreReplyComment() {
        tvHideOrMore?.hide()
        tvReplyComment?.apply {
            setPadding(0, 0, PADDING_TEXT, 0)
            maxLines = Integer.MAX_VALUE
            ellipsize = null
        }
    }

    private fun getReviewStar(ratingCount: Int): Int {
        return when (ratingCount) {
            1 -> {
                R.drawable.ic_rating_star_one
            }
            2 -> {
                R.drawable.ic_rating_star_two
            }
            3 -> {
                R.drawable.ic_rating_star_three
            }
            4 -> {
                R.drawable.ic_rating_star_four
            }
            5 -> {
                R.drawable.ic_rating_star_five
            }
            else -> {
                R.drawable.ic_rating_star_zero
            }
        }
    }

    interface ProductFeedbackDetailListener {
        fun onOptionFeedbackClicked(view: View, title: String, optionDetailListItemUnify: ArrayList<ListItemUnify>, isEmptyReply: Boolean)
    }
}