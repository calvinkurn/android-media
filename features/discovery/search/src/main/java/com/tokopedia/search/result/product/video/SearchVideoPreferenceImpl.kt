package com.tokopedia.search.result.product.video

import android.content.Context
import android.preference.PreferenceManager
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import javax.inject.Inject

class SearchVideoPreferenceImpl @Inject constructor(
    @ApplicationContext context: Context
) : SearchVideoPreference {
    private val sharedPref = PreferenceManager.getDefaultSharedPreferences(
        context.applicationContext
    )

    override val isSneakPeekEnabled: Boolean
        get() {
            return sharedPref.getBoolean(KEY_PLAY_WIDGET_AUTOPLAY, true)
        }

    companion object {
        private const val KEY_PLAY_WIDGET_AUTOPLAY = "play_widget_autoplay"
    }
}
