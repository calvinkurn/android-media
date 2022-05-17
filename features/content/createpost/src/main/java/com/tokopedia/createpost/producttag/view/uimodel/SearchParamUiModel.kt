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

    companion object {
        private const val KEY_QUERY = "q"

        val Empty = SearchParamUiModel()
    }
}