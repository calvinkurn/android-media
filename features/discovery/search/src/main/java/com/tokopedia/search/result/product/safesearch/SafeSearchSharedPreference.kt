package com.tokopedia.search.result.product.safesearch

import android.content.Context
import com.tokopedia.kotlin.extensions.boolean

class SafeSearchSharedPreference(
    context: Context
) : SafeSearchPreference {
    private val sharedPref = context.getSharedPreferences(
        SEARCH_SAFE_SEARCH_PREFERENCE,
        Context.MODE_PRIVATE
    )

    override var isShowAdult: Boolean by sharedPref.boolean(false, SHOW_ADULT)

    companion object {
        private const val SEARCH_SAFE_SEARCH_PREFERENCE = "search_safe_search_preference"

        private const val SHOW_ADULT = "show_adult"
    }
}
