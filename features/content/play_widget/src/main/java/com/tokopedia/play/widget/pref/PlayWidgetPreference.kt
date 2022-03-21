package com.tokopedia.play.widget.pref

import android.content.Context
import android.preference.PreferenceManager

/**
 * Created By : Jonathan Darwin on January 19, 2022
 */
object PlayWidgetPreference {

    private const val KEY_PLAY_WIDGET_AUTOPLAY = "play_widget_autoplay"

    fun getAutoPlay(context: Context, isAutoPlayFromBE: Boolean): Boolean {
        val isAutoPlayFromSettings = PreferenceManager.getDefaultSharedPreferences(context.applicationContext)
            .getBoolean(KEY_PLAY_WIDGET_AUTOPLAY, true)

        return if(isAutoPlayFromSettings) isAutoPlayFromBE else false
    }
}