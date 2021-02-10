package com.tokopedia.recommendation_widget_common.presentation

/**
 * Created by devara fikry on 16/04/19.
 */

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.productcard.ProductCardView
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.topads.sdk.R
import com.tokopedia.topads.sdk.utils.ImpresionTask

@Deprecated("RecommendationCardView replaced with ProductCardView v2")
open class RecommendationCardView : ProductCardView {

    companion object {
        private const val className: String = "com.tokopedia.recommendation_widget_common.presentation.RecommendationCardView"
    }

    protected var ratingContainer: LinearLayout? = null

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    override fun init(attrs: AttributeSet?) {
        super.init(attrs)

        ratingContainer = findViewById(R.id.rating_review_container)
    }

    fun setRecommendationModel(item: RecommendationItem, trackingListener: TrackingListener) {
        setTitle(item.name)
        setPrice(item.price)
        setImageUrl(item.imageUrl)
        setTopAdsVisible(item.isTopAds)
        setSlashedPrice(item.slashedPrice)
        setDiscount(item.discountPercentageInt)
        setWishlistButtonVisible(false)
        setRatingReviewCount(item.rating, item.countReview)
        setBadges(item.badgesUrl)
        setLocation(item.location)
        imageView.addOnImpressionListener(item,
                object: ViewHintListener {
                    override fun onViewHint() {
                        if(item.isTopAds){
                            ImpresionTask(className).execute(item.trackerImageUrl)
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
                ImpresionTask(className).execute(item.clickUrl)
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
            ratingContainer?.visibility = View.VISIBLE
        }
    }

    open fun setBadges(urls: List<String?>) {
        badgesContainerView.removeAllViews()
        if (urls.isEmpty()) badgesContainerView.visibility = View.GONE
        for (url in urls) {
            val view = LayoutInflater.from(context).inflate(com.tokopedia.topads.sdk.R.layout.layout_badge, null)
            ImageHandler.loadImageFitCenter(context, view.findViewById(R.id.badge), url)
            badgesContainerView.addView(view)
        }
    }

    interface TrackingListener {
        fun onImpressionTopAds(item: RecommendationItem)
        fun onImpressionOrganic(item: RecommendationItem)
        fun onClickTopAds(item: RecommendationItem)
        fun onClickOrganic(item: RecommendationItem)
    }
}
