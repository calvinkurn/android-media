package com.tokopedia.fakeresponse

import android.content.Context
import androidx.annotation.StringDef
import com.tokopedia.fakeresponse.SortBy.Companion.DEFAULT
import com.tokopedia.fakeresponse.SortBy.Companion.TIME_DESC

object Preference {

    private const val PREFERENCE_KEY = "fake_response_pref"
    private val PREFS = App.INSTANCE.getSharedPreferences(PREFERENCE_KEY, Context.MODE_PRIVATE)

    private val SORT_BY = "sort_by"

    fun updateSortBy(@SortBy sortBy: String) {
        PREFS.edit().putString(SORT_BY, sortBy).apply()
    }

    @SortBy
    fun getSortBy(): String {
        return PREFS.getString(SORT_BY, DEFAULT) ?: DEFAULT
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