package com.tokopedia.analytics.byteio.search

import com.tokopedia.analytics.byteio.AppLogAnalytics
import com.tokopedia.analytics.byteio.AppLogAnalytics.addPage
import com.tokopedia.analytics.byteio.AppLogAnalytics.intValue
import com.tokopedia.analytics.byteio.AppLogInterface
import com.tokopedia.analytics.byteio.AppLogParam
import com.tokopedia.analytics.byteio.AppLogParam.ENTER_FROM
import com.tokopedia.analytics.byteio.AppLogParam.ENTRANCE_FORM
import com.tokopedia.analytics.byteio.AppLogParam.IS_SHADOW
import com.tokopedia.analytics.byteio.AppLogParam.PAGE_NAME
import com.tokopedia.analytics.byteio.AppLogParam.PREVIOUS_PAGE
import com.tokopedia.analytics.byteio.AppLogParam.SOURCE_PAGE_TYPE
import com.tokopedia.analytics.byteio.EntranceForm
import com.tokopedia.analytics.byteio.EventName
import com.tokopedia.analytics.byteio.EventName.CART_ENTRANCE_CLICK
import com.tokopedia.analytics.byteio.EventName.CART_ENTRANCE_SHOW
import com.tokopedia.analytics.byteio.PageName
import com.tokopedia.analytics.byteio.search.AppLogSearch.Event.CHOOSE_SEARCH_FILTER
import com.tokopedia.analytics.byteio.search.AppLogSearch.Event.ENTER_SEARCH_BLANKPAGE
import com.tokopedia.analytics.byteio.search.AppLogSearch.Event.RD_TIKTOKEC_MEANINGFUL_RENDERED
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
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamKey.DEFAULT_SEARCH_KEYWORD
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamKey.DURATION
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamKey.ECOM_FILTER_CHOSEN
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamKey.ECOM_FILTER_NAME
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamKey.ECOM_FILTER_POSITION
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamKey.ECOM_FILTER_TYPE
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamKey.ECOM_SORT_CHOSEN
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamKey.ECOM_SORT_NAME
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamKey.EC_SEARCH_SESSION_ID
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
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamKey.RAW_QUERY
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamKey.SEARCH_ENTRANCE
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamKey.SEARCH_ID
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamKey.SEARCH_KEYWORD
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamKey.SEARCH_POSITION
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamKey.SEARCH_RESULT_ID
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamKey.SEARCH_TYPE
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamKey.SHOP_ID
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamKey.START_TO_NOW
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamKey.SUG_TYPE
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamKey.TOKEN_TYPE
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamKey.WORDS_CONTENT
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamKey.WORDS_NUM
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamKey.WORDS_POSITION
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamKey.WORDS_SOURCE
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamValue.GOODS_SEARCH
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamValue.HOMEPAGE
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamValue.SEARCH_HISTORY
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamValue.SEARCH_RESULT
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamValue.SEARCH_SUG
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamValue.STORE_SEARCH
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamValue.SUG
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamValue.SUG_RECOM
import org.json.JSONObject

object AppLogSearch {

    private val whitelistedEnterFrom = listOf(GOODS_SEARCH, STORE_SEARCH, PageName.HOME)
    private const val TRENDING_WORDS_CLICK_DATA_KEY = "trending_words_click_data"

    /**
     * Enter method where we should get and track new_sug_session and sug_type
     */
    val sugEnterMethod = listOf(
        DEFAULT_SEARCH_KEYWORD,
        SUG_RECOM,
        SEARCH_SUG,
        SEARCH_HISTORY
    )

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
        const val RD_TIKTOKEC_MEANINGFUL_RENDERED = "rd_tiktokec_meaningful_rendered"
    }

    object ParamKey {
        const val SEARCH_ENTRANCE = "search_entrance"
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
        const val IS_AD = "is_ad"
        const val PRODUCT_ID = "product_id"
        const val DEFAULT_SEARCH_KEYWORD = "default_search_keyword"
        const val START_TO_NOW = "start_to_now"
    }

    object ParamValue {
        const val HOMEPAGE = "homepage"
        const val GOODS_SEARCH = "goods_search"
        const val STORE_SEARCH = "store_search"
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
        const val CLICK_MORE_BUTTON = "click_more_button"
        const val CLICK_FAVORITE_BUTTON = "click_favourite_button"
        const val CLICK_MORE_FINDALIKE = "click_more_findalike"
        const val CLICK_SHOP_NAME = "click_shop_name"
        const val CLICK_ADD_TO_CART = "click_add_to_cart"
        const val CLICK_SEE_MORE = "click_see_more"
        const val CLICK_CARD_SEE_MORE = "click_card_see_more"
        const val SORT_RELEVANCE = "sort_relevance"
        const val SORT_REVIEW = "sort_review"
        const val SORT_PRICE_ASC = "sort_price_asc"
        const val SORT_PRICE_DESC = "sort_price_desc"
        const val SORT_NEWEST = "sort_newest"
        const val FILTER_PANEL = "filter_panel"
        const val FILTER_QUICK = "filter_quick"
        const val CORRECT_WORD = "correct_word"
        const val SEARCH_RESULT = "search_result"
        const val SEARCH_BAR_OUTER = "search_bar_outer"
        const val SEARCH_BAR_BUTTON = "search_bar_button"
        const val DEFAULT_SEARCH_KEYWORD_OUTER = "default_search_keyword_outer"
    }

    fun eventShowSearch() {
        AppLogAnalytics.send(
            SHOW_SEARCH,
            JSONObject(
                mapOf(
                    SEARCH_ENTRANCE to PageName.HOME,
                    ENTER_FROM to PageName.HOME
                )
            )
        )
    }

    data class TrendingWords(
        val index: Int,
        val content: String,
        val groupId: String,
        val imprId: String,
        val wordsSource: String,
        val searchEntrance: String
    ) {

        fun toMap() = mapOf(
            WORDS_SOURCE to wordsSource,
            WORDS_POSITION to index,
            WORDS_CONTENT to content,
            SEARCH_POSITION to HOMEPAGE,
            SEARCH_ENTRANCE to searchEntrance,
            GROUP_ID to groupId,
            IMPR_ID to imprId
        )
    }

    fun eventTrendingWordsShow(trendingWords: TrendingWords) {
        AppLogAnalytics.send(TRENDING_WORDS_SHOW, JSONObject(trendingWords.toMap()))
    }

    fun eventTrendingWordsClick() {
        val trendingWordsMap = AppLogAnalytics.getDataBeforeCurrent(TRENDING_WORDS_CLICK_DATA_KEY)

        if (trendingWordsMap !is Map<*, *>) return

        AppLogAnalytics.send(TRENDING_WORDS_CLICK, JSONObject(trendingWordsMap))
    }

    fun eventTrendingWordsClick(trendingWords: TrendingWords) {
        AppLogAnalytics.send(TRENDING_WORDS_CLICK, JSONObject(trendingWords.toMap()))
    }

    fun saveTrendingWordsClickData(trendingWords: TrendingWords) {
        AppLogAnalytics.putPageData(TRENDING_WORDS_CLICK_DATA_KEY, trendingWords.toMap())
    }

    fun cleanTrendingWordsClickData() {
        AppLogAnalytics.removePageData(TRENDING_WORDS_CLICK_DATA_KEY)
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
        val ecomFilterType: String? = null
    ) {
        fun json() = JSONObject(
            buildMap {
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
            }
        )
    }

    fun eventSearch(search: Search) {
        AppLogAnalytics.send(SEARCH, search.json())
    }

    fun eventEnterSearchBlankPage(
        enterFrom: String,
        enterMethod: String,
        searchEntrance: String
    ) {
        AppLogAnalytics.send(
            ENTER_SEARCH_BLANKPAGE,
            JSONObject(
                mapOf(
                    ENTER_FROM to enterFrom,
                    ENTER_METHOD to enterMethod,
                    SEARCH_ENTRANCE to searchEntrance
                )
            )
        )
    }

    data class TrendingShow(
        val imprId: String,
        val newSugSessionId: String,
        val rawQuery: String,
        val enterMethod: String,
        val wordsSource: String,
        val wordsNum: Int,
        val searchEntrance: String
    ) {
        fun json() = JSONObject(
            mapOf(
                SEARCH_POSITION to enterFrom(),
                SEARCH_ENTRANCE to searchEntrance,
                IMPR_ID to imprId,
                NEW_SUG_SESSION_ID to newSugSessionId,
                RAW_QUERY to rawQuery,
                ENTER_METHOD to enterMethod,
                WORDS_SOURCE to wordsSource,
                WORDS_NUM to wordsNum
            )
        )
    }

    fun eventTrendingShow(trendingShow: TrendingShow) {
        AppLogAnalytics.send(TRENDING_SHOW, trendingShow.json())
    }

    data class TrendingWordsSuggestion(
        val groupId: String,
        val imprId: String,
        val newSugSessionId: String,
        val rawQuery: String,
        val enterMethod: String,
        val sugType: String,
        val wordsContent: String,
        val wordsPosition: Int,
        val wordSource: String = SUG,
        val searchEntrance: String
    ) {
        fun json() = JSONObject(
            mapOf(
                SEARCH_POSITION to enterFrom(),
                SEARCH_ENTRANCE to searchEntrance,
                GROUP_ID to groupId,
                IMPR_ID to imprId,
                NEW_SUG_SESSION_ID to newSugSessionId,
                RAW_QUERY to rawQuery,
                ENTER_METHOD to enterMethod,
                SUG_TYPE to sugType,
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

    data class SearchResult(
        val imprId: String,
        val searchId: String,
        val searchEntrance: String,
        val searchResultId: String,
        val listItemId: String?,
        val itemRank: Int?,
        val listResultType: String?,
        val productID: String,
        val searchKeyword: String,
        val tokenType: String,
        val rank: Int,
        val isAd: Boolean,
        val isFirstPage: Boolean,
        val shopId: String?,
        val aladdinButtonType: String?
    ) {
        val trackId: String
            get() = "${searchId}_${(itemRank ?: rank)}"

        fun json() = JSONObject(
            buildMap {
                put(IMPR_ID, imprId)
                put(SEARCH_ID, searchId)
                put(SEARCH_ENTRANCE, searchEntrance)
                put(ENTER_FROM, GOODS_SEARCH)
                put(SEARCH_RESULT_ID, searchResultId)
                listItemId?.let { put(LIST_ITEM_ID, it) }
                itemRank?.let { put(ITEM_RANK, it) }
                listResultType?.let { put(LIST_RESULT_TYPE, it) }
                put(PRODUCT_ID, productID)
                put(SEARCH_KEYWORD, searchKeyword)
                put(TOKEN_TYPE, tokenType)
                put(RANK, rank)
                put(IS_AD, isAd.intValue)
                put(IS_FIRST_SCREEN, isFirstPage.intValue)
                shopId?.let { put(SHOP_ID, it) }
                aladdinButtonType?.let { put(ALADDIN_BUTTON_TYPE, it) }
            }
        )
    }

    fun enterFrom(): String {
        val actualEnterFrom = AppLogAnalytics.getLastDataBeforeCurrent(ENTER_FROM)?.toString() ?: ""

        return if (whitelistedEnterFrom.contains(actualEnterFrom)) {
            actualEnterFrom
        } else {
            ""
        }
    }

    fun eventSearchResultShow(searchResult: SearchResult) {
        AppLogAnalytics.send(SEARCH_RESULT_SHOW, searchResult.json())
    }

    fun eventSearchResultClick(searchResult: SearchResult) {
        AppLogAnalytics.send(SEARCH_RESULT_CLICK, searchResult.json())

        with(searchResult) {
            AppLogAnalytics.setGlobalParamOnClick(
                trackId = trackId,
                requestId = imprId
            )

            AppLogAnalytics.putPageData(SEARCH_RESULT_ID, searchResultId)
            listItemId?.let { AppLogAnalytics.putPageData(LIST_ITEM_ID, it) }
        }
    }

    data class Performance(
        val startToNowMilis: Long,
        val enterMethod: String
    ) {
        fun json() = JSONObject(
            buildMap {
                put(START_TO_NOW, startToNowMilis)
                put(ENTER_METHOD, enterMethod)
            }
        ).apply {
            addPage()
        }
    }

    fun eventPerformanceTracking(performance: Performance) {
        AppLogAnalytics.send(RD_TIKTOKEC_MEANINGFUL_RENDERED, performance.json())
    }

    data class ChooseSearchFilter(
        val searchID: String,
        val searchType: String,
        val keyword: String,
        val ecomSortName: String?,
        val ecomFilterName: String,
        val ecomFilterPosition: String,
        val buttonTypeClick: String,
        val searchEntrance: String
    ) {

        fun json() = JSONObject(
            buildMap {
                put(SEARCH_ENTRANCE, searchEntrance)
                put(SEARCH_ID, searchID)
                put(SEARCH_TYPE, searchType)
                put(SEARCH_KEYWORD, keyword)
                ecomSortName?.let { put(ECOM_SORT_NAME, it) }
                put(ECOM_FILTER_NAME, ecomFilterName)
                put(ECOM_FILTER_POSITION, ecomFilterPosition)
                put(BUTTON_TYPE_CLICK, buttonTypeClick)
            }
        )
    }

    fun eventChooseSearchFilter(chooseSearchFilter: ChooseSearchFilter) {
        AppLogAnalytics.putPageData(ECOM_FILTER_TYPE, chooseSearchFilter.buttonTypeClick)

        AppLogAnalytics.send(CHOOSE_SEARCH_FILTER, chooseSearchFilter.json())
    }

    data class Product(
        val entranceForm: EntranceForm,
        val isAd: Boolean,
        val productID: String,
        val searchID: String,
        val requestID: String,
        val searchResultID: String,
        val listItemId: String?,
        val itemRank: Int?,
        val listResultType: String?,
        val searchKeyword: String,
        val tokenType: String,
        val rank: Int,
        val shopID: String?,
        val searchEntrance: String,
        val sourcePageType: String
    ) {
        val trackId: String
            get() = "${searchID}_${(rank)}"

        val isAdInt: Int
            get() = isAd.intValue

        fun json() = JSONObject(
            buildMap {
                put(ENTRANCE_FORM, entranceForm.str)
                put(SOURCE_PAGE_TYPE, sourcePageType)
                put(IS_AD, isAdInt)
                put(PRODUCT_ID, productID)
                put(AppLogParam.TRACK_ID, trackId)
                put(AppLogParam.REQUEST_ID, requestID)
                put(SEARCH_ID, searchID)
                put(SEARCH_RESULT_ID, searchResultID)
                put(SEARCH_ENTRANCE, searchEntrance)
                put(ENTER_FROM, GOODS_SEARCH)
                listItemId?.let { put(LIST_ITEM_ID, it) }
                itemRank?.let { put(ITEM_RANK, it) }
                listResultType?.let { put(LIST_RESULT_TYPE, it) }
                put(SEARCH_KEYWORD, searchKeyword)
                put(TOKEN_TYPE, tokenType)
                put(RANK, rank)
                shopID?.let { put(SHOP_ID, it) }
            }
        ).apply {
            addPage()
        }
    }

    fun eventProductShow(product: Product) {
        AppLogAnalytics.send(EventName.PRODUCT_SHOW, product.json())
    }

    fun eventProductClick(product: Product) {
        AppLogAnalytics.send(EventName.PRODUCT_CLICK, product.json())

        with(product) {
            AppLogAnalytics.setGlobalParamOnClick(
                entranceForm = entranceForm.str,
                enterMethod = null,
                sourceModule = null,
                isAd = isAdInt,
                trackId = trackId,
                sourcePageType = sourcePageType,
                requestId = requestID
            )

            AppLogAnalytics.putPageData(SEARCH_RESULT_ID, searchResultID)
            listItemId?.let { AppLogAnalytics.putPageData(LIST_ITEM_ID, it) }
        }
    }

    fun eventCartEntranceShow() {
        AppLogAnalytics.send(CART_ENTRANCE_SHOW, cartEntranceJSON())
    }

    fun eventCartEntranceClick() {
        AppLogAnalytics.send(CART_ENTRANCE_CLICK, cartEntranceJSON())
    }

    private fun cartEntranceJSON() =
        JSONObject().apply {
            put(PREVIOUS_PAGE, AppLogAnalytics.getLastDataBeforeCurrent(PAGE_NAME))
            put(PAGE_NAME, SEARCH_RESULT)
        }

    fun updateSearchPageData(appLogInterface: AppLogInterface) {
        val updatedPageDataKeys = listOf(PAGE_NAME, ENTER_FROM, IS_SHADOW)

        val currentPageData = AppLogAnalytics.pageDataList
            .lastOrNull()
            ?.filterNot {
                updatedPageDataKeys.contains(it.key)
            }

        AppLogAnalytics.updateCurrentPageData(appLogInterface)

        currentPageData?.forEach { (key, value) -> AppLogAnalytics.putPageData(key, value) }
    }
}
