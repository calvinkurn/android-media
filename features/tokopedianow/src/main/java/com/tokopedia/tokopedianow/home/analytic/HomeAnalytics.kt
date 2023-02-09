package com.tokopedia.tokopedianow.home.analytic

import android.os.Bundle
import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.productcard.ProductCardModel.LabelGroup
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.ACTION.EVENT_ACTION_CLICK_ACCESS_PHOTO_MEDIA_FILES
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.ACTION.EVENT_ACTION_CLICK_CHANNEL_SHARE_BOTTOM_SHEET_SCREENSHOT
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.ACTION.EVENT_ACTION_CLICK_CLOSE_SCREENSHOT_SHARE_BOTTOM_SHEET
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.ACTION.EVENT_ACTION_CLICK_CLOSE_SHARE_BOTTOM_SHEET
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.ACTION.EVENT_ACTION_CLICK_SHARE_WIDGET_BUTTON
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.ACTION.EVENT_ACTION_CLICK_SHARING_CHANNEL
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.ACTION.EVENT_ACTION_IMPRESSION_CHANNEL_SHARE_BOTTOM_SHEET_SCREENSHOT
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.ACTION.EVENT_ACTION_IMPRESSION_SHARING_CHANNEL
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.CATEGORY.EVENT_CATEGORY_TOKOPEDIA_NOW
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.CATEGORY.EVENT_CATEGORY_TOP_NAV
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.CATEGORY.EVENT_CATEGORY_TOP_NAV_TOKOPEDIA_NOW
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_ADD_TO_CART
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_CAMPAIGN_CODE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_CLICK_COMMUNICATION
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_CLICK_GROWTH
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_CLICK_PG
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_CLICK_TOKONOW
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_NAME_ADD_TO_CART
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_NAME_REMOVE_FROM_CART
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_PRODUCT_CLICK
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_PRODUCT_VIEW
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_REMOVE_FROM_CART
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_SELECT_CONTENT
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_VIEW_GROWTH_IRIS
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_VIEW_ITEM
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_VIEW_ITEM_LIST
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_VIEW_TOKONOW_IRIS
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_WAREHOUSE_ID
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_AFFINITY_LABEL
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_BUSINESS_UNIT
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_CAMPAIGN_CODE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_CATEGORY_ID
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_CHANNEL_ID
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_CREATIVE_NAME
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_CREATIVE_SLOT
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_CURRENT_SITE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_DIMENSION_104
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_DIMENSION_38
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_DIMENSION_40
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_DIMENSION_45
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_DIMENSION_49
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_DIMENSION_79
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_DIMENSION_82
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_DIMENSION_84
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_DIMENSION_90
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_INDEX
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_ITEMS
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_ITEM_BRAND
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_ITEM_CATEGORY
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_ITEM_ID
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_ITEM_LIST
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_ITEM_NAME
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_ITEM_VARIANT
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_PAGE_SOURCE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_PRICE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_PRODUCT_ID
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_PROMOTIONS
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_QUANTITY
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_SHOP_ID
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_SHOP_NAME
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_SHOP_TYPE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_TRACKER_ID
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_USER_ID
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_WAREHOUSE_ID
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.BUSINESS_UNIT_SHARING_EXPERIENCE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.CURRENT_SITE_TOKOPEDIA_MARKET_PLACE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.DEFAULT_CATEGORY_ID
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.DEFAULT_NULL_VALUE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.PAGE_NAME_TOKOPEDIA_NOW
import com.tokopedia.tokopedianow.common.constant.ConstantValue.ADDITIONAL_POSITION
import com.tokopedia.tokopedianow.common.model.TokoNowProductCardCarouselItemUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowProductCardUiModel
import com.tokopedia.tokopedianow.common.model.categorymenu.TokoNowCategoryMenuUiModel
import com.tokopedia.tokopedianow.common.util.StringUtil.getOrDefaultZeroString
import com.tokopedia.tokopedianow.common.util.TrackerUtil.getTrackerPosition
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.ACTION.EVENT_ACTION_ATC_PAST_PURCHASE
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.ACTION.EVENT_ACTION_CLICK_ALL_CATEGORY
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.ACTION.EVENT_ACTION_CLICK_ALL_PRODUCT_RECOM
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.ACTION.EVENT_ACTION_CLICK_ALL_PRODUCT_RECOM_OOC
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.ACTION.EVENT_ACTION_CLICK_BANNER_LEFT_CAROUSEL
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.ACTION.EVENT_ACTION_CLICK_CART_BUTTON
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.ACTION.EVENT_ACTION_CLICK_CATEGORY_ON_CATEGORY
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.ACTION.EVENT_ACTION_CLICK_CHECK_DETAIL_RECEIVER_REFERRAL_WIDGET
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.ACTION.EVENT_ACTION_CLICK_CLOSE_QUEST_WIDGET
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.ACTION.EVENT_ACTION_CLICK_LEFT_CAROUSEL_ADD_TO_CART
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.ACTION.EVENT_ACTION_CLICK_LEGO_3
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.ACTION.EVENT_ACTION_CLICK_LEGO_6
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.ACTION.EVENT_ACTION_CLICK_LEGO_6_VIEW_ALL
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.ACTION.EVENT_ACTION_CLICK_MORE_SENDER_REFERRAL_WIDGET
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.ACTION.EVENT_ACTION_CLICK_PAST_PURCHASE
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.ACTION.EVENT_ACTION_CLICK_PRODUCT_LEFT_CAROUSEL
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.ACTION.EVENT_ACTION_CLICK_PRODUCT_RECOM
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.ACTION.EVENT_ACTION_CLICK_PRODUCT_RECOM_ADD_TO_CART
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.ACTION.EVENT_ACTION_CLICK_PRODUCT_RECOM_OOC
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.ACTION.EVENT_ACTION_CLICK_PRODUCT_RECOM_REMOVE_FROM_CART
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.ACTION.EVENT_ACTION_CLICK_QUEST_CARD_QUEST_WIDGET
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.ACTION.EVENT_ACTION_CLICK_REWARD_QUEST_WIDGET
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.ACTION.EVENT_ACTION_CLICK_SEARCH_BAR
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.ACTION.EVENT_ACTION_CLICK_SEE_DETAILS_QUEST_WIDGET
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.ACTION.EVENT_ACTION_CLICK_SHARE_SENDER_REFERRAL_WIDGET
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.ACTION.EVENT_ACTION_CLICK_SLIDER_BANNER
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.ACTION.EVENT_ACTION_CLICK_SWITCHER_WIDGET
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.ACTION.EVENT_ACTION_CLICK_TITLE_CARD_QUEST_WIDGET
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.ACTION.EVENT_ACTION_CLICK_USP_WIDGET
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.ACTION.EVENT_ACTION_CLICK_VIEW_ALL_LEFT_CAROUSEL
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.ACTION.EVENT_ACTION_IMPRESSION_CATEGORY
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.ACTION.EVENT_ACTION_IMPRESSION_FINISHED_QUEST_WIDGET
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.ACTION.EVENT_ACTION_IMPRESSION_LEFT_CAROUSEL
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.ACTION.EVENT_ACTION_IMPRESSION_LEGO_3
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.ACTION.EVENT_ACTION_IMPRESSION_LEGO_6
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.ACTION.EVENT_ACTION_IMPRESSION_PAST_PURCHASE
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.ACTION.EVENT_ACTION_IMPRESSION_PRODUCT_LEFT_CAROUSEL
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.ACTION.EVENT_ACTION_IMPRESSION_PRODUCT_RECOM
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.ACTION.EVENT_ACTION_IMPRESSION_PRODUCT_RECOM_OOC
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.ACTION.EVENT_ACTION_IMPRESSION_QUEST_WIDGET
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.ACTION.EVENT_ACTION_IMPRESSION_RECEIVER_REFERRAL_WIDGET
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.ACTION.EVENT_ACTION_IMPRESSION_SENDER_REFERRAL_WIDGET
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.ACTION.EVENT_ACTION_IMPRESSION_SLIDER_BANNER
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.ACTION.EVENT_ACTION_IMPRESSION_USP_WIDGET
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.ACTION.EVENT_ACTION_VIEW_SWITCHER_WIDGET
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.CATEGORY.EVENT_CATEGORY_HOME_PAGE
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.CATEGORY.EVENT_CATEGORY_HOME_PAGE_WITHOUT_HYPHEN
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.CATEGORY.EVENT_CATEGORY_RECOM_HOME_PAGE
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.TrackerId.ID_CLICK_ATC_PRODUCT_RECOM
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.TrackerId.ID_CLICK_PRODUCT_RECOM
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.TrackerId.ID_CLICK_REMOVE_ATC_PRODUCT_RECOM
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.TrackerId.ID_CLICK_VIEW_ALL
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.TrackerId.ID_IMPRESSION_PRODUCT_RECOM
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.VALUE.HOME_WIDGET
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.VALUE.LABEL_GROUP_HALAL
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.VALUE.LEGO_6_BANNER
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.VALUE.NORMAL_PRICE
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.VALUE.NOW15M
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.VALUE.NOW2HR
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.VALUE.PRODUCT_PAGE_SOURCE
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.VALUE.PRODUCT_TOPADS
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.VALUE.REFERRAL_STATUS
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.VALUE.SLASH_PRICE
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.VALUE.WITHOUT_HALAL_LABEL
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.VALUE.WITHOUT_VARIANT
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.VALUE.WITH_HALAL_LABEL
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.VALUE.WITH_VARIANT
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLeftCarouselAtcProductCardUiModel
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.track.builder.Tracker
import com.tokopedia.track.interfaces.Analytics
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class HomeAnalytics @Inject constructor(private val userSession: UserSessionInterface) {

    /**
     * NOW! Homepage Tracker
     * https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/2664
     */

    object CATEGORY {
        const val EVENT_CATEGORY_HOME_PAGE = "tokonow - homepage"
        const val EVENT_CATEGORY_HOME_PAGE_WITHOUT_HYPHEN = "tokonow homepage"
        const val EVENT_CATEGORY_RECOM_HOME_PAGE = "tokonow - recom homepage"
    }

    object ACTION {
        const val EVENT_ACTION_CLICK_SEARCH_BAR = "click search bar on homepage"
        const val EVENT_ACTION_CLICK_CART_BUTTON = "click cart button on homepage"
        const val EVENT_ACTION_CLICK_SLIDER_BANNER = "click slider banner"
        const val EVENT_ACTION_CLICK_ALL_CATEGORY = "click view all category widget"
        const val EVENT_ACTION_CLICK_CATEGORY_ON_CATEGORY = "click category on category widget"
        const val EVENT_ACTION_IMPRESSION_SLIDER_BANNER = "impression slider banner"
        const val EVENT_ACTION_CLICK_ALL_PRODUCT_RECOM = "click view all on tokonow recom homepage"
        const val EVENT_ACTION_CLICK_ALL_PRODUCT_RECOM_OOC =
            "click 'Cek Sekarang' on recom widget on tokonow homepage while the address is out of coverage (OOC)"
        const val EVENT_ACTION_CLICK_PRODUCT_RECOM =
            "click product on tokonow product recom homepage"
        const val EVENT_ACTION_CLICK_PRODUCT_RECOM_OOC =
            "click product on recom widget on tokonow homepage while the address is out of coverage (OOC)"
        const val EVENT_ACTION_IMPRESSION_PRODUCT_RECOM =
            "impression on tokonow product recom homepage"
        const val EVENT_ACTION_IMPRESSION_PRODUCT_RECOM_OOC =
            "view product on recom widget on tokonow homepage while the address is out of coverage (OOC)"
        const val EVENT_ACTION_IMPRESSION_PAST_PURCHASE = "impression on past purchase widget"
        const val EVENT_ACTION_CLICK_PAST_PURCHASE = "click product on past purchase widget"
        const val EVENT_ACTION_ATC_PAST_PURCHASE = "click atc on past purchase widget"
        const val EVENT_ACTION_CLICK_PRODUCT_RECOM_ADD_TO_CART =
            "click add to cart on tokonow product recom homepage"
        const val EVENT_ACTION_CLICK_LEFT_CAROUSEL_ADD_TO_CART =
            "click add to cart on left carousel"
        const val EVENT_ACTION_CLICK_PRODUCT_RECOM_REMOVE_FROM_CART = "click delete on tokonow product recom homepage"
        const val EVENT_ACTION_IMPRESSION_LEFT_CAROUSEL = "impression left carousel widget"
        const val EVENT_ACTION_CLICK_BANNER_LEFT_CAROUSEL =
            "click left banner on left carousel widget"
        const val EVENT_ACTION_CLICK_VIEW_ALL_LEFT_CAROUSEL =
            "click view all on left carousel widget"
        const val EVENT_ACTION_IMPRESSION_PRODUCT_LEFT_CAROUSEL =
            "impression product on left carousel"
        const val EVENT_ACTION_CLICK_PRODUCT_LEFT_CAROUSEL = "click product on left carousel"
        const val EVENT_ACTION_IMPRESSION_LEGO_3 = "impression lego 3 banner"
        const val EVENT_ACTION_CLICK_LEGO_3 = "click lego 3 banner"
        const val EVENT_ACTION_IMPRESSION_QUEST_WIDGET = "impression quest widget"
        const val EVENT_ACTION_CLICK_SEE_DETAILS_QUEST_WIDGET = "click see details quest widget"
        const val EVENT_ACTION_CLICK_TITLE_CARD_QUEST_WIDGET = "click title card on quest widget"
        const val EVENT_ACTION_CLICK_QUEST_CARD_QUEST_WIDGET = "click quest card on quest widget"
        const val EVENT_ACTION_IMPRESSION_FINISHED_QUEST_WIDGET =
            "impression finish card quest widget"
        const val EVENT_ACTION_CLICK_REWARD_QUEST_WIDGET = "click cek hadiah saya on quest widget"
        const val EVENT_ACTION_CLICK_CLOSE_QUEST_WIDGET = "click close quest widget"
        const val EVENT_ACTION_IMPRESSION_USP_WIDGET = "impression usp widget"
        const val EVENT_ACTION_CLICK_USP_WIDGET = "click drop down on usp widget"
        const val EVENT_ACTION_CLICK_CHECK_DETAIL_RECEIVER_REFERRAL_WIDGET =
            "click cek detail on tokonow referral widget - receiver"
        const val EVENT_ACTION_IMPRESSION_RECEIVER_REFERRAL_WIDGET =
            "view tokonow referral widget - receiver"
        const val EVENT_ACTION_CLICK_SHARE_SENDER_REFERRAL_WIDGET =
            "click bagikan ke temanmu on tokonow referral widget - sender"
        const val EVENT_ACTION_CLICK_MORE_SENDER_REFERRAL_WIDGET =
            "click selengkapnya on tokonow referral widget - sender"
        const val EVENT_ACTION_IMPRESSION_SENDER_REFERRAL_WIDGET =
            "view tokonow referral widget - sender"
        const val EVENT_ACTION_VIEW_SWITCHER_WIDGET = "view switcher widget"
        const val EVENT_ACTION_CLICK_SWITCHER_WIDGET = "click switcher widget"
        const val EVENT_ACTION_IMPRESSION_LEGO_6 = "impression lego 6 banner"
        const val EVENT_ACTION_CLICK_LEGO_6 = "click lego 6 banner"
        const val EVENT_ACTION_CLICK_LEGO_6_VIEW_ALL = "click lego 6 banner view all"
        const val EVENT_ACTION_IMPRESSION_CATEGORY = "impression category banner"
    }

    object VALUE {
        const val PRODUCT_PAGE_SOURCE = "tokonow homepage.tokonow homepage"
        const val LABEL_GROUP_HALAL = "Halal"
        const val WITH_HALAL_LABEL = "with halal label"
        const val WITHOUT_HALAL_LABEL = "without halal label"
        const val SLASH_PRICE = "slash price"
        const val NORMAL_PRICE = "normal price"
        const val WITH_VARIANT = "with variant"
        const val WITHOUT_VARIANT = "without variant"
        const val PRODUCT_TOPADS = "product topads"
        const val HOMEPAGE_TOKONOW = "homepage tokonow"
        const val HOME_WIDGET = "homewidget"
        const val REFERRAL_STATUS = "1"
        const val NOW2HR = "now2hr"
        const val NOW15M = "now15"
        const val LEGO_6_BANNER = "lego 6 banner"
    }

    object TrackerId {
        const val ID_CLICK_VIEW_ALL = "17134"
        const val ID_CLICK_PRODUCT_RECOM = "17135"
        const val ID_CLICK_ATC_PRODUCT_RECOM = "17136"
        const val ID_IMPRESSION_PRODUCT_RECOM = "17137"
        const val ID_CLICK_REMOVE_ATC_PRODUCT_RECOM = "20148"
    }

    fun onClickSearchBar() {
        hitCommonHomeTracker(
            getDataLayer(
                event = EVENT_CLICK_PG,
                action = EVENT_ACTION_CLICK_SEARCH_BAR,
                category = EVENT_CATEGORY_TOP_NAV
            )
        )
    }

    fun onClickCartButton() {
        hitCommonHomeTracker(
            getDataLayer(
                event = EVENT_CLICK_TOKONOW,
                action = EVENT_ACTION_CLICK_CART_BUTTON,
                category = EVENT_CATEGORY_TOP_NAV
            )
        )
    }

    fun onClickAllCategory() {
        hitCommonHomeTracker(
            getDataLayer(
                event = EVENT_CLICK_PG,
                action = EVENT_ACTION_CLICK_ALL_CATEGORY,
                category = EVENT_CATEGORY_HOME_PAGE
            )
        )
    }

    fun onClickBannerPromo(position: Int, channelModel: ChannelModel, channelGrid: ChannelGrid) {
        val dataLayer = getEcommerceDataLayer(
            event = EVENT_SELECT_CONTENT,
            action = EVENT_ACTION_CLICK_SLIDER_BANNER,
            category = EVENT_CATEGORY_HOME_PAGE,
            affinityLabel = channelModel.trackingAttributionModel.persona,
            promotions = arrayListOf(
                ecommerceDataLayerBannerClicked(
                    channelModel = channelModel,
                    channelGrid = channelGrid,
                    position = position
                )
            )
        )
        getTracker().sendEnhanceEcommerceEvent(EVENT_SELECT_CONTENT, dataLayer)
    }

    fun onImpressBannerPromo(channelModel: ChannelModel, channelGrid: ChannelGrid, warehouseId: String, position: Int) {
        val ecommerceDataLayerBanner = ecommerceDataLayerBanner(
            channelModel = channelModel,
            channelGrid = channelGrid,
            position = position,
            itemName = "/ - p${position.getTrackerPosition()} - slider banner - banner - ${channelModel.channelHeader.name}"
        )

        val promotions = arrayListOf(
            ecommerceDataLayerBanner
        )

        val dataLayer = getEcommerceDataLayer(
            event = EVENT_VIEW_ITEM,
            action = EVENT_ACTION_IMPRESSION_SLIDER_BANNER,
            category = EVENT_CATEGORY_HOME_PAGE,
            affinityLabel = channelModel.trackingAttributionModel.persona,
            promotions = promotions
        )
        dataLayer.putString(KEY_WAREHOUSE_ID, warehouseId)
        getTracker().sendEnhanceEcommerceEvent(EVENT_VIEW_ITEM, dataLayer)
    }

    fun onClickCategory(position: Int, categoryId: String, headerName: String, categoryName: String) {
        val dataLayer = getEcommerceDataLayer(
            event = EVENT_SELECT_CONTENT,
            action = EVENT_ACTION_CLICK_CATEGORY_ON_CATEGORY,
            category = EVENT_CATEGORY_HOME_PAGE,
            label = "category_widget - $categoryId - $categoryName",
            affinityLabel = "null",
            promotions = arrayListOf(
                ecommerceDataLayerCategoryClicked(
                    categoryId = categoryId,
                    position = position.getTrackerPosition(),
                    headerName = headerName
                )
            )
        )
        getTracker().sendEnhanceEcommerceEvent(EVENT_SELECT_CONTENT, dataLayer)
    }

    fun trackCategoryImpression(category: TokoNowCategoryMenuUiModel, warehouseId: String) {
        val eventLabel = "${category.id} - ${category.title} - $warehouseId"

        val event = getMarketplaceDataLayer(
            event = EVENT_VIEW_ITEM,
            action = EVENT_ACTION_IMPRESSION_CATEGORY,
            label = eventLabel
        ).apply {
            putString(KEY_CHANNEL_ID, category.id)
            putString(KEY_USER_ID, userSession.userId)
            putString(KEY_WAREHOUSE_ID, warehouseId)
        }

        getTracker().sendEnhanceEcommerceEvent(EVENT_VIEW_ITEM, event)
    }

    fun onClickAllProductRecom(channelId: String, headerName: String, isOoc: Boolean) {
        val dataLayer = getDataLayer(
            event = EVENT_CLICK_TOKONOW,
            action = if (isOoc) EVENT_ACTION_CLICK_ALL_PRODUCT_RECOM_OOC else EVENT_ACTION_CLICK_ALL_PRODUCT_RECOM,
            category = if (isOoc) EVENT_CATEGORY_HOME_PAGE_WITHOUT_HYPHEN else EVENT_CATEGORY_RECOM_HOME_PAGE,
            label = if (isOoc) " - $headerName" else "$channelId - $headerName"
        )

        if (!isOoc) {
            dataLayer[KEY_TRACKER_ID] = ID_CLICK_VIEW_ALL
        }

        hitCommonHomeTracker(dataLayer)
    }

    fun onClickProductRecom(
        channelId: String,
        headerName: String,
        recommendationItem: TokoNowProductCardCarouselItemUiModel,
        position: Int
    ) {
        val productId = recommendationItem.productCardModel.productId
        val dataLayer = getEcommerceDataLayer(
            event = EVENT_SELECT_CONTENT,
            action = EVENT_ACTION_CLICK_PRODUCT_RECOM,
            category = EVENT_CATEGORY_RECOM_HOME_PAGE,
            label = "$channelId - $headerName",
            items = arrayListOf(
                productItemDataLayer(
                    index = position.getTrackerPosition().toString(),
                    productId = recommendationItem.productCardModel.productId,
                    productName = recommendationItem.productCardModel.name,
                    price = recommendationItem.productCardModel.price.filter { it.isDigit() }.toLongOrZero(),
                    productCategory = ""
                ).apply {
                    putString(KEY_DIMENSION_40, "/tokonow - recomproduct - carousel - ${recommendationItem.recomType} - ${recommendationItem.pageName} - $headerName")
                    putString(KEY_DIMENSION_84, channelId)
                }
            ),
            productId = recommendationItem.productCardModel.productId
        )
        dataLayer.putString(KEY_PRODUCT_ID, productId)
        dataLayer.putString(KEY_ITEM_LIST, "/tokonow - recomproduct - carousel - ${recommendationItem.recomType} - ${recommendationItem.pageName} - $headerName")
        dataLayer.putString(KEY_TRACKER_ID, ID_CLICK_PRODUCT_RECOM)

        getTracker().sendEnhanceEcommerceEvent(EVENT_PRODUCT_CLICK, dataLayer)
    }

    fun onImpressProductRecom(
        channelId: String,
        headerName: String,
        recommendationItem: TokoNowProductCardCarouselItemUiModel,
        position: Int
    ) {
        val productId = recommendationItem.productCardModel.productId
        val items = arrayListOf(
            productItemDataLayer(
                index = position.getTrackerPosition().toString(),
                productId = recommendationItem.productCardModel.productId,
                productName = recommendationItem.productCardModel.name,
                price = recommendationItem.productCardModel.price.filter { it.isDigit() }.toLongOrZero(),
                productCategory = ""
            ).apply {
                putString(KEY_DIMENSION_40, "/tokonow - recomproduct - carousel - ${recommendationItem.recomType} - ${recommendationItem.pageName} - $headerName")
                putString(KEY_DIMENSION_84, channelId)
            }
        )
        val dataLayer = getEcommerceDataLayer(
            event = EVENT_VIEW_ITEM_LIST,
            action = EVENT_ACTION_IMPRESSION_PRODUCT_RECOM,
            category = EVENT_CATEGORY_RECOM_HOME_PAGE,
            label = "$channelId - $headerName",
            items = items
        )
        dataLayer.putString(KEY_PRODUCT_ID, productId)
        dataLayer.putString(KEY_ITEM_LIST, "/tokonow - recomproduct - carousel - ${recommendationItem.recomType} - ${recommendationItem.pageName} - $headerName")
        dataLayer.putString(KEY_TRACKER_ID, ID_IMPRESSION_PRODUCT_RECOM)

        getTracker().sendEnhanceEcommerceEvent(EVENT_PRODUCT_VIEW, dataLayer)
    }

    fun onClickProductRecomOoc(
        headerName: String,
        recommendationItem: RecommendationItem,
        position: Int
    ) {
        val productId = recommendationItem.productId
        val dataLayer = getEcommerceDataLayer(
            event = EVENT_SELECT_CONTENT,
            action = EVENT_ACTION_CLICK_PRODUCT_RECOM_OOC,
            category = EVENT_CATEGORY_HOME_PAGE_WITHOUT_HYPHEN,
            label = " - $headerName",
            items = arrayListOf(
                productItemDataLayer(
                    index = position.getTrackerPosition().toString(),
                    productId = recommendationItem.productId.toString(),
                    productName = recommendationItem.name,
                    price = recommendationItem.price.filter { it.isDigit() }.toLongOrZero(),
                    productCategory = recommendationItem.categoryBreadcrumbs
                ).apply {
                    putString(KEY_DIMENSION_90, "$EVENT_CATEGORY_HOME_PAGE_WITHOUT_HYPHEN.$EVENT_CATEGORY_HOME_PAGE_WITHOUT_HYPHEN")
                }
            ),
            productId = recommendationItem.productId.toString()
        )
        dataLayer.putString(KEY_PRODUCT_ID, productId.toString())
        dataLayer.remove(KEY_PAGE_SOURCE)
        dataLayer.putString(
            KEY_ITEM_LIST,
            "/tokonow - ${recommendationItem.pageName} - rekomendasi untuk anda - ${recommendationItem.recommendationType} - ${if (recommendationItem.isTopAds) PRODUCT_TOPADS else ""} - ooc"
        )
        getTracker().sendEnhanceEcommerceEvent(EVENT_SELECT_CONTENT, dataLayer)
    }

    fun onImpressProductRecomOoc(
        headerName: String,
        recommendationItem: RecommendationItem,
        position: Int
    ) {
        val productId = recommendationItem.productId
        val items = arrayListOf(
            productItemDataLayer(
                index = position.getTrackerPosition().toString(),
                productId = recommendationItem.productId.toString(),
                productName = recommendationItem.name,
                price = recommendationItem.price.filter { it.isDigit() }.toLongOrZero(),
                productCategory = recommendationItem.categoryBreadcrumbs
            )
        )
        val dataLayer = getEcommerceDataLayer(
            event = EVENT_VIEW_ITEM_LIST,
            action = EVENT_ACTION_IMPRESSION_PRODUCT_RECOM_OOC,
            category = EVENT_CATEGORY_HOME_PAGE_WITHOUT_HYPHEN,
            label = " - $headerName",
            items = items
        )
        dataLayer.putString(KEY_PRODUCT_ID, productId.toString())
        dataLayer.remove(KEY_PAGE_SOURCE)
        getTracker().sendEnhanceEcommerceEvent(EVENT_VIEW_ITEM_LIST, dataLayer)
    }

    fun onClickProductRecomAddToCart(
        channelId: String,
        headerName: String,
        quantity: String,
        recommendationItem: TokoNowProductCardCarouselItemUiModel,
        position: Int,
        cartId: String
    ) {
        val item = productItemDataLayer(
            index = position.getTrackerPosition().toString(),
            productId = recommendationItem.productCardModel.productId,
            productName = recommendationItem.productCardModel.name,
            price = recommendationItem.productCardModel.price.filter { it.isDigit() }.toLongOrZero()
        ).apply {
            putString(
                KEY_DIMENSION_40,
                "/tokonow - recomproduct - carousel - ${recommendationItem.recomType} - ${recommendationItem.pageName} - $headerName"
            )
            putString(KEY_DIMENSION_45, cartId)
            putString(KEY_DIMENSION_84, channelId)
            putString(KEY_QUANTITY, quantity)
            putString(KEY_SHOP_ID, recommendationItem.shopId)
            putString(KEY_SHOP_NAME, recommendationItem.shopName)
            putString(KEY_SHOP_TYPE, recommendationItem.shopType)
            putString(KEY_CATEGORY_ID, "")
        }

        val productId = recommendationItem.productCardModel.productId
        val dataLayer = getEcommerceDataLayer(
            event = EVENT_ADD_TO_CART,
            action = EVENT_ACTION_CLICK_PRODUCT_RECOM_ADD_TO_CART,
            category = EVENT_CATEGORY_RECOM_HOME_PAGE,
            label = "$channelId - $headerName",
            items = arrayListOf(item),
            productId = productId
        )
        dataLayer.putString(KEY_PRODUCT_ID, productId)
        dataLayer.putString(KEY_TRACKER_ID, ID_CLICK_ATC_PRODUCT_RECOM)

        getTracker().sendEnhanceEcommerceEvent(EVENT_NAME_ADD_TO_CART, dataLayer)
    }

    fun onClickProductRecomRemoveFromCart(
        channelId: String,
        headerName: String,
        quantity: String,
        recommendationItem: TokoNowProductCardCarouselItemUiModel,
        position: Int
    ) {
        val item = productItemDataLayer(
            index = position.getTrackerPosition().toString(),
            productId = recommendationItem.productCardModel.productId,
            productName = recommendationItem.productCardModel.name,
            price = recommendationItem.productCardModel.price.filter { it.isDigit() }.toLongOrZero()
        ).apply {
            putString(KEY_QUANTITY, quantity)
            putString(KEY_SHOP_ID, recommendationItem.shopId)
            putString(KEY_SHOP_NAME, recommendationItem.shopName)
            putString(KEY_SHOP_TYPE, recommendationItem.shopType)
            putString(KEY_CATEGORY_ID, "")
        }

        val dataLayer = getEcommerceDataLayer(
            event = EVENT_REMOVE_FROM_CART,
            action = EVENT_ACTION_CLICK_PRODUCT_RECOM_REMOVE_FROM_CART,
            category = EVENT_CATEGORY_RECOM_HOME_PAGE,
            label = "$channelId - $headerName",
            items = arrayListOf(item),
            productId = recommendationItem.productCardModel.productId,
            pageSource = ""
        )
        dataLayer.putString(KEY_TRACKER_ID, ID_CLICK_REMOVE_ATC_PRODUCT_RECOM)

        getTracker().sendEnhanceEcommerceEvent(EVENT_NAME_REMOVE_FROM_CART, dataLayer)
    }

    fun onClickLeftCarouselAddToCart(
        quantity: String,
        uiModel: HomeLeftCarouselAtcProductCardUiModel,
        cartId: String
    ) {
        val item = productItemDataLayer(
            productId = uiModel.id.toString(),
            productName = uiModel.productCardModel.name,
            price = uiModel.productCardModel.price.filter { it.isDigit() }.toLongOrZero()
        ).apply {
            putString(KEY_DIMENSION_40, "/tokonow - recomproduct - carousel - ${uiModel.recommendationType} - ${uiModel.channelPageName} - ${uiModel.channelHeaderName}")
            putString(KEY_DIMENSION_45, cartId)
            putString(KEY_DIMENSION_90, PRODUCT_PAGE_SOURCE)
            putString(KEY_QUANTITY, quantity)
            putString(KEY_SHOP_ID, uiModel.shopId)
            putString(KEY_SHOP_NAME, uiModel.shopName)
            putString(KEY_SHOP_TYPE, "")
            putString(KEY_CATEGORY_ID, uiModel.categoryId)
        }

        val dataLayer = getEcommerceDataLayer(
            event = EVENT_ADD_TO_CART,
            action = EVENT_ACTION_CLICK_LEFT_CAROUSEL_ADD_TO_CART,
            category = EVENT_CATEGORY_HOME_PAGE,
            label = "${uiModel.channelId} - ${uiModel.channelHeaderName} - ${uiModel.warehouseId}",
            items = arrayListOf(item),
            productId = uiModel.id.toString(),
            pageSource = ""
        ).apply {
            putString(KEY_CAMPAIGN_CODE, uiModel.campaignCode)
            putString(KEY_CHANNEL_ID, uiModel.channelId)
            putString(KEY_WAREHOUSE_ID, uiModel.warehouseId)
        }
        getTracker().sendEnhanceEcommerceEvent(EVENT_ADD_TO_CART, dataLayer)
    }

    fun onImpressRepurchase(
        position: Int,
        data: TokoNowProductCardUiModel
    ) {
        val items = arrayListOf(
            productCardItemDataLayer(
                position = position.toString(),
                id = data.productId,
                name = data.product.productName,
                price = data.product.formattedPrice
            )
        )

        val eventLabel = getProductCardLabel(data)
        val dataLayer = getProductDataLayer(
            event = EVENT_VIEW_ITEM_LIST,
            action = EVENT_ACTION_IMPRESSION_PAST_PURCHASE,
            category = EVENT_CATEGORY_HOME_PAGE,
            label = eventLabel,
            items = items,
            itemList = "/ - p$position - past purchase - carousel - ${data.headerName}"
        )
        getTracker().sendEnhanceEcommerceEvent(EVENT_VIEW_ITEM_LIST, dataLayer)
    }

    fun onClickRepurchase(position: Int, data: TokoNowProductCardUiModel) {
        val items = arrayListOf(
            productCardItemDataLayer(
                position = position.toString(),
                id = data.productId,
                name = data.product.productName,
                price = data.product.formattedPrice
            )
        )

        val eventLabel = getProductCardLabel(data)

        val dataLayer = getProductDataLayer(
            event = EVENT_SELECT_CONTENT,
            action = EVENT_ACTION_CLICK_PAST_PURCHASE,
            category = EVENT_CATEGORY_HOME_PAGE,
            label = eventLabel,
            items = items,
            itemList = "/ - p$position - past purchase - carousel - ${data.headerName}"
        )
        getTracker().sendEnhanceEcommerceEvent(EVENT_SELECT_CONTENT, dataLayer)
    }

    fun onRepurchaseAddToCart(quantity: Int, data: TokoNowProductCardUiModel) {
        val items = arrayListOf(
            productCardItemDataLayer(
                id = data.productId,
                name = data.product.productName,
                price = data.product.formattedPrice
            ).apply {
                putString(KEY_CATEGORY_ID, "")
                putString(KEY_QUANTITY, quantity.toString())
                putString(KEY_SHOP_ID, data.shopId)
                putString(KEY_SHOP_NAME, "")
                putString(KEY_SHOP_TYPE, "")
            }
        )

        val eventLabel = getProductCardLabel(data)
        val dataLayer = getProductDataLayer(
            event = EVENT_ADD_TO_CART,
            action = EVENT_ACTION_ATC_PAST_PURCHASE,
            category = EVENT_CATEGORY_HOME_PAGE,
            label = eventLabel,
            items = items
        )
        getTracker().sendEnhanceEcommerceEvent(EVENT_ADD_TO_CART, dataLayer)
    }

    fun trackImpressionLeftCarousel(channelId: String, channelHeaderName: String) {
        val dataLayer = getMarketplaceDataLayer(
            event = EVENT_VIEW_ITEM,
            action = EVENT_ACTION_IMPRESSION_LEFT_CAROUSEL,
            label = "$channelId - $channelHeaderName"
        )

        getTracker().sendEnhanceEcommerceEvent(EVENT_VIEW_ITEM, dataLayer)
    }

    fun trackClickBannerLeftCarousel(channelId: String, channelHeaderName: String) {
        val dataLayer = getMarketplaceDataLayer(
            event = EVENT_SELECT_CONTENT,
            action = EVENT_ACTION_CLICK_BANNER_LEFT_CAROUSEL,
            label = "$channelId - $channelHeaderName"
        )

        getTracker().sendEnhanceEcommerceEvent(EVENT_SELECT_CONTENT, dataLayer)
    }

    fun trackClickViewAllLeftCarousel(id: String, headerName: String) {
        val dataLayer = getMarketplaceDataLayer(
            event = EVENT_SELECT_CONTENT,
            action = EVENT_ACTION_CLICK_VIEW_ALL_LEFT_CAROUSEL,
            label = "$id - $headerName"
        )

        getTracker().sendEnhanceEcommerceEvent(EVENT_SELECT_CONTENT, dataLayer)
    }

    fun trackImpressionProductLeftCarousel(
        position: Int,
        product: HomeLeftCarouselAtcProductCardUiModel
    ) {
        val productItem = productItemDataLayer(
            index = position.getTrackerPosition().toString(),
            productId = product.id.orEmpty(),
            productName = product.productCardModel.name,
            price = product.productCardModel.price.filter { it.isDigit() }.toLongOrZero(),
            productBrand = product.brandId,
            productCategory = product.categoryId
        )
        productItem.putString(KEY_DIMENSION_90, PRODUCT_PAGE_SOURCE)

        val items = arrayListOf(productItem)

        val dataLayer = getMarketplaceDataLayer(
            event = EVENT_VIEW_ITEM_LIST,
            action = EVENT_ACTION_IMPRESSION_PRODUCT_LEFT_CAROUSEL,
            label = "${product.channelId} - ${product.channelHeaderName}"
        ).apply {
            putParcelableArrayList(KEY_ITEMS, items)
            putString(KEY_USER_ID, userSession.userId)
            putString(KEY_ITEM_LIST, "/ - p$position - dynamic channel left carousel - carousel - ${product.channelHeaderName}")
        }

        getTracker().sendEnhanceEcommerceEvent(EVENT_VIEW_ITEM_LIST, dataLayer)
    }

    fun trackImpressionProductLeftCarousel(
        position: Int,
        channelModel: ChannelModel,
        grid: ChannelGrid
    ) {
        val items = arrayListOf(
            productItemDataLayer(
                index = position.toString(),
                productId = grid.id,
                productName = grid.name,
                price = grid.price.filter { it.isDigit() }.toLongOrZero(),
                productBrand = grid.brandId,
                productCategory = grid.categoryId
            )
        )

        val dataLayer = getMarketplaceDataLayer(
            event = EVENT_VIEW_ITEM_LIST,
            action = EVENT_ACTION_IMPRESSION_PRODUCT_LEFT_CAROUSEL,
            label = "${channelModel.id} - ${channelModel.channelHeader.name}"
        ).apply {
            putParcelableArrayList(KEY_ITEMS, items)
            putString(KEY_USER_ID, userSession.userId)
        }

        getTracker().sendEnhanceEcommerceEvent(EVENT_VIEW_ITEM_LIST, dataLayer)
    }

    fun trackClickProductLeftCarousel(
        position: Int,
        product: HomeLeftCarouselAtcProductCardUiModel
    ) {
        val headerName = product.channelHeaderName

        val items = arrayListOf(
            productItemDataLayer(
                index = position.getTrackerPosition().toString(),
                productId = product.id.orEmpty(),
                productName = product.productCardModel.name,
                price = product.productCardModel.price.filter { it.isDigit() }.toLongOrZero(),
                productBrand = product.brandId,
                productCategory = product.categoryId
            )
        )

        val itemList = "/ - p$position - dynamic channel left carousel - carousel - $headerName"
        val dataLayer = getMarketplaceDataLayer(
            event = EVENT_SELECT_CONTENT,
            action = EVENT_ACTION_CLICK_PRODUCT_LEFT_CAROUSEL,
            label = "${product.id} - $headerName"
        ).apply {
            putString(KEY_ITEM_LIST, itemList)
            putParcelableArrayList(KEY_ITEMS, items)
            putString(KEY_USER_ID, userSession.userId)
        }

        getTracker().sendEnhanceEcommerceEvent(EVENT_SELECT_CONTENT, dataLayer)
    }

    fun trackClickProductLeftCarousel(
        position: Int,
        channelModel: ChannelModel,
        grid: ChannelGrid
    ) {
        val headerName = channelModel.channelHeader.name

        val items = arrayListOf(
            productItemDataLayer(
                index = position.toString(),
                productId = grid.id,
                productName = grid.name,
                price = grid.price.filter { it.isDigit() }.toLongOrZero(),
                productBrand = grid.brandId,
                productCategory = grid.categoryId
            )
        )

        val itemList = "/ - p$position - dynamic channel left carousel - carousel - $headerName"
        val dataLayer = getMarketplaceDataLayer(
            event = EVENT_SELECT_CONTENT,
            action = EVENT_ACTION_CLICK_PRODUCT_LEFT_CAROUSEL,
            label = "${channelModel.id} - $headerName"
        ).apply {
            putString(KEY_ITEM_LIST, itemList)
            putParcelableArrayList(KEY_ITEMS, items)
            putString(KEY_USER_ID, userSession.userId)
        }

        getTracker().sendEnhanceEcommerceEvent(EVENT_SELECT_CONTENT, dataLayer)
    }

    fun trackImpressionLego3Banner(channelModel: ChannelModel) {
        val promotions = ArrayList(
            channelModel.channelGrids.mapIndexed { position, channelGrid ->
                ecommerceDataLayerBanner(
                    channelModel = channelModel,
                    channelGrid = channelGrid,
                    position = position,
                    itemName = "/ - p${position.getTrackerPosition()} - lego 3 banner - ${channelModel.channelHeader.name}"
                )
            }
        )

        val dataLayer = getMarketplaceDataLayer(
            event = EVENT_VIEW_ITEM,
            action = EVENT_ACTION_IMPRESSION_LEGO_3,
            label = "${channelModel.id} - ${channelModel.channelHeader.name}"
        ).apply {
            putParcelableArrayList(KEY_PROMOTIONS, promotions)
            putString(KEY_USER_ID, userSession.userId)
        }

        getTracker().sendEnhanceEcommerceEvent(EVENT_VIEW_ITEM, dataLayer)
    }

    fun trackClickLego3Banner(position: Int, channelModel: ChannelModel, channelGrid: ChannelGrid) {
        val dataLayerBanner = ecommerceDataLayerBanner(
            channelModel = channelModel,
            channelGrid = channelGrid,
            position = position,
            itemName = "/ - p${position.getTrackerPosition()} - lego 3 banner - ${channelModel.channelHeader.name}"
        )

        val promotions = arrayListOf(dataLayerBanner)

        val dataLayer = getMarketplaceDataLayer(
            event = EVENT_SELECT_CONTENT,
            action = EVENT_ACTION_CLICK_LEGO_3,
            label = "${channelModel.id} - ${channelModel.channelHeader.name} - ${channelGrid.name}"
        ).apply {
            putParcelableArrayList(KEY_PROMOTIONS, promotions)
            putString(KEY_USER_ID, userSession.userId)
        }

        getTracker().sendEnhanceEcommerceEvent(EVENT_SELECT_CONTENT, dataLayer)
    }

    fun trackImpressionLego6Banner(channelModel: ChannelModel, warehouseId: String, position: Int) {
        val nullString = "null"
        val headerName = channelModel.channelHeader.name
        val eventLabel = "${channelModel.id} - $headerName - $warehouseId - ${position + 1}"

        val promotions = ArrayList(
            channelModel.channelGrids.mapIndexed { position, channelGrid ->
                Bundle().apply {
                    val gridPosition = position.getTrackerPosition().toString()
                    putString(KEY_CREATIVE_NAME, channelGrid.attribution)
                    putString(KEY_CREATIVE_SLOT, gridPosition)
                    putString(KEY_ITEM_ID, "${channelModel.id}_${channelGrid.id}_${nullString}_$nullString")
                    putString(KEY_ITEM_NAME, "/ - p$gridPosition - $LEGO_6_BANNER - $headerName")
                }
            }
        )

        val event = getMarketplaceDataLayer(
            event = EVENT_VIEW_ITEM,
            action = EVENT_ACTION_IMPRESSION_LEGO_6,
            label = eventLabel
        ).apply {
            putString(KEY_CAMPAIGN_CODE, channelModel.trackingAttributionModel.campaignCode)
            putString(KEY_CHANNEL_ID, channelModel.id)
            putParcelableArrayList(KEY_PROMOTIONS, promotions)
            putString(KEY_WAREHOUSE_ID, warehouseId)
            putString(KEY_USER_ID, userSession.userId)
        }

        getTracker().sendEnhanceEcommerceEvent(EVENT_VIEW_ITEM, event)
    }

    fun trackClickLego6Banner(
        channelModel: ChannelModel,
        channelGrid: ChannelGrid,
        warehouseId: String,
        position: Int,
        parentPosition: Int
    ) {
        val nullString = "null"
        val headerName = channelModel.channelHeader.name
        val eventLabel = "${channelModel.id} - $headerName - $warehouseId - ${parentPosition.getTrackerPosition()}"

        val promotions = arrayListOf(
            Bundle().apply {
                val gridPosition = position.getTrackerPosition().toString()
                putString(KEY_CREATIVE_NAME, channelGrid.attribution)
                putString(KEY_CREATIVE_SLOT, gridPosition)
                putString(KEY_ITEM_ID, "${channelModel.id}_${channelGrid.id}_${nullString}_$nullString")
                putString(KEY_ITEM_NAME, "/ - p$gridPosition - $LEGO_6_BANNER - $headerName")
            }
        )

        val event = getMarketplaceDataLayer(
            event = EVENT_SELECT_CONTENT,
            action = EVENT_ACTION_CLICK_LEGO_6,
            label = eventLabel
        ).apply {
            putString(KEY_CAMPAIGN_CODE, channelModel.trackingAttributionModel.campaignCode)
            putString(KEY_CHANNEL_ID, channelModel.id)
            putParcelableArrayList(KEY_PROMOTIONS, promotions)
            putString(KEY_WAREHOUSE_ID, warehouseId)
            putString(KEY_USER_ID, userSession.userId)
        }

        getTracker().sendEnhanceEcommerceEvent(EVENT_SELECT_CONTENT, event)
    }

    fun trackClickLego6ViewAll(channelModel: ChannelModel, warehouseId: String, position: Int) {
        val headerName = channelModel.channelHeader.name
        val eventLabel = "${channelModel.id} - $headerName - $warehouseId - ${position + 1}"

        val event = getMarketplaceDataLayer(
            event = EVENT_SELECT_CONTENT,
            action = EVENT_ACTION_CLICK_LEGO_6_VIEW_ALL,
            label = eventLabel
        ).apply {
            putString(KEY_CAMPAIGN_CODE, channelModel.trackingAttributionModel.campaignCode)
            putString(KEY_CHANNEL_ID, channelModel.id)
            putString(KEY_WAREHOUSE_ID, warehouseId)
            putString(KEY_USER_ID, userSession.userId)
        }

        getTracker().sendEnhanceEcommerceEvent(EVENT_SELECT_CONTENT, event)
    }

    fun trackImpressionQuestWidget() {
        val dataLayer = getMarketplaceDataLayer(
            event = EVENT_VIEW_ITEM,
            action = EVENT_ACTION_IMPRESSION_QUEST_WIDGET
        )

        getTracker().sendEnhanceEcommerceEvent(EVENT_VIEW_ITEM, dataLayer)
    }

    fun trackClickSeeDetailsQuestWidget() {
        val dataLayer = getMarketplaceDataLayer(
            event = EVENT_SELECT_CONTENT,
            action = EVENT_ACTION_CLICK_SEE_DETAILS_QUEST_WIDGET
        )

        getTracker().sendEnhanceEcommerceEvent(EVENT_SELECT_CONTENT, dataLayer)
    }

    fun trackClickTitleCardQuestWidget() {
        val dataLayer = getMarketplaceDataLayer(
            event = EVENT_SELECT_CONTENT,
            action = EVENT_ACTION_CLICK_TITLE_CARD_QUEST_WIDGET
        )

        getTracker().sendEnhanceEcommerceEvent(EVENT_SELECT_CONTENT, dataLayer)
    }

    fun trackClickCardQuestWidget() {
        val dataLayer = getMarketplaceDataLayer(
            event = EVENT_SELECT_CONTENT,
            action = EVENT_ACTION_CLICK_QUEST_CARD_QUEST_WIDGET
        )

        getTracker().sendEnhanceEcommerceEvent(EVENT_SELECT_CONTENT, dataLayer)
    }

    fun trackImpressionFinishedQuestWidget() {
        val dataLayer = getMarketplaceDataLayer(
            event = EVENT_VIEW_ITEM,
            action = EVENT_ACTION_IMPRESSION_FINISHED_QUEST_WIDGET
        )

        getTracker().sendEnhanceEcommerceEvent(EVENT_VIEW_ITEM, dataLayer)
    }

    fun trackClickRewardQuestWidget() {
        val dataLayer = getMarketplaceDataLayer(
            event = EVENT_SELECT_CONTENT,
            action = EVENT_ACTION_CLICK_REWARD_QUEST_WIDGET
        )

        getTracker().sendEnhanceEcommerceEvent(EVENT_SELECT_CONTENT, dataLayer)
    }

    fun trackClickCloseQuestWidget() {
        val dataLayer = getMarketplaceDataLayer(
            event = EVENT_SELECT_CONTENT,
            action = EVENT_ACTION_CLICK_CLOSE_QUEST_WIDGET
        )

        getTracker().sendEnhanceEcommerceEvent(EVENT_SELECT_CONTENT, dataLayer)
    }

    fun trackImpressionUSPWidget() {
        val dataLayer = getMarketplaceDataLayer(
            event = EVENT_VIEW_ITEM,
            action = EVENT_ACTION_IMPRESSION_USP_WIDGET
        )

        getTracker().sendEnhanceEcommerceEvent(EVENT_VIEW_ITEM, dataLayer)
    }

    fun trackClickUSPWidget() {
        val dataLayer = getMarketplaceDataLayer(
            event = EVENT_SELECT_CONTENT,
            action = EVENT_ACTION_CLICK_USP_WIDGET
        )

        getTracker().sendEnhanceEcommerceEvent(EVENT_SELECT_CONTENT, dataLayer)
    }

    /*
        -- Global Sharing Bottom Sheet --
        Thanos : https://mynakama.tokopedia.com/datatracker/requestdetail/1963
    */

    // - 1
    fun trackClickShareButtonTopNav() {
        val label = "$DEFAULT_CATEGORY_ID - " +
            "$DEFAULT_CATEGORY_ID - $DEFAULT_CATEGORY_ID"

        val pageSource =
            "$PAGE_NAME_TOKOPEDIA_NOW.$DEFAULT_NULL_VALUE.$DEFAULT_NULL_VALUE.$DEFAULT_NULL_VALUE"

        val dataLayer = getDataLayer(
            EVENT_CLICK_COMMUNICATION,
            EVENT_ACTION_CLICK_SHARE_WIDGET_BUTTON,
            EVENT_CATEGORY_TOP_NAV_TOKOPEDIA_NOW,
            label
        )

        dataLayer[KEY_PAGE_SOURCE] = pageSource
        dataLayer[KEY_BUSINESS_UNIT] = BUSINESS_UNIT_SHARING_EXPERIENCE
        dataLayer[KEY_CURRENT_SITE] = BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE
        dataLayer[KEY_USER_ID] = userSession.userId.getOrDefaultZeroString()

        getTracker().sendGeneralEvent(dataLayer)
    }

    // - 2
    fun trackClickCloseShareBottomSheet() {
        val label = "$DEFAULT_CATEGORY_ID - " +
            "$DEFAULT_CATEGORY_ID - $DEFAULT_CATEGORY_ID"

        val dataLayer = getDataLayer(
            EVENT_CLICK_COMMUNICATION,
            EVENT_ACTION_CLICK_CLOSE_SHARE_BOTTOM_SHEET,
            EVENT_CATEGORY_TOKOPEDIA_NOW,
            label
        )

        dataLayer[KEY_BUSINESS_UNIT] = BUSINESS_UNIT_SHARING_EXPERIENCE
        dataLayer[KEY_CURRENT_SITE] = BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE
        dataLayer[KEY_USER_ID] = userSession.userId.getOrDefaultZeroString()

        getTracker().sendGeneralEvent(dataLayer)
    }

    // - 3
    fun trackClickChannelShareBottomSheet(channel: String) {
        val label = "$channel - $DEFAULT_CATEGORY_ID - " +
            "$DEFAULT_CATEGORY_ID - $DEFAULT_CATEGORY_ID"

        val dataLayer = getDataLayer(
            EVENT_CLICK_TOKONOW,
            EVENT_ACTION_CLICK_SHARING_CHANNEL,
            EVENT_CATEGORY_TOKOPEDIA_NOW,
            label
        )

        dataLayer[KEY_BUSINESS_UNIT] = BUSINESS_UNIT_SHARING_EXPERIENCE
        dataLayer[KEY_CURRENT_SITE] = BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE
        dataLayer[KEY_USER_ID] = userSession.userId.getOrDefaultZeroString()

        getTracker().sendGeneralEvent(dataLayer)
    }

    // - 4
    fun trackImpressChannelShareBottomSheet() {
        val label = "$DEFAULT_CATEGORY_ID - " +
            "$DEFAULT_CATEGORY_ID - $DEFAULT_CATEGORY_ID"

        val dataLayer = getDataLayer(
            EVENT_VIEW_TOKONOW_IRIS,
            EVENT_ACTION_IMPRESSION_SHARING_CHANNEL,
            EVENT_CATEGORY_TOKOPEDIA_NOW,
            label
        )

        dataLayer[KEY_BUSINESS_UNIT] = BUSINESS_UNIT_SHARING_EXPERIENCE
        dataLayer[KEY_CURRENT_SITE] = BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE
        dataLayer[KEY_USER_ID] = userSession.userId.getOrDefaultZeroString()

        getTracker().sendGeneralEvent(dataLayer)
    }

    // - 5
    fun trackImpressChannelShareBottomSheetScreenShot() {
        val label = "$DEFAULT_CATEGORY_ID - " +
            "$DEFAULT_CATEGORY_ID - $DEFAULT_CATEGORY_ID"

        val dataLayer = getDataLayer(
            EVENT_VIEW_TOKONOW_IRIS,
            EVENT_ACTION_IMPRESSION_CHANNEL_SHARE_BOTTOM_SHEET_SCREENSHOT,
            EVENT_CATEGORY_TOKOPEDIA_NOW,
            label
        )

        dataLayer[KEY_BUSINESS_UNIT] = BUSINESS_UNIT_SHARING_EXPERIENCE
        dataLayer[KEY_CURRENT_SITE] = BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE
        dataLayer[KEY_USER_ID] = userSession.userId.getOrDefaultZeroString()

        getTracker().sendGeneralEvent(dataLayer)
    }

    // - 6
    fun trackClickCloseScreenShotShareBottomSheet() {
        val label = "$DEFAULT_CATEGORY_ID - " +
            "$DEFAULT_CATEGORY_ID - $DEFAULT_CATEGORY_ID"

        val dataLayer = getDataLayer(
            EVENT_CLICK_TOKONOW,
            EVENT_ACTION_CLICK_CLOSE_SCREENSHOT_SHARE_BOTTOM_SHEET,
            EVENT_CATEGORY_TOKOPEDIA_NOW,
            label
        )

        dataLayer[KEY_BUSINESS_UNIT] = BUSINESS_UNIT_SHARING_EXPERIENCE
        dataLayer[KEY_CURRENT_SITE] = BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE
        dataLayer[KEY_USER_ID] = userSession.userId.getOrDefaultZeroString()

        getTracker().sendGeneralEvent(dataLayer)
    }

    // - 7
    fun trackClickChannelShareBottomSheetScreenshot(channel: String) {
        val label = "$channel - $DEFAULT_CATEGORY_ID - " +
            "$DEFAULT_CATEGORY_ID - $DEFAULT_CATEGORY_ID"

        val dataLayer = getDataLayer(
            EVENT_CLICK_TOKONOW,
            EVENT_ACTION_CLICK_CHANNEL_SHARE_BOTTOM_SHEET_SCREENSHOT,
            EVENT_CATEGORY_TOKOPEDIA_NOW,
            label
        )

        dataLayer[KEY_BUSINESS_UNIT] = BUSINESS_UNIT_SHARING_EXPERIENCE
        dataLayer[KEY_CURRENT_SITE] = BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE
        dataLayer[KEY_USER_ID] = userSession.userId.getOrDefaultZeroString()

        getTracker().sendGeneralEvent(dataLayer)
    }

    // - 8
    fun trackClickAccessMediaAndFiles(accessText: String) {
        val label = "$accessText - $DEFAULT_CATEGORY_ID - " +
            "$DEFAULT_CATEGORY_ID - $DEFAULT_CATEGORY_ID"

        val dataLayer = getDataLayer(
            EVENT_CLICK_COMMUNICATION,
            EVENT_ACTION_CLICK_ACCESS_PHOTO_MEDIA_FILES,
            EVENT_CATEGORY_TOKOPEDIA_NOW,
            label
        )

        dataLayer[KEY_BUSINESS_UNIT] = BUSINESS_UNIT_SHARING_EXPERIENCE
        dataLayer[KEY_CURRENT_SITE] = BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE
        dataLayer[KEY_USER_ID] = userSession.userId.getOrDefaultZeroString()

        getTracker().sendGeneralEvent(dataLayer)
    }

    // - 9
    fun trackClickShareButtonWidget() {
        val label = "$HOME_WIDGET - $DEFAULT_CATEGORY_ID - " +
            "$DEFAULT_CATEGORY_ID - $DEFAULT_CATEGORY_ID"

        val dataLayer = getDataLayer(
            EVENT_CLICK_COMMUNICATION,
            EVENT_ACTION_CLICK_SHARE_WIDGET_BUTTON,
            EVENT_CATEGORY_TOKOPEDIA_NOW,
            label
        )

        dataLayer[KEY_BUSINESS_UNIT] = BUSINESS_UNIT_SHARING_EXPERIENCE
        dataLayer[KEY_CURRENT_SITE] = BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE
        dataLayer[KEY_USER_ID] = userSession.userId.getOrDefaultZeroString()

        getTracker().sendGeneralEvent(dataLayer)
    }

    /*
        -- Referral Widget --
        Thanos : https://mynakama.tokopedia.com/datatracker/requestdetail/view/2906
    */

    private fun getEventLabelReferralWidget(
        slug: String,
        referralCode: String,
        userStatus: String,
        referralStatus: String,
        warehouseId: String
    ): String {
        return "$slug - $referralCode - $userStatus - $referralStatus - $warehouseId"
    }

    // - 1
    fun sendImpressSenderReferralWidget(
        slug: String,
        referralCode: String,
        userStatus: String,
        campaignCode: String,
        warehouseId: String
    ) {
        Tracker.Builder()
            .setEvent(EVENT_VIEW_ITEM)
            .setEventAction(EVENT_ACTION_IMPRESSION_SENDER_REFERRAL_WIDGET)
            .setEventCategory(EVENT_CATEGORY_HOME_PAGE)
            .setEventLabel(
                getEventLabelReferralWidget(
                    slug = slug,
                    referralCode = referralCode,
                    userStatus = userStatus,
                    referralStatus = REFERRAL_STATUS,
                    warehouseId = warehouseId
                )
            )
            .setBusinessUnit(BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE)
            .setCustomProperty(EVENT_CAMPAIGN_CODE, campaignCode)
            .setCurrentSite(CURRENT_SITE_TOKOPEDIA_MARKET_PLACE)
            .setUserId(userSession.userId.getOrDefaultZeroString())
            .setCustomProperty(EVENT_WAREHOUSE_ID, warehouseId)
            .build()
            .send()
    }

    // - 2
    fun sendClickMoreSenderReferralWidget(
        slug: String,
        referralCode: String,
        userStatus: String,
        campaignCode: String,
        warehouseId: String
    ) {
        Tracker.Builder()
            .setEvent(EVENT_SELECT_CONTENT)
            .setEventAction(EVENT_ACTION_CLICK_MORE_SENDER_REFERRAL_WIDGET)
            .setEventCategory(EVENT_CATEGORY_HOME_PAGE)
            .setEventLabel(
                getEventLabelReferralWidget(
                    slug = slug,
                    referralCode = referralCode,
                    userStatus = userStatus,
                    referralStatus = REFERRAL_STATUS,
                    warehouseId = warehouseId
                )
            )
            .setBusinessUnit(BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE)
            .setCustomProperty(EVENT_CAMPAIGN_CODE, campaignCode)
            .setCurrentSite(CURRENT_SITE_TOKOPEDIA_MARKET_PLACE)
            .setUserId(userSession.userId.getOrDefaultZeroString())
            .setCustomProperty(EVENT_WAREHOUSE_ID, warehouseId)
            .build()
            .send()
    }

    // - 3
    fun sendClickShareSenderReferralWidget(
        slug: String,
        referralCode: String,
        userStatus: String,
        campaignCode: String,
        warehouseId: String
    ) {
        Tracker.Builder()
            .setEvent(EVENT_SELECT_CONTENT)
            .setEventAction(EVENT_ACTION_CLICK_SHARE_SENDER_REFERRAL_WIDGET)
            .setEventCategory(EVENT_CATEGORY_HOME_PAGE)
            .setEventLabel(
                getEventLabelReferralWidget(
                    slug = slug,
                    referralCode = referralCode,
                    userStatus = userStatus,
                    referralStatus = REFERRAL_STATUS,
                    warehouseId = warehouseId
                )
            )
            .setBusinessUnit(BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE)
            .setCustomProperty(EVENT_CAMPAIGN_CODE, campaignCode)
            .setCurrentSite(CURRENT_SITE_TOKOPEDIA_MARKET_PLACE)
            .setUserId(userSession.userId.getOrDefaultZeroString())
            .setCustomProperty(EVENT_WAREHOUSE_ID, warehouseId)
            .build()
            .send()
    }

    // - 4
    fun sendImpressReceiverReferralWidget(
        slug: String,
        referralCode: String,
        userStatus: String,
        campaignCode: String,
        warehouseId: String
    ) {
        Tracker.Builder()
            .setEvent(EVENT_VIEW_ITEM)
            .setEventAction(EVENT_ACTION_IMPRESSION_RECEIVER_REFERRAL_WIDGET)
            .setEventCategory(EVENT_CATEGORY_HOME_PAGE)
            .setEventLabel(
                getEventLabelReferralWidget(
                    slug = slug,
                    referralCode = referralCode,
                    userStatus = userStatus,
                    referralStatus = REFERRAL_STATUS,
                    warehouseId = warehouseId
                )
            )
            .setBusinessUnit(BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE)
            .setCustomProperty(EVENT_CAMPAIGN_CODE, campaignCode)
            .setCurrentSite(CURRENT_SITE_TOKOPEDIA_MARKET_PLACE)
            .setUserId(userSession.userId.getOrDefaultZeroString())
            .setCustomProperty(EVENT_WAREHOUSE_ID, warehouseId)
            .build()
            .send()
    }

    // - 5
    fun sendClickCheckDetailReceiverReferralWidget(
        slug: String,
        referralCode: String,
        userStatus: String,
        campaignCode: String,
        warehouseId: String
    ) {
        Tracker.Builder()
            .setEvent(EVENT_SELECT_CONTENT)
            .setEventAction(EVENT_ACTION_CLICK_CHECK_DETAIL_RECEIVER_REFERRAL_WIDGET)
            .setEventCategory(EVENT_CATEGORY_HOME_PAGE)
            .setEventLabel(
                getEventLabelReferralWidget(
                    slug = slug,
                    referralCode = referralCode,
                    userStatus = userStatus,
                    referralStatus = REFERRAL_STATUS,
                    warehouseId = warehouseId
                )
            )
            .setBusinessUnit(BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE)
            .setCustomProperty(EVENT_CAMPAIGN_CODE, campaignCode)
            .setCurrentSite(CURRENT_SITE_TOKOPEDIA_MARKET_PLACE)
            .setUserId(userSession.userId.getOrDefaultZeroString())
            .setCustomProperty(EVENT_WAREHOUSE_ID, warehouseId)
            .build()
            .send()
    }

    /*
        -- Switcher Widget --
        Thanos : https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/2972
    */

    private fun getEventLabelSwitcherWidget(
        userId: String,
        whIdOrigin: String,
        whIdDestination: String,
        isNow15: Boolean
    ): String {
        var switcherName = NOW2HR
        if (isNow15) {
            switcherName = NOW15M
        }
        return "$switcherName - $userId - $whIdOrigin - $whIdDestination"
    }

    // - 1
    fun sendImpressSwitcherWidget(
        userId: String,
        whIdOrigin: String,
        whIdDestination: String,
        isNow15: Boolean
    ) {
        Tracker.Builder()
            .setEvent(EVENT_VIEW_GROWTH_IRIS)
            .setEventAction(EVENT_ACTION_VIEW_SWITCHER_WIDGET)
            .setEventCategory(EVENT_CATEGORY_HOME_PAGE)
            .setEventLabel(
                getEventLabelSwitcherWidget(
                    userId,
                    whIdOrigin,
                    whIdDestination,
                    isNow15
                )
            )
            .setBusinessUnit(BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE)
            .setCurrentSite(CURRENT_SITE_TOKOPEDIA_MARKET_PLACE)
            .setUserId(userId)
            .setCustomProperty(EVENT_WAREHOUSE_ID, whIdOrigin)
            .build()
            .send()
    }

    // - 2
    fun sendClickSwitcherWidget(
        userId: String,
        whIdOrigin: String,
        whIdDestination: String,
        isNow15: Boolean
    ) {
        Tracker.Builder()
            .setEvent(EVENT_CLICK_GROWTH)
            .setEventAction(EVENT_ACTION_CLICK_SWITCHER_WIDGET)
            .setEventCategory(EVENT_CATEGORY_HOME_PAGE)
            .setEventLabel(
                getEventLabelSwitcherWidget(
                    userId,
                    whIdOrigin,
                    whIdDestination,
                    isNow15
                )
            )
            .setBusinessUnit(BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE)
            .setCurrentSite(CURRENT_SITE_TOKOPEDIA_MARKET_PLACE)
            .setUserId(userId)
            .setCustomProperty(EVENT_WAREHOUSE_ID, whIdOrigin)
            .build()
            .send()
    }

    private fun ecommerceDataLayerBannerClicked(
        channelModel: ChannelModel,
        channelGrid: ChannelGrid,
        position: Int
    ): Bundle {
        return Bundle().apply {
            val trackerPosition = position.getTrackerPosition()
            putString(KEY_CREATIVE_NAME, channelGrid.attribution)
            putString(KEY_CREATIVE_SLOT, trackerPosition.toString())
            putString(KEY_DIMENSION_104, channelModel.trackingAttributionModel.campaignCode)
            putString(KEY_DIMENSION_38, channelGrid.attribution)
            putString(KEY_DIMENSION_79, channelModel.trackingAttributionModel.brandId)
            putString(KEY_DIMENSION_82, channelModel.trackingAttributionModel.categoryId)
            putString(
                KEY_ITEM_ID,
                "0_" + channelGrid.id + "_" + channelModel.trackingAttributionModel.persoType + "_" + channelModel.trackingAttributionModel.categoryId
            )
            putString(KEY_ITEM_NAME, "/ - p$trackerPosition - slider banner - banner - ${channelModel.channelHeader.name}")
        }
    }

    private fun ecommerceDataLayerBanner(
        channelModel: ChannelModel,
        channelGrid: ChannelGrid,
        position: Int,
        itemName: String
    ): Bundle {
        return Bundle().apply {
            putString(KEY_CREATIVE_NAME, channelGrid.attribution)
            putString(KEY_CREATIVE_SLOT, position.getTrackerPosition().toString())
            putString(
                KEY_ITEM_ID,
                "0_" + channelGrid.id + "_" + channelModel.trackingAttributionModel.persoType + "_" + channelModel.trackingAttributionModel.categoryId
            )
            putString(KEY_ITEM_NAME, itemName)
        }
    }

    private fun ecommerceDataLayerCategoryClicked(position: Int, categoryId: String, headerName: String): Bundle {
        val nullString = "null"
        return Bundle().apply {
            putString(KEY_CREATIVE_NAME, nullString)
            putString(KEY_CREATIVE_SLOT, position.toString())
            putString(KEY_DIMENSION_49, nullString)
            putString(KEY_DIMENSION_38, nullString)
            putString(KEY_DIMENSION_79, nullString)
            putString(KEY_DIMENSION_82, nullString)
            putString(KEY_ITEM_ID, "0_" + categoryId + "_" + nullString + "_" + nullString)
            putString(KEY_ITEM_NAME, "/ - p$position - category widget - widget - $headerName")
        }
    }

    private fun getDataLayer(
        event: String,
        action: String,
        category: String,
        label: String = ""
    ): MutableMap<String, Any> {
        return DataLayer.mapOf(
            TrackAppUtils.EVENT,
            event,
            TrackAppUtils.EVENT_ACTION,
            action,
            TrackAppUtils.EVENT_CATEGORY,
            category,
            TrackAppUtils.EVENT_LABEL,
            label
        )
    }

    private fun getMarketplaceDataLayer(event: String, action: String, label: String = ""): Bundle {
        return Bundle().apply {
            putString(TrackAppUtils.EVENT, event)
            putString(TrackAppUtils.EVENT_ACTION, action)
            putString(TrackAppUtils.EVENT_CATEGORY, EVENT_CATEGORY_HOME_PAGE)
            putString(TrackAppUtils.EVENT_LABEL, label)
            putString(KEY_BUSINESS_UNIT, BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE)
            putString(KEY_CURRENT_SITE, BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE)
        }
    }

    private fun getEcommerceDataLayer(
        event: String,
        action: String,
        category: String,
        label: String = "",
        affinityLabel: String = "",
        promotions: ArrayList<Bundle>
    ): Bundle {
        return Bundle().apply {
            putString(TrackAppUtils.EVENT, event)
            putString(TrackAppUtils.EVENT_ACTION, action)
            putString(TrackAppUtils.EVENT_CATEGORY, category)
            putString(TrackAppUtils.EVENT_LABEL, label)
            putString(KEY_AFFINITY_LABEL, affinityLabel)
            putString(KEY_BUSINESS_UNIT, BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE)
            putString(KEY_CURRENT_SITE, CURRENT_SITE_TOKOPEDIA_MARKET_PLACE)
            putParcelableArrayList(KEY_PROMOTIONS, promotions)
            putString(KEY_USER_ID, userSession.userId)
        }
    }

    private fun getEcommerceDataLayer(
        event: String,
        action: String,
        category: String,
        label: String = "",
        items: ArrayList<Bundle>,
        productId: String = "",
        pageSource: String = PRODUCT_PAGE_SOURCE
    ): Bundle {
        return Bundle().apply {
            putString(TrackAppUtils.EVENT, event)
            putString(TrackAppUtils.EVENT_ACTION, action)
            putString(TrackAppUtils.EVENT_CATEGORY, category)
            putString(TrackAppUtils.EVENT_LABEL, label)
            putString(KEY_BUSINESS_UNIT, BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE)
            putString(KEY_CURRENT_SITE, CURRENT_SITE_TOKOPEDIA_MARKET_PLACE)
            putString(KEY_USER_ID, userSession.userId)
            putParcelableArrayList(KEY_ITEMS, items)
            if (productId.isNotBlank()) {
                putString(KEY_PRODUCT_ID, productId)
            }
            if (pageSource.isNotBlank()) {
                putString(KEY_PAGE_SOURCE, pageSource)
            }
        }
    }

    private fun getProductDataLayer(
        event: String,
        action: String,
        category: String,
        label: String = "",
        itemList: String = "",
        affinityLabel: String = "",
        items: ArrayList<Bundle>
    ): Bundle {
        return Bundle().apply {
            putString(TrackAppUtils.EVENT, event)
            putString(TrackAppUtils.EVENT_ACTION, action)
            putString(TrackAppUtils.EVENT_CATEGORY, category)
            putString(TrackAppUtils.EVENT_LABEL, label)
            putString(KEY_AFFINITY_LABEL, affinityLabel)
            putString(KEY_BUSINESS_UNIT, BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE)
            putString(KEY_CURRENT_SITE, CURRENT_SITE_TOKOPEDIA_MARKET_PLACE)
            if (itemList.isNotBlank()) {
                putString(KEY_ITEM_LIST, itemList)
            }
            putParcelableArrayList(KEY_ITEMS, items)
            putString(KEY_USER_ID, userSession.userId)
        }
    }

    private fun productItemDataLayer(
        index: String = "",
        productId: String = "",
        productName: String = "",
        price: Long = 0L,
        productBrand: String = "",
        productCategory: String = "",
        productVariant: String = ""
    ): Bundle {
        return Bundle().apply {
            if (index.isNotBlank()) {
                putString(KEY_INDEX, index)
            }
            putString(KEY_ITEM_BRAND, productBrand)
            putString(KEY_ITEM_CATEGORY, productCategory)
            putString(KEY_ITEM_ID, productId)
            putString(KEY_ITEM_NAME, productName)
            putString(KEY_ITEM_VARIANT, productVariant)
            putLong(KEY_PRICE, price)
        }
    }

    private fun productCardItemDataLayer(
        position: String = "",
        id: String,
        name: String,
        price: String,
        brand: String = "",
        category: String = "",
        variant: String = ""
    ): Bundle {
        return Bundle().apply {
            putString(KEY_DIMENSION_104, "")
            putString(KEY_DIMENSION_38, "")
            putString(KEY_DIMENSION_79, "")
            putString(KEY_DIMENSION_82, "")
            if (position.isNotBlank()) {
                putString(KEY_INDEX, position)
            }
            putString(KEY_ITEM_BRAND, brand)
            putString(KEY_ITEM_CATEGORY, category)
            putString(KEY_ITEM_ID, id)
            putString(KEY_ITEM_NAME, name)
            putString(KEY_ITEM_VARIANT, variant)
            putString(KEY_PRICE, price)
        }
    }

    private fun getTracker(): Analytics {
        return TrackApp.getInstance().gtm
    }

    private fun hitCommonHomeTracker(dataLayer: MutableMap<String, Any>) {
        getTracker().sendGeneralEvent(dataLayer.getHomeGeneralTracker())
    }

    private fun MutableMap<String, Any>.getHomeGeneralTracker(): MutableMap<String, Any> {
        this[KEY_CURRENT_SITE] = CURRENT_SITE_TOKOPEDIA_MARKET_PLACE
        this[KEY_BUSINESS_UNIT] = BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE
        return this
    }

    private fun getHalalLabel(labelGroup: List<LabelGroup>): String {
        val withHalalLabel = labelGroup.firstOrNull { it.title == LABEL_GROUP_HALAL }
        return if (withHalalLabel == null) WITHOUT_HALAL_LABEL else WITH_HALAL_LABEL
    }

    private fun getSlashedPriceLabel(slashedPrice: String?): String {
        return if (slashedPrice.isNullOrEmpty()) NORMAL_PRICE else SLASH_PRICE
    }

    private fun getVariantLabel(parentProductId: String?): String {
        return if (parentProductId.isNullOrEmpty()) WITHOUT_VARIANT else WITH_VARIANT
    }

    private fun getProductCardLabel(data: TokoNowProductCardUiModel): String {
        val halalLabel = getHalalLabel(data.product.labelGroupList)
        val slashedPriceLabel = getSlashedPriceLabel(data.product.slashedPrice)
        val variantLabel = getVariantLabel(data.parentId)
        return "$halalLabel - $slashedPriceLabel - $variantLabel"
    }
}
