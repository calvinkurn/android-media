package com.tokopedia.review.feature.gallery.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.review.R
import com.tokopedia.unifycomponents.BaseCustomView

class ReviewGalleryLoadingView: BaseCustomView {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private var loadingItems: MutableList<ReviewGalleryLoadingItem?> = mutableListOf()

    private fun init() {
        View.inflate(context, R.layout.widget_review_gallery_loading_view, this)
        bindViews()
    }

    private fun bindViews() {
        loadingItems.addAll(
            listOf(
                findViewById(R.id.review_gallery_item_loading),
                findViewById(R.id.review_gallery_item_loading_2),
                findViewById(R.id.review_gallery_item_loading_3),
                findViewById(R.id.review_gallery_item_loading_4)
            )
        )
    }

    fun showError() {
        loadingItems.forEach {
            it?.showError()
        }
    }

    fun showLoading() {
        loadingItems.forEach {
            it?.showLoading()
        }
    }
}