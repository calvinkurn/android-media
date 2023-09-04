package com.tokopedia.home.analytics.v2

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.home.analytics.HomePageTracking.ECOMMERCE
import com.tokopedia.home.analytics.HomePageTracking.PROMOTIONS
import com.tokopedia.home.analytics.v2.BaseTracking.Event.PROMO_CLICK
import com.tokopedia.home.analytics.v2.BaseTracking.Event.PROMO_VIEW
import com.tokopedia.home_component.visitable.BestSellerProductDataModel
import com.tokopedia.recommendation_widget_common.extension.hasLabelGroupFulfillment
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.widget.bestseller.model.BestSellerDataModel
import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst
import com.tokopedia.home_component.visitable.BestSellerDataModel as BestSellerRevampDataModel
import com.tokopedia.track.constant.TrackerConstant.TRACKER_ID

/**
 * Created by Lukas on 09/11/20.
 */
object BestSellerWidgetTracker : BaseTracking(){
    private const val IMPRESSION_ON_PRODUCT = "impression on product %s"
    private const val CLICK_ON_PRODUCT = "click on product %s"
    private const val BEST_SELLER = "dynamic channel best seller"
    private const val TOPADS = "topads"
    private const val NONTOPADS = "non topads"

    private const val CAROUSEL = "carousel"

    //list:"/ - p{x} - dynamic channel best seller - product - {topads/non topads} - {carousel/non carousel} - {recommendation_type} - {recomm_page_name}|{chips_filter_group}|{recomm_model_source} - {header name}"
    private const val LIST_BEST_SELLER = "/ - p%s - dynamic channel best seller - product - %s - %s - %s - %s|%s|%s - %s"
    private const val IMPRESSION_FILTER_BEST_SELLER = "impression chips filter on dynamic channel best seller"
    private const val CLICK_FILTER_BEST_SELLER = "click chips filter on dynamic channel best seller"
    private const val CLICK_SEE_ALL_BEST_SELLER = "click view all on dynamic channel best seller"
    private const val CLICK_SEE_ALL_CARD_BEST_SELLER = "click view all card on dynamic channel best seller"

    private const val TRACKER_ID_CHIPS_FILTER_IMPRESSION = "43523"

    fun getImpressionTracker(recommendationItem: RecommendationItem, bestSellerDataModel: BestSellerDataModel, userId: String, position: Int) =
        BaseTrackerBuilder()
                .constructBasicProductView(
                        event = Event.PRODUCT_VIEW,
                        eventCategory = Category.HOMEPAGE,
                        eventAction = IMPRESSION_ON_PRODUCT.format(BEST_SELLER),
                        eventLabel = Label.NONE,
                        list = getCustomListBestSellerString(recommendationItem, position, bestSellerDataModel),
                        buildCustomList = buildCustomListBestSeller(position, bestSellerDataModel),
                        products = listOf(mapToProductTracking(recommendationItem, bestSellerDataModel.id, bestSellerDataModel.title, bestSellerDataModel.pageName))
                )
                .appendBusinessUnit(BusinessUnit.DEFAULT)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .appendChannelId(bestSellerDataModel.id)
                .build()

    fun getImpressionTracker(
        bestSellerProductDataModel: BestSellerProductDataModel,
        bestSellerDataModel: BestSellerRevampDataModel,
        userId: String,
        position: Int
    ) = BaseTrackerBuilder()
            .constructBasicProductView(
                event = Event.PRODUCT_VIEW,
                eventCategory = Category.HOMEPAGE,
                eventAction = IMPRESSION_ON_PRODUCT.format(BEST_SELLER),
                eventLabel = Label.NONE,
                list = getCustomListBestSellerString(
                    bestSellerProductDataModel,
                    position,
                    bestSellerDataModel,
                ),
                buildCustomList = buildCustomListBestSeller(position, bestSellerDataModel),
                products = listOf(
                    mapToProductTracking(
                        bestSellerProductDataModel,
                        bestSellerDataModel.id,
                        bestSellerDataModel.title,
                        bestSellerDataModel.pageName,
                    )
                )
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
                    position + 1,
                    if (it.isTopAds == true) TOPADS else NONTOPADS,
                    CAROUSEL,
                    it.recommendationType,
                    it.pageName,
                    bestSellerDataModel.chipsPosition,
                    "",
                    it.headerName
            )
        }
    }

    private fun buildCustomListBestSeller(
        position: Int,
        bestSellerDataModel: BestSellerRevampDataModel
    ): (BaseTrackerConst.Product) -> String {
        return {
            String.format(
                LIST_BEST_SELLER,
                position + 1,
                if (it.isTopAds == true) TOPADS else NONTOPADS,
                CAROUSEL,
                it.recommendationType,
                it.pageName,
                bestSellerDataModel.activeChipPosition,
                "",
                it.headerName
            )
        }
    }

    private fun getCustomListBestSellerString(recommendationItem: RecommendationItem, position: Int, bestSellerDataModel: BestSellerDataModel): String {
        return String.format(
            LIST_BEST_SELLER,
            position + 1,
            if (recommendationItem.isTopAds) TOPADS else NONTOPADS,
            CAROUSEL,
            recommendationItem.recommendationType,
            recommendationItem.pageName,
            bestSellerDataModel.chipsPosition,
            "",
            recommendationItem.header
        )
    }

    private fun getCustomListBestSellerString(
        bestSellerProductDataModel: BestSellerProductDataModel,
        position: Int,
        bestSellerDataModel: BestSellerRevampDataModel,
    ): String {
        return String.format(
            LIST_BEST_SELLER,
            position + 1,
            if (bestSellerProductDataModel.isTopAds) TOPADS else NONTOPADS,
            CAROUSEL,
            bestSellerProductDataModel.recommendationType,
            bestSellerProductDataModel.pageName,
            bestSellerDataModel.activeChipPosition,
            "",
            bestSellerProductDataModel.header
        )
    }

    fun sendClickTracker(recommendationItem: RecommendationItem, bestSellerDataModel: BestSellerDataModel, userId: String, position: Int) {
        val bestSellerId = bestSellerDataModel.id
        val bestSellerTitle = bestSellerDataModel.title
        val productTrackingMap = mapToProductTracking(
            recommendationItem,
            bestSellerId,
            bestSellerTitle,
            bestSellerDataModel.pageName
        )
        val listName = getCustomListBestSellerString(recommendationItem, position, bestSellerDataModel)
        val customListBestSeller = buildCustomListBestSeller(position, bestSellerDataModel)

        sendClickTracker(
            bestSellerId,
            bestSellerTitle,
            listName,
            productTrackingMap,
            customListBestSeller,
            userId
        )
    }

    fun sendClickTracker(
        bestSellerProductDataModel: BestSellerProductDataModel,
        bestSellerDataModel: BestSellerRevampDataModel,
        userId: String,
        position: Int,
    ) {
        val bestSellerId = bestSellerDataModel.id
        val bestSellerTitle = bestSellerDataModel.title
        val productTrackingMap = mapToProductTracking(
            bestSellerProductDataModel,
            bestSellerId,
            bestSellerTitle,
            bestSellerDataModel.pageName
        )
        val listName = getCustomListBestSellerString(bestSellerProductDataModel, position, bestSellerDataModel)
        val customListBestSeller = buildCustomListBestSeller(position, bestSellerDataModel)

        sendClickTracker(
            bestSellerId,
            bestSellerTitle,
            listName,
            productTrackingMap,
            customListBestSeller,
            userId
        )
    }

    private fun sendClickTracker(
        bestSellerId: String,
        bestSellerTitle: String,
        listName: String,
        productTrackingMap: BaseTrackerConst.Product,
        customListBestSeller: (BaseTrackerConst.Product) -> String,
        userId: String
    ) {
        val tracker = BaseTrackerBuilder()
            .constructBasicProductClick(
                event = Event.PRODUCT_CLICK,
                eventCategory = Category.HOMEPAGE,
                eventAction = CLICK_ON_PRODUCT.format(BEST_SELLER),
                eventLabel = "$bestSellerId - $bestSellerTitle",
                list = listName,
                products = listOf(productTrackingMap),
                buildCustomList = customListBestSeller
            )
            .appendBusinessUnit(BusinessUnit.DEFAULT)
            .appendCurrentSite(CurrentSite.DEFAULT)
            .appendUserId(userId)
            .appendChannelId(bestSellerId)
            .build()
        getTracker().sendEnhanceEcommerceEvent(tracker)
    }

    fun getFilterImpressionTracker(
        categoryId: String,
        channelId: String,
        headerName: String,
        userId: String,
        position: Int,
        ncpRank: Int,
        totalFilterCount: Int,
        chipsValue: String,
    ): Map<String, Any> {
        return DataLayer.mapOf(
            Event.KEY, Event.PROMO_VIEW,
            Category.KEY, Category.HOMEPAGE,
            Action.KEY, IMPRESSION_FILTER_BEST_SELLER,
            Label.KEY, "$channelId - null - $headerName - null - null - $totalFilterCount - null",
            BusinessUnit.KEY, BusinessUnit.DEFAULT,
            CurrentSite.KEY, CurrentSite.DEFAULT,
            UserId.KEY, userId,
            TRACKER_ID, TRACKER_ID_CHIPS_FILTER_IMPRESSION,
            ECOMMERCE, DataLayer.mapOf(
                PROMO_VIEW, DataLayer.mapOf(PROMOTIONS, DataLayer.listOf(
                    filterChipsAsObjectDataLayer(
                        listOf(
                            channelId,
                            categoryId,
                            headerName,
                            position,
                            ncpRank,
                            totalFilterCount,
                            chipsValue
                        ).joinToString(" - ")
                    )
                ))
            )
        )
    }

    private fun filterChipsAsObjectDataLayer(creativeName: String): Any =
        DataLayer.mapOf(
            "creative_name", creativeName,
            "id", "null",
            "name", "null",
            "creative_slot", "null"
        )

    fun sendFilterClickTracker(
        categoryId: String,
        channelId: String,
        headerName: String,
        position: Int,
        ncpRank: Int,
        chipsValue: String,
    ) {
        val creativeName = listOf(
            channelId,
            categoryId,
            headerName,
            position,
            ncpRank,
            chipsValue,
        ).joinToString(" - ")

        getTracker().sendEnhanceEcommerceEvent(
            DataLayer.mapOf(
                Event.KEY, PROMO_CLICK,
                Category.KEY, Category.HOMEPAGE,
                Action.KEY, CLICK_FILTER_BEST_SELLER,
                Label.KEY, creativeName,
                ECOMMERCE, DataLayer.mapOf(
                    PROMO_CLICK, DataLayer.mapOf(
                        PROMOTIONS, DataLayer.listOf(filterChipsAsObjectDataLayer(creativeName))
                    )
                )
            )
        )
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
                brand = "",
                warehouseId = recommendationItem.warehouseId.toString(),
                isFulfillment = recommendationItem.labelGroupList.hasLabelGroupFulfillment()
        )

    }

    private fun mapToProductTracking(
        bestSellerProductDataModel: BestSellerProductDataModel,
        channelId: String,
        headerName: String,
        pageName: String
    ): BaseTrackerConst.Product{
        return BaseTrackerConst.Product(
            id = bestSellerProductDataModel.productId,
            name = bestSellerProductDataModel.name,
            isTopAds = bestSellerProductDataModel.isTopAds,
            recommendationType = bestSellerProductDataModel.recommendationType,
            headerName = headerName,
            isCarousel = true,
            productPrice = bestSellerProductDataModel.price.toString(),
            productPosition = bestSellerProductDataModel.position.toString(),
            isFreeOngkir = bestSellerProductDataModel.isFreeOngkirActive && !bestSellerProductDataModel.hasLabelGroupFulfillment(),
            isFreeOngkirExtra = bestSellerProductDataModel.isFreeOngkirActive && bestSellerProductDataModel.hasLabelGroupFulfillment(),
            pageName = pageName,
            cartId = bestSellerProductDataModel.cartId,
            channelId = channelId,
            category = bestSellerProductDataModel.categoryBreadcrumbs,
            variant = "",
            brand = "",
            warehouseId = bestSellerProductDataModel.warehouseId,
            isFulfillment = bestSellerProductDataModel.productCardModel.getLabelFulfillment() != null
        )
    }
}
