package com.tokopedia.recommendation_widget_common.presentation

/**
 * Created by devara fikry on 16/04/19.
 */

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.productcard.ProductCardView
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem

class RecommendationCardView : ProductCardView {
    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    fun setRecommendationModel(item: RecommendationItem, trackingListener: TrackingListener) {
        setTitle(item.name)
        setPrice(item.price)
        setImageUrl(item.imageUrl)
        setTopAdsVisible(item.isTopAds)
        setWishlistButtonVisible(TextUtils.isEmpty(item.wishlistUrl))
        setRatingReviewCount(item.rating, item.countReview)

        imageView.setViewHintListener(
                item
        ) {
            //Impression Here
            trackingListener.onImpression(item)
        }

        setOnClickListener {
            //Click here
            trackingListener.onClick(item)
            RouteManager.route(context!!, ApplinkConstInternalMarketplace.PRODUCT_DETAIL, item.productId.toString())
        }
    }

    interface TrackingListener {
        fun onImpression(item: RecommendationItem)
        fun onClick(item: RecommendationItem)
    }
}
