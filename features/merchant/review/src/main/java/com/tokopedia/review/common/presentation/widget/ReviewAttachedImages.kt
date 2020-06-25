package com.tokopedia.review.common.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.review.R
import com.tokopedia.review.common.presentation.adapter.ReviewAttachedImagesAdapter
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

    fun setImages(attachedImages: List<String>) {
        val attachedImageAdapter = ReviewAttachedImagesAdapter()
        this.reviewAttachedImages.apply {
            adapter = attachedImageAdapter
            layoutManager = LinearLayoutManager(context)
            attachedImageAdapter.setData(attachedImages)
            show()
        }
    }

}