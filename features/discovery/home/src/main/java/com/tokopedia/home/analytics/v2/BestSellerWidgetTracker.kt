package com.tokopedia.home.analytics.v2

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.recommendation_widget_common.extension.hasLabelGroupFulfillment
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.widget.bestseller.model.BestSellerDataModel
import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst

/**
 * Created by Lukas on 09/11/20.
 */
object BestSellerWidgetTracker : BaseTracking(){
    private const val IMPRESSION_ON_PRODUCT = "impression on product %s"
    private const val CLICK_ON_PRODUCT = "click on product %s"
    private const val BEST_SELLER = "dynamic channel best seller"

    //list:"/ - p{x} - dynamic channel best seller - product - {topads/non topads} - {carousel/non carousel} - {recommendation_type} - {recomm_page_name}|{chips_filter_group}|{recomm_model_source} - {header name}"
    private const val LIST_BEST_SELLER = "/ - p%s - dynamic channel best seller - product - %s - %s - %s - %s|%s|%s - %s"
    private const val CLICK_FILTER_BEST_SELLER = "click chips filter on dynamic channel best seller"
    private const val CLICK_SEE_ALL_BEST_SELLER = "click view all on dynamic channel best seller"
    private const val CLICK_SEE_ALL_CARD_BEST_SELLER = "click view all card on dynamic channel best seller"
    fun getImpressionTracker(recommendationItem: RecommendationItem, bestSellerDataModel: BestSellerDataModel, userId: String, position: Int) =
        BaseTrackerBuilder()
                .constructBasicProductView(
                        event = Event.PRODUCT_VIEW,
                        eventCategory = Category.HOMEPAGE,
                        eventAction = IMPRESSION_ON_PRODUCT.format(BEST_SELLER),
                        eventLabel = Label.NONE,
                        list = "",
                        buildCustomList = buildCustomListBestSeller(position, bestSellerDataModel),
                        products = listOf(mapToProductTracking(recommendationItem, bestSellerDataModel.id, bestSellerDataModel.title, bestSellerDataModel.pageName))
                )
                .appendBusinessUnit(BusinessUnit.DEFAULT)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .appendChannelId(bestSellerDataModel.id)
                .build()

    private fun buildCustomListBestSeller(position: Int, bestSellerDataModel: BestSellerDataModel): (BaseTrackerConst.Product) -> String {
        return {
            String.format(
                    LIST_BEST_SELLER,
                    position,
                    if (it.isTopAds == true) "topads" else "non topads",
                    "carousel",
                    it.recommendationType,
                    it.pageName,
                    bestSellerDataModel.chipsPosition,
                    "",
                    it.headerName
            )
        }
    }

    fun sendClickTracker(recommendationItem: RecommendationItem, bestSellerDataModel: BestSellerDataModel, userId: String, position: Int) {
        val tracker = BaseTrackerBuilder()
                .constructBasicProductClick(
                        event = Event.PRODUCT_CLICK,
                        eventCategory = Category.HOMEPAGE,
                        eventAction = CLICK_ON_PRODUCT.format(BEST_SELLER),
                        eventLabel = "${bestSellerDataModel.id} - ${bestSellerDataModel.title}",
                        list = "",
                        products = listOf(mapToProductTracking(recommendationItem, bestSellerDataModel.id, bestSellerDataModel.title, bestSellerDataModel.pageName)),
                        buildCustomList = buildCustomListBestSeller(position, bestSellerDataModel)
                )
                .appendBusinessUnit(BusinessUnit.DEFAULT)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .appendChannelId(bestSellerDataModel.id)
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