package com.tokopedia.play.widget.ui.mapper

import android.content.SharedPreferences
import android.util.Log
import com.tokopedia.play.widget.data.PlayWidget
import com.tokopedia.play.widget.ui.model.PlayWidgetConfigUiModel
import javax.inject.Inject

/**
 * Created by jegul on 07/10/20
 */
class PlayWidgetConfigMapper @Inject constructor(
    private val pref: SharedPreferences,
) {

    fun mapWidgetConfig(data: PlayWidget): PlayWidgetConfigUiModel {
        /** TODO("key and default value make it cleaner") */
        val autoPlayPref = pref.getBoolean("play_widget_autoplay", true)
        Log.d("<LOG>", "autoPlayPref: $autoPlayPref")

        return PlayWidgetConfigUiModel(
            autoPlay = if(autoPlayPref) data.meta.autoplay else false,
            autoPlayAmount = data.meta.autoplayAmount,
            autoRefresh = data.meta.autoRefresh,
            autoRefreshTimer = data.meta.autoRefreshTimer,
            maxAutoPlayCellularDuration = data.meta.maxAutoplayCell,
            maxAutoPlayWifiDuration = data.meta.maxAutoplayWifi,
            businessWidgetPosition = data.meta.businessWidgetPosition
        )
    }
}