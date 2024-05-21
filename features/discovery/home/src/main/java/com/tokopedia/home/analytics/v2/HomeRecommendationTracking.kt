package com.tokopedia.home.analytics.v2

import android.os.Bundle
import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.home.analytics.v2.HomeRecommendationTracking.CustomAction.CLICK_ON_BANNER_FOR_YOU_WIDGET
import com.tokopedia.home.analytics.v2.HomeRecommendationTracking.CustomAction.CLICK_ON_VIDEO_RECOMMENDATION_CARD_FOR_YOU_VIDEO
import com.tokopedia.home.analytics.v2.HomeRecommendationTracking.CustomAction.FOR_YOU
import com.tokopedia.home.analytics.v2.HomeRecommendationTracking.CustomAction.IMPRESSION_ON_BANNER_RECOMMENDATION_CARD_FOR_YOU
import com.tokopedia.home.analytics.v2.HomeRecommendationTracking.CustomAction.IMPRESSION_VIDEO_RECOMMENDATION_CARD_FOR_YOU_VIDEO
import com.tokopedia.home.analytics.v2.HomeRecommendationTracking.CustomAction.ITEM_NAME_FOR_YOU_VIDEO_FORMAT
import com.tokopedia.home.analytics.v2.HomeRecommendationTracking.CustomAction.ITEM_NAME_NEW_FOR_YOU_FORMAT
import com.tokopedia.home.analytics.v2.HomeRecommendationTracking.CustomAction.PLAY
import com.tokopedia.home.analytics.v2.HomeRecommendationTracking.CustomAction.RECOMMENDATION_CARD_FOR_YOU
import com.tokopedia.home.analytics.v2.HomeRecommendationTracking.CustomAction.TRACKER_ID_47626
import com.tokopedia.home.analytics.v2.HomeRecommendationTracking.CustomAction.TRACKER_ID_47716
import com.tokopedia.home.analytics.v2.HomeRecommendationTracking.CustomAction.TRACKER_ID_48661
import com.tokopedia.home.analytics.v2.HomeRecommendationTracking.CustomAction.TRACKER_ID_48662
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.recommendation_widget_common.extension.LABEL_FULFILLMENT
import com.tokopedia.recommendation_widget_common.infinite.foryou.entity.ContentCardModel
import com.tokopedia.recommendation_widget_common.infinite.foryou.play.PlayCardModel
import com.tokopedia.recommendation_widget_common.infinite.foryou.recom.RecommendationCardModel
import com.tokopedia.recommendation_widget_common.infinite.foryou.topads.model.BannerTopAdsModel
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
        const val RECOMMENDATION_ADD_WISHLIST_LOGIN =
            "add wishlist - product recommendation - login"
        const val RECOMMENDATION_REMOVE_WISHLIST_LOGIN =
            "remove wishlist - product recommendation - login"
        const val RECOMMENDATION_ADD_WISHLIST_NON_LOGIN =
            "add wishlist - product recommendation - non login"
        const val BANNER_INSIDE_RECOMMENDATION = "banner inside recommendation tab"
        const val BANNER_ADS_INSIDE_RECOMMENDATION = "banner inside recommendation tab ads"
        const val BANNER_FIELD = "/ - banner inside recom tab - %s - "
        const val BANNER_ADS_FIELD = "/ - p%s - banner inside recomm tab ads"
        private const val LABEL_FULFILLMENT = "fulfillment"

        const val CLICK_ON_BANNER_FOR_YOU_WIDGET = "click on banner recommendation card for you"
        const val IMPRESSION_ON_BANNER_RECOMMENDATION_CARD_FOR_YOU =
            "impression on banner recommendation card for you"
        const val RECOMMENDATION_CARD_FOR_YOU = "recommendation card for you"
        const val TRACKER_ID_47716 = "47716"
        const val TRACKER_ID_47626 = "47626"

        const val CLICK_ON_VIDEO_RECOMMENDATION_CARD_FOR_YOU_VIDEO =
            "click on video recommendation card for you video"
        const val IMPRESSION_VIDEO_RECOMMENDATION_CARD_FOR_YOU_VIDEO =
            "impression on video recommendation card for you video"
        const val PLAY = "play"
        const val FOR_YOU = "ForYou"
        const val ITEM_NAME_FOR_YOU_VIDEO_FORMAT =
            "/ - p%s - recommendation card for you video - banner - %s - %s - %s - %s"
        const val TRACKER_ID_48661 = "48661"
        const val TRACKER_ID_48662 = "48662"

        const val ITEM_NAME_NEW_FOR_YOU_FORMAT = "/ - p%s - %s - banner - %s - %s - %s - %s"
        const val FOR_YOU_CREATIVE_NAME = "ForYou card"
    }

    private object ActionField {
        // note for https://tokopedia.atlassian.net/browse/AN-20317
        //  '/ - p2{ - non login} - {homepage recommendation tab} - rekomendasi untuk anda - {recommendation_type} - {recomm_page_name}{ - product topads}'

        private const val BASE = "/ - p2 - %s%s - rekomendasi untuk anda - %s - %s"
        val RECOMMENDATION_ACTION_FIELD_NON_LOGIN = BASE.format("non login", " - %s", "%s", "%s")
        val RECOMMENDATION_ACTION_FIELD_NON_LOGIN_TOP_ADS =
            BASE.format("non login", " - %s", "%s", "%s - product topads")
        val RECOMMENDATION_ACTION_FIELD_LOGIN = BASE.format("", "%s", "%s", "%s")
        val RECOMMENDATION_ACTION_FIELD_LOGIN_TOP_ADS =
            BASE.format("", "%s", "%s", "%s - product topads")

        const val PLAY_SHORT_VIDEO_LAYOUT_ITEM = "play short video"
    }

    fun getRecommendationProductClickNonLogin(
        tabName: String,
        homeRecommendationItemDataModel: RecommendationCardModel
    ) = BaseTrackerBuilder().constructBasicProductClick(
        event = Event.PRODUCT_CLICK,
        eventCategory = Category.HOMEPAGE,
        eventAction = CustomAction.RECOMMENDATION_CLICK_BASE,
        eventLabel = tabName,
        list = ActionField.RECOMMENDATION_ACTION_FIELD_NON_LOGIN.format(
            tabName,
            homeRecommendationItemDataModel.recommendationProductItem.recommendationType,
            homeRecommendationItemDataModel.pageName
        ),
        products = listOf(mapToProductTracking(homeRecommendationItemDataModel))
    ).build()

    fun getRecommendationProductClickLogin(
        tabName: String,
        homeRecommendationItemDataModel: RecommendationCardModel
    ) = BaseTrackerBuilder().constructBasicProductClick(
        event = Event.PRODUCT_CLICK,
        eventCategory = Category.HOMEPAGE,
        eventAction = CustomAction.RECOMMENDATION_CLICK_BASE,
        eventLabel = tabName,
        list = ActionField.RECOMMENDATION_ACTION_FIELD_LOGIN.format(
            tabName,
            homeRecommendationItemDataModel.recommendationProductItem.recommendationType,
            homeRecommendationItemDataModel.pageName
        ),
        products = listOf(mapToProductTracking(homeRecommendationItemDataModel))
    ).build()

    fun getRecommendationProductClickLoginTopAds(
        tabName: String,
        homeRecommendationItemDataModel: RecommendationCardModel
    ) = BaseTrackerBuilder().constructBasicProductClick(
        event = Event.PRODUCT_CLICK,
        eventCategory = Category.HOMEPAGE,
        eventAction = CustomAction.RECOMMENDATION_CLICK_BASE,
        eventLabel = tabName,
        list = ActionField.RECOMMENDATION_ACTION_FIELD_LOGIN_TOP_ADS.format(
            tabName,
            homeRecommendationItemDataModel.recommendationProductItem.recommendationType,
            homeRecommendationItemDataModel.pageName
        ),
        products = listOf(mapToProductTracking(homeRecommendationItemDataModel))
    ).build()

    fun getRecommendationProductClickNonLoginTopAds(
        tabName: String,
        homeRecommendationItemDataModel: RecommendationCardModel
    ) = BaseTrackerBuilder().constructBasicProductClick(
        event = Event.PRODUCT_CLICK,
        eventCategory = Category.HOMEPAGE,
        eventAction = CustomAction.RECOMMENDATION_CLICK_BASE,
        eventLabel = tabName,
        list = ActionField.RECOMMENDATION_ACTION_FIELD_NON_LOGIN_TOP_ADS.format(
            tabName,
            homeRecommendationItemDataModel.recommendationProductItem.recommendationType,
            homeRecommendationItemDataModel.pageName
        ),
        products = listOf(mapToProductTracking(homeRecommendationItemDataModel))
    ).build()

    fun getRecommendationProductViewLogin(
        tabName: String,
        homeRecommendationItemDataModel: RecommendationCardModel
    ) = BaseTrackerBuilder().constructBasicProductView(
        event = Event.PRODUCT_VIEW,
        eventCategory = Category.HOMEPAGE,
        eventAction = CustomAction.RECOMMENDATION_VIEW_BASE,
        eventLabel = tabName,
        list = ActionField.RECOMMENDATION_ACTION_FIELD_LOGIN.format(
            tabName,
            homeRecommendationItemDataModel.recommendationProductItem.recommendationType,
            homeRecommendationItemDataModel.pageName
        ),
        products = listOf(mapToProductTracking(homeRecommendationItemDataModel))
    ).build()

    fun getRecommendationProductViewLoginTopAds(
        tabName: String,
        homeRecommendationItemDataModel: RecommendationCardModel
    ) = BaseTrackerBuilder().constructBasicProductView(
        event = Event.PRODUCT_VIEW,
        eventCategory = Category.HOMEPAGE,
        eventAction = CustomAction.RECOMMENDATION_VIEW_BASE,
        eventLabel = tabName,
        list = ActionField.RECOMMENDATION_ACTION_FIELD_LOGIN_TOP_ADS.format(
            tabName,
            homeRecommendationItemDataModel.recommendationProductItem.recommendationType,
            homeRecommendationItemDataModel.pageName
        ),
        products = listOf(mapToProductTracking(homeRecommendationItemDataModel))
    ).build()

    fun getRecommendationProductViewNonLogin(
        tabName: String,
        homeRecommendationItemDataModel: RecommendationCardModel
    ) = BaseTrackerBuilder().constructBasicProductView(
        event = Event.PRODUCT_VIEW,
        eventCategory = Category.HOMEPAGE,
        eventAction = CustomAction.RECOMMENDATION_VIEW_BASE,
        eventLabel = tabName,
        list = ActionField.RECOMMENDATION_ACTION_FIELD_NON_LOGIN.format(
            tabName,
            homeRecommendationItemDataModel.recommendationProductItem.recommendationType,
            homeRecommendationItemDataModel.pageName
        ),
        products = listOf(mapToProductTracking(homeRecommendationItemDataModel))
    ).build()

    fun getRecommendationProductViewNonLoginTopAds(
        tabName: String,
        homeRecommendationItemDataModel: RecommendationCardModel
    ) = BaseTrackerBuilder().constructBasicProductView(
        event = Event.PRODUCT_VIEW,
        eventCategory = Category.HOMEPAGE,
        eventAction = CustomAction.RECOMMENDATION_VIEW_BASE,
        eventLabel = tabName,
        list = ActionField.RECOMMENDATION_ACTION_FIELD_NON_LOGIN_TOP_ADS.format(
            tabName,
            homeRecommendationItemDataModel.recommendationProductItem.recommendationType,
            homeRecommendationItemDataModel.pageName
        ),
        products = listOf(mapToProductTracking(homeRecommendationItemDataModel))
    ).build()

    fun getRecommendationAddWishlistLogin(productId: String, tabName: String): Map<String, Any> =
        DataLayer.mapOf(
            Event.KEY,
            CustomEvent.CLICK_HOMEPAGE,
            Category.KEY,
            Category.HOMEPAGE,
            Action.KEY,
            CustomAction.RECOMMENDATION_ADD_WISHLIST_LOGIN,
            Label.KEY,
            "$productId - $tabName"
        )

    fun getRecommendationAddWishlistNonLogin(productId: String, tabName: String): Map<String, Any> =
        DataLayer.mapOf(
            Event.KEY,
            CustomEvent.CLICK_HOMEPAGE,
            Category.KEY,
            Category.HOMEPAGE,
            Action.KEY,
            CustomAction.RECOMMENDATION_ADD_WISHLIST_NON_LOGIN,
            Label.KEY,
            "$productId - $tabName"
        )

    fun getRecommendationRemoveWishlistLogin(productId: String, tabName: String): Map<String, Any> =
        DataLayer.mapOf(
            Event.KEY,
            CustomEvent.CLICK_HOMEPAGE,
            Category.KEY,
            Category.HOMEPAGE,
            Action.KEY,
            CustomAction.RECOMMENDATION_REMOVE_WISHLIST_LOGIN,
            Label.KEY,
            "$productId - $tabName"
        )

    // https://mynakama.tokopedia.com/datatracker/requestdetail/view/4265
    fun sendClickEntityCardTracking(
        model: ContentCardModel,
        position: Int,
        userId: String
    ) {
        val bundle = Bundle().apply {
            putString(Event.KEY, Event.SELECT_CONTENT)
            putString(Action.KEY, CLICK_ON_BANNER_FOR_YOU_WIDGET)
            putString(Category.KEY, Category.HOMEPAGE)
            putString(
                Label.KEY,
                "${model.layoutCard} - ${model.layoutItem} - ${model.title}"
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
            putParcelableArrayList(
                Promotion.KEY,
                arrayListOf(
                    Bundle().also {
                        it.putString(Promotion.CREATIVE_NAME, CustomAction.FOR_YOU_CREATIVE_NAME)
                        it.putString(Promotion.CREATIVE_SLOT, creativeSlot)
                        it.putString(Promotion.ITEM_ID, model.id)
                        it.putString(
                            Promotion.ITEM_NAME,
                            ITEM_NAME_NEW_FOR_YOU_FORMAT.format(
                                creativeSlot,
                                RECOMMENDATION_CARD_FOR_YOU,
                                model.categoryId,
                                model.layoutCard,
                                model.layoutItem,
                                model.title
                            )
                        )
                    }
                )
            )
            putString(UserId.KEY, userId)
        }

        getTracker().sendEnhanceEcommerceEvent(Event.SELECT_CONTENT, bundle)
    }

    fun sendClickVideoRecommendationCardTracking(
        homeRecomPlayWidgetUiModel: PlayCardModel,
        position: Int,
        userId: String
    ) {
        val playWidgetTrackerModel = homeRecomPlayWidgetUiModel.playVideoTrackerUiModel
        val playVideoWidgetUiModel = homeRecomPlayWidgetUiModel.playVideoWidgetUiModel
        val widgetPosition = "0"
        val homeChannelId = "0"

        val itemPosition = (position + Int.ONE).toString()

        val itemId =
            "${homeChannelId}_${homeRecomPlayWidgetUiModel.cardId}_${playWidgetTrackerModel.playChannelId}"

        val bundle = Bundle().apply {
            putString(Event.KEY, Event.SELECT_CONTENT)
            putString(Action.KEY, CLICK_ON_VIDEO_RECOMMENDATION_CARD_FOR_YOU_VIDEO)
            putString(Category.KEY, Category.HOMEPAGE)
            putString(
                Label.KEY,
                "$FOR_YOU - ${playWidgetTrackerModel.videoType} - ${playWidgetTrackerModel.partnerId} - ${playWidgetTrackerModel.playChannelId} - $itemPosition - $widgetPosition - ${playVideoWidgetUiModel.isAutoPlay} - ${playWidgetTrackerModel.recommendationType} - $homeChannelId"
            )
            putString(
                TrackerId.KEY,
                TRACKER_ID_48662
            )
            putString(
                BusinessUnit.KEY,
                PLAY
            )
            putString(
                CampaignCode.KEY,
                ""
            )
            putString(
                ChannelId.KEY,
                homeChannelId
            )
            putString(
                CurrentSite.KEY,
                CurrentSite.DEFAULT
            )
            putParcelableArrayList(
                Promotion.KEY,
                arrayListOf(
                    Bundle().also {
                        it.putString(Promotion.CREATIVE_NAME, CustomAction.FOR_YOU_CREATIVE_NAME)
                        it.putString(Promotion.CREATIVE_SLOT, itemPosition)
                        it.putString(Promotion.ITEM_ID, itemId)
                        it.putString(
                            Promotion.ITEM_NAME,
                            ITEM_NAME_FOR_YOU_VIDEO_FORMAT.format(
                                itemPosition,
                                playWidgetTrackerModel.categoryId,
                                playWidgetTrackerModel.layoutCard,
                                ActionField.PLAY_SHORT_VIDEO_LAYOUT_ITEM,
                                playVideoWidgetUiModel.title
                            )
                        )
                    }
                )
            )
            putString(UserId.KEY, userId)
        }

        getTracker().sendEnhanceEcommerceEvent(Event.SELECT_CONTENT, bundle)
    }

    fun getImpressPlayVideoWidgetTracking(
        homeRecomPlayWidgetUiModel: PlayCardModel,
        position: Int,
        userId: String
    ): HashMap<String, Any> {
        val playWidgetTrackerModel = homeRecomPlayWidgetUiModel.playVideoTrackerUiModel
        val playVideoWidgetUiModel = homeRecomPlayWidgetUiModel.playVideoWidgetUiModel
        val widgetPosition = "0"
        val homeChannelId = "0"

        val itemPosition = (position + Int.ONE).toString()

        val itemName = ITEM_NAME_FOR_YOU_VIDEO_FORMAT.format(
            itemPosition,
            playWidgetTrackerModel.categoryId,
            playWidgetTrackerModel.layoutCard,
            ActionField.PLAY_SHORT_VIDEO_LAYOUT_ITEM,
            playVideoWidgetUiModel.title
        )

        val itemId =
            "${homeChannelId}_${homeRecomPlayWidgetUiModel.cardId}_${playWidgetTrackerModel.playChannelId}"

        val promotion = mapOf(
            Promotion.CREATIVE_NAME to CustomAction.FOR_YOU_CREATIVE_NAME,
            Promotion.CREATIVE_SLOT to itemPosition,
            Items.ITEM_ID to itemId,
            Items.ITEM_NAME to itemName
        )

        return DataLayer.mapOf(
            Event.KEY,
            Event.PROMO_VIEW,
            Action.KEY,
            IMPRESSION_VIDEO_RECOMMENDATION_CARD_FOR_YOU_VIDEO,
            Category.KEY,
            Category.HOMEPAGE,
            Label.KEY,
            "$FOR_YOU - ${playWidgetTrackerModel.videoType} - ${playWidgetTrackerModel.partnerId} - ${playWidgetTrackerModel.playChannelId} - $itemPosition - $widgetPosition - ${playVideoWidgetUiModel.isAutoPlay} - ${playWidgetTrackerModel.recommendationType} - $homeChannelId",
            BusinessUnit.KEY,
            PLAY,
            CurrentSite.KEY,
            CurrentSite.DEFAULT,
            Ecommerce.KEY,
            DataLayer.mapOf(
                Ecommerce.PROMO_VIEW,
                DataLayer.mapOf(
                    Promotion.KEY,
                    listOf(
                        promotion
                    )
                )
            ),
            UserId.KEY,
            userId,
            TrackerId.KEY,
            TRACKER_ID_48661
        ) as HashMap<String, Any>
    }

    // https://mynakama.tokopedia.com/datatracker/requestdetail/view/4265
    fun getImpressEntityCardTracking(
        model: ContentCardModel,
        position: Int,
        userId: String
    ): HashMap<String, Any> {
        val creativeSlot = (position + Int.ONE).toString()
        val itemName = ITEM_NAME_NEW_FOR_YOU_FORMAT.format(
            creativeSlot,
            RECOMMENDATION_CARD_FOR_YOU,
            model.categoryId,
            model.layoutCard,
            model.layoutItem,
            model.title
        )

        return DataLayer.mapOf(
            Event.KEY,
            Event.PROMO_VIEW,
            Action.KEY,
            IMPRESSION_ON_BANNER_RECOMMENDATION_CARD_FOR_YOU,
            Category.KEY,
            Category.HOMEPAGE,
            Label.KEY,
            "${model.layoutCard} - ${model.layoutItem} - ${model.title}",
            BusinessUnit.KEY,
            BusinessUnit.DEFAULT,
            CurrentSite.KEY,
            CurrentSite.DEFAULT,
            Ecommerce.KEY,
            DataLayer.mapOf(
                Ecommerce.PROMO_VIEW,
                DataLayer.mapOf(
                    Promotion.KEY,
                    listOf(
                        mapOf(
                            Promotion.CREATIVE_NAME to CustomAction.FOR_YOU_CREATIVE_NAME,
                            Promotion.CREATIVE_SLOT to creativeSlot,
                            Items.ITEM_ID to model.id,
                            Items.ITEM_NAME to itemName
                        )
                    )
                )
            ),
            UserId.KEY,
            userId,
            TrackerId.KEY,
            TRACKER_ID_47626
        ) as HashMap<String, Any>
    }

    // https://mynakama.tokopedia.com/datatracker/requestdetail/view/4265
    fun getImpressBannerTopAdsTracking(
        homeTopAdsRecommendationBannerTopAdsUiModel: BannerTopAdsModel,
        position: Int,
        userId: String
    ): Map<String, Any> {
        val trackingBuilder = BaseTrackerBuilder()
        val creativeSlot = (position + Int.ONE).toString()
        val itemName = ITEM_NAME_NEW_FOR_YOU_FORMAT.format(
            creativeSlot,
            RECOMMENDATION_CARD_FOR_YOU,
            homeTopAdsRecommendationBannerTopAdsUiModel.categoryId,
            homeTopAdsRecommendationBannerTopAdsUiModel.layoutCard,
            homeTopAdsRecommendationBannerTopAdsUiModel.layoutItem,
            homeTopAdsRecommendationBannerTopAdsUiModel.topAdsImageUiModel?.bannerName.orEmpty()
        )

        val listPromotions = arrayListOf(
            Promotion(
                creative = CustomAction.FOR_YOU_CREATIVE_NAME,
                position = creativeSlot,
                id = homeTopAdsRecommendationBannerTopAdsUiModel.cardId,
                name = itemName
            )
        )

        return trackingBuilder.constructBasicPromotionView(
            event = Event.PROMO_VIEW,
            eventCategory = Category.HOMEPAGE,
            eventAction = IMPRESSION_ON_BANNER_RECOMMENDATION_CARD_FOR_YOU,
            eventLabel = "${homeTopAdsRecommendationBannerTopAdsUiModel.layoutCard} - ${homeTopAdsRecommendationBannerTopAdsUiModel.layoutItem} - ${homeTopAdsRecommendationBannerTopAdsUiModel.topAdsImageUiModel?.bannerName.orEmpty()}",
            promotions = listPromotions
        ).appendBusinessUnit(BusinessUnit.DEFAULT).appendCurrentSite(CurrentSite.DEFAULT)
            .appendUserId(userId).appendCustomKeyValue(
                TrackerId.KEY,
                TRACKER_ID_47626
            ).build()
    }

    // https://mynakama.tokopedia.com/datatracker/requestdetail/view/4265
    fun sendClickBannerTopAdsTracking(
        bannerTopAdsUiModel: BannerTopAdsModel,
        position: Int,
        userId: String
    ) {
        val bundle = Bundle().apply {
            putString(Event.KEY, Event.SELECT_CONTENT)
            putString(Action.KEY, CLICK_ON_BANNER_FOR_YOU_WIDGET)
            putString(Category.KEY, Category.HOMEPAGE)
            putString(
                Label.KEY,
                "${bannerTopAdsUiModel.layoutCard} - ${bannerTopAdsUiModel.layoutItem} - ${bannerTopAdsUiModel.topAdsImageUiModel?.bannerName.orEmpty()}"
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
            putParcelableArrayList(
                Promotion.KEY,
                arrayListOf(
                    Bundle().also {
                        it.putString(Promotion.CREATIVE_NAME, CustomAction.FOR_YOU_CREATIVE_NAME)
                        it.putString(Promotion.CREATIVE_SLOT, creativeSlot)
                        it.putString(Promotion.ITEM_ID, bannerTopAdsUiModel.cardId)
                        it.putString(
                            Promotion.ITEM_NAME,
                            ITEM_NAME_NEW_FOR_YOU_FORMAT.format(
                                creativeSlot,
                                RECOMMENDATION_CARD_FOR_YOU,
                                bannerTopAdsUiModel.categoryId,
                                bannerTopAdsUiModel.layoutCard,
                                bannerTopAdsUiModel.layoutItem,
                                bannerTopAdsUiModel.topAdsImageUiModel?.bannerName.orEmpty()
                            )
                        )
                    }
                )
            )
            putString(UserId.KEY, userId)
        }

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(Event.SELECT_CONTENT, bundle)
    }

    private fun mapToProductTracking(homeRecommendationItemDataModel: RecommendationCardModel) =
        Product(
            id = homeRecommendationItemDataModel.recommendationProductItem.id,
            name = homeRecommendationItemDataModel.recommendationProductItem.name,
            variant = "",
            productPrice = homeRecommendationItemDataModel.recommendationProductItem.priceInt.toString(),
            productPosition = (homeRecommendationItemDataModel.position + 1).toString(),
            isFreeOngkir = homeRecommendationItemDataModel.recommendationProductItem.freeOngkirIsActive && !homeRecommendationItemDataModel.recommendationProductItem.labelGroup.any { it.position == LABEL_FULFILLMENT },
            isFreeOngkirExtra = homeRecommendationItemDataModel.recommendationProductItem.freeOngkirIsActive && homeRecommendationItemDataModel.recommendationProductItem.labelGroup.any { it.position == LABEL_FULFILLMENT },
            category = homeRecommendationItemDataModel.recommendationProductItem.categoryBreadcrumbs,
            brand = "",
            clusterId = homeRecommendationItemDataModel.recommendationProductItem.clusterID,
            isTopAds = null
        )
}
