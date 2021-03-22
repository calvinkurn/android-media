package com.tokopedia.home.analytics.v2

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.recommendation_widget_common.extension.hasLabelGroupFulfillment
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst

/**
 * Created by Lukas on 09/11/20.
 */
object BestSellerWidgetTracker : BaseTracking(){
    private const val IMPRESSION_ON_PRODUCT = "impression on product %s"
    private const val CLICK_ON_PRODUCT = "click on product %s"
    private const val BEST_SELLER = "dynamic channel best seller"
    private const val LIST_BEST_SELLER = "/ - p%s - dynamic channel best seller - product"
    private const val CLICK_FILTER_BEST_SELLER = "click chips filter on dynamic channel best seller"
    private const val CLICK_SEE_ALL_BEST_SELLER = "click view all on dynamic channel best seller"
    private const val CLICK_SEE_ALL_CARD_BEST_SELLER = "click view all card on dynamic channel best seller"

    fun getImpressionTracker(recommendationItem: RecommendationItem, channelId: String, headerName: String, pageName: String, userId: String, position: Int) =
        BaseTrackerBuilder()
                .constructBasicProductView(
                        Event.PRODUCT_VIEW,
                        Category.HOMEPAGE,
                        IMPRESSION_ON_PRODUCT.format(BEST_SELLER),
                        Label.NONE,
                        LIST_BEST_SELLER.format(position),
                        listOf(mapToProductTracking(recommendationItem, channelId, headerName, pageName))
                )
                .appendBusinessUnit(BusinessUnit.DEFAULT)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .appendChannelId(channelId)
                .build()

    fun sendClickTracker(recommendationItem: RecommendationItem, channelId: String, headerName: String, pageName: String, userId: String, position: Int) {
        val tracker = BaseTrackerBuilder()
                .constructBasicProductClick(
                        Event.PRODUCT_CLICK,
                        Category.HOMEPAGE,
                        CLICK_ON_PRODUCT.format(BEST_SELLER),
                        "$channelId - $headerName",
                        LIST_BEST_SELLER.format(position),
                        listOf(mapToProductTracking(recommendationItem, channelId, headerName, pageName))
                )
                .appendBusinessUnit(BusinessUnit.DEFAULT)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .appendChannelId(channelId)
                .build()
        getTracker().sendEnhanceEcommerceEvent(tracker)
    }

    fun sendFilterClickTracker(categoryId: String, channelId: String, headerName: String, userId: String) {
        val tracker = DataLayer.mapOf(
                Event.KEY, Event.CLICK_HOMEPAGE,
                Category.KEY, Category.HOMEPAGE,
                Action.KEY, CLICK_FILTER_BEST_SELLER,
                Label.KEY, "$channelId - $categoryId - $headerName",
                BusinessUnit.KEY, BusinessUnit.DEFAULT,
                CurrentSite.KEY, CurrentSite.DEFAULT,
                ChannelId.KEY, channelId,
                UserId.KEY, userId
        )
        getTracker().sendGeneralEvent(tracker)
    }

    fun sendViewAllClickTracker(channelId: String, headerName: String, userId: String) {
        val tracker = DataLayer.mapOf(
                Event.KEY, Event.CLICK_HOMEPAGE,
                Category.KEY, Category.HOMEPAGE,
                Action.KEY, CLICK_SEE_ALL_BEST_SELLER,
                Label.KEY, "$channelId - $headerName",
                BusinessUnit.KEY, BusinessUnit.DEFAULT,
                CurrentSite.KEY, CurrentSite.DEFAULT,
                ChannelId.KEY, channelId,
                UserId.KEY, userId
        )
        getTracker().sendEnhanceEcommerceEvent(tracker)
    }

    fun sendViewAllCardClickTracker(channelId: String, headerName: String, userId: String) {
        val tracker = DataLayer.mapOf(
                Event.KEY, Event.CLICK_HOMEPAGE,
                Category.KEY, Category.HOMEPAGE,
                Action.KEY, CLICK_SEE_ALL_CARD_BEST_SELLER,
                Label.KEY, "$channelId - $headerName",
                BusinessUnit.KEY, BusinessUnit.DEFAULT,
                CurrentSite.KEY, CurrentSite.DEFAULT,
                ChannelId.KEY, channelId,
                UserId.KEY, userId
        )
        getTracker().sendGeneralEvent(tracker)
    }

    private fun mapToProductTracking(recommendationItem: RecommendationItem, channelId: String, headerName: String, pageName: String): BaseTrackerConst.Product{
        return BaseTrackerConst.Product(
                id = recommendationItem.productId.toString(),
                name = recommendationItem.name,
                isTopAds = recommendationItem.isTopAds,
                recommendationType = recommendationItem.recommendationType,
                headerName = headerName,
                isCarousel = true,
                productPrice = recommendationItem.priceInt.toString(),
                productPosition = recommendationItem.position.toString(),
                isFreeOngkir = recommendationItem.isFreeOngkirActive && !recommendationItem.labelGroupList.hasLabelGroupFulfillment(),
                isFreeOngkirExtra = recommendationItem.isFreeOngkirActive && recommendationItem.labelGroupList.hasLabelGroupFulfillment(),
                pageName = pageName,
                cartId = recommendationItem.cartId,
                channelId = channelId,
                category = recommendationItem.categoryBreadcrumbs,
                variant = "",
                brand = ""
        )

    }
}