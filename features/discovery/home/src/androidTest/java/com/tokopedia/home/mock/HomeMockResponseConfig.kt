package com.tokopedia.home.mock

import android.content.Context
import com.tokopedia.home.test.R
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper.getRawString

internal open class HomeMockResponseConfig(private val isLinkedBalanceWidget: Boolean = true) : MockModelConfig() {
    companion object {
        const val KEY_QUERY_DYNAMIC_HOME_CHANNEL = "homeData"
        const val KEY_QUERY_DYNAMIC_HOME_CHANNEL_ATF_1 = "\"param\": \"channel_ids=65312\""
        const val KEY_QUERY_DYNAMIC_HOME_CHANNEL_ATF_2 = "\"param\": \"channel_ids=45397\""

        const val KEY_QUERY_DYNAMIC_HOME_CHANNEL_ONLY = "getDynamicChannel"
        const val KEY_QUERY_DYNAMIC_HOME_CHANNEL_V2 = "getHomeChannelV2"
        const val KEY_QUERY_DYNAMIC_POSITION = "dynamicPosition"
        const val KEY_QUERY_DYNAMIC_POSITION_ICON = "homeIcon"
        const val KEY_QUERY_DYNAMIC_POSITION_ICON_V2 = "getHomeIconV2"
        const val KEY_QUERY_DYNAMIC_POSITION_TICKER = "homeTicker"
        const val KEY_QUERY_DYNAMIC_POSITION_TICKER_V2 = "getHomeTickerV2"
        const val KEY_QUERY_DYNAMIC_HOME_SUCCESS_OCC = "add_to_cart_occ"
        const val KEY_CONTAINS_WIDGET_TAB = "widget_tab"
        const val KEY_CONTAINS_WIDGET_GRID = "widget_grid"
        const val KEY_CONTAINS_DYNAMIC_HOME_RECOM_FEED = "recommendation_product"
        const val KEY_CONTAINS_DYNAMIC_HOME_RECOM_FEED_V2 = "getHomeRecommendationProductV2"
        const val KEY_CONTAINS_SUGGESTED_REVIEW = "suggestedProductReview"
        const val KEY_CONTAINS_PLAY_DC = "playGetLiveDynamicChannels"
        const val KEY_CONTAINS_RECHARGE = "rechargeRecommendation"
        const val KEY_CONTAINS_RECHARGE_BU_WIDGET = "getBUWidget"
        const val KEY_CONTAINS_SALAM = "salamWidget"
        const val KEY_CONTAINS_POPULAR_KEYWORDS = "PopularKeywords"
        const val KEY_CONTAINS_RECOMMENDATION_TAB = "getRecommendation"
        const val KEY_CONTAINS_RECOMMENDATION_TAB_V2 = "getHomeRecommendationTabV2"
        const val KEY_CONTAINS_UNIVERSE_PLACEHOLDER = "universe_placeholder"
        const val KEY_QUERY_FLOATING_EGG = "gamiFloating"
        const val KEY_CONTAINS_TOKOPOINTS_LIST = "tokopointsDrawerList"
        const val KEY_CONTAINS_WALLET = "isGetTopup:true"
        const val KEY_CONTAINS_FLAG = "homeData"
        const val KEY_CONTAINS_PRODUCT_REVIEW = "suggestedProductReview"
        const val KEY_CONTAINS_PRODUCT_RECOMMENDATION = "productRecommendation"
        const val KEY_CONTAINS_HEADLINE_ADS = "DisplayHeadlineAds"
        const val KEY_CONTAINS_NOTIFICATION = "Notification"
        const val KEY_CONTAINS_CHIPS = "RecommendationFilterChipsQuery"
        const val KEY_CONTAINS_WALLETAPP_GETBALANCES = "walletAppGetBalance"
        const val KEY_CONTAINS_PLAY_GET_WIDGET_V2 = "playGetWidgetV2"
        const val KEY_CONTAINS_OCC = "mutation add_to_cart_occ_multi"
        const val KEY_CONTAINS_SLIDE = "homeSlides"
        const val KEY_CONTAINS_HOME_BANNER_V2 = "getHomeBannerV2"
        const val KEY_CONTAINS_CM_HOME_WIDGET = "notifier_getHtdw"
        const val KEY_CONTAINS_PAYLATER_WIDGET = "paylater_getHomeWidget"
        const val KEY_CONTAINS_MISSION_WIDGET = "getHomeMissionWidget"
        const val KEY_CONTAINS_HOME_BALANCE_WIDGET = "getHomeBalanceWidget"
    }

    override fun createMockModel(context: Context): MockModelConfig {
        addMockResponse(
            KEY_QUERY_DYNAMIC_HOME_CHANNEL,
            getRawString(context, R.raw.response_mock_data_dynamic_home_channel),
            FIND_BY_QUERY_NAME
        )

        addMockResponse(
            KEY_QUERY_DYNAMIC_HOME_CHANNEL_ATF_1,
            getRawString(context, R.raw.response_mock_data_dynamic_home_channel_atf_1),
            FIND_BY_CONTAINS
        )

        addMockResponse(
            KEY_QUERY_DYNAMIC_HOME_CHANNEL_ATF_2,
            getRawString(context, R.raw.response_mock_data_dynamic_home_channel_atf_2),
            FIND_BY_CONTAINS
        )

        addMockResponse(
            KEY_QUERY_DYNAMIC_POSITION,
            getRawString(context, R.raw.response_mock_data_dynamic_position),
            FIND_BY_CONTAINS
        )

        addMockResponse(
            KEY_QUERY_DYNAMIC_POSITION_ICON,
            getRawString(context, R.raw.response_mock_data_dynamic_position_icon),
            FIND_BY_CONTAINS
        )

        addMockResponse(
            KEY_QUERY_DYNAMIC_POSITION_ICON_V2,
            getRawString(context, R.raw.response_mock_data_dynamic_position_icon_v2),
            FIND_BY_CONTAINS
        )

        addMockResponse(
            KEY_QUERY_DYNAMIC_POSITION_TICKER,
            getRawString(context, R.raw.response_mock_data_dynamic_position_ticker),
            FIND_BY_CONTAINS
        )

        addMockResponse(
            KEY_QUERY_DYNAMIC_POSITION_TICKER_V2,
            getRawString(context, R.raw.response_mock_data_dynamic_position_ticker_v2),
            FIND_BY_CONTAINS
        )

        addMockResponse(
            KEY_QUERY_DYNAMIC_HOME_CHANNEL_ONLY,
            getRawString(context, R.raw.response_mock_data_dynamic_home_channel_screenshot),
            FIND_BY_CONTAINS
        )

        addMockResponse(
            KEY_QUERY_DYNAMIC_HOME_CHANNEL_V2,
            getRawString(context, R.raw.response_mock_data_dynamic_home_channel_screenshot_v2),
            FIND_BY_CONTAINS
        )

        addMockResponse(
            KEY_CONTAINS_DYNAMIC_HOME_RECOM_FEED,
            getRawString(context, R.raw.response_mock_data_dynamic_home_recom_feed),
            FIND_BY_CONTAINS
        )

        addMockResponse(
            KEY_CONTAINS_DYNAMIC_HOME_RECOM_FEED_V2,
            getRawString(context, R.raw.response_mock_data_dynamic_home_recom_feed_v2),
            FIND_BY_CONTAINS
        )

        addMockResponse(
            KEY_CONTAINS_POPULAR_KEYWORDS,
            getRawString(context, R.raw.response_mock_data_dynamic_home_popular_keyword),
            FIND_BY_CONTAINS
        )

        addMockResponse(
            KEY_QUERY_DYNAMIC_HOME_SUCCESS_OCC,
            getRawString(context, R.raw.response_mock_data_home_success_occ),
            FIND_BY_QUERY_NAME
        )

        addMockResponse(
            KEY_CONTAINS_WIDGET_TAB,
            getRawString(context, R.raw.response_mock_data_home_widget_tab),
            FIND_BY_CONTAINS
        )

        addMockResponse(
            KEY_CONTAINS_WIDGET_GRID,
            getRawString(context, R.raw.response_mock_data_home_widget_grid),
            FIND_BY_CONTAINS
        )

        addMockResponse(
            KEY_CONTAINS_SUGGESTED_REVIEW,
            getRawString(context, R.raw.response_mock_data_suggested_review),
            FIND_BY_CONTAINS
        )

        addMockResponse(
            KEY_CONTAINS_PLAY_DC,
            getRawString(context, R.raw.response_mock_data_play_widget),
            FIND_BY_CONTAINS
        )

        addMockResponse(
            KEY_CONTAINS_RECHARGE,
            getRawString(context, R.raw.response_mock_data_recharge_recommendation),
            FIND_BY_CONTAINS
        )

        addMockResponse(
            KEY_CONTAINS_SALAM,
            getRawString(context, R.raw.response_mock_data_salam_content),
            FIND_BY_CONTAINS
        )

        addMockResponse(
            KEY_CONTAINS_RECHARGE_BU_WIDGET,
            getRawString(context, R.raw.response_mock_data_recharge_bu_widget),
            FIND_BY_CONTAINS
        )

        addMockResponse(
            KEY_CONTAINS_RECOMMENDATION_TAB,
            getRawString(context, R.raw.response_mock_data_dynamic_home_recom_feed_tab),
            FIND_BY_CONTAINS
        )

        addMockResponse(
            KEY_CONTAINS_RECOMMENDATION_TAB_V2,
            getRawString(context, R.raw.response_mock_data_dynamic_home_recom_feed_tab_v2),
            FIND_BY_CONTAINS
        )

        addMockResponse(
            KEY_CONTAINS_UNIVERSE_PLACEHOLDER,
            getRawString(context, R.raw.response_mock_data_universe_placeholder),
            FIND_BY_CONTAINS
        )

        addMockResponse(
            KEY_QUERY_FLOATING_EGG,
            getRawString(context, R.raw.response_mock_data_home_egg),
            FIND_BY_CONTAINS
        )

        addMockResponse(
            KEY_CONTAINS_WALLET,
            getRawString(context, R.raw.response_mock_data_wallet),
            FIND_BY_CONTAINS
        )

        addMockResponse(
            KEY_CONTAINS_TOKOPOINTS_LIST,
            getRawString(context, R.raw.response_mock_data_tokopoints_list),
            FIND_BY_CONTAINS
        )

        addMockResponse(
            KEY_CONTAINS_FLAG,
            getRawString(context, R.raw.response_mock_data_home_flag),
            FIND_BY_CONTAINS
        )

        addMockResponse(
            KEY_CONTAINS_PRODUCT_REVIEW,
            getRawString(context, R.raw.response_mock_data_suggested_product_review),
            FIND_BY_CONTAINS
        )

        addMockResponse(
            KEY_CONTAINS_PRODUCT_RECOMMENDATION,
            getRawString(context, R.raw.response_mock_data_best_selling_widget),
            FIND_BY_CONTAINS
        )

        addMockResponse(
            KEY_CONTAINS_HEADLINE_ADS,
            getRawString(context, R.raw.response_mock_data_headline_ads),
            FIND_BY_CONTAINS
        )

        addMockResponse(
            KEY_CONTAINS_NOTIFICATION,
            getRawString(context, R.raw.response_mock_data_navigation_notification),
            FIND_BY_CONTAINS
        )

        addMockResponse(
            KEY_CONTAINS_CHIPS,
            getRawString(context, R.raw.response_mock_data_best_seller_chips),
            FIND_BY_CONTAINS
        )

        addMockResponse(
            KEY_CONTAINS_WALLETAPP_GETBALANCES,
            if (isLinkedBalanceWidget) {
                getRawString(context, R.raw.response_mock_data_walletapp)
            } else {
                getRawString(context, R.raw.response_mock_data_walletapp_not_linked)
            },
            FIND_BY_CONTAINS
        )
        addMockResponse(
            KEY_CONTAINS_PLAY_GET_WIDGET_V2,
            getRawString(context, R.raw.response_mock_data_play_widget_v2),
            FIND_BY_CONTAINS
        )
        addMockResponse(
            KEY_CONTAINS_OCC,
            getRawString(context, R.raw.response_mock_data_occ_list_carousel),
            FIND_BY_CONTAINS
        )
        addMockResponse(
            KEY_CONTAINS_SLIDE,
            getRawString(context, R.raw.response_mock_data_slider_banner),
            FIND_BY_CONTAINS
        )
        addMockResponse(
            KEY_CONTAINS_HOME_BANNER_V2,
            getRawString(context, R.raw.response_mock_data_slider_banner_v2),
            FIND_BY_CONTAINS
        )
        addMockResponse(
            KEY_CONTAINS_CM_HOME_WIDGET,
            getRawString(context, R.raw.response_cm_home_widget),
            FIND_BY_CONTAINS
        )
        addMockResponse(
            KEY_CONTAINS_PAYLATER_WIDGET,
            getRawString(context, R.raw.response_paylater_widget),
            FIND_BY_CONTAINS
        )
        addMockResponse(
            KEY_CONTAINS_MISSION_WIDGET,
            getRawString(context, R.raw.response_mock_data_mission_widget),
            FIND_BY_CONTAINS
        )
        addMockResponse(
            KEY_CONTAINS_HOME_BALANCE_WIDGET,
            getRawString(context, R.raw.response_mock_data_home_balance_widget),
            FIND_BY_CONTAINS
        )
        updateMock(context)
        return this
    }

    open fun updateMock(context: Context) {}
}
