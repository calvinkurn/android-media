package com.tokopedia.product.detail.view.fragment.partialview

import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.tokopedia.gallery.viewmodel.ImageReviewItem
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.product.detail.view.adapter.ImageReviewAdapter
import kotlinx.android.synthetic.main.partial_product_image_review.view.*

class PartialImageReviewView private constructor(private val view: View,
                                                 private val onImageClick: ((ImageReviewItem, Boolean) -> Unit)? = null) {
    companion object {
        fun build(_view: View, _onImageClick: ((ImageReviewItem, Boolean) -> Unit)?) =
                PartialImageReviewView(_view, _onImageClick)
    }

    init {
        with(view){
            image_review_list.layoutManager = GridLayoutManager(context, 4)
        }
    }

    fun renderData(imageReviews: List<ImageReviewItem>){
        view.image_review_list.adapter = ImageReviewAdapter(imageReviews.toMutableList(), true, onImageClick)
        with(view) {
            if (imageReviews.isNotEmpty())
                visible()
            else
                gone()
        }
    }
}