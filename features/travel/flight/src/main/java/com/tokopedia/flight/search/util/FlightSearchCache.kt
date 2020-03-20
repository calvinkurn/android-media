package com.tokopedia.flight.search.util

import android.content.Context
import android.content.SharedPreferences

/**
 * @author by furqan on 19/03/2020
 */
class FlightSearchCache(context: Context) {

    private val sharedPrefs: SharedPreferences = context.getSharedPreferences(CACHE_NAME, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPrefs.edit()

    fun setSearchCoachMarkIsShowed() {
        editor.putBoolean(SEARCH_COACH_MARK, true)
                .apply()
    }

    fun isSearchCoachMarkShowed(): Boolean = sharedPrefs.getBoolean(SEARCH_COACH_MARK, DEFAULT_COACH_MARK)

    companion object {
        private const val CACHE_NAME = "FlightSearchCache"
        private const val SEARCH_COACH_MARK = "SEARCH_COACH_MARK"
        private const val DEFAULT_COACH_MARK = false
    }
}