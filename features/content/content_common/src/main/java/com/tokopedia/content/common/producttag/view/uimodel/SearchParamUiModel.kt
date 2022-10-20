package com.tokopedia.content.common.producttag.view.uimodel

import com.tokopedia.filter.common.helper.getSortFilterCount


/**
 * Created By : Jonathan Darwin on May 17, 2022
 */
data class SearchParamUiModel(
    val value: HashMap<String, Any>,
) {

    var query: String
        get() = value[KEY_QUERY]?.toString() ?: ""
        set(newValue) {
            value[KEY_QUERY] = newValue
        }

    var start: Int
        get() = try { value[KEY_START]?.toString()?.toInt() ?: 0 }
                catch (e: Exception) { 0 }
        set(newValue) {
            value[KEY_START] = newValue
        }

    var rows: Int
        get() = try { value[KEY_ROWS]?.toString()?.toInt() ?: LIMIT_PER_PAGE }
                catch (e: Exception) { LIMIT_PER_PAGE }
        set(newValue) {
            value[KEY_ROWS] = newValue
        }

    var shopId: String
        get() = value[KEY_SHOP_ID]?.toString() ?: ""
        set(newValue) {
            value[KEY_SHOP_ID] = newValue
        }

    var userId: String
        get() = value[KEY_USER_ID]?.toString() ?: ""
        set(newValue) {
            value[KEY_USER_ID] = newValue
        }

    var source: String
        get() = value[KEY_SOURCE]?.toString() ?: ""
        set(newValue) {
            value[KEY_SOURCE] = newValue
        }

    var pageSource: String
        get() = value[KEY_PAGE_SOURCE]?.toString() ?: ""
        set(newValue) {
            value[KEY_PAGE_SOURCE] = newValue
        }

    var device: String
        get() = value[KEY_DEVICE]?.toString() ?: ""
        set(newValue) {
            value[KEY_DEVICE] = newValue
        }

    var componentId: String
        get() = value[KEY_COMPONENT_ID]?.toString() ?: ""
        set(newValue) {
            value[KEY_COMPONENT_ID] = newValue
        }

    var prevQuery: String
        get() = value[KEY_PREV_QUERY]?.toString() ?: ""
        set(newValue) {
            value[KEY_PREV_QUERY] = newValue
        }

    val isFirstPage: Boolean
        get() = start == 0

    /** Add param 1 by 1, if exists then param will be appended */
    fun addParam(key: String, value: Any) {
        val newValue = if(this.value.containsKey(key))
            (this.value[key] as String) + DEFAULT_MULTIPLE_PARAM_SEPARATOR + value
         else value

        this.value[key] = newValue
    }

    fun removeParam(key: String, value: String) {
        val map = this.value
        if(map.containsKey(key)) {
            if(map[key] is String) {
                val split = map[key].toString().split(DEFAULT_MULTIPLE_PARAM_SEPARATOR).toMutableList()
                split.remove(value)

                if(split.size > 0)
                    map[key] = split.joinToString(separator = DEFAULT_MULTIPLE_PARAM_SEPARATOR)
                else map.remove(key)
            }
            else map.remove(key)
        }
    }

    fun isParamFound(key: String, value: Any): Boolean {
        val map = this.value
        if(map.containsKey(key)) {
            if(map[key] is String) {
                val split = map[key].toString().split(DEFAULT_MULTIPLE_PARAM_SEPARATOR)
                return split.firstOrNull { it == value } != null
            }

            return value == map[key]
        }

        return false
    }

    fun resetPagination() {
        rows = LIMIT_PER_PAGE
        start = 0
    }

    fun joinToString(): String {
        return getCleanMap().map {
                it.toString()
            }
            .joinToString(separator = DEFAULT_PARAM_SEPARATOR)
            .replace("#",",")
    }

    fun toTrackerString(): String {
        return getCleanMap().map {
                it.toString()
            }
            .joinToString(separator = ";")
            .replace("#",",")
            .replace("=", ":")
    }

    fun getFilterCount(): Int {
        return getSortFilterCount(getCleanMap())
    }

    fun hasFilterApplied(): Boolean {
        return getFilterCount() > 0
    }

    /** remove special param for feed */
    private fun getCleanMap(): HashMap<String, Any> {
        val copyMap = HashMap(value)

        feedAdditionalKey.forEach {
            copyMap.remove(it)
        }

        return copyMap
    }

    private fun setDefaultParam() {
        value[KEY_DEVICE] = "android"
        value[KEY_FROM] = "feed_content"
        value[KEY_SOURCE] = "universe"
    }

    private val feedAdditionalKey = listOf(KEY_PREV_QUERY)

    companion object {
        private const val KEY_DEVICE = "device"
        private const val KEY_FROM = "from"
        private const val KEY_SOURCE = "source"
        private const val KEY_PAGE_SOURCE = "page_source"

        private const val KEY_START = "start"
        private const val KEY_ROWS = "rows"
        private const val KEY_SHOP_ID = "shop_id"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_PREV_QUERY = "prev_query"

        private const val LIMIT_PER_PAGE = 20

        private const val DEFAULT_MULTIPLE_PARAM_SEPARATOR = "#"
        private const val DEFAULT_PARAM_SEPARATOR = "&"

        const val SOURCE_SEARCH_PRODUCT = "search_product"
        const val SOURCE_SEARCH_SHOP = "search_shop"
        const val KEY_QUERY = "q"
        const val KEY_COMPONENT_ID = "srp_component_id"

        val Empty: SearchParamUiModel
            get() = SearchParamUiModel(value = hashMapOf()).apply {
                resetPagination()
                setDefaultParam()
                query = ""
            }
    }
}