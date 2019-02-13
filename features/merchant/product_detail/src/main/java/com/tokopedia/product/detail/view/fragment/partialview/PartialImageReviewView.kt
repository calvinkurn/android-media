package com.tokopedia.product.detail.view.fragment.partialview

import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.tokopedia.gallery.viewmodel.ImageReviewItem
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.product.detail.view.adapter.ImageReviewAdapter
import kotlinx.android.synthetic.main.partial_product_image_review.view.*

class PartialImageReviewView private constructor(private val view: View) {
    companion object {
        fun build(_view: View) = PartialImageReviewView(_view)
    }

    init {
        with(view){
            image_review_list.layoutManager = GridLayoutManager(context, 4)
        }
    }

    fun renderData(imageReviews: List<ImageReviewItem>){
        view.image_review_list.adapter = ImageReviewAdapter(imageReviews.toMutableList())
        if (imageReviews.isNotEmpty())
            view.base_image_review.visible()
        else
            view.base_image_review.gone()
    }
}