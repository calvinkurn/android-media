package com.tokopedia.createpost.producttag.view.uimodel


/**
 * Created By : Jonathan Darwin on May 17, 2022
 */
data class SearchParamUiModel(
    val value: HashMap<String, Any> = hashMapOf(),
) {

    var query: String
        get() = value[KEY_QUERY]?.toString() ?: ""
        set(newValue) {
            value[KEY_QUERY] = newValue
        }

    var start: Int
        get() = try { value[KEY_START]?.toString()?.toInt() ?: LIMIT_PER_PAGE }
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

    /** Add param 1 by 1, if exists then param will be appended */
    fun addParam(key: String, value: Any) {
        val newValue = if(this.value.containsKey(key))
            (this.value[key] as String) + DEFAULT_MULTIPLE_PARAM_SEPARATOR + value
         else value

        this.value[key] = newValue
    }

    /** Rewrite existing param if any */
    fun rewriteParam(key: String, value: Any) {
        this.value[key] = value
    }

    fun removeParam(key: String, value: String) {
        if(this.value.containsKey(key)) {
            if(this.value[key] is String) {
                val split = this.value[key].toString().split(DEFAULT_MULTIPLE_PARAM_SEPARATOR).toMutableList()
                split.remove(value)

                this.value[key] = split.joinToString(separator = DEFAULT_MULTIPLE_PARAM_SEPARATOR)
            }
            else this.value.remove(key)
        }
    }

    fun isParamFound(key: String, value: Any): Boolean {
        if(this.value.containsKey(key)) {
            if(this.value[key] is String) {
                val split = this.value[key].toString().split(DEFAULT_MULTIPLE_PARAM_SEPARATOR)
                return split.firstOrNull { it == value } != null
            }

            return value == this.value[key]
        }

        return false
    }

    fun resetPagination() {
        rows = LIMIT_PER_PAGE
        start = 0
    }

    fun toCompleteParam(): String {
        return value.map {
            it.toString()
        }.joinToString(separator = DEFAULT_PARAM_SEPARATOR)
    }

    fun setDefaultParam() {
        value[KEY_DEVICE] = "android"
        value[KEY_FROM] = "feed_content"
        value[KEY_SOURCE] = "universe"
    }

    companion object {
        private const val KEY_DEVICE = "device"
        private const val KEY_FROM = "from"
        private const val KEY_SOURCE = "source"

        private const val KEY_QUERY = "q"
        private const val KEY_START = "start"
        private const val KEY_ROWS = "rows"
        private const val KEY_SHOP_ID = "shop_id"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_OB = "ob"

        private const val LIMIT_PER_PAGE = 20

        private const val DEFAULT_MULTIPLE_PARAM_SEPARATOR = "#"
        private const val DEFAULT_PARAM_SEPARATOR = "&"

        const val SOURCE_SEARCH_PRODUCT = "search_product"

        val Empty: SearchParamUiModel
            get() = SearchParamUiModel().apply {
                resetPagination()
                setDefaultParam()
                query = ""
            }
    }
}