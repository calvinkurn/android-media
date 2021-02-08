package com.tokopedia.play.widget.ui.mapper

import com.tokopedia.play.widget.data.PlayWidget
import com.tokopedia.play.widget.ui.model.PlayWidgetConfigUiModel
import javax.inject.Inject

/**
 * Created by jegul on 07/10/20
 */
class PlayWidgetConfigMapper @Inject constructor() {

    fun mapWidgetConfig(data: PlayWidget): PlayWidgetConfigUiModel = PlayWidgetConfigUiModel(
            autoPlay = data.meta.autoplay,
            autoPlayAmount = data.meta.autoplayAmount,
            autoRefresh = data.meta.autoRefresh,
            autoRefreshTimer = data.meta.autoRefreshTimer,
            maxAutoPlayCellularDuration = data.meta.maxAutoplayCell,
            maxAutoPlayWifiDuration = data.meta.maxAutoplayWifi,
            businessWidgetPosition = data.meta.businessWidgetPosition
    )
}