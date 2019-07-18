package com.tokopedia.recommendation_widget_common.presentation

/**
 * Created by devara fikry on 16/04/19.
 */

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.productcard.ProductCardView
import com.tokopedia.recommendation_widget_common.R
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.topads.sdk.utils.ImpresionTask

class RecommendationCardView : ProductCardView {

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    fun setRecommendationModel(item: RecommendationItem, trackingListener: TrackingListener) {
        setTitle(item.name)
        setPrice(item.price)
        setImageUrl(item.imageUrl)
        setTopAdsVisible(item.isTopAds)
        setSlashedPrice(item.slashedPrice)
        setDiscount(item.discountPercentage)
        setWishlistButtonVisible(TextUtils.isEmpty(item.wishlistUrl))
        setWishlistButtonVisible(false)
        setRatingReviewCount(item.rating, item.countReview)
        setBadges(item.badgesUrl)
        setLocation(item.location)
        imageView.addOnImpressionListener(item,
                object: ViewHintListener {
                    override fun onViewHint() {
                        if(item.isTopAds){
                            ImpresionTask().execute(item.trackerImageUrl)
                            //Impression for topads item
                            trackingListener.onImpressionTopAds(item)
                        } else {
                            //Impression for organic item
                            trackingListener.onImpressionOrganic(item)
                        }
                    }
                }
        )

        setOnClickListener {
            if (item.isTopAds) {
                ImpresionTask().execute(item.clickUrl)
                //Click for topads item
                trackingListener.onClickTopAds(item)
            } else {
                //Click for organic item
                trackingListener.onClickOrganic(item)
            }
            context?.run {
                RouteManager.route(context, ApplinkConstInternalMarketplace.PRODUCT_DETAIL, item.productId.toString())
            }
        }
    }

    override fun setRatingReviewCount(rating: Int, reviewCount: Int) {
        if (rating in 1..5) {
            setRatingVisible()
            ratingView.setImageResource(getRatingDrawable(rating))
            reviewCountView.text = "($reviewCount)"
        } else {
            if (fixedHeight) {
                ratingView.visibility = View.INVISIBLE
                reviewCountView.visibility = View.INVISIBLE
            } else {
                ratingView.visibility = View.GONE
                reviewCountView.visibility = View.GONE
            }
        }
    }

    private fun setRatingVisible(){
        ratingView.visibility = View.VISIBLE
        reviewCountView.visibility = View.VISIBLE
        if (ratingContainer != null) {
            ratingContainer.visibility = View.VISIBLE
        }
    }

    interface TrackingListener {
        fun onImpressionTopAds(item: RecommendationItem)
        fun onImpressionOrganic(item: RecommendationItem)
        fun onClickTopAds(item: RecommendationItem)
        fun onClickOrganic(item: RecommendationItem)
        fun onWishlistClick(item: RecommendationItem, isAddWishlist: Boolean, callback: ((Boolean, Throwable?) -> Unit))
    }
}
