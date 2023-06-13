package com.tokopedia.tkpd.flashsale.util.preference

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import javax.inject.Inject

class PreferenceDataStore @Inject constructor(@ApplicationContext context: Context) {

    companion object {
        private const val PREFERENCE_FILE_NAME = "tokopedia_flash_sale_prefs"
        private const val PREFERENCE_KEY_TICKER_MULTI_LOCATION_DISMISSED = "multi_location_ticker_dismissed"
    }

    private val preference by lazy {
        context.getSharedPreferences(
            PREFERENCE_FILE_NAME,
            Context.MODE_PRIVATE
        )
    }

    fun markMultiLocationTickerAsDismissed() {
        val editor = preference.edit()
        with(editor) {
            putBoolean(PREFERENCE_KEY_TICKER_MULTI_LOCATION_DISMISSED, true)
            apply()
        }
    }

    fun isMultiLocationTickerDismissed(): Boolean {
        return preference.getBoolean(PREFERENCE_KEY_TICKER_MULTI_LOCATION_DISMISSED, false)
    }

}
