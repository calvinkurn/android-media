package com.tokopedia.product.detail.view.fragment.partialview

import android.app.Activity
import android.view.View
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.data.model.ProductInfo
import com.tokopedia.product.detail.common.data.model.Rating
import com.tokopedia.product.detail.data.model.shop.ShopShipment
import com.tokopedia.product.detail.view.activity.CourierActivity
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

    fun renderRating(rating: Rating) {
        view.tv_rating.text = rating.ratingScore
    }

    fun renderClickShipment(activity: Activity, productId: String, shipment: List<ShopShipment>) {
        with(view){
            icon_courier.setOnClickListener {
                context.startActivity(CourierActivity.createIntent(context, productId, shipment))
                activity.overridePendingTransition(0,0)
            }
            txt_courier.setOnClickListener {
                context.startActivity(CourierActivity.createIntent(context, productId, shipment))
                activity.overridePendingTransition(0,0)
            }
        }
    }
}