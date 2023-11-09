package com.tokopedia.home.analytics.v2

import android.os.Bundle
import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.home.analytics.v2.HomeRecommendationTracking.CustomAction.BANNER_ADS_INSIDE_RECOMMENDATION
import com.tokopedia.home.analytics.v2.HomeRecommendationTracking.CustomAction.BANNER_INSIDE_RECOMMENDATION
import com.tokopedia.home.analytics.v2.HomeRecommendationTracking.CustomAction.CLICK_ON_BANNER_FOR_YOU_WIDGET
import com.tokopedia.home.analytics.v2.HomeRecommendationTracking.CustomAction.IMPRESSION_ON_BANNER_RECOMMENDATION_CARD_FOR_YOU
import com.tokopedia.home.analytics.v2.HomeRecommendationTracking.CustomAction.ITEM_NAME_NEW_FOR_YOU_FORMAT
import com.tokopedia.home.analytics.v2.HomeRecommendationTracking.CustomAction.RECOMMENDATION_CARD_FOR_YOU
import com.tokopedia.home.analytics.v2.HomeRecommendationTracking.CustomAction.TRACKER_ID_47626
import com.tokopedia.home.analytics.v2.HomeRecommendationTracking.CustomAction.TRACKER_ID_47716
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.BannerRecommendationDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationBannerTopAdsOldDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationItemDataModel
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.recommendation_widget_common.extension.LABEL_FULFILLMENT
import com.tokopedia.recommendation_widget_common.widget.entitycard.model.RecomEntityCardUiModel
import com.tokopedia.track.TrackApp
import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst

object HomeRecommendationTracking : BaseTrackerConst() {

    private object CustomEvent {
        const val CLICK_HOMEPAGE = "clickHomepage"
    }

    private object CustomAction {
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

        const val CLICK_ON_BANNER_FOR_YOU_WIDGET = "click on banner recommendation card for you"
        const val IMPRESSION_ON_BANNER_RECOMMENDATION_CARD_FOR_YOU = "impression on banner recommendation card for you"
        const val RECOMMENDATION_CARD_FOR_YOU = "recommendation card for you"
        const val TRACKER_ID_47716 = "47716"
        const val TRACKER_ID_47626 = "47626"

        const val ITEM_NAME_NEW_FOR_YOU_FORMAT = "/ - p%s - %s - banner - %s - %s - %s - %s"
    }

    private object ActionField {
        // note for https://tokopedia.atlassian.net/browse/AN-20317
        //  '/ - p2{ - non login} - {homepage recommendation tab} - rekomendasi untuk anda - {recommendation_type} - {recomm_page_name}{ - product topads}'

        private const val BASE = "/ - p2 - %s%s - rekomendasi untuk anda - %s - %s"
        val RECOMMENDATION_ACTION_FIELD_NON_LOGIN = BASE.format("non login", " - %s", "%s", "%s")
        val RECOMMENDATION_ACTION_FIELD_NON_LOGIN_TOP_ADS = BASE.format("non login", " - %s", "%s", "%s - product topads")
        val RECOMMENDATION_ACTION_FIELD_LOGIN = BASE.format("", "%s", "%s", "%s")
        val RECOMMENDATION_ACTION_FIELD_LOGIN_TOP_ADS = BASE.format("", "%s", "%s", "%s - product topads")
    }

    fun getRecommendationProductClickNonLogin(tabName: String, homeRecommendationItemDataModel: HomeRecommendationItemDataModel) =
        BaseTrackerBuilder().constructBasicProductClick(
            event = Event.PRODUCT_CLICK,
            eventCategory = Category.HOMEPAGE,
            eventAction = CustomAction.RECOMMENDATION_CLICK_BASE,
            eventLabel = tabName,
            list = ActionField.RECOMMENDATION_ACTION_FIELD_NON_LOGIN.format(tabName, homeRecommendationItemDataModel.recommendationProductItem.recommendationType, homeRecommendationItemDataModel.pageName),
            products = listOf(mapToProductTracking(homeRecommendationItemDataModel))
        ).build()

    fun getRecommendationProductClickLogin(tabName: String, homeRecommendationItemDataModel: HomeRecommendationItemDataModel) =
        BaseTrackerBuilder().constructBasicProductClick(
            event = Event.PRODUCT_CLICK,
            eventCategory = Category.HOMEPAGE,
            eventAction = CustomAction.RECOMMENDATION_CLICK_BASE,
            eventLabel = tabName,
            list = ActionField.RECOMMENDATION_ACTION_FIELD_LOGIN.format(tabName, homeRecommendationItemDataModel.recommendationProductItem.recommendationType, homeRecommendationItemDataModel.pageName),
            products = listOf(mapToProductTracking(homeRecommendationItemDataModel))
        ).build()

    fun getRecommendationProductClickLoginTopAds(tabName: String, homeRecommendationItemDataModel: HomeRecommendationItemDataModel) =
        BaseTrackerBuilder().constructBasicProductClick(
            event = Event.PRODUCT_CLICK,
            eventCategory = Category.HOMEPAGE,
            eventAction = CustomAction.RECOMMENDATION_CLICK_BASE,
            eventLabel = tabName,
            list = ActionField.RECOMMENDATION_ACTION_FIELD_LOGIN_TOP_ADS.format(tabName, homeRecommendationItemDataModel.recommendationProductItem.recommendationType, homeRecommendationItemDataModel.pageName),
            products = listOf(mapToProductTracking(homeRecommendationItemDataModel))
        ).build()

    fun getRecommendationProductClickNonLoginTopAds(tabName: String, homeRecommendationItemDataModel: HomeRecommendationItemDataModel) =
        BaseTrackerBuilder().constructBasicProductClick(
            event = Event.PRODUCT_CLICK,
            eventCategory = Category.HOMEPAGE,
            eventAction = CustomAction.RECOMMENDATION_CLICK_BASE,
            eventLabel = tabName,
            list = ActionField.RECOMMENDATION_ACTION_FIELD_NON_LOGIN_TOP_ADS.format(tabName, homeRecommendationItemDataModel.recommendationProductItem.recommendationType, homeRecommendationItemDataModel.pageName),
            products = listOf(mapToProductTracking(homeRecommendationItemDataModel))
        ).build()

    fun getRecommendationProductViewLogin(tabName: String, homeRecommendationItemDataModel: HomeRecommendationItemDataModel) =
        BaseTrackerBuilder().constructBasicProductView(
            event = Event.PRODUCT_VIEW,
            eventCategory = Category.HOMEPAGE,
            eventAction = CustomAction.RECOMMENDATION_VIEW_BASE,
            eventLabel = tabName,
            list = ActionField.RECOMMENDATION_ACTION_FIELD_LOGIN.format(tabName, homeRecommendationItemDataModel.recommendationProductItem.recommendationType, homeRecommendationItemDataModel.pageName),
            products = listOf(mapToProductTracking(homeRecommendationItemDataModel))
        ).build()

    fun getRecommendationProductViewLoginTopAds(tabName: String, homeRecommendationItemDataModel: HomeRecommendationItemDataModel) =
        BaseTrackerBuilder().constructBasicProductView(
            event = Event.PRODUCT_VIEW,
            eventCategory = Category.HOMEPAGE,
            eventAction = CustomAction.RECOMMENDATION_VIEW_BASE,
            eventLabel = tabName,
            list = ActionField.RECOMMENDATION_ACTION_FIELD_LOGIN_TOP_ADS.format(tabName, homeRecommendationItemDataModel.recommendationProductItem.recommendationType, homeRecommendationItemDataModel.pageName),
            products = listOf(mapToProductTracking(homeRecommendationItemDataModel))
        ).build()

    fun getRecommendationProductViewNonLogin(tabName: String, homeRecommendationItemDataModel: HomeRecommendationItemDataModel) =
        BaseTrackerBuilder().constructBasicProductView(
            event = Event.PRODUCT_VIEW,
            eventCategory = Category.HOMEPAGE,
            eventAction = CustomAction.RECOMMENDATION_VIEW_BASE,
            eventLabel = tabName,
            list = ActionField.RECOMMENDATION_ACTION_FIELD_NON_LOGIN.format(tabName, homeRecommendationItemDataModel.recommendationProductItem.recommendationType, homeRecommendationItemDataModel.pageName),
            products = listOf(mapToProductTracking(homeRecommendationItemDataModel))
        ).build()

    fun getRecommendationProductViewNonLoginTopAds(tabName: String, homeRecommendationItemDataModel: HomeRecommendationItemDataModel) =
        BaseTrackerBuilder().constructBasicProductView(
            event = Event.PRODUCT_VIEW,
            eventCategory = Category.HOMEPAGE,
            eventAction = CustomAction.RECOMMENDATION_VIEW_BASE,
            eventLabel = tabName,
            list = ActionField.RECOMMENDATION_ACTION_FIELD_NON_LOGIN_TOP_ADS.format(tabName, homeRecommendationItemDataModel.recommendationProductItem.recommendationType, homeRecommendationItemDataModel.pageName),
            products = listOf(mapToProductTracking(homeRecommendationItemDataModel))
        ).build()

    fun getRecommendationAddWishlistLogin(productId: String, tabName: String): Map<String, Any> = DataLayer.mapOf(
        Event.KEY,
        CustomEvent.CLICK_HOMEPAGE,
        Category.KEY,
        Category.HOMEPAGE,
        Action.KEY,
        CustomAction.RECOMMENDATION_ADD_WISHLIST_LOGIN,
        Label.KEY,
        "$productId - $tabName"
    )

    fun getRecommendationAddWishlistNonLogin(productId: String, tabName: String): Map<String, Any> = DataLayer.mapOf(
        Event.KEY,
        CustomEvent.CLICK_HOMEPAGE,
        Category.KEY,
        Category.HOMEPAGE,
        Action.KEY,
        CustomAction.RECOMMENDATION_ADD_WISHLIST_NON_LOGIN,
        Label.KEY,
        "$productId - $tabName"
    )

    fun getRecommendationRemoveWishlistLogin(productId: String, tabName: String): Map<String, Any> = DataLayer.mapOf(
        Event.KEY,
        CustomEvent.CLICK_HOMEPAGE,
        Category.KEY,
        Category.HOMEPAGE,
        Action.KEY,
        CustomAction.RECOMMENDATION_REMOVE_WISHLIST_LOGIN,
        Label.KEY,
        "$productId - $tabName"
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

    fun getImpressionBannerTopAds(homeRecommendationBannerTopAdsOldDataModel: HomeRecommendationBannerTopAdsOldDataModel, tabPosition: Int, position: Int) = BaseTrackerBuilder().constructBasicPromotionView(
        Event.PROMO_VIEW,
        Category.HOMEPAGE,
        Action.IMPRESSION_ON.format(BANNER_ADS_INSIDE_RECOMMENDATION),
        Label.NONE,
        listOf(
            Promotion(
                id = homeRecommendationBannerTopAdsOldDataModel.topAdsImageViewModel?.bannerId.toString(),
                name = CustomAction.BANNER_ADS_FIELD.format(tabPosition.toString()),
                position = position.toString(),
                creative = homeRecommendationBannerTopAdsOldDataModel.topAdsImageViewModel?.imageUrl ?: "",
                creativeUrl = homeRecommendationBannerTopAdsOldDataModel.topAdsImageViewModel?.imageUrl ?: ""
            )
        )
    ).build()

    fun getClickBannerTopAds(homeRecommendationBannerTopAdsOldDataModel: HomeRecommendationBannerTopAdsOldDataModel, tabPosition: Int, position: Int) = BaseTrackerBuilder().constructBasicPromotionClick(
        event = Event.PROMO_CLICK,
        eventCategory = Category.HOMEPAGE,
        eventAction = Action.CLICK_ON.format(BANNER_ADS_INSIDE_RECOMMENDATION),
        eventLabel = Label.NONE,
        promotions = listOf(
            Promotion(
                id = homeRecommendationBannerTopAdsOldDataModel.topAdsImageViewModel?.bannerId.toString(),
                name = CustomAction.BANNER_ADS_FIELD.format(tabPosition.toString()),
                position = position.toString(),
                creative = homeRecommendationBannerTopAdsOldDataModel.topAdsImageViewModel?.imageUrl ?: "",
                creativeUrl = homeRecommendationBannerTopAdsOldDataModel.topAdsImageViewModel?.imageUrl ?: ""
            )
        )
    ).build()

    fun sendClickEntityCardTracking(
        recomEntityCardUiModel: RecomEntityCardUiModel,
        position: Int,
        userId: String
    ) {
        val bundle = Bundle().apply {
            putString(Event.KEY, Event.SELECT_CONTENT)
            putString(Action.KEY, CLICK_ON_BANNER_FOR_YOU_WIDGET)
            putString(Category.KEY, Category.HOMEPAGE)
            putString(
                Label.KEY,
                "${recomEntityCardUiModel.layoutCard} - ${recomEntityCardUiModel.layoutItem} - ${recomEntityCardUiModel.title}"
            )
            putString(
                TrackerId.KEY,
                TRACKER_ID_47716
            )
            putString(
                BusinessUnit.KEY,
                BusinessUnit.DEFAULT
            )
            putString(
                CurrentSite.KEY,
                CurrentSite.DEFAULT
            )
            val creativeSlot = (position + Int.ONE).toString()
            putBundle(
                Promotion.KEY,
                Bundle().also {
                    it.putString(Promotion.CREATIVE_NAME, "")
                    it.putString(Promotion.CREATIVE_SLOT, creativeSlot)
                    it.putString(Promotion.ITEM_ID, "")
                    it.putString(
                        Promotion.ITEM_NAME,
                        ITEM_NAME_NEW_FOR_YOU_FORMAT.format(creativeSlot, RECOMMENDATION_CARD_FOR_YOU, recomEntityCardUiModel.categoryId, recomEntityCardUiModel.layoutCard, recomEntityCardUiModel.layoutItem, recomEntityCardUiModel.title)
                    )
                }
            )
            putString(UserId.KEY, userId)
        }

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(Event.PROMO_CLICK, bundle)
    }

    fun getImpressEntityCardTracking(
        recomEntityCardUiModel: RecomEntityCardUiModel,
        position: Int,
        userId: String
    ): Map<String, Any> {
        val trackingBuilder = BaseTrackerBuilder()
        val creativeSlot = (position + Int.ONE).toString()
        val itemName = ITEM_NAME_NEW_FOR_YOU_FORMAT.format(creativeSlot, RECOMMENDATION_CARD_FOR_YOU, recomEntityCardUiModel.categoryId, recomEntityCardUiModel.layoutCard, recomEntityCardUiModel.layoutItem, recomEntityCardUiModel.title)

        val listPromotions = arrayListOf(
            Promotion(
                creative = "",
                position = creativeSlot,
                id = "",
                name = itemName
            )
        )

        return trackingBuilder.constructBasicPromotionView(
            event = Event.VIEW_ITEM,
            eventCategory = Category.HOMEPAGE,
            eventAction = IMPRESSION_ON_BANNER_RECOMMENDATION_CARD_FOR_YOU,
            eventLabel = "${recomEntityCardUiModel.layoutCard} - ${recomEntityCardUiModel.layoutItem} - ${recomEntityCardUiModel.title}",
            promotions = listPromotions
        ).appendBusinessUnit(BusinessUnit.DEFAULT)
            .appendCurrentSite(CurrentSite.DEFAULT)
            .appendUserId(userId)
            .appendCustomKeyValue(
                TrackerId.KEY,
                TRACKER_ID_47626
            ).build()
    }

    private fun mapToProductTracking(homeRecommendationItemDataModel: HomeRecommendationItemDataModel) = Product(
        id = homeRecommendationItemDataModel.recommendationProductItem.id,
        name = homeRecommendationItemDataModel.recommendationProductItem.name,
        variant = "",
        productPrice = homeRecommendationItemDataModel.recommendationProductItem.priceInt.toString(),
        productPosition = homeRecommendationItemDataModel.position.toString(),
        isFreeOngkir = homeRecommendationItemDataModel.recommendationProductItem.freeOngkirIsActive && !homeRecommendationItemDataModel.recommendationProductItem.labelGroup.any { it.position == LABEL_FULFILLMENT },
        isFreeOngkirExtra = homeRecommendationItemDataModel.recommendationProductItem.freeOngkirIsActive && homeRecommendationItemDataModel.recommendationProductItem.labelGroup.any { it.position == LABEL_FULFILLMENT },
        category = homeRecommendationItemDataModel.recommendationProductItem.categoryBreadcrumbs,
        brand = "",
        clusterId = homeRecommendationItemDataModel.recommendationProductItem.clusterID,
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
