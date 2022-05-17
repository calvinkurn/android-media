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


    companion object {
        private const val KEY_QUERY = "q"
        private const val KEY_START = "start"
        private const val KEY_ROWS = "rows"
        private const val KEY_SHOP_ID = "shop_id"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_OB = "ob"

        private const val LIMIT_PER_PAGE = 20

        val Empty: SearchParamUiModel
            get() = SearchParamUiModel().apply {
                rows = LIMIT_PER_PAGE
                start = 0
                query = ""
            }
    }
}