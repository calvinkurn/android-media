package com.tokopedia.discovery.common.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPrefSimilarSearchCoachMarkLocalCache(
    context: Context?
): SimilarSearchCoachMarkLocalCache {

    private val sharedPref: SharedPreferences? by lazy {
        context?.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    override fun shouldShowThreeDotsCoachmark(): Boolean {
        val shouldShow = sharedPref?.getBoolean(KEY_SHOW_THREE_DOTS_COACHMARK, true) ?: false
        if (shouldShow) {
            setShown(KEY_SHOW_THREE_DOTS_COACHMARK)
        }
        return shouldShow
    }

    override fun shouldShowSimilarSearchProductOptionCoachmark(): Boolean {
        val shouldShow = sharedPref?.getBoolean(KEY_SHOW_SIMILAR_SEARCH_PRODUCT_OPTIONS_COACHMARK, true) ?: false
        if (shouldShow) {
            setShown(KEY_SHOW_SIMILAR_SEARCH_PRODUCT_OPTIONS_COACHMARK)
        }
        return shouldShow
    }

    private fun setShown(key: String) {
        val sharedPref = sharedPref ?: return
        sharedPref.edit().putBoolean(key, false).apply()
    }

    companion object {
        private const val PREF_NAME = "SimilarSearchCoachmarkSharedPref"
        private const val KEY_SHOW_THREE_DOTS_COACHMARK = "KEY_SHOW_THREE_DOTS_COACHMARK"
        private const val KEY_SHOW_SIMILAR_SEARCH_PRODUCT_OPTIONS_COACHMARK = "KEY_SHOW_SIMILAR_SEARCH_PRODUCT_OPTIONS_COACHMARK"
    }
}
