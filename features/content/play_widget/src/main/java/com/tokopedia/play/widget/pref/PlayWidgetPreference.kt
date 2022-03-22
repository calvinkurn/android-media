package com.tokopedia.play.widget.pref

import android.content.Context
import android.preference.PreferenceManager
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on January 19, 2022
 */
class PlayWidgetPreference @Inject constructor(
    context: Context
) {
    private val sharedPref = PreferenceManager.getDefaultSharedPreferences(context.applicationContext)

    fun getAutoPlay(isAutoPlayFromBE: Boolean): Boolean {
        val isAutoPlayFromSettings = sharedPref.getBoolean(KEY_PLAY_WIDGET_AUTOPLAY, true)
        return if(isAutoPlayFromSettings) isAutoPlayFromBE else false
    }

    companion object {
        private const val KEY_PLAY_WIDGET_AUTOPLAY = "play_widget_autoplay"
    }
}

//object a {
//
//    private const val KEY_PLAY_WIDGET_AUTOPLAY = "play_widget_autoplay"
//
//    fun getAutoPlay(context: Context, isAutoPlayFromBE: Boolean): Boolean {
//        val isAutoPlayFromSettings = PreferenceManager.getDefaultSharedPreferences(context.applicationContext)
//            .getBoolean(KEY_PLAY_WIDGET_AUTOPLAY, true)
//
//        return if(isAutoPlayFromSettings) isAutoPlayFromBE else false
//    }
//}