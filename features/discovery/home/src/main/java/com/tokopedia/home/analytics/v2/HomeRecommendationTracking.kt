package com.tokopedia.home.analytics.v2

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.home.analytics.v2.HomeRecommendationTracking.CustomAction.BANNER_ADS_INSIDE_RECOMMENDATION
import com.tokopedia.home.analytics.v2.HomeRecommendationTracking.CustomAction.BANNER_INSIDE_RECOMMENDATION
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.BannerRecommendationDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationBannerTopAdsDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationItemDataModel
import com.tokopedia.recommendation_widget_common.extension.LABEL_FULFILLMENT
import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst

object HomeRecommendationTracking : BaseTrackerConst(){

    private object CustomEvent{
        const val CLICK_HOMEPAGE = "clickHomepage"
    }

    private object CustomAction{
        val RECOMMENDATION_VIEW_BASE = Action.IMPRESSION.format("product recommendation")
        val RECOMMENDATION_CLICK_BASE = Action.CLICK.format("product recommendation")
        const val RECOMMENDATION_ADD_WISHLIST_LOGIN = "add wishlist - product recommendation - login"
        const val RECOMMENDATION_REMOVE_WISHLIST_LOGIN = "remove wishlist - product recommendation - login"
        const val RECOMMENDATION_ADD_WISHLIST_NON_LOGIN = "add wishlist - product recommendation - non login"
        const val BANNER_INSIDE_RECOMMENDATION = "banner inside recommendation tab"
        const val BANNER_ADS_INSIDE_RECOMMENDATION = "banner inside recommendation tab ads"
        const val BANNER_FIELD = "/ - banner inside recom tab - %s - "
        const val BANNER_ADS_FIELD = "/ - p%s - banner inside recomm tab ads"
        private const val LABEL_FULFILLMENT = "fulfillment"
    }

    private object ActionField{
        // note for https://tokopedia.atlassian.net/browse/AN-20317
        //  '/ - p2{ - non login} - {homepage recommendation tab} - rekomendasi untuk anda - {recommendation_type} - {recomm_page_name}{ - product topads}'

        private const val BASE = "/ - p2 - %s%s - rekomendasi untuk anda - %s - %s"
        val RECOMMENDATION_ACTION_FIELD_NON_LOGIN = BASE.format("non login", " - %s", "%s", "%s")
        val RECOMMENDATION_ACTION_FIELD_NON_LOGIN_TOP_ADS = BASE.format("non login", " - %s", "%s", "%s - product topads")
        val RECOMMENDATION_ACTION_FIELD_LOGIN = BASE.format("", "%s", "%s", "%s")
        val RECOMMENDATION_ACTION_FIELD_LOGIN_TOP_ADS = BASE.format("", "%s", "%s", "%s - product topads")
    }

    fun getRecommendationProductClickNonLogin(tabName: String, homeRecommendationItemDataModel: HomeRecommendationItemDataModel)
            = BaseTrackerBuilder().constructBasicProductClick(
            event = Event.PRODUCT_CLICK,
            eventCategory = Category.HOMEPAGE,
            eventAction = CustomAction.RECOMMENDATION_CLICK_BASE,
            eventLabel = tabName,
            list = ActionField.RECOMMENDATION_ACTION_FIELD_NON_LOGIN.format(tabName, homeRecommendationItemDataModel.product.recommendationType, homeRecommendationItemDataModel.pageName),
            products = listOf(mapToProductTracking(homeRecommendationItemDataModel))
    ).build()

    fun getRecommendationProductClickLogin(tabName: String, homeRecommendationItemDataModel: HomeRecommendationItemDataModel)
            = BaseTrackerBuilder().constructBasicProductClick(
            event = Event.PRODUCT_CLICK,
            eventCategory = Category.HOMEPAGE,
            eventAction = CustomAction.RECOMMENDATION_CLICK_BASE,
            eventLabel = tabName,
            list = ActionField.RECOMMENDATION_ACTION_FIELD_LOGIN.format(tabName, homeRecommendationItemDataModel.product.recommendationType, homeRecommendationItemDataModel.pageName),
            products = listOf(mapToProductTracking(homeRecommendationItemDataModel))
    ).build()

    fun getRecommendationProductClickLoginTopAds(tabName: String, homeRecommendationItemDataModel: HomeRecommendationItemDataModel)
            = BaseTrackerBuilder().constructBasicProductClick(
            event = Event.PRODUCT_CLICK,
            eventCategory = Category.HOMEPAGE,
            eventAction = CustomAction.RECOMMENDATION_CLICK_BASE,
            eventLabel = tabName,
            list = ActionField.RECOMMENDATION_ACTION_FIELD_LOGIN_TOP_ADS.format(tabName, homeRecommendationItemDataModel.product.recommendationType, homeRecommendationItemDataModel.pageName),
            products = listOf(mapToProductTracking(homeRecommendationItemDataModel))
    ).build()

    fun getRecommendationProductClickNonLoginTopAds(tabName: String, homeRecommendationItemDataModel: HomeRecommendationItemDataModel)
            = BaseTrackerBuilder().constructBasicProductClick(
            event = Event.PRODUCT_CLICK,
            eventCategory = Category.HOMEPAGE,
            eventAction = CustomAction.RECOMMENDATION_CLICK_BASE,
            eventLabel = tabName,
            list = ActionField.RECOMMENDATION_ACTION_FIELD_NON_LOGIN_TOP_ADS.format(tabName, homeRecommendationItemDataModel.product.recommendationType, homeRecommendationItemDataModel.pageName),
            products = listOf(mapToProductTracking(homeRecommendationItemDataModel))
    ).build()

    fun getRecommendationProductViewLogin(tabName: String, homeRecommendationItemDataModel: HomeRecommendationItemDataModel)
            = BaseTrackerBuilder().constructBasicProductView(
            event = Event.PRODUCT_VIEW,
            eventCategory = Category.HOMEPAGE,
            eventAction = CustomAction.RECOMMENDATION_VIEW_BASE,
            eventLabel = tabName,
            list = ActionField.RECOMMENDATION_ACTION_FIELD_LOGIN.format(tabName, homeRecommendationItemDataModel.product.recommendationType, homeRecommendationItemDataModel.pageName),
            products = listOf(mapToProductTracking(homeRecommendationItemDataModel))
    ).build()

    fun getRecommendationProductViewLoginTopAds(tabName: String, homeRecommendationItemDataModel: HomeRecommendationItemDataModel)
            = BaseTrackerBuilder().constructBasicProductView(
            event = Event.PRODUCT_VIEW,
            eventCategory = Category.HOMEPAGE,
            eventAction = CustomAction.RECOMMENDATION_VIEW_BASE,
            eventLabel = tabName,
            list = ActionField.RECOMMENDATION_ACTION_FIELD_LOGIN_TOP_ADS.format(tabName, homeRecommendationItemDataModel.product.recommendationType, homeRecommendationItemDataModel.pageName),
            products = listOf(mapToProductTracking(homeRecommendationItemDataModel))
    ).build()

    fun getRecommendationProductViewNonLogin(tabName: String, homeRecommendationItemDataModel: HomeRecommendationItemDataModel)
            = BaseTrackerBuilder().constructBasicProductView(
            event = Event.PRODUCT_VIEW,
            eventCategory = Category.HOMEPAGE,
            eventAction = CustomAction.RECOMMENDATION_VIEW_BASE,
            eventLabel = tabName,
            list = ActionField.RECOMMENDATION_ACTION_FIELD_NON_LOGIN.format(tabName, homeRecommendationItemDataModel.product.recommendationType, homeRecommendationItemDataModel.pageName),
            products = listOf(mapToProductTracking(homeRecommendationItemDataModel))
    ).build()

    fun getRecommendationProductViewNonLoginTopAds(tabName: String, homeRecommendationItemDataModel: HomeRecommendationItemDataModel)
            = BaseTrackerBuilder().constructBasicProductView(
            event = Event.PRODUCT_VIEW,
            eventCategory = Category.HOMEPAGE,
            eventAction = CustomAction.RECOMMENDATION_VIEW_BASE,
            eventLabel = tabName,
            list = ActionField.RECOMMENDATION_ACTION_FIELD_NON_LOGIN_TOP_ADS.format(tabName, homeRecommendationItemDataModel.product.recommendationType, homeRecommendationItemDataModel.pageName),
            products = listOf(mapToProductTracking(homeRecommendationItemDataModel))
    ).build()

    fun getRecommendationAddWishlistLogin(productId: String, tabName: String): Map<String, Any> = DataLayer.mapOf(
            Event.KEY, CustomEvent.CLICK_HOMEPAGE,
            Category.KEY, Category.HOMEPAGE,
            Action.KEY, CustomAction.RECOMMENDATION_ADD_WISHLIST_LOGIN,
            Label.KEY, "$productId - $tabName"
    )

    fun getRecommendationAddWishlistNonLogin(productId: String, tabName: String): Map<String, Any> = DataLayer.mapOf(
            Event.KEY, CustomEvent.CLICK_HOMEPAGE,
            Category.KEY, Category.HOMEPAGE,
            Action.KEY, CustomAction.RECOMMENDATION_ADD_WISHLIST_NON_LOGIN,
            Label.KEY, "$productId - $tabName"
    )

    fun getRecommendationRemoveWishlistLogin(productId: String, tabName: String): Map<String, Any> = DataLayer.mapOf(
            Event.KEY, CustomEvent.CLICK_HOMEPAGE,
            Category.KEY, Category.HOMEPAGE,
            Action.KEY, CustomAction.RECOMMENDATION_REMOVE_WISHLIST_LOGIN,
            Label.KEY, "$productId - $tabName"
    )

    fun getBannerRecommendation(bannerRecommendationDataModel: BannerRecommendationDataModel): Map<String, Any> {
        val trackerBuilder = BaseTrackerBuilder()
        return trackerBuilder.constructBasicPromotionView(
                event = Event.PROMO_VIEW,
                eventCategory = Category.HOMEPAGE,
                eventAction = Action.IMPRESSION_ON.format(BANNER_INSIDE_RECOMMENDATION),
                eventLabel = bannerRecommendationDataModel.tabName,
                promotions = listOf(mapToPromoTracking(bannerRecommendationDataModel))
        ).build()
    }

    fun getImpressionBannerTopAds(homeRecommendationBannerTopAdsDataModel: HomeRecommendationBannerTopAdsDataModel, tabPosition: Int, position: Int) = BaseTrackerBuilder().constructBasicPromotionView(
            Event.PROMO_VIEW,
            Category.HOMEPAGE,
            Action.IMPRESSION_ON.format(BANNER_ADS_INSIDE_RECOMMENDATION),
            Label.NONE,
            listOf(
                    Promotion(
                            id = homeRecommendationBannerTopAdsDataModel.topAdsImageViewModel?.bannerId.toString(),
                            name = CustomAction.BANNER_ADS_FIELD.format(tabPosition.toString()),
                            position = position.toString(),
                            creative = homeRecommendationBannerTopAdsDataModel.topAdsImageViewModel?.imageUrl ?: "",
                            creativeUrl = homeRecommendationBannerTopAdsDataModel.topAdsImageViewModel?.imageUrl ?: ""
                    )
            )
    ).build()

    fun getClickBannerTopAds(homeRecommendationBannerTopAdsDataModel: HomeRecommendationBannerTopAdsDataModel, tabPosition: Int, position: Int) = BaseTrackerBuilder().constructBasicPromotionClick(
            event = Event.PROMO_CLICK,
            eventCategory = Category.HOMEPAGE,
            eventAction = Action.CLICK_ON.format(BANNER_ADS_INSIDE_RECOMMENDATION),
            eventLabel = Label.NONE,
            promotions = listOf(
                    Promotion(
                            id = homeRecommendationBannerTopAdsDataModel.topAdsImageViewModel?.bannerId.toString(),
                            name = CustomAction.BANNER_ADS_FIELD.format(tabPosition.toString()),
                            position = position.toString(),
                            creative = homeRecommendationBannerTopAdsDataModel.topAdsImageViewModel?.imageUrl ?: "",
                            creativeUrl = homeRecommendationBannerTopAdsDataModel.topAdsImageViewModel?.imageUrl ?: ""
                    )
            )
    ).build()

    private fun mapToProductTracking(homeRecommendationItemDataModel: HomeRecommendationItemDataModel) = Product(
            id = homeRecommendationItemDataModel.product.id,
            name = homeRecommendationItemDataModel.product.name,
            variant = "",
            productPrice = homeRecommendationItemDataModel.product.priceInt.toString(),
            productPosition = homeRecommendationItemDataModel.position.toString(),
            isFreeOngkir = homeRecommendationItemDataModel.product.freeOngkirInformation.isActive && !homeRecommendationItemDataModel.product.labelGroup.any { it.position == LABEL_FULFILLMENT },
            isFreeOngkirExtra = homeRecommendationItemDataModel.product.freeOngkirInformation.isActive && homeRecommendationItemDataModel.product.labelGroup.any { it.position == LABEL_FULFILLMENT },
            category = homeRecommendationItemDataModel.product.categoryBreadcrumbs,
            brand = "",
            clusterId = homeRecommendationItemDataModel.product.clusterId,
            isTopAds = null
    )

    private fun mapToPromoTracking(bannerRecommendationDataModel: BannerRecommendationDataModel) = Promotion(
            id = bannerRecommendationDataModel.id.toString(),
            name = CustomAction.BANNER_FIELD.format(bannerRecommendationDataModel.tabName),
            position = bannerRecommendationDataModel.position.toString(),
            promoIds = Label.NONE,
            promoCodes = Label.NONE,
            creative = bannerRecommendationDataModel.buAttribution + "_" + bannerRecommendationDataModel.creativeName
    )
}