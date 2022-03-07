package com.tokopedia.review.common.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.review.common.data.ProductrevReviewAttachment
import com.tokopedia.review.common.presentation.adapter.ReviewAttachedImagesAdapter
import com.tokopedia.review.common.util.ReviewAttachedImagesClickListener
import com.tokopedia.review.feature.inbox.history.presentation.util.ReviewHistoryItemListener
import com.tokopedia.review.inbox.databinding.WidgetReviewAttachedImagesBinding
import com.tokopedia.unifycomponents.BaseCustomView

class ReviewAttachedImages : BaseCustomView {

    constructor(context: Context): super(context)
    constructor(context: Context, attrs: AttributeSet): super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr)

    private var binding = WidgetReviewAttachedImagesBinding.inflate(LayoutInflater.from(context), this, true)

    fun setImages(attachedImages: List<ProductrevReviewAttachment>,
                  productName: String,
                  reviewAttachedImagesClickListener: ReviewAttachedImagesClickListener,
                  reviewHistoryItemListener: ReviewHistoryItemListener? = null,
                  productId: String? = null,
                  feedbackId: String? = null) {
        val attachedImageAdapter = ReviewAttachedImagesAdapter(reviewAttachedImagesClickListener, productName, reviewHistoryItemListener, productId, feedbackId)
        binding.reviewAttachedImages.apply {
            adapter = attachedImageAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            attachedImageAdapter.setData(attachedImages)
        }
    }

}