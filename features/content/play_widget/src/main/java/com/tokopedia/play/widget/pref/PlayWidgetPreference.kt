package com.tokopedia.play.widget.pref

import android.content.SharedPreferences
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on January 19, 2022
 */
class PlayWidgetPreference @Inject constructor(
    private val sharedPreferences: SharedPreferences
){
    fun getAutoPlay(): Boolean = sharedPreferences.getBoolean(KEY_PLAY_WIDGET_AUTOPLAY, true)

    companion object {
        const val KEY_PLAY_WIDGET_AUTOPLAY = "play_widget_autoplay"
    }
}