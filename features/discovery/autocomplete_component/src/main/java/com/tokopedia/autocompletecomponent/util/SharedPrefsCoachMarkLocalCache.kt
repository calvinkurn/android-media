package com.tokopedia.autocompletecomponent.util

import android.content.Context
import android.content.SharedPreferences
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import java.util.Calendar
import javax.inject.Inject

class SharedPrefsCoachMarkLocalCache @Inject constructor(
    @ApplicationContext
    context: Context?
) : CoachMarkLocalCache {

    private val sharedPref: SharedPreferences? by lazy {
        context?.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    override fun shouldShowPlusIconCoachMark(): Boolean {
        val sharedPref = sharedPref ?: return false
        val value = sharedPref.getString(KEY_COACH_MARK_PLUS_ICON, "")
        return if (value.isNullOrBlank()) {
            true
        } else {
            val timeStampList = value.split(',')
            if (timeStampList.size > 2) {
                false
            } else {
                val currentTimeStamp = Calendar.getInstance().timeInMillis
                val lastTimeStamp = timeStampList.lastOrNull()?.trim().toLongOrZero()
                currentTimeStamp - lastTimeStamp > ONE_DAY
            }
        }
    }

    override fun markShowPlusIconCoachMark() {
        val sharedPref = sharedPref ?: return
        val value = sharedPref.getString(KEY_COACH_MARK_PLUS_ICON, "")
        val timeStampList: List<String> = if (value.isNullOrBlank()) {
            emptyList()
        } else {
            value.split(',')
        }
        val currentTimeStamp = Calendar.getInstance().timeInMillis
        if (timeStampList.isNotEmpty()) {
            val lastTimeStamp = timeStampList.lastOrNull()?.trim().toLongOrZero()
            if (lastTimeStamp + ONE_DAY > currentTimeStamp) return
        }
        val newTimeStampList = timeStampList + currentTimeStamp.toString()
        sharedPref.edit()
            .putString(KEY_COACH_MARK_PLUS_ICON, newTimeStampList.joinToString(separator = ","))
            .apply()
    }

    override fun shouldShowAddedKeywordCoachMark(): Boolean {
        val sharedPref = sharedPref ?: return false
        return sharedPref.getBoolean(KEY_COACH_MARK_ADDED_KEYWORD, true)
    }

    override fun markShowAddedKeywordCoachMark() {
        val sharedPref = sharedPref ?: return
        sharedPref.edit()
            .putBoolean(KEY_COACH_MARK_ADDED_KEYWORD, false)
            .apply()
    }

    companion object {
        private const val PREF_NAME = "AutoCompleteCoachMarkSharedPref"
        const val KEY_COACH_MARK_PLUS_ICON = "KEY_COACH_MARK_PLUS_ICON"
        const val KEY_COACH_MARK_ADDED_KEYWORD = "KEY_COACH_MARK_ADDED_KEYWORD"

        private const val ONE_DAY = 24 * 60 * 60 * 1000L
    }
}
