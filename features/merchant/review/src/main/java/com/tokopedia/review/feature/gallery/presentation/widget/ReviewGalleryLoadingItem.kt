package com.tokopedia.review.feature.gallery.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.review.R
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.LoaderUnify

class ReviewGalleryLoadingItem : BaseCustomView {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private var loading: LoaderUnify? = null
    private var errorImage: ImageUnify? = null

    private fun init() {
        View.inflate(context, R.layout.widget_review_gallery_loading_item, this)
        bindViews()
    }

    private fun bindViews() {
        loading = findViewById(R.id.review_gallery_image_shimmering)
        errorImage = findViewById(R.id.review_gallery_image_load_error)
    }

    fun showError() {
        loading?.hide()
        errorImage?.show()
    }

    fun showLoading() {
        loading?.show()
        errorImage?.hide()
    }
}