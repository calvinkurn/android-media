package com.tokopedia.review.feature.reviewreply.view.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.imagepreviewslider.presentation.activity.ImagePreviewSliderActivity
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.review.R
import com.tokopedia.review.common.util.PaddingItemDecoratingReview
import com.tokopedia.review.common.util.getReviewStar
import com.tokopedia.review.common.util.toRelativeDate
import com.tokopedia.review.databinding.WidgetReplyFeedbackItemBinding
import com.tokopedia.review.feature.reviewdetail.view.model.FeedbackUiModel
import com.tokopedia.review.feature.reviewreply.view.adapter.ReviewReplyFeedbackImageAdapter
import com.tokopedia.review.feature.reviewreply.view.adapter.ReviewReplyListener
import com.tokopedia.review.feature.reviewreply.view.model.ProductReplyUiModel
import com.tokopedia.unifyprinciples.Typography

class FeedbackItemReply : BaseCustomView, ReviewReplyListener {

    companion object {
        const val DATE_REVIEW_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'"
        private const val isAutoReply = false
    }

    private val replyReviewFeedbackImageAdapter by lazy {
        ReviewReplyFeedbackImageAdapter(this)
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding = WidgetReplyFeedbackItemBinding.inflate(LayoutInflater.from(context), this, true)

    fun setData(data: FeedbackUiModel, productReplyUiModel: ProductReplyUiModel) {
        binding.ivRatingFeedback.setImageResource(getReviewStar(data.rating.orZero()))
        binding.tvFeedbackUser.text = MethodChecker.fromHtml(String.format(context.getString(R.string.label_name_reviewer_builder), data.reviewerName.orEmpty()))
        binding.tvFeedbackDate.text = data.reviewTime.orEmpty() toRelativeDate  (DATE_REVIEW_FORMAT)
        setupFeedbackReview(data.reviewText.orEmpty())
        setImageAttachment(data, productReplyUiModel)
        setReplyView(data)
        showKejarUlasanLabel(data.isKejarUlasan)
        setBadRatingReason(data.badRatingReason)
        setBadRatingReasonDisclaimer(data.badRatingDisclaimer)
    }

    private fun setReplyView(data: FeedbackUiModel) {
        binding.tvReplyComment.text = data.replyText
        binding.tvReplyDate.text = data.replyTime.orEmpty() toRelativeDate  (DATE_REVIEW_FORMAT)
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
            binding.tvFeedbackReview.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_44))
        } else {
            binding.tvFeedbackReview.apply {
                setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_96))
                text = feedbackText
            }
        }
    }

    private fun setImageAttachment(element: FeedbackUiModel, productReply: ProductReplyUiModel) {
        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvItemAttachmentFeedback.apply {
            layoutManager = linearLayoutManager
            if (itemDecorationCount == 0) {
                addItemDecoration(PaddingItemDecoratingReview())
            }
            adapter = replyReviewFeedbackImageAdapter
        }
        if (element.attachments.isEmpty()) {
            binding.rvItemAttachmentFeedback.hide()
        } else {
            replyReviewFeedbackImageAdapter.setAttachmentUiData(element.attachments)
            replyReviewFeedbackImageAdapter.setFeedbackId(element.feedbackID)
            replyReviewFeedbackImageAdapter.setProductTitle(productReply.productName.toString())
            replyReviewFeedbackImageAdapter.submitList(element.attachments)
            binding.rvItemAttachmentFeedback.show()
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

    override fun onImageItemClicked(imageUrls: List<String>, thumbnailsUrl: List<String>,
                                    title: String, feedbackId: String, position: Int) {
        context.run {
            startActivity(ImagePreviewSliderActivity.getCallingIntent(
                    context = this,
                    title = title,
                    imageUrls = imageUrls,
                    imageThumbnailUrls = thumbnailsUrl,
                    imagePosition = position
            ))
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
}
