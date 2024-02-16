package com.tokopedia.analytics.byteio.search

import com.tokopedia.analytics.byteio.AppLogAnalytics
import com.tokopedia.analytics.byteio.AppLogAnalytics.addPage
import com.tokopedia.analytics.byteio.AppLogParam
import com.tokopedia.analytics.byteio.AppLogParam.EVENT_ORIGIN_FEATURE
import com.tokopedia.analytics.byteio.AppLogParam.EVENT_ORIGIN_FEATURE_DEFAULT_VALUE
import com.tokopedia.analytics.byteio.AppLogParam.ITEM_ORDER
import com.tokopedia.analytics.byteio.AppLogParam.REQUEST_ID
import com.tokopedia.analytics.byteio.AppLogParam.SOURCE_MODULE
import com.tokopedia.analytics.byteio.AppLogParam.TRACK_ID
import com.tokopedia.analytics.byteio.EventName
import com.tokopedia.analytics.byteio.search.AppLogSearch.Event.CHOOSE_SEARCH_FILTER
import com.tokopedia.analytics.byteio.search.AppLogSearch.Event.ENTER_SEARCH_BLANKPAGE
import com.tokopedia.analytics.byteio.search.AppLogSearch.Event.SEARCH
import com.tokopedia.analytics.byteio.search.AppLogSearch.Event.SEARCH_RESULT_CLICK
import com.tokopedia.analytics.byteio.search.AppLogSearch.Event.SEARCH_RESULT_SHOW
import com.tokopedia.analytics.byteio.search.AppLogSearch.Event.SHOW_SEARCH
import com.tokopedia.analytics.byteio.search.AppLogSearch.Event.TRENDING_SHOW
import com.tokopedia.analytics.byteio.search.AppLogSearch.Event.TRENDING_WORDS_CLICK
import com.tokopedia.analytics.byteio.search.AppLogSearch.Event.TRENDING_WORDS_SHOW
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamKey.ALADDIN_BUTTON_TYPE
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamKey.BLANKPAGE_ENTER_FROM
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamKey.BLANKPAGE_ENTER_METHOD
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamKey.BUTTON_TYPE_CLICK
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamKey.DURATION
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamKey.ECOM_FILTER_CHOSEN
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamKey.ECOM_FILTER_NAME
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamKey.ECOM_FILTER_POSITION
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamKey.ECOM_FILTER_TYPE
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamKey.ECOM_SORT_CHOSEN
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamKey.ECOM_SORT_NAME
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamKey.EC_SEARCH_SESSION_ID
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamKey.ENTER_FROM
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamKey.ENTER_METHOD
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamKey.GROUP_ID
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamKey.IMPR_ID
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamKey.IS_AD
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamKey.IS_FIRST_SCREEN
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamKey.IS_SUCCESS
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamKey.ITEM_RANK
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamKey.LIST_ITEM_ID
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamKey.LIST_RESULT_TYPE
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamKey.NEW_SUG_SESSION_ID
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamKey.PRE_CLICK_ID
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamKey.PRE_SEARCH_ID
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamKey.PRODUCT_ID
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamKey.RANK
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamKey.RATE
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamKey.RAW_QUERY
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamKey.SEARCH_ENTRANCE
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamKey.SEARCH_ID
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamKey.SEARCH_KEYWORD
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamKey.SEARCH_POSITION
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamKey.SEARCH_RESULT_ID
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamKey.SEARCH_TYPE
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamKey.SHOP_ID
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamKey.SUG_TYPE
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamKey.TOKEN_TYPE
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamKey.TRACK_ID
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamKey.VOLUME
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamKey.WORDS_CONTENT
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamKey.WORDS_NUM
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamKey.WORDS_POSITION
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamKey.WORDS_SOURCE
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamValue.FILTER_PANEL
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamValue.GOODS_SEARCH
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamValue.HOMEPAGE
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamValue.STORE_SEARCH
import org.json.JSONObject

object AppLogSearch {

    object Event {
        const val SHOW_SEARCH = "show_search"
        const val TRENDING_WORDS_SHOW = "trending_words_show"
        const val TRENDING_WORDS_CLICK = "trending_words_click"
        const val SEARCH = "search"
        const val ENTER_SEARCH_BLANKPAGE = "enter_search_blankpage"
        const val TRENDING_SHOW = "trending_show"
        const val SEARCH_RESULT_SHOW = "search_result_show"
        const val SEARCH_RESULT_CLICK = "search_result_click"
        const val CHOOSE_SEARCH_FILTER = "choose_search_filter"
    }

    object ParamKey {
        const val SEARCH_ENTRANCE = "search_entrance"
        const val ENTER_FROM = "enter_from"
        const val WORDS_SOURCE = "words_source"
        const val WORDS_POSITION = "words_position"
        const val WORDS_CONTENT = "words_content"
        const val SEARCH_POSITION = "search_position"
        const val GROUP_ID = "group_id"
        const val IMPR_ID = "impr_id"
        const val SEARCH_TYPE = "search_type"
        const val ENTER_METHOD = "enter_method"
        const val SEARCH_KEYWORD = "search_keyword"
        const val DURATION = "duration"
        const val IS_SUCCESS = "is_success"
        const val PRE_SEARCH_ID = "pre_search_id"
        const val EC_SEARCH_SESSION_ID = "ec_search_session_id"
        const val WORDS_NUM = "words_num"
        const val RAW_QUERY = "raw_query"
        const val NEW_SUG_SESSION_ID = "new_sug_session_id"
        const val SUG_TYPE = "sug_type"
        const val PRE_CLICK_ID = "pre_click_id"
        const val BLANKPAGE_ENTER_FROM = "blankpage_enter_from"
        const val BLANKPAGE_ENTER_METHOD = "blankpage_enter_method"
        const val SEARCH_ID = "search_id"
        const val SEARCH_RESULT_ID = "search_result_id"
        const val LIST_ITEM_ID = "list_item_id"
        const val ITEM_RANK = "item_rank"
        const val LIST_RESULT_TYPE = "list_result_type"
        const val TOKEN_TYPE = "token_type"
        const val RANK = "rank"
        const val IS_FIRST_SCREEN = "is_first_screen"
        const val SHOP_ID = "shop_id"
        const val ALADDIN_BUTTON_TYPE = "aladdin_button_type"
        const val ECOM_SORT_NAME = "ecom_sort_name"
        const val ECOM_FILTER_NAME = "ecom_filter_name"
        const val ECOM_FILTER_POSITION = "ecom_filter_position"
        const val BUTTON_TYPE_CLICK = "button_type_click"
        const val ECOM_SORT_CHOSEN = "ecom_sort_chosen"
        const val ECOM_FILTER_CHOSEN = "ecom_filter_chosen"
        const val ECOM_FILTER_TYPE = "ecom_filter_type"

        // Potentially can be in global constants
        const val VOLUME = "volume"
        const val RATE = "rate"
        const val IS_AD = "is_ad"
        const val PRODUCT_ID = "product_id"
        const val TRACK_ID = "track_id"
        const val REQUEST_ID = "request_id"
    }

    object ParamValue {
        const val HOMEPAGE = "homepage"
        const val SEARCH_BAR_OUTER = "search_bar_outer"
        const val GOODS_SEARCH = "goods_search"
        const val STORE_SEARCH = "store_search"
        const val DEFAULT_SEARCH_KEYWORD = "default_search_keyword"
        const val DEFAULT_SEARCH_KEYWORD_OUTER = "default_search_keyword_outer"
        const val ENTER = "enter"
        const val CANCEL = "cancel"
        const val RECOM_SEARCH = "recom_search"
        const val SEARCH_HISTORY = "search_history"
        const val SUG = "sug"
        const val CLICK_SEARCH_BAR = "click_search_bar"
        const val SEARCH_SUG = "search_sug"
        const val NORMAL_SEARCH = "normal_search"
        const val SUG_RECOM = "sug_recom"
        const val GOODS = "goods"
        const val VIDEO_GOODS = "video_goods"
        const val SHOP = "shop"
        const val SHOP_SMALL = "shop_small"
        const val SHOP_BIG = "shop_big"
        const val GOODS_COLLECT = "goods_collect"
        const val CLICK_MORE_BUTTON = "click_more_button" //"Click three dots on the product card"
        const val CLICK_FAVORITE_BUTTON = "click_favourite_button" // "Click on the three dots of the product card to add to favorites"
        const val CLICK_MORE_FINDALIKE = "click_more_findalike" //"Click on the three points of the product card to find similarities"
        const val CLICK_BACK = "click_back" //"Top back button"
        const val CLICK_SHOPPING_CART = "click_shopping_cart" // "Click on shopping cart"
        const val CLICK_SETTING = "click_setting" // "Click Settings"
        const val CLICK_SHOP_NAME = "click_shop_name" // "Click on the name of the shop to go to the shop homepage" (Shop Ads)
        const val SORT_RELEVANCE = "sort_relevance"
        const val SORT_REVIEW = "sort_review"
        const val SORT_PRICE_ASC = "sort_price_asc"
        const val SORT_PRICE_DESC = "sort_price_desc"
        const val SORT_NEWEST = "sort_newest"
        const val FILTER_PANEL = "filter_panel"
        const val FILTER_GUID = "filter_guid" // TODO:: What is navigation filtering?
        const val FILTER_QUICK = "filter_quick"
    }

    fun <K, V> eventShowSearch(vararg data: Pair<K, V>) {
        AppLogAnalytics.send(SHOW_SEARCH, JSONObject(mapOf(*data)))
    }

    fun <K, V> eventTrendingWordsShow(vararg data: Pair<K, V>) {
        AppLogAnalytics.send(TRENDING_WORDS_SHOW, JSONObject(mapOf(*data)))
    }

    fun <K, V> eventTrendingWordsClick(vararg data: Pair<K, V>) {
        AppLogAnalytics.send(TRENDING_WORDS_CLICK, JSONObject(mapOf(*data)))
    }

    fun <K, V> eventSearchFromPlaceholder() {
        AppLogAnalytics.send(
            SEARCH,
            JSONObject(
                mapOf(
                    IMPR_ID to "", // TODO:: Search ID From BE
                    ENTER_FROM to "", // TODO:: Homepage, otherwise EMPTY
                    SEARCH_TYPE to "", // TODO:: GOODS_SEARCH || STORE_SEARCH
                    // TODO:: DEFAULT_SEARCH_KEYWORD (Enter SRP From Initial State)
                    //  || DEFAULT_SEARCH_KEYWORD_OUTER (Homepage, not applicable for tokopedia)
                    ENTER_METHOD to "",
                    SEARCH_KEYWORD to "", // TODO:: Keyword
                    DURATION to 0, // TODO:: PLT in Millisecond!
                    IS_SUCCESS to 0, // TODO:: 0 Fail, 1 Success
                    PRE_SEARCH_ID to "", // TODO:: Previous Search ID, make similar to PREVIOUS_KEYWORD?
                    EC_SEARCH_SESSION_ID to "", // TODO:: Search Session ID. From FE? Need SharedPreferences to clear on Homepage
                )
            )
        )
    }

    // /////////////// -- IJ -- /////////////

    fun <K, V> eventSearchFromInitialState() {
        AppLogAnalytics.send(
            SEARCH,
            JSONObject(
                mapOf(
                    IMPR_ID to "", // TODO:: Search ID From BE
                    ENTER_FROM to "", // TODO:: Homepage, otherwise EMPTY
                    SEARCH_TYPE to "", // TODO:: GOODS_SEARCH || STORE_SEARCH
                    //  TODO:: || RECOM_SEARCH (suggestion? what's this?)
                    //   || SEARCH_HISTORY (search history on initial state)
                    ENTER_METHOD to "",
                    SEARCH_KEYWORD to "", // TODO:: Keyword
                    DURATION to 0, // TODO:: PLT in Millisecond!
                    IS_SUCCESS to 0, // TODO:: 0 Fail, 1 Success
                    PRE_SEARCH_ID to "", // TODO:: Previous Search ID, make similar to PREVIOUS_KEYWORD?
                    EC_SEARCH_SESSION_ID to "", // TODO:: Search Session ID. From FE? Need SharedPreferences to clear on Homepage
                )
            )
        )
    }

    fun <K, V> eventSearchFromSuggestion() {
        AppLogAnalytics.send(
            SEARCH,
            JSONObject(
                mapOf(
                    IMPR_ID to "", // TODO:: Search ID From BE
                    ENTER_FROM to "", // TODO:: Homepage, otherwise EMPTY
                    SEARCH_TYPE to "", // TODO:: GOODS_SEARCH || STORE_SEARCH
                    ENTER_METHOD to "", // TODO:: SEARCH_SUG (Sug word) || NORMAL_SEARCH (Manual input) || SUG_RECOM (Recommended words)
                    SEARCH_KEYWORD to "", // TODO:: Keyword
                    DURATION to 0, // TODO:: PLT in Millisecond!
                    IS_SUCCESS to 0, // TODO:: 0 Fail, 1 Success
                    PRE_SEARCH_ID to "", // TODO:: Previous Search ID, make similar to PREVIOUS_KEYWORD?
                    EC_SEARCH_SESSION_ID to "", // TODO:: Search Session ID. From FE? Need SharedPreferences to clear on Homepage
                    SUG_TYPE to "", // TODO:: CAMPAIGN || PRODUCT || STORE.
                    NEW_SUG_SESSION_ID to "", // TODO:: Reset every time query is entirely deleted. Similar to ec_search_session_id ? How to generate? What about opening Suggestion from SRP?
                    PRE_CLICK_ID to "", // TODO:: Can show tech implementation on TikTok?
                    BLANKPAGE_ENTER_FROM to "", // TODO:: ENTER_FROM of eventEnterSearchBlankPage(). Don't send this key if not from Initial State
                    BLANKPAGE_ENTER_METHOD to "", // TODO:: ENTER_METHOD of eventEnterSearchBlankPage(). Don't send this key if not from Initial State
                )
            )
        )
    }

    fun <K, V> eventSearchFromFilter() {
        AppLogAnalytics.send(
            SEARCH,
            JSONObject(
                mapOf(
                    IMPR_ID to "", // TODO:: Search ID From BE
                    ENTER_FROM to "", // TODO:: Homepage, otherwise EMPTY
                    SEARCH_TYPE to "", // TODO:: GOODS_SEARCH || STORE_SEARCH
                    ENTER_METHOD to "", // TODO:: SEARCH_SUG (Sug word) || NORMAL_SEARCH (Manual input) || SUG_RECOM (Recommended words)
                    // TODO::
                    //                //sort_relevance: related (toko: Paling Sesuai)
                    //                //sort_best_sellers: sales (toko: no such item)
                    //                //sort_review: good review (toko: Ulasan, TT no such sorting method)
                    //                //sort_price_asc: Price in ascending order (Toko: Harga Terendah)
                    //                //sort_price_desc: Price in descending order (Toko: Harga Tertinggi)
                    //                //sort_newest: New and Old (toko: Terbaru)
                    ECOM_SORT_CHOSEN to "",
                    ECOM_FILTER_CHOSEN to JSONObject(),
                    ECOM_FILTER_TYPE to FILTER_PANEL,
                    SEARCH_KEYWORD to "", // TODO:: Keyword
                )
            )
        )
    }

    fun <K, V> eventEnterSearchBlankPage() {
        AppLogAnalytics.send(
            ENTER_SEARCH_BLANKPAGE,
            JSONObject(
                mapOf(
                    SEARCH_ENTRANCE to "", // TODO:: HOMEPAGE. Other page empty
                    ENTER_FROM to "", // TODO:: HOMEPAGE || GOODS_SEARCH || STORE_SEARCH.
                    ENTER_METHOD to "", // TODO:: ENTER
                )
            )
        )
    }

    fun <K, V> eventTrendingShow() {
        AppLogAnalytics.send(
            TRENDING_SHOW,
            JSONObject(
                mapOf(
                    WORDS_SOURCE to "", // TODO:: SUG
                    WORDS_NUM to 0, // TODO:: Total number of all suggestion
                    SEARCH_POSITION to "", // TODO:: HOMEPAGE || GOODS_SEARCH || STORE_SEARCH.
                    SEARCH_ENTRANCE to "", // TODO:: HOMEPAGE. Other page = ""
                    IMPR_ID to "", // TODO:: Request ID from Suggestion GQL BE
                    RAW_QUERY to "", // TODO:: Query from user
                    NEW_SUG_SESSION_ID to "", // TODO:: Reset every time query is entirely deleted. System.currentTimeMillis()
                    ENTER_METHOD to "", // TODO:: "ENTER (from home to Initial State to Suggestion) || CLICK_SEARCH_BAR (from SRP)
                )
            )
        )
    }

    fun <K, V> eventTrendingWordsShowSuggestion() {
        AppLogAnalytics.send(
            TRENDING_WORDS_SHOW,
            JSONObject(
                mapOf(
                    WORDS_SOURCE to "", //TODO:: SUG
                    WORDS_POSITION to 0, // TODO:: Index of Suggestion words
                    WORDS_CONTENT to "", // TODO:: Words content
                    SEARCH_POSITION to "", // TODO:: HOMEPAGE || GOODS_SEARCH || STORE_SEARCH.
                    SEARCH_ENTRANCE to "", // TODO:: HOMEPAGE. Other pages = ""
                    GROUP_ID to "", // TODO:: Group ID
                    IMPR_ID to "", // TODO:: Request ID
                    RAW_QUERY to "", // TODO:: Raw Query
                    NEW_SUG_SESSION_ID to "", // TODO::  Reset every time query is entirely deleted. System.currentTimeMillis()
                    SUG_TYPE to "", // TODO:: CAMPAIGN || PRODUCT || STORE.
                    ENTER_METHOD to "", // TODO:: "ENTER (from home to Initial State to Suggestion) || CLICK_SEARCH_BAR (from SRP)
                )
            )
        )
    }

    fun <K, V> eventTrendingWordsClickSuggestion() {
        AppLogAnalytics.send(
            TRENDING_WORDS_CLICK,
            JSONObject(
                mapOf(
                    WORDS_SOURCE to "", //TODO:: SUG
                    WORDS_POSITION to 0, // TODO:: Index of Suggestion words
                    WORDS_CONTENT to "", // TODO:: Words content
                    SEARCH_POSITION to "", // TODO:: HOMEPAGE || GOODS_SEARCH || STORE_SEARCH.
                    SEARCH_ENTRANCE to "", // TODO:: HOMEPAGE. Other pages = ""
                    GROUP_ID to "", // TODO:: Group ID
                    IMPR_ID to "", // TODO:: Request ID
                    RAW_QUERY to "", // TODO:: Raw Query
                    NEW_SUG_SESSION_ID to "", // TODO:: Reset every time query is entirely deleted. System.currentTimeMillis()
                    SUG_TYPE to "", // TODO:: CAMPAIGN || PRODUCT || STORE.
                    ENTER_METHOD to "", // TODO:: "ENTER (from home to Initial State to Suggestion) || CLICK_SEARCH_BAR (from SRP)
                )
            )
        )
    }



    // /////////////// -- IJ -- /////////////

    fun <K, V> eventSearchResultShow() {
        AppLogAnalytics.send(
            SEARCH_RESULT_SHOW,
            JSONObject(
                mapOf(
                    IMPR_ID to "", // TODO:: From BE
                    SEARCH_ID to "", // TODO:: Generated System.currentTimeMillis()
                    SEARCH_ENTRANCE to "", // TODO:: HOMEPAGE. What about other page?
                    ENTER_FROM to "", // TODO:: GOODS_SEARCH || STORE_SEARCH
                    SEARCH_RESULT_ID to "", // TODO:: Id of impressed search widgets (Headline ads, carousel, regular products etc). To be decided with BE?
                    LIST_ITEM_ID to "", //TODO:: Only add for widget with sub items (Headline Ads, Carousel). Id of sub cards
                    ITEM_RANK to 0, // TODO:: Index of position inside widget. should still send for non carousel ?
                    LIST_RESULT_TYPE to "", // TODO:: GOODS if going to PDP || SHOP if going to Shop Page.
                    PRODUCT_ID to "", // TODO:: Product Id
                    SEARCH_KEYWORD to "", // TODO:: Query
                    TOKEN_TYPE to "", // TODO:: GOODS || VIDEO_GOODS || SHOP_SMALL || SHOP_BIG || GOODS_COLLECT
                    RANK to 0, // TODO:: Index in SRP
                    IS_AD to "", // TODO:: Is Ad. Should this be int?
                    IS_FIRST_SCREEN to 0, // TODO:: Is first page?
                    SHOP_ID to "" // TODO:: Shop Id of product cards
                )
            )
        )
    }

    fun <K, V> eventSearchResultClick() {
        AppLogAnalytics.send(
            SEARCH_RESULT_CLICK,
            JSONObject(
                mapOf(
                    IMPR_ID to "", // TODO:: From BE
                    SEARCH_ID to "", // TODO:: What's the difference with IMPR_ID? Is this IMPR_ID from Page 1?
                    SEARCH_ENTRANCE to "", // TODO:: HOMEPAGE. What about other page?
                    ENTER_FROM to "", // TODO:: GOODS_SEARCH || STORE_SEARCH
                    SEARCH_RESULT_ID to "", // TODO:: Id of impressed search widgets (Headline ads, carousel, regular products etc). To be decided with BE.
                    LIST_ITEM_ID to "", //TODO:: Only add for widget with sub items (Headline Ads, Carousel). Id of sub cards
                    ITEM_RANK to 0, // TODO:: Index of position inside widget. should still send for non carousel ?
                    LIST_RESULT_TYPE to "", // TODO:: GOODS if going to PDP || SHOP if going to Shop Page.
                    PRODUCT_ID to "", // TODO:: Product Id
                    SEARCH_KEYWORD to "", // TODO:: Query
                    TOKEN_TYPE to "", // TODO:: GOODS || VIDEO_GOODS || SHOP_SMALL || SHOP_BIG || GOODS_COLLECT
                    RANK to 0, // TODO:: Index in SRP
                    IS_AD to "", // TODO:: Is Ad. Should this be int?
                    IS_FIRST_SCREEN to 0, // TODO:: Is first page?
                    SHOP_ID to "", // TODO:: Shop Id of product cards
                    //TODO::
                    //"{
                    //""click_more_button"": ""Click three dots on the product card""
                    //""click_favourite_button:"" Click on the three dots of the product card to add to favorites ""
                    //""click_more_findalike"": ""Click on the three points of the product card to find similarities""
                    //""click_back"": ""Top back button""
                    //""click_shopping_cart"": ""Click on shopping cart""
                    //""click_setting"": ""Click Settings""
                    //""click_shop_name"": ""Click on the name of the shop to go to the shop homepage"" (Shop Ads)
                    //}"
                    ALADDIN_BUTTON_TYPE to "",
                )
            )
        )
    }

    fun <K, V> eventChooseSearchFilter() {
        AppLogAnalytics.send(
            CHOOSE_SEARCH_FILTER,
            JSONObject(
                mapOf(
                    SEARCH_ENTRANCE to HOMEPAGE,
                    SEARCH_ID to "",
                    SEARCH_TYPE to "", // TODO:: GOODS_SEARCH || STORE_SEARCH
                    SEARCH_KEYWORD to "", // TODO:: keyword
                    // TODO:: Sorting type:
                    //sort_relevance: related (toko: Paling Sesuai)
                    //sort_best_sellers: sales (toko: no such item)
                    //sort_review: good review (toko: Ulasan, TT no such sorting method)
                    //sort_price_asc: Price in ascending order (Toko: Harga Terendah)
                    //sort_price_desc: Price in descending order (Toko: Harga Tertinggi)
                    //sort_newest: New and Old (toko: Terbaru)
                    ECOM_SORT_NAME to "",
                    ECOM_FILTER_NAME to "",
                    ECOM_FILTER_POSITION to "",// Filter position, starts from 0
                    //TODO::
//                FILTER_PANEL = "filter_panel"
//                FILTER_GUID = "filter_guid" // TODO:: What is navigation filtering?
//                FILTER_QUICK = "filter_quick"
                    BUTTON_TYPE_CLICK to "",
                )
            )
        )
    }

    fun <K, V> eventProductShow() {
        AppLogAnalytics.send(EventName.PRODUCT_SHOW, tiktokEcommerce())
    }

    fun <K, V> eventProductClick() {
        AppLogAnalytics.send(EventName.PRODUCT_CLICK, tiktokEcommerce())
    }

    private fun tiktokEcommerce() = JSONObject(
        mapOf(
            EVENT_ORIGIN_FEATURE to EVENT_ORIGIN_FEATURE_DEFAULT_VALUE,
            SOURCE_MODULE to "", // TODO:: Is this search?
            ITEM_ORDER to 0, // TODO:: Product Position from 1
            VOLUME to 0, // TODO:: Count sold of product
            RATE to 0f, // TODO:: Rating
            IS_AD to 0, // TODO:: Is ad
            PRODUCT_ID to "", // TODO:: productid
            AppLogParam.TRACK_ID to "", // TODO:: search_id + _ + item_rank
            AppLogParam.REQUEST_ID to "", // TODO:: Request ID from BE
            SEARCH_ID to "", // TODO:: Search ID, check above
            SEARCH_RESULT_ID to "", //TODO:: Search Result ID from BE
            SEARCH_ENTRANCE to HOMEPAGE,
            ENTER_FROM to "", // TODO:: GOODS_SEARCH || STORE_SEARCH
            LIST_ITEM_ID to "",
        )
    ).apply {
        addPage()
    }
}
