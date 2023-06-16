package com.tokopedia.topads.dashboard.recommendation.common

object RecommendationConstants {
    const val TYPE_INSIGHT = 0
    const val TYPE_PERFORMANCE = 1
    const val TYPE_CHIPS = 2
    const val TYPE_POSITIVE_KEYWORD = 3
    const val TYPE_KEYWORD_BID = 4
    const val TYPE_GROUP_BID = 5
    const val TYPE_DAILY_BUDGET = 6
    const val TYPE_NEGATIVE_KEYWORD_BID = 7
    const val TYPE_EMPTY_STATE = 8
    const val TYPE_UN_OPTIMIZED_GROUP = 9

    const val PRODUCT_BID_TYPE_SEARCH = "product_search"
    const val PRODUCT_BID_TYPE_BROWSE = "product_browse"
    const val TYPE_PRODUCT_VALUE = 1
    const val TYPE_SHOP_VALUE = 3
    const val PRODUCT_KEY = "product"
    const val HEADLINE_KEY = "headline"
    const val PERFORMANCE_FREQUENTLY_THRESHOLD = 20
    const val PERFORMANCE_RARITY_THRESHOLD = 5
    const val PERFORMANCE_NOT_RATED_THRESHOLD = 0
    const val DEFAULT_SELECTED_INSIGHT_TYPE = 0
    const val KEYWORD_TYPE_POSITIVE_PHRASE = "positive_phrase"
    const val KEYWORD_TYPE_POSITIVE_EXACT = "positive_exact"
    const val KEYWORD_TYPE_POSITIVE_BROAD = "positive_broad"
    const val KEYWORD_TYPE_NEGATIVE_PHRASE = "negative_phrase"
    const val KEYWORD_TYPE_NEGATIVE_EXACT = "negative_exact"
    const val KEYWORD_TYPE_NEGATIVE_BROAD = "negative_broad"
    const val KEYWORD_STATUS_ACTIVE = "active"
    const val KEYWORD_STATUS_INACTIVE = "inactive"
    const val KEYWORD_STATUS_DELETED = "deleted"
    const val INVALID_INSIGHT_TYPE = -1
    const val ACTION_CREATE_PARAM = "create"
    const val ACTION_EDIT_PARAM = "edit"

    const val KEY_AD_GROUP_TYPES = "adGroupTypes"
    const val TAB_NAME_PRODUCT = "Iklan Produk"
    const val TAB_NAME_SHOP = "Iklan Toko"

    const val AD_GROUP_TYPE_KEY = "adGroupType"
    const val AD_GROUP_NAME_KEY = "adGroupName"
    const val AD_GROUP_ID_KEY = "groupId"
    const val AD_GROUP_COUNT_KEY = "count"
    const val INSIGHT_TYPE_KEY = "insightType"
    const val INSIGHT_TYPE_LIST_KEY = "insightTypeList"
    const val GROUP_DETAIL_BUNDLE_KEY = "groupDetailBundle"
    const val INSIGHT_MULTIPLIER = 50
    const val INSIGHT_GROUP_BID_MAX_BID = 5000
    const val INSIGHT_DAILY_BUDGET_MAX_BID = 10000000
    const val INSIGHT_PRICING_FAIL_MAX_BID_FALLBACK_VALUE = 10000
    const val INSIGHT_PRICING_FAIL_MIN_BID_FALLBACK_VALUE = 400

    const val SEARCH_REPORT_EDU_URL = "https://seller.tokopedia.com/edu/topads-laporan-pencarian/"
    const val HEADLINE_INSIGHT_MUTATION_SOURCE = "android.insight_center_headline_keyword_recom"
    const val PRODUCT_INSIGHT_MUTATION_SOURCE = "product_recom_app"

    object InsightTypeConstants {
        const val INSIGHT_TYPE_ALL = 0
        const val INSIGHT_TYPE_POSITIVE_KEYWORD = 1
        const val INSIGHT_TYPE_KEYWORD_BID = 2
        const val INSIGHT_TYPE_GROUP_BID = 3
        const val INSIGHT_TYPE_DAILY_BUDGET = 4
        const val INSIGHT_TYPE_NEGATIVE_KEYWORD = 5

        const val INSIGHT_TYPE_ALL_INPUT = "Semua"
        const val INSIGHT_TYPE_POSITIVE_KEYWORD_INPUT = "keyword_new_positive"
        const val INSIGHT_TYPE_KEYWORD_BID_INPUT = "keyword_bid"
        const val INSIGHT_TYPE_GROUP_BID_INPUT = "group_bid"
        const val INSIGHT_TYPE_DAILY_BUDGET_INPUT = "group_daily_budget"
        const val INSIGHT_TYPE_NEGATIVE_KEYWORD_INPUT = "keyword_new_negative"

        const val INSIGHT_TYPE_ALL_NAME = "Semua"
        const val INSIGHT_TYPE_POSITIVE_KEYWORD_NAME = "Kata Kunci"
        const val INSIGHT_TYPE_KEYWORD_BID_NAME = "Biaya Kata Kunci"
        const val INSIGHT_TYPE_GROUP_BID_NAME = "Biaya Iklan"
        const val INSIGHT_TYPE_DAILY_BUDGET_NAME = "Anggaran Harian"
        const val INSIGHT_TYPE_NEGATIVE_KEYWORD_NAME = "Kata Kunci Negatif"
    }

    object InsightGqlInputSource {
        // topadsGetShopInfoV2_1
        // topAdsGetTotalAdGroupsWithInsightByShopID
        // topAdsListAllInsightCounts
        const val SOURCE_INSIGHT_CENTER_LANDING_PAGE = "android.insight_center_landing_page"

        // topAdsGetTotalAdGroupsWithInsightByShopID
        // topAdsListAllInsightCounts
        // topAdsBatchGetKeywordInsightByGroupIDV3
        // topAdsBatchGetAdGroupBidInsightByGroupID
        const val SOURCE_INSIGHT_CENTER_GROUP_DETAIL_PAGE = "android.insight_center_group_detail_page"

        // topAdsGetTotalAdGroupsWithInsightByShopID
        // topAdsListAllInsightCounts
        const val SOURCE_TOP_ADS_DASHBOARD = "android.top_ads_dashboard"
    }
}
