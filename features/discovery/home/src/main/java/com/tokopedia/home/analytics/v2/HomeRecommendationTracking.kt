package com.tokopedia.home.analytics.v2

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeFeedViewModel

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

        val RECOMMENDATION_ADD_WISHLIST_LOGIN = "add wishlist - product recommendation - login"
        val RECOMMENDATION_REMOVE_WISHLIST_LOGIN = "remove wishlist - product recommendation - login"
        val RECOMMENDATION_ADD_WISHLIST_NON_LOGIN = "add wishlist - product recommendation - non login"
    }

    private object ActionField{
        private const val BASE = "/ - p2 - %s%s - rekomendasi untuk anda - %s"
        val RECOMMENDATION_ACTION_FIELD_NON_LOGIN = BASE.format("non login", " - %s", "%s")
        val RECOMMENDATION_ACTION_FIELD_NON_LOGIN_TOP_ADS = BASE.format("non login", " - %s", "%s - product topads")
        val RECOMMENDATION_ACTION_FIELD_LOGIN = BASE.format("", "%s", "%s")
        val RECOMMENDATION_ACTION_FIELD_LOGIN_TOP_ADS = BASE.format("", "%s", "%s - product topads")
    }

    fun getRecommendationProductClickNonLogin(tabName: String, homeFeedViewModel: HomeFeedViewModel) = getBasicProductClick(
            event = Event.PRODUCT_CLICK,
            eventCategory = Category.HOMEPAGE,
            eventAction = CustomAction.RECOMMENDATION_CLICK_NON_LOGIN_NON_TOPADS,
            eventLabel = tabName,
            list = ActionField.RECOMMENDATION_ACTION_FIELD_NON_LOGIN.format(tabName, homeFeedViewModel.recommendationType),
            products = listOf(homeFeedViewModel)
    )

    fun getRecommendationProductClickLogin(tabName: String, homeFeedViewModel: HomeFeedViewModel) = getBasicProductClick(
            event = Event.PRODUCT_CLICK,
            eventCategory = Category.HOMEPAGE,
            eventAction = CustomAction.RECOMMENDATION_CLICK_LOGIN_NON_TOPADS,
            eventLabel = tabName,
            list = ActionField.RECOMMENDATION_ACTION_FIELD_LOGIN.format(tabName, homeFeedViewModel.recommendationType),
            products = listOf(homeFeedViewModel)
    )

    fun getRecommendationProductClickLoginTopAds(tabName: String, homeFeedViewModel: HomeFeedViewModel) = getBasicProductClick(
            event = Event.PRODUCT_CLICK,
            eventCategory = Category.HOMEPAGE,
            eventAction = CustomAction.RECOMMENDATION_CLICK_LOGIN_TOPADS,
            eventLabel = tabName,
            list = ActionField.RECOMMENDATION_ACTION_FIELD_LOGIN_TOP_ADS.format(tabName, homeFeedViewModel.recommendationType),
            products = listOf(homeFeedViewModel)
    )

    fun getRecommendationProductClickNonLoginTopAds(tabName: String, homeFeedViewModel: HomeFeedViewModel) = getBasicProductClick(
            event = Event.PRODUCT_CLICK,
            eventCategory = Category.HOMEPAGE,
            eventAction = CustomAction.RECOMMENDATION_CLICK_NON_LOGIN_TOPADS,
            eventLabel = tabName,
            list = ActionField.RECOMMENDATION_ACTION_FIELD_NON_LOGIN_TOP_ADS.format(tabName, homeFeedViewModel.recommendationType),
            products = listOf(homeFeedViewModel)
    )

    fun getRecommendationProductViewLogin(tabName: String, homeFeedViewModel: HomeFeedViewModel) = getBasicProductView(
            event = Event.PRODUCT_VIEW,
            eventCategory = Category.HOMEPAGE,
            eventAction = CustomAction.RECOMMENDATION_VIEW_LOGIN_NON_TOPADS,
            eventLabel = tabName,
            list = ActionField.RECOMMENDATION_ACTION_FIELD_LOGIN.format(tabName, homeFeedViewModel.recommendationType),
            products = listOf(homeFeedViewModel)
    )

    fun getRecommendationProductViewLoginTopAds(tabName: String, homeFeedViewModel: HomeFeedViewModel) = getBasicProductView(
            event = Event.PRODUCT_VIEW,
            eventCategory = Category.HOMEPAGE,
            eventAction = CustomAction.RECOMMENDATION_VIEW_LOGIN_TOPADS,
            eventLabel = tabName,
            list = ActionField.RECOMMENDATION_ACTION_FIELD_LOGIN_TOP_ADS.format(tabName, homeFeedViewModel.recommendationType),
            products = listOf(homeFeedViewModel)
    )

    fun getRecommendationProductViewNonLogin(tabName: String, homeFeedViewModel: HomeFeedViewModel) = getBasicProductView(
            event = Event.PRODUCT_VIEW,
            eventCategory = Category.HOMEPAGE,
            eventAction = CustomAction.RECOMMENDATION_VIEW_NON_LOGIN_NON_TOPADS,
            eventLabel = tabName,
            list = ActionField.RECOMMENDATION_ACTION_FIELD_NON_LOGIN.format(tabName, homeFeedViewModel.recommendationType),
            products = listOf(homeFeedViewModel)
    )

    fun getRecommendationProductViewNonLoginTopAds(tabName: String, homeFeedViewModel: HomeFeedViewModel) = getBasicProductView(
            event = Event.PRODUCT_VIEW,
            eventCategory = Category.HOMEPAGE,
            eventAction = CustomAction.RECOMMENDATION_VIEW_NON_LOGIN_TOPADS,
            eventLabel = tabName,
            list = ActionField.RECOMMENDATION_ACTION_FIELD_NON_LOGIN_TOP_ADS.format(tabName, homeFeedViewModel.recommendationType),
            products = listOf(homeFeedViewModel)
    )

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
}