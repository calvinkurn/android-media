package com.tokopedia.analytics.byteio.search

import com.tokopedia.analytics.byteio.AppLogAnalytics
import com.tokopedia.analytics.byteio.AppLogAnalytics.addPage
import com.tokopedia.analytics.byteio.AppLogAnalytics.addSourcePageType
import com.tokopedia.analytics.byteio.AppLogAnalytics.intValue
import com.tokopedia.analytics.byteio.AppLogParam
import com.tokopedia.analytics.byteio.AppLogParam.ITEM_ORDER
import com.tokopedia.analytics.byteio.AppLogParam.SOURCE_MODULE
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
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamKey.VOLUME
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamKey.WORDS_CONTENT
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamKey.WORDS_NUM
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamKey.WORDS_POSITION
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamKey.WORDS_SOURCE
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamValue.CORRECT_WORD
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamValue.ENTER
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamValue.FILTER_PANEL
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamValue.HOMEPAGE
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamValue.SEARCH_BAR_OUTER
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamValue.SUG
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
        const val SEARCH_REVISE_WORD_SHOW = "search_revise_word_show"
        const val SEARCH_REVISE_WORD_CLICK = "search_revise_word_click"
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
        const val SEARCH_CORRECT_WORD = "search_correct_word"

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
        const val TAB_SEARCH = "tab_search"
        const val REFRESH = "refresh"
        const val GOODS = "goods"
        const val VIDEO_GOODS = "video_goods"
        const val SHOP = "shop"
        const val SHOP_SMALL = "shop_small"
        const val SHOP_BIG = "shop_big"
        const val GOODS_COLLECT = "goods_collect"
        const val CLICK_MORE_BUTTON = "click_more_button" //"Click three dots on the product card"
        const val CLICK_FAVORITE_BUTTON =
            "click_favourite_button" // "Click on the three dots of the product card to add to favorites"
        const val CLICK_MORE_FINDALIKE =
            "click_more_findalike" //"Click on the three points of the product card to find similarities"
        const val CLICK_BACK = "click_back" //"Top back button"
        const val CLICK_SHOPPING_CART = "click_shopping_cart" // "Click on shopping cart"
        const val CLICK_SETTING = "click_setting" // "Click Settings"
        const val CLICK_SHOP_NAME =
            "click_shop_name" // "Click on the name of the shop to go to the shop homepage" (Shop Ads)
        const val SORT_RELEVANCE = "sort_relevance"
        const val SORT_REVIEW = "sort_review"
        const val SORT_PRICE_ASC = "sort_price_asc"
        const val SORT_PRICE_DESC = "sort_price_desc"
        const val SORT_NEWEST = "sort_newest"
        const val FILTER_PANEL = "filter_panel"
        const val FILTER_GUID = "filter_guid" // TODO:: What is navigation filtering?
        const val FILTER_QUICK = "filter_quick"
        const val CORRECT_WORD = "correct_word"
    }

    fun eventShowSearch(currentPageName: String) {
        AppLogAnalytics.send(
            SHOW_SEARCH, JSONObject(
                mapOf(
                    SEARCH_ENTRANCE to if (currentPageName == "/") HOMEPAGE else "",
                    ENTER_FROM to if (currentPageName == "/") HOMEPAGE else "",
                )
            )
        )
    }

    data class TrendingWords(
        val index: Int,
        val content: String,
        val groupId: String,
        val imprId: String,
    ) {

        fun toMap() = mapOf(
            WORDS_SOURCE to SEARCH_BAR_OUTER,
            WORDS_POSITION to index,
            WORDS_CONTENT to content,
            SEARCH_POSITION to HOMEPAGE,
            SEARCH_ENTRANCE to HOMEPAGE,
            GROUP_ID to groupId,
            IMPR_ID to imprId,
        )
    }

    fun eventTrendingWordsShow(trendingWords: TrendingWords) {
        AppLogAnalytics.send(TRENDING_WORDS_SHOW, JSONObject(trendingWords.toMap()))
    }

    fun eventTrendingWordsClick(trendingWords: TrendingWords) {
        AppLogAnalytics.send(TRENDING_WORDS_CLICK, JSONObject(trendingWords.toMap()))
    }

    data class Search(
        val imprId: String,
        val enterFrom: String,
        val searchType: String,
        val enterMethod: String,
        val searchKeyword: String,
        val durationMs: Long? = null,
        val isSuccess: Boolean? = null,
        val preSearchId: String? = null,
        val ecSearchSessionId: String? = null,
        val sugType: String? = null,
        val newSugSessionId: String? = null,
        val preClickId: String? = null,
        val blankPageEnterFrom: String? = null,
        val blankPageEnterMethod: String? = null,
        val ecomSortChosen: String? = null,
        val ecomFilterChosen: Map<String, Any>? = null,
        val ecomFilterType: String? = null,
    ) {
        fun json() = JSONObject(buildMap {
            put(IMPR_ID, imprId)
            put(ENTER_FROM, enterFrom)
            put(SEARCH_TYPE, searchType)
            put(ENTER_METHOD, enterMethod)
            put(SEARCH_KEYWORD, searchKeyword)

            durationMs?.let { put(DURATION, it) }
            isSuccess?.let { put(IS_SUCCESS, it.intValue) }
            preSearchId?.let { put(PRE_SEARCH_ID, it) }
            ecSearchSessionId?.let { put(EC_SEARCH_SESSION_ID, it) }
            sugType?.let { put(SUG_TYPE, it) }
            newSugSessionId?.let { put(NEW_SUG_SESSION_ID, it) }
            preClickId?.let { put(PRE_CLICK_ID, it) }
            blankPageEnterFrom?.let { put(BLANKPAGE_ENTER_FROM, it) }
            blankPageEnterMethod?.let { put(BLANKPAGE_ENTER_METHOD, it) }
            ecomSortChosen?.let { put(ECOM_SORT_CHOSEN, it) }
            ecomFilterChosen?.let { put(ECOM_FILTER_CHOSEN, it) }
            ecomFilterType?.let { put(ECOM_FILTER_TYPE, it) }
        })
    }

    fun eventSearch(search: Search) {
        AppLogAnalytics.send(SEARCH, search.json())
    }

    sealed class EventSearch(val from: String) {
        class Placeholder : EventSearch(PLACEHOLDER) {
            override fun json() = JSONObject(
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
        }

        class InitialState : EventSearch(INITIAL_STATE) {
            override fun json() = JSONObject(
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
        }

        class Suggestion : EventSearch(SUGGESTION) {
            override fun json() = JSONObject(
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
        }

        class Filter : EventSearch(FILTER) {
            override fun json() = JSONObject(
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
        }

        class TypoCorrection : EventSearch(TYPO_CORRECTION) {
            override fun json() = JSONObject(
                mapOf(
                    ENTER_METHOD to CORRECT_WORD,
                    SEARCH_KEYWORD to "", //TODO:: Keyword
                )
            )
        }

        fun send() {
            AppLogAnalytics.send(SEARCH, json())
        }

        abstract fun json(): JSONObject

        companion object {
            const val PLACEHOLDER = "PLACEHOLDER"
            const val INITIAL_STATE = "INITIAL_STATE"
            const val SUGGESTION = "SUGGESTION"
            const val FILTER = "FILTER"
            const val TYPO_CORRECTION = "TYPO_CORRECTION"

            fun create(from: String): EventSearch = when (from) {
                PLACEHOLDER -> Placeholder()
                INITIAL_STATE -> InitialState()
                SUGGESTION -> Suggestion()
                FILTER -> Filter()
                TYPO_CORRECTION -> TypoCorrection()
                else -> Placeholder()
            }
        }
    }

    // /////////////// -- IJ -- /////////////

    fun eventEnterSearchBlankPage(
        enterFrom: String,
        searchEntrance: String
    ) {
        AppLogAnalytics.send(
            ENTER_SEARCH_BLANKPAGE,
            JSONObject(
                mapOf(
                    ENTER_FROM to enterFrom,
                    ENTER_METHOD to ENTER,
                    SEARCH_ENTRANCE to searchEntrance
                )
            )
        )
    }

    data class TrendingShow(
        val searchPosition: String,
        val searchEntrance: String,
        val imprId: String,
        val newSugSessionId: Long,
        val rawQuery: String,
        val enterMethod: String,
        val wordsNum: Int,
        val wordSource: String = SUG,
    ) {
        fun json() = JSONObject(
            mapOf(
                SEARCH_POSITION to searchPosition,
                SEARCH_ENTRANCE to searchEntrance,
                IMPR_ID to imprId, // TODO:: Request ID from Suggestion GQL BE
                NEW_SUG_SESSION_ID to newSugSessionId,
                RAW_QUERY to rawQuery,
                ENTER_METHOD to enterMethod,
                WORDS_SOURCE to wordSource,
                WORDS_NUM to wordsNum,
            )
        )
    }

    fun eventTrendingShow(trendingShow: TrendingShow) {
        AppLogAnalytics.send(
            TRENDING_SHOW,
            trendingShow.json()
        )
    }

    data class TrendingWordsSuggestion(
        val searchPosition: String,
        val searchEntrance: String,
        val groupId: String,
        val imprId: String,
        val newSugSessionId: Long,
        val rawQuery: String,
        val enterMethod: String,
        val sugType: String,
        val wordsContent: String,
        val wordsPosition: Int,
        val wordSource: String = SUG,
    ) {
        fun json() = JSONObject(
            mapOf(
                SEARCH_POSITION to searchPosition,
                SEARCH_ENTRANCE to searchEntrance,
                GROUP_ID to groupId, // TODO:: Group ID
                IMPR_ID to imprId, // TODO:: Request ID from Suggestion GQL BE
                NEW_SUG_SESSION_ID to newSugSessionId,
                RAW_QUERY to rawQuery,
                ENTER_METHOD to enterMethod,
                SUG_TYPE to sugType, // TODO:: CAMPAIGN || PRODUCT || STORE.
                WORDS_CONTENT to wordsContent,
                WORDS_POSITION to wordsPosition,
                WORDS_SOURCE to wordSource
            )
        )
    }

    fun eventTrendingWordsShowSuggestion(trendingWordsSuggestion: TrendingWordsSuggestion) {
        AppLogAnalytics.send(
            TRENDING_WORDS_SHOW,
            trendingWordsSuggestion.json()
        )
    }

    fun eventTrendingWordsClickSuggestion(trendingWordsSuggestion: TrendingWordsSuggestion) {
        AppLogAnalytics.send(
            TRENDING_WORDS_CLICK,
            trendingWordsSuggestion.json()
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

    data class ChooseSearchFilter(
        val searchEntrance: String = HOMEPAGE,
        val searchId: String,
        val searchType: String,
        val searchKeyword: String,
        val ecomSortName: String,
        val ecomFilterName: String,
        val ecomFilterPosition: Int,
        val buttonTypeClick: String
    ) {
        fun json() = JSONObject(
            mapOf(
                SEARCH_ENTRANCE to searchEntrance,
                SEARCH_ID to searchId,
                SEARCH_TYPE to searchType, // TODO:: GOODS_SEARCH || STORE_SEARCH
                SEARCH_KEYWORD to searchKeyword, // TODO:: keyword
                // TODO:: Sorting type:
                //sort_relevance: related (toko: Paling Sesuai)
                //sort_best_sellers: sales (toko: no such item)
                //sort_review: good review (toko: Ulasan, TT no such sorting method)
                //sort_price_asc: Price in ascending order (Toko: Harga Terendah)
                //sort_price_desc: Price in descending order (Toko: Harga Tertinggi)
                //sort_newest: New and Old (toko: Terbaru)
                ECOM_SORT_NAME to ecomSortName,
                ECOM_FILTER_NAME to ecomFilterName,
                ECOM_FILTER_POSITION to ecomFilterPosition, // Filter position, starts from 0
                //TODO::
//                FILTER_PANEL = "filter_panel"
//                FILTER_GUID = "filter_guid" // TODO:: What is navigation filtering?
//                FILTER_QUICK = "filter_quick"
                BUTTON_TYPE_CLICK to buttonTypeClick,
            )
        )
    }

    fun eventChooseSearchFilter(chooseSearchFilter: ChooseSearchFilter) {
        AppLogAnalytics.send(
            CHOOSE_SEARCH_FILTER,
            chooseSearchFilter.json()
        )
    }

    fun <K, V> eventProductShow() {
        AppLogAnalytics.send(EventName.PRODUCT_SHOW, tiktokecJSON())
    }

    fun <K, V> eventProductClick() {
        AppLogAnalytics.send(EventName.PRODUCT_CLICK, tiktokecJSON())
    }

    //TODO:: Update source_module to null
    //TODO:: Update source_page_type to goods_search
    //TODO:: Update entrance_form
    private fun tiktokecJSON() = JSONObject(
        mapOf(
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
            ITEM_RANK to 0, // TODO:: item index in carousels
            LIST_RESULT_TYPE to "",
            SEARCH_KEYWORD to "",
            TOKEN_TYPE to "",
            RANK to 0, // TODO:: Item index in whole page
            SHOP_ID to "",
        )
    ).apply {
        addPage()
//        addSourceModule() // TODO milhamj diapain ini?
        addSourcePageType()
    }
}
