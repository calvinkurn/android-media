package com.tokopedia.campaignlist.common.util

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import javax.inject.Inject

class PreferenceDataStore @Inject constructor(@ApplicationContext context: Context) {

    companion object {
        private const val PREFERENCE_FILE_NAME = "TICKER_STATE_PREFERENCE"
        private const val PREFERENCE_KEY_TICKER_DISMISSED = "IS_DISMISS_TICKER"
    }

    private val preference by lazy { context.getSharedPreferences(PREFERENCE_FILE_NAME, Context.MODE_PRIVATE) }

    fun markTickerAsDismissed() {
        val editor = preference.edit()
        with(editor) {
            putBoolean(PREFERENCE_KEY_TICKER_DISMISSED, true)
            apply()
        }
    }

    fun isTickerDismissed(): Boolean {
        return preference.getBoolean(PREFERENCE_KEY_TICKER_DISMISSED, false)
    }

}