package com.tokopedia.flight.search.presentation.util

import android.content.Context
import android.content.SharedPreferences
import com.tokopedia.utils.date.DateUtil
import com.tokopedia.utils.date.addTimeToSpesificDate
import com.tokopedia.utils.date.toDate
import com.tokopedia.utils.date.toString
import java.util.*

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

    fun setInternationalTransitTag(transitTag: String) {
        editor.putString(INTERNATIONAL_TRANSIT_TAG, transitTag)
                .apply()
    }

    fun getInternationalTransitTag(): String = sharedPrefs.getString(INTERNATIONAL_TRANSIT_TAG, "")
            ?: ""

    fun setBackgroundRefreshTime(refreshTimeInSeconds: Int) {
        val expiredTime = DateUtil.getCurrentDate().addTimeToSpesificDate(Calendar.SECOND, refreshTimeInSeconds)
        editor.putString(
                BACKGROUND_REFRESH_TIME,
                expiredTime.toString(DateUtil.YYYY_MM_DD_T_HH_MM_SS_Z)
        ).apply()
    }

    fun isBackgroundCacheExpired(): Boolean {
        val expiredTimeString = sharedPrefs.getString(BACKGROUND_REFRESH_TIME, "") ?: ""
        return if (expiredTimeString.isNotEmpty()) {
            val expiredTime = expiredTimeString.toDate(DateUtil.YYYY_MM_DD_T_HH_MM_SS_Z)
            DateUtil.getCurrentDate().after(expiredTime)
        } else {
            false
        }
    }

    companion object {
        private const val CACHE_NAME = "FlightSearchCache"
        private const val SEARCH_COACH_MARK = "SEARCH_COACH_MARK"
        private const val INTERNATIONAL_TRANSIT_TAG = "INTERNATIONAL_TRANSIT_TAG"
        private const val BACKGROUND_REFRESH_TIME = "BACKGROUND_REFRESH_TIME"
        private const val DEFAULT_COACH_MARK = false
    }
}