package com.tokopedia.review.common.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.review.common.data.ProductrevReviewAttachment
import com.tokopedia.review.common.presentation.adapter.ReviewAttachedImagesAdapter
import com.tokopedia.review.common.util.ReviewAttachedImagesClickListener
import com.tokopedia.review.feature.inbox.history.presentation.util.ReviewHistoryItemListener
import com.tokopedia.review.inbox.R
import com.tokopedia.unifycomponents.BaseCustomView
import kotlinx.android.synthetic.main.widget_review_attached_images.view.*

class ReviewAttachedImages : BaseCustomView {

    constructor(context: Context): super(context) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet): super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        View.inflate(context, R.layout.widget_review_attached_images, this)
    }

    fun setImages(attachedImages: List<ProductrevReviewAttachment>,
                  productName: String,
                  reviewAttachedImagesClickListener: ReviewAttachedImagesClickListener,
                  reviewHistoryItemListener: ReviewHistoryItemListener? = null,
                  productId: String? = null,
                  feedbackId: String? = null) {
        val attachedImageAdapter = ReviewAttachedImagesAdapter(reviewAttachedImagesClickListener, productName, reviewHistoryItemListener, productId, feedbackId)
        this.reviewAttachedImages.apply {
            adapter = attachedImageAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            attachedImageAdapter.setData(attachedImages)
        }
    }

}