package com.tokopedia.review.feature.reading.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.review.feature.reading.data.ProductReview
import com.tokopedia.review.feature.reading.data.ProductReviewAttachments
import com.tokopedia.review.feature.reading.presentation.adapter.ReadReviewAttachedImagesAdapter
import com.tokopedia.review.feature.reading.presentation.listener.ReadReviewAttachedImagesListener
import com.tokopedia.unifycomponents.BaseCustomView

class ReadReviewAttachedImages : BaseCustomView {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private var reviewAttachedImages: RecyclerView? = null

    private fun init() {
        View.inflate(context, com.tokopedia.review.inbox.R.layout.widget_review_attached_images, this)
        reviewAttachedImages = findViewById(com.tokopedia.review.inbox.R.id.reviewAttachedImages)
    }

    fun setImages(attachedImages: List<ProductReviewAttachments>, listener: ReadReviewAttachedImagesListener,
                  productReview: ProductReview, shopId: String, viewHolderPosition: Int) {
        val attachedImageAdapter = ReadReviewAttachedImagesAdapter(listener, viewHolderPosition)
        reviewAttachedImages?.apply {
            adapter = attachedImageAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            attachedImageAdapter.setData(attachedImages, productReview, shopId)
        }
    }

}