package com.tokopedia.reviewseller.feature.reviewreply.view.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.imagepreviewslider.presentation.activity.ImagePreviewSliderActivity
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.reviewseller.R
import com.tokopedia.reviewseller.common.util.PaddingItemDecoratingReviewSeller
import com.tokopedia.reviewseller.common.util.getReviewStar
import com.tokopedia.reviewseller.common.util.toRelativeDate
import com.tokopedia.reviewseller.feature.reviewdetail.view.model.FeedbackUiModel
import com.tokopedia.reviewseller.feature.reviewreply.view.adapter.ReviewReplyFeedbackImageAdapter
import com.tokopedia.reviewseller.feature.reviewreply.view.adapter.ReviewReplyListener
import com.tokopedia.reviewseller.feature.reviewreply.view.model.ProductReplyUiModel
import kotlinx.android.synthetic.main.widget_reply_feedback_item.view.*

class FeedbackItemReply : BaseCustomView, ReviewReplyListener {

    companion object {
        const val DATE_REVIEW_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'"
        private const val isAutoReply = false
    }

    private val replyReviewFeedbackImageAdapter by lazy {
        ReviewReplyFeedbackImageAdapter(this)
    }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        View.inflate(context, R.layout.widget_reply_feedback_item, this)
    }

    fun setData(data: FeedbackUiModel, productReplyUiModel: ProductReplyUiModel) {
        ivRatingFeedback.setImageResource(getReviewStar(data.rating.orZero()))
        tvFeedbackUser?.text = MethodChecker.fromHtml(context.getString(R.string.label_name_reviewer_builder, data.reviewerName.orEmpty()))
        tvFeedbackDate?.text = data.reviewTime.orEmpty() toRelativeDate  (DATE_REVIEW_FORMAT)
        setupFeedbackReview(data.reviewText.orEmpty())
        setImageAttachment(data, productReplyUiModel)
        setReplyView(data)
    }

    private fun setReplyView(data: FeedbackUiModel) {
        tvReplyComment?.text = data.replyText
        tvReplyDate?.text = data.replyTime.orEmpty() toRelativeDate  (DATE_REVIEW_FORMAT)
        if (data.autoReply == isAutoReply) {
            tvReplyUser?.text = context?.getString(R.string.user_reply)
        } else {
            tvReplyUser?.text = context?.getString(R.string.otomatis_reply)
        }
    }

    private fun setupFeedbackReview(feedbackText: String) {
        if (feedbackText.isEmpty()) {
            tvFeedbackReview?.text = context.getString(R.string.review_not_found)
            tvFeedbackReview?.setTextColor(ContextCompat.getColor(context, R.color.clr_review_not_found))
        } else {
            tvFeedbackReview?.apply {
                setTextColor(ContextCompat.getColor(context, R.color.clr_f531353b))
                text = feedbackText
            }
        }
    }

    private fun setImageAttachment(element: FeedbackUiModel, productReply: ProductReplyUiModel) {
        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rvItemAttachmentFeedback?.apply {
            layoutManager = linearLayoutManager
            if (itemDecorationCount == 0) {
                addItemDecoration(PaddingItemDecoratingReviewSeller())
            }
            adapter = replyReviewFeedbackImageAdapter
        }
        if (element.attachments.isEmpty()) {
            rvItemAttachmentFeedback?.hide()
        } else {
            replyReviewFeedbackImageAdapter.setAttachmentUiData(element.attachments)
            replyReviewFeedbackImageAdapter.setFeedbackId(element.feedbackID.toString())
            replyReviewFeedbackImageAdapter.setProductTitle(productReply.productName.toString())
            replyReviewFeedbackImageAdapter.submitList(element.attachments)
            rvItemAttachmentFeedback?.show()
        }
    }

    override fun onImageItemClicked(imageUrls: List<String>, thumbnailsUrl: List<String>,
                                    title: String, feedbackId: String, position: Int) {
        context?.run {
            startActivity(ImagePreviewSliderActivity.getCallingIntent(
                    context = this,
                    title = title,
                    imageUrls = imageUrls,
                    imageThumbnailUrls = thumbnailsUrl,
                    imagePosition = position
            ))
        }
    }
}
