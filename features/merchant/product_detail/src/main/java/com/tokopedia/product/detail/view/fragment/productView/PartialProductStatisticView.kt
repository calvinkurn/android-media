package com.tokopedia.product.detail.view.fragment.productView

import android.view.View
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.product.ProductInfo
import kotlinx.android.synthetic.main.partial_product_rating_talk_courier.view.*

class PartialProductStatisticView private constructor(private val view: View) {
    companion object {
        fun build(_view: View) = PartialProductStatisticView(_view)
    }

    fun renderData(data: ProductInfo, onReviewClicked: (()->Unit)? = null,
                   onDiscussionClicked: (()-> Unit)? = null){
        with(view){
            txt_review.text = context.getString(R.string.template_review, data.stats.countReview)
            base_layout_rating.setOnClickListener { onReviewClicked?.invoke() }
            txt_review.setOnClickListener { onReviewClicked?.invoke() }
            txt_discussion.text = context.getString(R.string.template_talk, data.stats.countTalk)
            txt_discussion.setOnClickListener { onDiscussionClicked?.invoke() }
            icon_discussion.setOnClickListener { onDiscussionClicked?.invoke() }
        }
    }
}