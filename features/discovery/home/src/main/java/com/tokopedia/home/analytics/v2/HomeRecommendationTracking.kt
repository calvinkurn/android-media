package com.tokopedia.home.analytics.v2

import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeFeedViewModel

object HomeRecommendationTracking : BaseTracking(){

    private object CustomAction{
        val RECOMMENDATION_CLICK_NON_LOGIN_TOPADS = Action.CLICK.format("product recommendation") + " - non login"
        val RECOMMENDATION_CLICK_LOGIN_TOPADS = Action.CLICK.format("product recommendation")
        val RECOMMENDATION_VIEW_NON_LOGIN = Action.IMPRESSION.format("product recommendation") + " - non login"
        val RECOMMENDATION_VIEW_LOGIN = Action.IMPRESSION.format("product recommendation")
    }

    private object ActionField{
        private const val BASE = "/ - p2 - %s%s - rekomendasi untuk anda - %s"
        val RECOMMENDATION_ACTION_FIELD_NON_LOGIN = BASE.format("non login", "%s", "%s")
        val RECOMMENDATION_ACTION_FIELD_LOGIN = BASE.format("", "%s", "%s")
    }

    fun getRecommendationProductClickNonLoginTopAds(tabName: String, homeFeedViewModel: HomeFeedViewModel) = getBasicProductClick(
            event = Event.PRODUCT_CLICK,
            eventCategory = Category.HOMEPAGE,
            eventAction = CustomAction.RECOMMENDATION_CLICK_NON_LOGIN_TOPADS,
            eventLabel = tabName,
            list = ActionField.RECOMMENDATION_ACTION_FIELD_NON_LOGIN.format(tabName, homeFeedViewModel.recommendationType),
            products = listOf(homeFeedViewModel)
    )

    fun getRecommendationProductClickLoginTopAds(tabName: String, homeFeedViewModel: HomeFeedViewModel) = getBasicProductClick(
            event = Event.PRODUCT_CLICK,
            eventCategory = Category.HOMEPAGE,
            eventAction = CustomAction.RECOMMENDATION_CLICK_LOGIN_TOPADS,
            eventLabel = tabName,
            list = ActionField.RECOMMENDATION_ACTION_FIELD_LOGIN.format(tabName, homeFeedViewModel.recommendationType),
            products = listOf(homeFeedViewModel)
    )

    // sama
    fun getRecommendationProductViewLogin(tabName: String, homeFeedViewModel: HomeFeedViewModel) = getBasicProductView(
            event = Event.PRODUCT_VIEW,
            eventCategory = Category.HOMEPAGE,
            eventAction = CustomAction.RECOMMENDATION_VIEW_LOGIN,
            eventLabel = tabName,
            list = ActionField.RECOMMENDATION_ACTION_FIELD_LOGIN.format(tabName, homeFeedViewModel.recommendationType),
            products = listOf(homeFeedViewModel)
    )

    fun getRecommendationProductViewNonLogin(tabName: String, homeFeedViewModel: HomeFeedViewModel) = getBasicProductView(
            event = Event.PRODUCT_VIEW,
            eventCategory = Category.HOMEPAGE,
            eventAction = CustomAction.RECOMMENDATION_VIEW_NON_LOGIN,
            eventLabel = tabName,
            list = ActionField.RECOMMENDATION_ACTION_FIELD_NON_LOGIN.format(tabName, homeFeedViewModel.recommendationType),
            products = listOf(homeFeedViewModel)
    )
}