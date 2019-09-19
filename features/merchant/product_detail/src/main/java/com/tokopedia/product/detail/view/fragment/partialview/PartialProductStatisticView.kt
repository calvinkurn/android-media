package com.tokopedia.product.detail.view.fragment.partialview

import android.app.Activity
import android.view.View
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.data.model.product.ProductInfo
import com.tokopedia.product.detail.common.data.model.product.Rating
import kotlinx.android.synthetic.main.partial_product_rating_talk_courier.view.*

class PartialProductStatisticView private constructor(private val view: View) {
    companion object {
        fun build(_view: View) = PartialProductStatisticView(_view)
    }

    fun renderData(data: ProductInfo, onReviewClicked: (() -> Unit)? = null,
                   onDiscussionClicked: (() -> Unit)? = null) {
        with(view) {
            txt_review.text = context.getString(R.string.template_review, data.stats.countReview)
            base_layout_rating.setOnClickListener { onReviewClicked?.invoke() }
            txt_review.setOnClickListener { onReviewClicked?.invoke() }
            txt_discussion.text = context.getString(R.string.template_talk, data.stats.countTalk)
            txt_discussion.setOnClickListener { onDiscussionClicked?.invoke() }
            icon_discussion.setOnClickListener { onDiscussionClicked?.invoke() }
            visible()
        }
    }

    fun renderRating(rating: Rating) {
        with(view) {
            tv_rating.text = rating.ratingScore
            visible()
        }
    }

    fun renderClickShipping(activity: Activity, onShipmentClicked: (() -> Unit)? = null){
        with(view){
            icon_courier.setOnClickListener {
                onShipmentClicked?.invoke()
                activity.overridePendingTransition(0, 0)
            }
            txt_courier.setOnClickListener {
                onShipmentClicked?.invoke()
                activity.overridePendingTransition(0, 0)
            }
        }
    }
}