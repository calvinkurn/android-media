package com.tokopedia.home.analytics.v2

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.home.analytics.v2.HomeRecommendationTracking.CustomAction.BANNER_ADS_INSIDE_RECOMMENDATION
import com.tokopedia.home.analytics.v2.HomeRecommendationTracking.CustomAction.BANNER_INSIDE_RECOMMENDATION
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.BannerRecommendationDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationBannerTopAdsDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationItemDataModel
import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst

object HomeRecommendationTracking : BaseTracking(){

    private object CustomEvent{
        const val CLICK_HOMEPAGE = "clickHomepage"
    }

    private object CustomAction{
        val RECOMMENDATION_CLICK_NON_LOGIN_NON_TOPADS = Action.CLICK.format("product recommendation") + " - non login"
        val RECOMMENDATION_CLICK_LOGIN_NON_TOPADS = Action.CLICK.format("product recommendation")
        val RECOMMENDATION_CLICK_NON_LOGIN_TOPADS = Action.CLICK.format("product recommendation") + " - non login - topads"
        val RECOMMENDATION_CLICK_LOGIN_TOPADS = Action.CLICK.format("product recommendation") + " - topads"

        val RECOMMENDATION_VIEW_NON_LOGIN_NON_TOPADS = Action.IMPRESSION.format("product recommendation") + " - non login"
        val RECOMMENDATION_VIEW_NON_LOGIN_TOPADS = Action.IMPRESSION.format("product recommendation") + " - non login - topads"
        val RECOMMENDATION_VIEW_LOGIN_NON_TOPADS = Action.IMPRESSION.format("product recommendation")
        val RECOMMENDATION_VIEW_LOGIN_TOPADS = Action.IMPRESSION.format("product recommendation") + " - topads"

        const val RECOMMENDATION_ADD_WISHLIST_LOGIN = "add wishlist - product recommendation - login"
        const val RECOMMENDATION_REMOVE_WISHLIST_LOGIN = "remove wishlist - product recommendation - login"
        const val RECOMMENDATION_ADD_WISHLIST_NON_LOGIN = "add wishlist - product recommendation - non login"
        const val BANNER_INSIDE_RECOMMENDATION = "banner inside recommendation tab"
        const val BANNER_ADS_INSIDE_RECOMMENDATION = "banner inside recommendation tab ads"
        const val BANNER_FIELD = "/ - banner inside recom tab - %s - "
        const val BANNER_ADS_FIELD = "/ - p%s - banner inside recomm tab ads"
    }

    private object ActionField{
        private const val BASE = "/ - p2 - %s%s - rekomendasi untuk anda - %s"
        val RECOMMENDATION_ACTION_FIELD_NON_LOGIN = BASE.format("non login", " - %s", "%s")
        val RECOMMENDATION_ACTION_FIELD_NON_LOGIN_TOP_ADS = BASE.format("non login", " - %s", "%s - product topads")
        val RECOMMENDATION_ACTION_FIELD_LOGIN = BASE.format("", "%s", "%s")
        val RECOMMENDATION_ACTION_FIELD_LOGIN_TOP_ADS = BASE.format("", "%s", "%s - product topads")
    }

    fun getRecommendationProductClickNonLogin(tabName: String, homeRecommendationItemDataModel: HomeRecommendationItemDataModel) = BaseTrackerBuilder().constructBasicProductClick(
            event = Event.PRODUCT_CLICK,
            eventCategory = Category.HOMEPAGE,
            eventAction = CustomAction.RECOMMENDATION_CLICK_NON_LOGIN_NON_TOPADS,
            eventLabel = tabName,
            list = ActionField.RECOMMENDATION_ACTION_FIELD_NON_LOGIN.format(tabName, homeRecommendationItemDataModel.product.recommendationType),
            products = listOf(mapToProductTracking(homeRecommendationItemDataModel))
    ).build()

    fun getRecommendationProductClickLogin(tabName: String, homeRecommendationItemDataModel: HomeRecommendationItemDataModel) = BaseTrackerBuilder().constructBasicProductClick(
            event = Event.PRODUCT_CLICK,
            eventCategory = Category.HOMEPAGE,
            eventAction = CustomAction.RECOMMENDATION_CLICK_LOGIN_NON_TOPADS,
            eventLabel = tabName,
            list = ActionField.RECOMMENDATION_ACTION_FIELD_LOGIN.format(tabName, homeRecommendationItemDataModel.product.recommendationType),
            products = listOf(mapToProductTracking(homeRecommendationItemDataModel))
    ).build()

    fun getRecommendationProductClickLoginTopAds(tabName: String, homeRecommendationItemDataModel: HomeRecommendationItemDataModel) = BaseTrackerBuilder().constructBasicProductClick(
            event = Event.PRODUCT_CLICK,
            eventCategory = Category.HOMEPAGE,
            eventAction = CustomAction.RECOMMENDATION_CLICK_LOGIN_TOPADS,
            eventLabel = tabName,
            list = ActionField.RECOMMENDATION_ACTION_FIELD_LOGIN_TOP_ADS.format(tabName, homeRecommendationItemDataModel.product.recommendationType),
            products = listOf(mapToProductTracking(homeRecommendationItemDataModel))
    ).build()

    fun getRecommendationProductClickNonLoginTopAds(tabName: String, homeRecommendationItemDataModel: HomeRecommendationItemDataModel) = BaseTrackerBuilder().constructBasicProductClick(
            event = Event.PRODUCT_CLICK,
            eventCategory = Category.HOMEPAGE,
            eventAction = CustomAction.RECOMMENDATION_CLICK_NON_LOGIN_TOPADS,
            eventLabel = tabName,
            list = ActionField.RECOMMENDATION_ACTION_FIELD_NON_LOGIN_TOP_ADS.format(tabName, homeRecommendationItemDataModel.product.recommendationType),
            products = listOf(mapToProductTracking(homeRecommendationItemDataModel))
    ).build()

    fun getRecommendationProductViewLogin(tabName: String, homeRecommendationItemDataModel: HomeRecommendationItemDataModel) = BaseTrackerBuilder().constructBasicProductView(
            event = Event.PRODUCT_VIEW,
            eventCategory = Category.HOMEPAGE,
            eventAction = CustomAction.RECOMMENDATION_VIEW_LOGIN_NON_TOPADS,
            eventLabel = tabName,
            list = ActionField.RECOMMENDATION_ACTION_FIELD_LOGIN.format(tabName, homeRecommendationItemDataModel.product.recommendationType),
            products = listOf(mapToProductTracking(homeRecommendationItemDataModel))
    ).build()

    fun getRecommendationProductViewLoginTopAds(tabName: String, homeRecommendationItemDataModel: HomeRecommendationItemDataModel) = BaseTrackerBuilder().constructBasicProductView(
            event = Event.PRODUCT_VIEW,
            eventCategory = Category.HOMEPAGE,
            eventAction = CustomAction.RECOMMENDATION_VIEW_LOGIN_TOPADS,
            eventLabel = tabName,
            list = ActionField.RECOMMENDATION_ACTION_FIELD_LOGIN_TOP_ADS.format(tabName, homeRecommendationItemDataModel.product.recommendationType),
            products = listOf(mapToProductTracking(homeRecommendationItemDataModel))
    ).build()

    fun getRecommendationProductViewNonLogin(tabName: String, homeRecommendationItemDataModel: HomeRecommendationItemDataModel) = BaseTrackerBuilder().constructBasicProductView(
            event = Event.PRODUCT_VIEW,
            eventCategory = Category.HOMEPAGE,
            eventAction = CustomAction.RECOMMENDATION_VIEW_NON_LOGIN_NON_TOPADS,
            eventLabel = tabName,
            list = ActionField.RECOMMENDATION_ACTION_FIELD_NON_LOGIN.format(tabName, homeRecommendationItemDataModel.product.recommendationType),
            products = listOf(mapToProductTracking(homeRecommendationItemDataModel))
    ).build()

    fun getRecommendationProductViewNonLoginTopAds(tabName: String, homeRecommendationItemDataModel: HomeRecommendationItemDataModel) = BaseTrackerBuilder().constructBasicProductView(
            event = Event.PRODUCT_VIEW,
            eventCategory = Category.HOMEPAGE,
            eventAction = CustomAction.RECOMMENDATION_VIEW_NON_LOGIN_TOPADS,
            eventLabel = tabName,
            list = ActionField.RECOMMENDATION_ACTION_FIELD_NON_LOGIN_TOP_ADS.format(tabName, homeRecommendationItemDataModel.product.recommendationType),
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

    fun getBannerRecommendation(bannerRecommendationDataModel: BannerRecommendationDataModel) = getBasicPromotionView(
            Event.PROMO_VIEW,
            Category.HOMEPAGE,
            Action.IMPRESSION_ON.format(BANNER_INSIDE_RECOMMENDATION),
            bannerRecommendationDataModel.tabName,
            listOf(mapToPromoTracking(bannerRecommendationDataModel))
    )

    fun getImpressionBannerTopAds(homeRecommendationBannerTopAdsDataModel: HomeRecommendationBannerTopAdsDataModel, tabPosition: Int, position: Int) = BaseTrackerBuilder().constructBasicPromotionView(
            Event.PROMO_VIEW,
            Category.HOMEPAGE_TOPADS,
            Action.IMPRESSION_ON.format(BANNER_ADS_INSIDE_RECOMMENDATION),
            Label.NONE,
            listOf(
                    BaseTrackerConst.Promotion(
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
            eventCategory = Category.HOMEPAGE_TOPADS,
            eventAction = Action.CLICK_ON.format(BANNER_ADS_INSIDE_RECOMMENDATION),
            eventLabel = Label.NONE,
            promotions = listOf(
                    BaseTrackerConst.Promotion(
                            id = homeRecommendationBannerTopAdsDataModel.topAdsImageViewModel?.bannerId.toString(),
                            name = CustomAction.BANNER_ADS_FIELD.format(tabPosition.toString()),
                            position = position.toString(),
                            creative = homeRecommendationBannerTopAdsDataModel.topAdsImageViewModel?.imageUrl ?: "",
                            creativeUrl = homeRecommendationBannerTopAdsDataModel.topAdsImageViewModel?.imageUrl ?: ""
                    )
            )
    ).build()

    private fun mapToProductTracking(homeRecommendationItemDataModel: HomeRecommendationItemDataModel) = BaseTrackerConst.Product(
            id = homeRecommendationItemDataModel.product.id,
            name = homeRecommendationItemDataModel.product.name,
            variant = "",
            productPrice = homeRecommendationItemDataModel.product.priceInt.toString(),
            productPosition = homeRecommendationItemDataModel.position.toString(),
            isFreeOngkir = homeRecommendationItemDataModel.product.freeOngkirInformation.isActive,
            category = homeRecommendationItemDataModel.product.categoryBreadcrumbs,
            brand = "",
            clusterId = homeRecommendationItemDataModel.product.clusterId,
            recommendationType = homeRecommendationItemDataModel.product.recommendationType,
            pageName = homeRecommendationItemDataModel.pageName
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