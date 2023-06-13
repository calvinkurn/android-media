package com.tokopedia.fakeresponse

import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.StringDef
import com.tokopedia.fakeresponse.SortBy.Companion.DEFAULT
import com.tokopedia.fakeresponse.SortBy.Companion.TIME_DESC

object Preference {

    private const val PREFERENCE_KEY = "fake_response_pref"

    private val PREFS: SharedPreferences? by lazy {
        App.INSTANCE.getSharedPreferences(PREFERENCE_KEY, Context.MODE_PRIVATE)
    }

    private val SORT_BY = "sort_by"
    private val NOTIFICATION_ENABLE = "fake_response_notification_enable"

    fun updateSortBy(@SortBy sortBy: String) {
        PREFS?.edit()?.putString(SORT_BY, sortBy)?.apply()
    }

    fun updateNotification(isEnable: Boolean) {
        PREFS?.edit()?.putBoolean(NOTIFICATION_ENABLE, isEnable)?.apply()
    }

    @SortBy
    fun getSortBy(): String {
        return PREFS?.getString(SORT_BY, DEFAULT) ?: DEFAULT
    }

    fun getIsEnableNotification(context: Context): Boolean {
        val sharedPreferences =
            context.getSharedPreferences(PREFERENCE_KEY, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(NOTIFICATION_ENABLE, true)
    }

}

@Retention(AnnotationRetention.SOURCE)
@StringDef(TIME_DESC, DEFAULT)
annotation class SortBy {
    companion object {
        const val DEFAULT = "default"
        const val TIME_DESC = "time_desc"
    }
}
