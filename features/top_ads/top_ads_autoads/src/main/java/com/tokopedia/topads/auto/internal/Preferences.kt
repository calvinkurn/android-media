package com.tokopedia.topads.auto.internal

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

/**
 * Author errysuprayogi on 18,June,2019
 */
object Preferences {

    private val KEY_DAILY_BUDGET = "autoads_daily_budget"

    private fun getSharedPreference(context: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

    fun setDailyBudget(context: Context, budget: Int) {
        val editor = getSharedPreference(context).edit()
        editor.putInt(KEY_DAILY_BUDGET, budget)
        editor.apply()
    }

    fun getDailyBudget(context: Context): Int {
        return getSharedPreference(context).getInt(KEY_DAILY_BUDGET, 0)
    }
}
